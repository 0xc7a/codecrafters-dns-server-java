package dns.server;

import dns.env.DnsPacketIndicator;
import dns.env.Environment;
import dns.message.DnsAnswer;
import dns.message.DnsHeader;
import dns.message.DnsMessage;
import dns.message.DnsQuestion;
import dns.util.Pair;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public record DnsForwarder(DnsMessage request) {

    public DnsForwarder {
        Objects.requireNonNull(request, "Request must not be null.");
    }

    public DnsMessage forward() throws IOException {
        List<DnsAnswer> answers = request.getQuestions().stream()
                .map(question -> DnsMessage.builder()
                        .withHeader(DnsHeader.builder()
                                .withIdentifier(request.getHeader().getIdentifier())
                                .withQRIndicator(DnsPacketIndicator.QUERY)
                                .withOperationCode(request.getHeader().getOperationCode())
                                .withQuestionCount((short) 1)
                                .isRecursionDesired(request.getHeader().isRecursionDesired())
                                .build())
                        .withQuestion(question)
                        .build())
                .map(message -> CompletableFuture.supplyAsync(forwardDnsMessage(message)))
                .map(CompletableFuture::join)
                .map(DnsMessage::getAnswers)
                .flatMap(List::stream)
                .toList();

        return DnsMessage.builder()
                .withHeader(DnsHeader.builder()
                        .withIdentifier(request.getHeader().getIdentifier())
                        .withQRIndicator(DnsPacketIndicator.RESPONSE)
                        .withOperationCode(request.getHeader().getOperationCode())
                        .withQuestionCount((short) request.getQuestions().size())
                        .withAnswerRecordsCount((short) answers.size())
                        .isRecursionDesired(request.getHeader().isRecursionDesired())
                        .build())
                .withQuestions(request.getQuestions().toArray(DnsQuestion[]::new))
                .withAnswers(answers.toArray(DnsAnswer[]::new))
                .build();
    }

    private Supplier<DnsMessage> forwardDnsMessage(DnsMessage message) {
        return () -> {
            try (DatagramSocket serverSocket = new DatagramSocket()) {
                final DnsMessageSender sender = new DnsMessageSender(serverSocket, Environment.getInstance().getForwardAddress(), message);
                sender.send();

                final DnsMessageReceiver receiver = new DnsMessageReceiver(serverSocket);
                final Pair<DnsMessage, SocketAddress> request = receiver.receive();
                return request.first();
            } catch (IOException e) {
                System.out.printf("IOException: %s%n", e.getMessage());
                return null;
            }
        };
    }

}
