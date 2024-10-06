package dns.env;

import java.util.Arrays;
import java.util.Optional;

// https://www.rfc-editor.org/rfc/rfc1035#section-3.2.4
public enum DnsClass {
    IN(1), // the Internet
    @Deprecated CS(2), // the CSNET class
    CH(3), // the CHAOS class
    HS(4); // Hesiod [Dyer 87]

    public static final int CLASS_SIZE_BYTES = 2;

    private final int value;

    DnsClass(int value) {
        this.value = value;
    }

    public short getValue() {
        return (short) value;
    }

    public static Optional<DnsClass> findDnsClass(short value) {
        return Arrays.stream(DnsClass.values()).filter(dnsClass -> dnsClass.value == value).findFirst();
    }
}