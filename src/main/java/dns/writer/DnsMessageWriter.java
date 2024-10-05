package dns.writer;

import dns.env.Environment;
import dns.message.DnsMessage;

import java.nio.ByteBuffer;

public class DnsMessageWriter extends Writer<DnsMessage> {

    public DnsMessageWriter(DnsMessage data) {
        this.data = data;
    }

    @Override
    public byte[] write() {
        ByteBuffer buffer = ByteBuffer.allocate(Environment.BUFFER_SIZE);
        WriterFactory.write(data.getHeader()).ifPresent(buffer::put);
        WriterFactory.write(data.getQuestion()).ifPresent(buffer::put);
        WriterFactory.write(data.getAnswer()).ifPresent(buffer::put);
        return buffer.array();
    }

}
