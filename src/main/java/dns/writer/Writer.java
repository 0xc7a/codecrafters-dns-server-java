package dns.writer;

import dns.message.DnsRecord;

public abstract class Writer<T extends DnsRecord> {

    protected T data;

    public abstract byte[] write();

}
