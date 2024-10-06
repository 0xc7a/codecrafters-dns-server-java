package dns.message;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DnsQuestion implements DnsRecord {

    private final String name;
    private final List<DnsLabel> labels;
    private final DnsType dnsType;
    private final DnsClass dnsClass;

    public DnsQuestion(DnsQuestion.Builder builder) {
        Objects.requireNonNull(builder.name, "Name must not be null.");
        Objects.requireNonNull(builder.dnsType, "Type must not be null.");
        Objects.requireNonNull(builder.dnsClass, "Class must not be null.");

        this.name = builder.name;
        if (builder.labels.isEmpty()) {
            this.labels = Validator.validateDomain(builder.name).stream().map(DnsLabel::new).toList();
        } else {
            this.labels = List.copyOf(builder.labels);
        }
        this.dnsType = builder.dnsType;
        this.dnsClass = builder.dnsClass;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
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

    public static class Builder {

        private String name;
        private List<DnsLabel> labels = new ArrayList<>();
        private DnsType dnsType;
        private DnsClass dnsClass;

        public Builder forName(String name) {
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

        public DnsQuestion build() {
            return new DnsQuestion(this);
        }

    }

    public static DnsQuestion sampleDnsQuestion() {
        return DnsQuestion.builder()
                .forName("codecrafters.io")
                .forDnsType(DnsType.A)
                .forDnsClass(DnsClass.IN)
                .build();
    }

}
