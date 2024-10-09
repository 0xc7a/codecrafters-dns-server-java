package dns.reader;

import dns.message.DnsMessage;

import java.nio.ByteBuffer;

public abstract class Reader {

    protected ByteBuffer buffer;

    protected DnsMessage.Builder messageBuilder;

    protected int bufferPosition;

    public abstract void read();

}
