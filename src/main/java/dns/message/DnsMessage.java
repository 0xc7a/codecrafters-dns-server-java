package dns.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// https://www.rfc-editor.org/rfc/rfc1035#section-4.1
public class DnsMessage implements DnsRecord {

    private final DnsHeader header;
    private final List<DnsQuestion> questions;
    private final List<DnsAnswer> answers;

    public DnsMessage(DnsMessage.Builder builder) {
        Objects.requireNonNull(builder.header, "Header must not be null.");

        this.header = builder.header;
        this.questions = List.copyOf(builder.questions);
        this.answers = List.copyOf(builder.answers);
    }

    public DnsHeader getHeader() {
        return header;
    }

    public List<DnsQuestion> getQuestions() {
        return questions;
    }

    public List<DnsAnswer> getAnswers() {
        return answers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private DnsHeader header;
        private List<DnsQuestion> questions = new ArrayList<>();
        private List<DnsAnswer> answers = new ArrayList<>();

        public Builder withHeader(DnsHeader header) {
            this.header = header;
            return this;
        }

        public Builder withQuestion(DnsQuestion question) {
            this.questions.add(question);
            return this;
        }

        public Builder withQuestions(DnsQuestion... question) {
            this.questions.addAll(List.of(question));
            return this;
        }

        public Builder withAnswer(DnsAnswer answer) {
            this.answers.add(answer);
            return this;
        }

        public Builder withAnswers(DnsAnswer... answer) {
            this.answers.addAll(List.of(answer));
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
