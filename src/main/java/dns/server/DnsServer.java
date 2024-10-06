package dns.server;

import dns.env.DnsClass;
import dns.env.DnsPacketIndicator;
import dns.env.DnsType;
import dns.env.Environment;
import dns.message.*;
import dns.reader.DnsMessageReader;
import dns.writer.WriterFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class DnsServer {

    public DnsServer() {
    }

    public void start() {
        try (DatagramSocket serverSocket = new DatagramSocket(Environment.PORT)) {
            while (true) {
                final byte[] requestBuffer = new byte[Environment.BUFFER_SIZE];
                final DatagramPacket request = new DatagramPacket(requestBuffer, requestBuffer.length);
                serverSocket.receive(request);
                System.out.println("Received data");
                DnsMessage requestMessage = readRequest(requestBuffer);

                DnsMessage replyMessage = buildReply(requestMessage);
                byte[] reply = WriterFactory.write(replyMessage).orElseGet(() -> new byte[Environment.BUFFER_SIZE]);
                final DatagramPacket response = new DatagramPacket(reply, reply.length, request.getSocketAddress());
                serverSocket.send(response);
            }
        } catch (IOException e) {
            System.out.printf("IOException: %s%n", e.getMessage());
        }
    }

    private DnsMessage readRequest(byte[] buffer) throws IOException {
        DnsMessageReader messageReader = new DnsMessageReader(ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN));
        return messageReader.read();
    }

    private DnsMessage buildReply(DnsMessage request) {
        DnsQuestion question = request.getQuestion();

        DnsAnswer answer = DnsAnswer.builder()
                .withName(question.getName())
                .forDnsType(DnsType.A)
                .forDnsClass(DnsClass.IN)
                .withTTL(42)
                .withData("8.8.8.8")
                .build();

        DnsHeader header = DnsHeader.builder()
                .withIdentifier(request.getHeader().getIdentifier())
                .withQRIndicator(DnsPacketIndicator.RESPONSE)
                .withOperationCode(request.getHeader().getOperationCode())
                .isAuthoritative(false)
                .isTruncated(false)
                .isRecursionDesired(request.getHeader().isRecursionDesired())
                .isRecursionAvailable(false)
                .withResponseCode((byte) (request.getHeader().getOperationCode() == 0 ? 0 : 4))
                .withQuestionCount((short) 1)
                .withAnswerRecordsCount((short) 1)
                .build();

        return DnsMessage.builder()
                .withHeader(header)
                .withQuestion(question)
                .withAnswer(answer)
                .build();
    }

}
