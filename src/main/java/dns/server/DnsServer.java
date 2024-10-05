package dns.server;

import dns.env.DnsPacketIndicator;
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

                DnsMessageReader messageReader = new DnsMessageReader(ByteBuffer.wrap(requestBuffer).order(ByteOrder.BIG_ENDIAN));
                DnsMessage requestMessage = messageReader.read();

                DnsHeader replyHeader = DnsHeader.builder()
                        .withIdentifier(requestMessage.getHeader().getIdentifier())
                        .withQRIndicator(DnsPacketIndicator.RESPONSE)
                        .withOperationCode(requestMessage.getHeader().getOperationCode())
                        .isAuthoritative(false)
                        .isTruncated(false)
                        .isRecursionDesired(requestMessage.getHeader().isRecursionDesired())
                        .isRecursionAvailable(false)
                        .withResponseCode((byte) (requestMessage.getHeader().getOperationCode() == 0 ? 0 : 4))
                        .withQuestionCount((short) 1)
                        .withAnswerRecordsCount((short) 1)
                        .build();

                DnsMessage replyMessage = DnsMessage.builder()
                        .withHeader(replyHeader)
                        .withQuestion(DnsQuestion.sampleDnsQuestion())
                        .withAnswer(DnsAnswer.sampleDnsAnswer())
                        .build();

                WriterFactory
                        .write(replyMessage)
                        .ifPresent(reply -> {
                            final DatagramPacket response = new DatagramPacket(reply, reply.length, request.getSocketAddress());
                            try {
                                serverSocket.send(response);
                            } catch (IOException e) {
                                System.out.printf("IOException: %s%n", e.getMessage());
                            }
                        });
            }
        } catch (IOException e) {
            System.out.printf("IOException: %s%n", e.getMessage());
        }
    }

}
