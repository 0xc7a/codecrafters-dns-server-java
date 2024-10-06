package dns.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class DnsLabel {

    private final String content;
    private int index;

    public DnsLabel(String content) {
        Objects.requireNonNull(content, "Content must not be null.");
        this.content = content;
    }

    public DnsLabel(String content, int index) {
        this(content);
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }

    public byte[] getLabel() {
        return ByteBuffer.allocate(content.length() + 1 /* 1 byte length */)
                .put((byte) content.length())
                .put(content.getBytes(StandardCharsets.UTF_8))
                .array();
    }

}
