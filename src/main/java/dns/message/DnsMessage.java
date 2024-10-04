package dns.message;

import dns.env.DnsClass;
import dns.env.DnsPacketIndicator;
import dns.env.DnsType;
import dns.env.Environment;

import java.nio.ByteBuffer;

// https://www.rfc-editor.org/rfc/rfc1035#section-4.1
public class DnsMessage {

    public byte[] getMessage() {
        DnsQuestion question = new DnsQuestion("codecrafters.io", DnsType.A, DnsClass.IN);

        DnsHeader header = DnsHeader.builder()
                .withIdentifier((short) 1234)
                .forQRIndicator(DnsPacketIndicator.RESPONSE)
                .withQuestionCount((short) 1)
                .build();

        return ByteBuffer
                .allocate(Environment.BUFFER_SIZE)
                .put(header.getHeader())
                .put(question.getQuestion())
                .array();
    }

}
