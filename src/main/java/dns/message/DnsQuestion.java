package dns.message;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.env.Environment;
import dns.util.Validator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;

public class DnsQuestion {

    private final List<DnsLabel> labels;
    private final DnsType dnsType;
    private final DnsClass dnsClass;

    public DnsQuestion(String name, DnsType dnsType, DnsClass dnsClass) {
        Objects.requireNonNull(name, "Name must not be null.");
        Objects.requireNonNull(dnsType, "Type must not be null.");
        Objects.requireNonNull(dnsClass, "Class must not be null.");

        this.labels = Validator.validateDomain(name).stream().map(DnsLabel::new).toList();
        this.dnsType = dnsType;
        this.dnsClass = dnsClass;
    }

    public byte[] getQuestion() {
        List<byte[]> labelsList = labels.stream().map(DnsLabel::getLabel).toList();

        int size = labelsList.stream().mapToInt(l -> l.length).sum()
                + 1 /* null byte */
                + DnsType.TYPE_SIZE_BYTES
                + DnsClass.CLASS_SIZE_BYTES;

        ByteBuffer buffer = ByteBuffer
                .allocate(size)
                .order(ByteOrder.BIG_ENDIAN);

        labelsList.forEach(buffer::put);

        return buffer
                .put(Environment.NULL_BYTE)
                .putShort(dnsType.getValue())
                .putShort(dnsClass.getValue())
                .array();
    }

}
