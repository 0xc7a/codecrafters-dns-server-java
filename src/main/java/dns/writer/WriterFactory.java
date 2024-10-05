package dns.writer;

import dns.message.*;

import java.util.Optional;

public final class WriterFactory {

    public static <T extends DnsRecord> Optional<byte[]> write(T record) {
        return switch (record) {
            case DnsHeader header -> Optional.ofNullable(new DnsHeaderWriter(header).write());
            case DnsQuestion question -> Optional.ofNullable(new DnsQuestionWriter(question).write());
            case DnsAnswer answer -> Optional.ofNullable(new DnsAnswerWriter(answer).write());
            case DnsMessage message -> Optional.ofNullable(new DnsMessageWriter(message).write());
            case null -> Optional.empty();
            default -> throw new UnsupportedOperationException("Unknown DnsRecord.");
        };
    }

}
