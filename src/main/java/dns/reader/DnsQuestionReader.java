package dns.reader;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.env.Environment;
import dns.message.DnsQuestion;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DnsQuestionReader extends Reader<DnsQuestion> {

    public DnsQuestionReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public DnsQuestion read() {
        StringBuilder builder = new StringBuilder();
        byte[] word;

        byte value = buffer.get();
        while (value != Environment.NULL_BYTE) {
            word = new byte[value];
            buffer.get(word, 0, value);
            if (!builder.isEmpty()) {
                builder.append(".");
            }
            builder.append(new String(word, StandardCharsets.UTF_8));
            value = buffer.get();
        }

        DnsType dnsType = DnsType.findDnsType(buffer.getShort()).orElse(null);

        DnsClass dnsClass = DnsClass.findDnsClass(buffer.getShort()).orElse(null);

        return new DnsQuestion(builder.toString(), dnsType, dnsClass);
    }

}
