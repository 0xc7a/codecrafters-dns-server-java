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
        ByteBuffer buffer = ByteBuffer.allocate(Environment.getInstance().getBufferSize());
        WriterFactory.write(data.getHeader()).ifPresent(buffer::put);
        data.getQuestions().forEach(question -> WriterFactory.write(question).ifPresent(buffer::put));
        data.getAnswers().forEach(answer -> WriterFactory.write(answer).ifPresent(buffer::put));
        return buffer.array();
    }

}
