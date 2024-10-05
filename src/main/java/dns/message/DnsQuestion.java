package dns.message;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.util.Validator;

import java.util.List;
import java.util.Objects;

public class DnsQuestion implements DnsRecord {

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

    public List<DnsLabel> getLabels() {
        return labels;
    }

    public DnsType getDnsType() {
        return dnsType;
    }

    public DnsClass getDnsClass() {
        return dnsClass;
    }

    public static DnsQuestion sampleDnsQuestion() {
        return new DnsQuestion("codecrafters.io", DnsType.A, DnsClass.IN);
    }

}
