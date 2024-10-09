package dns.server;

import dns.env.Environment;
import dns.message.DnsMessage;
import dns.writer.WriterFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public record DnsMessageSender(DatagramSocket socket, SocketAddress address, DnsMessage message) {

    public void send() throws IOException {
        final byte[] reply = WriterFactory.write(message).orElseGet(() -> new byte[Environment.getInstance().getBufferSize()]);
        final DatagramPacket response = new DatagramPacket(reply, reply.length, address);
        socket.send(response);
    }

}
