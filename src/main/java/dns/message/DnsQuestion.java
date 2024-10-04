package dns.message;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.env.Environment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DnsQuestion {

    private static class Patterns {
        private static final Supplier<Pattern> PATTERN_DOMAIN_NAME = () -> Pattern.compile("^(?:https?://)?(?:www\\.)?([a-z0-9]+(?:-[a-z0-9]+)*)\\.([a-z0-9]+)$");
    }

    private final List<DnsLabel> labels;
    private final DnsType dnsType;
    private final DnsClass dnsClass;

    public DnsQuestion(String name, DnsType dnsType, DnsClass dnsClass) {
        Objects.requireNonNull(name, "Name must not be null.");
        Objects.requireNonNull(dnsType, "DnsType must not be null.");
        Objects.requireNonNull(dnsClass, "DnsClass must not be null.");

        Matcher matcher = Patterns.PATTERN_DOMAIN_NAME.get().matcher(name);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Name must be a valid domain name.");
        }

        this.labels = Stream.of(matcher.group(1), matcher.group(2)).map(DnsLabel::new).toList();
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
