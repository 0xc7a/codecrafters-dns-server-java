package dns.server;

import dns.env.Environment;
import dns.reply.DnsReply;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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

                DnsReply dnsReply = new DnsReply();
                byte[] reply = dnsReply.getReply();
                final DatagramPacket response = new DatagramPacket(reply, reply.length, request.getSocketAddress());
                serverSocket.send(response);
            }
        } catch (IOException e) {
            System.out.printf("IOException: %s%n", e.getMessage());
        }
    }

}
