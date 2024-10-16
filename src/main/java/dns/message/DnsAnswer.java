package dns.message;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.util.Validator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// https://www.rfc-editor.org/rfc/rfc1035#section-3.2.1
public class DnsAnswer implements DnsRecord {

    private final List<DnsLabel> labels;
    private final DnsType dnsType;
    private final DnsClass dnsClass;
    private final int ttl;
    private final byte[] data;

    public DnsAnswer(DnsAnswer.Builder builder) {
        Objects.requireNonNull(builder.name, "Name must not be null.");
        Objects.requireNonNull(builder.dnsType, "Type must not be null.");
        if (builder.dnsType != DnsType.A) {
            throw new UnsupportedOperationException("Only Type A is supported.");
        }
        Objects.requireNonNull(builder.dnsClass, "Class must not be null.");
        Objects.requireNonNull(builder.data, "RData must not be null.");

        if (builder.labels.isEmpty()) {
            this.labels = Validator.validateDomain(builder.name).stream().map(DnsLabel::new).toList();
        } else {
            this.labels = List.copyOf(builder.labels);
        }
        this.dnsType = builder.dnsType;
        this.dnsClass = builder.dnsClass;
        this.ttl = builder.ttl;
        this.data = builder.data;
    }

    public List<DnsLabel> getLabels() {
        return labels;
    }

    public DnsType getDnsType() {
        return dnsType;
    }

    public DnsClass getDnsClass() {
        return dnsClass;
    }

    public int getTtl() {
        return ttl;
    }

    public byte[] getData() {
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private final List<DnsLabel> labels = new ArrayList<>();
        private DnsType dnsType;
        private DnsClass dnsClass;
        private int ttl;
        private byte[] data;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLabels(DnsLabel... label) {
            this.labels.addAll(List.of(label));
            return this;
        }

        public Builder withLabel(DnsLabel label) {
            this.labels.add(label);
            return this;
        }

        public Builder forDnsType(DnsType dnsType) {
            this.dnsType = dnsType;
            return this;
        }

        public Builder forDnsClass(DnsClass dnsClass) {
            this.dnsClass = dnsClass;
            return this;
        }

        public Builder withTTL(int ttl) {
            this.ttl = ttl;
            return this;
        }

        public Builder withData(byte[] data) {
            this.data = data;
            return this;
        }

        public DnsAnswer build() {
            return new DnsAnswer(this);
        }

    }

    public static DnsAnswer sampleDnsAnswer() {
        return DnsAnswer.builder()
                .withName("codecrafters.io")
                .forDnsType(DnsType.A)
                .forDnsClass(DnsClass.IN)
                .withTTL(42)
                .withData("8.8.8.8".getBytes(StandardCharsets.UTF_8))
                .build();
    }

}
