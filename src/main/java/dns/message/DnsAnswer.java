package dns.message;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.util.Validator;

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

        this.labels = Validator.validateDomain(builder.name).stream().map(DnsLabel::new).toList();
        this.dnsType = builder.dnsType;
        this.dnsClass = builder.dnsClass;
        this.ttl = builder.ttl;
        this.data = Validator.validateIPv4(builder.data);
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
        private DnsType dnsType;
        private DnsClass dnsClass;
        private int ttl;
        private String data;

        public Builder withName(String name) {
            this.name = name;
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

        public Builder withData(String data) {
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
                .withData("8.8.8.8")
                .build();
    }

}
