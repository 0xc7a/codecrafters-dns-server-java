package dns.server;

import dns.env.Environment;
import dns.message.*;
import dns.util.Pair;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Objects;

public final class DnsServer {

    public DnsServer() {
    }

    public void start() {
        try (DatagramSocket serverSocket = new DatagramSocket(Environment.getInstance().getPort())) {
            while (true) {
                final DnsMessageReceiver receiver = new DnsMessageReceiver(serverSocket);
                final Pair<DnsMessage, SocketAddress> request = receiver.receive();
                System.out.println("Received data");

                final DnsMessage reply = resolveDnsRequest(request.first());
                final DnsMessageSender sender = new DnsMessageSender(serverSocket, request.second(), reply);
                sender.send();
            }
        } catch (IOException e) {
            System.out.printf("IOException: %s%n", e.getMessage());
        }
    }

    private DnsMessage resolveDnsRequest(DnsMessage request) throws IOException {
        if (Objects.nonNull(Environment.getInstance().getForwardAddress())) {
            DnsForwarder forwarder = new DnsForwarder(request);
            return forwarder.forward();
        } else {
            DnsResolver resolver = new DnsResolver(request);
            return resolver.resolve();
        }
    }

}
