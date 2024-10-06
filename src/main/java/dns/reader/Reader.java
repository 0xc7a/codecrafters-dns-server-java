package dns.reader;

import java.nio.ByteBuffer;

public abstract class Reader<T> {

    protected ByteBuffer buffer;

    protected int bufferPosition;

    public abstract T read();

}
