package dns.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public record DnsLabel(String content) {

    public DnsLabel {
        Objects.requireNonNull(content, "Content must not be null.");
    }

    public byte[] getLabel() {
        return ByteBuffer.allocate(content.length() + 1 /* 1 byte length */)
                .put((byte) content.length())
                .put(content.getBytes(StandardCharsets.UTF_8))
                .array();
    }

}
