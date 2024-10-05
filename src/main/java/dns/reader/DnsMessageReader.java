package dns.reader;

import dns.message.DnsHeader;
import dns.message.DnsMessage;

import java.nio.ByteBuffer;

public class DnsMessageReader extends Reader<DnsMessage> {

    public DnsMessageReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public DnsMessage read() {
        DnsMessage.Builder message = DnsMessage.builder();

        DnsHeaderReader headerReader = new DnsHeaderReader(buffer.slice(0, DnsHeader.HEADER_SIZE_BYTES));
        message.withHeader(headerReader.read());

        return message.build();
    }

}
