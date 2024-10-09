package dns.server;

import dns.env.Environment;
import dns.message.DnsMessage;
import dns.reader.DnsMessageReader;
import dns.util.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public record DnsMessageReceiver(DatagramSocket socket) {

    public Pair<DnsMessage, SocketAddress> receive() throws IOException {
        final byte[] requestBuffer = new byte[Environment.getInstance().getBufferSize()];
        final DatagramPacket request = new DatagramPacket(requestBuffer, requestBuffer.length);
        socket.receive(request);
        return new Pair<>(DnsMessageReader.read(requestBuffer), request.getSocketAddress());
    }

}
