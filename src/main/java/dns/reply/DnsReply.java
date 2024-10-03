package dns.reply;

import dns.env.DnsPacketIndicator;
import dns.env.Environment;

public class DnsReply {

    private final byte[] buffer;

    public DnsReply() {
        buffer = new byte[Environment.BUFFER_SIZE];
    }

    public byte[] getReply() {
        DnsHeader header = DnsHeader.builder()
                .withIdentifier((short) 1234)
                .forIndicator(DnsPacketIndicator.REPLY)
                .build();
        byte[] headerBytes = header.getHeader();

        System.arraycopy(headerBytes, 0, buffer, 0, headerBytes.length);

        return buffer;
    }

}
