package dns.message;

import dns.env.DnsClass;
import dns.env.DnsPacketIndicator;
import dns.env.DnsType;
import dns.env.Environment;

import java.nio.ByteBuffer;

// https://www.rfc-editor.org/rfc/rfc1035#section-4.1
public class DnsMessage {

    public byte[] getMessage() {
        DnsAnswer answer = DnsAnswer.builder()
                .withName("codecrafters.io")
                .forDnsType(DnsType.A)
                .forDnsClass(DnsClass.IN)
                .withTTL(42)
                .withData("8.8.8.8")
                .build();

        DnsQuestion question = new DnsQuestion("codecrafters.io", DnsType.A, DnsClass.IN);

        DnsHeader header = DnsHeader.builder()
                .withIdentifier((short) 1234)
                .forQRIndicator(DnsPacketIndicator.RESPONSE)
                .withQuestionCount((short) 1)
                .withAnswerRecordsCount((short) 1)
                .build();

        return ByteBuffer
                .allocate(Environment.BUFFER_SIZE)
                .put(header.getHeader())
                .put(question.getQuestion())
                .put(answer.getAnswer())
                .array();
    }

}
