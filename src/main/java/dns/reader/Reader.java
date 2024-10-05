package dns.reader;

import dns.message.DnsRecord;

import java.nio.ByteBuffer;

public abstract class Reader<T extends DnsRecord> {

    protected ByteBuffer buffer;

    public abstract T read();

}
