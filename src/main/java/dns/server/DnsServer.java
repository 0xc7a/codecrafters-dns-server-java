package dns.server;

import dns.env.Environment;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public final class DnsServer {

    public DnsServer() {
    }

    public void start() {
        try (DatagramSocket serverSocket = new DatagramSocket(Environment.PORT)) {
            while (true) {
                final byte[] requestBuffer = new byte[512];
                final DatagramPacket request = new DatagramPacket(requestBuffer, requestBuffer.length);
                serverSocket.receive(request);
                System.out.println("Received data");

                final byte[] responseBuffer = new byte[512];
                final DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length, request.getSocketAddress());
                serverSocket.send(response);
            }
        } catch (IOException e) {
            System.out.printf("IOException: %s%n", e.getMessage());
        }
    }

}
