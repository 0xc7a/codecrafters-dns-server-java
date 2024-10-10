package dns.reader;

import dns.message.DnsMessage;

import java.nio.ByteBuffer;

public abstract class Reader {

    protected ByteBuffer buffer;

    protected DnsMessage.Builder messageBuilder;

    public abstract int read();

}
