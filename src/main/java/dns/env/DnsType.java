package dns.env;

import jdk.jfr.Experimental;

// https://www.rfc-editor.org/rfc/rfc1035#section-3.2.2
public enum DnsType {
    A(1), // host address
    NS(2), // an authoritative name server
    @Deprecated MD(3), // a mail destination
    @Deprecated MF(4), // a mail forwarder
    CNAME(5), // the canonical name for an alias
    SOA(6), // marks the start of a zone of authority
    @Experimental MB(7), // a mailbox domain name
    @Experimental MG(8), // a mail group member
    @Experimental MR(9), // a mail rename domain name
    @Experimental NULL(10), // a null RR
    WKS(11), // a well known service description
    PTR(12), // a domain name pointer
    HINFO(13), // host information
    MINFO(14), // mailbox or mail list information
    MX(15), // mail exchange
    TXT(16); // text strings

    public static final int TYPE_SIZE_BYTES = 2;

    private final int value;

    DnsType(int value) {
        this.value = value;
    }

    public short getValue() {
        return (short) value;
    }
}