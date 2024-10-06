package dns.reader;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.message.DnsLabel;
import dns.message.DnsQuestion;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DnsQuestionsReader extends Reader<DnsQuestion[]> {

    private final int questionCount;

    public DnsQuestionsReader(ByteBuffer buffer, int questionCount) {
        this.buffer = buffer;
        this.questionCount = questionCount;
    }

    @Override
    public DnsQuestion[] read() {
        List<DnsQuestion> questions = new ArrayList<>();

        DnsQuestion.Builder question;
        StringBuilder builder;
        byte[] word;
        byte value;
        String label;
        int labelIndex;

        for (int index = 0; index < questionCount; index++) {
            question = DnsQuestion.builder();
            builder = new StringBuilder();
            value = buffer.get();
            labelIndex = buffer.arrayOffset() + buffer.position();

            while (value > 0) {
                word = new byte[value];
                buffer.get(word, 0, value);
                if (!builder.isEmpty()) {
                    builder.append(".");
                }
                label = new String(word, StandardCharsets.UTF_8);
                builder.append(label);
                question = question.withLabel(new DnsLabel(label, labelIndex));
                value = buffer.get();
                labelIndex = buffer.arrayOffset() + buffer.position();
            }

            if (value < 0) {
                byte pointer = buffer.get();

                Optional<DnsQuestion> pointerQuestion = questions.stream()
                        .filter(q -> q.getLabels()
                                .stream()
                                .anyMatch(l -> l.getIndex() == pointer + 1))
                        .findFirst();

                if (pointerQuestion.isPresent()) {
                    DnsLabel[] labels = pointerQuestion.get()
                            .getLabels()
                            .stream()
                            .filter(l -> l.getIndex() >= pointer)
                            .toArray(DnsLabel[]::new);

                    question = question.withLabels(labels);
                    builder.append(".");
                    builder.append(Arrays.stream(labels).map(DnsLabel::getContent).collect(Collectors.joining(".")));
                }
            }

            question = question.forName(builder.toString());

            DnsType dnsType = DnsType.findDnsType(buffer.getShort()).orElse(null);
            question = question.forDnsType(dnsType);

            DnsClass dnsClass = DnsClass.findDnsClass(buffer.getShort()).orElse(null);
            question = question.forDnsClass(dnsClass);

            questions.add(question.build());
        }

        this.bufferPosition = buffer.position();

        return questions.toArray(DnsQuestion[]::new);
    }

}
