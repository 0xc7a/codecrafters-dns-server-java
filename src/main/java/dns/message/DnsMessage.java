package dns.message;

import java.util.Objects;

// https://www.rfc-editor.org/rfc/rfc1035#section-4.1
public class DnsMessage implements DnsRecord {

    private final DnsHeader header;
    private final DnsQuestion question;
    private final DnsAnswer answer;

    public DnsMessage(DnsMessage.Builder builder) {
        Objects.requireNonNull(builder.header, "Header must not be null.");

        this.header = builder.header;
        this.question = builder.question;
        this.answer = builder.answer;
    }

    public DnsHeader getHeader() {
        return header;
    }

    public DnsQuestion getQuestion() {
        return question;
    }

    public DnsAnswer getAnswer() {
        return answer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private DnsHeader header;
        private DnsQuestion question;
        private DnsAnswer answer;

        public Builder withHeader(DnsHeader header) {
            this.header = header;
            return this;
        }

        public Builder withQuestion(DnsQuestion question) {
            this.question = question;
            return this;
        }

        public Builder withAnswer(DnsAnswer answer) {
            this.answer = answer;
            return this;
        }

        public DnsMessage build() {
            return new DnsMessage(this);
        }

    }

    public static DnsMessage sampleDnsMessage() {
        return DnsMessage.builder()
                .withHeader(DnsHeader.sampleDnsHeader())
                .withQuestion(DnsQuestion.sampleDnsQuestion())
                .withAnswer(DnsAnswer.sampleDnsAnswer())
                .build();
    }

}
