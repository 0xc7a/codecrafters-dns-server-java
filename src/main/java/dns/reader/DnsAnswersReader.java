package dns.reader;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.message.DnsAnswer;
import dns.message.DnsLabel;
import dns.message.DnsQuestion;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DnsAnswersReader extends Reader {

    public DnsAnswersReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void read() {
        List<DnsAnswer> answers = new ArrayList<>();

        final int totalAnswers = messageBuilder.getHeader().getAnswerRecordsCount();
        if (totalAnswers == 0) {
            return;
        }

        DnsAnswer.Builder answer;
        StringBuilder builder;
        byte[] word;
        byte value;

        for (int index = 0; index < totalAnswers; index++) {
            answer = DnsAnswer.builder();
            builder = new StringBuilder();

            value = buffer.get();

            while (value > 0) {
                word = new byte[value];
                buffer.get(word, 0, value);

                if (!builder.isEmpty()) {
                    builder.append(".");
                }
                builder.append(new String(word, StandardCharsets.UTF_8));

                value = buffer.get();
            }

            if (value < 0) {
                byte pointer = buffer.get();

                List<DnsLabel> labels = messageBuilder.getQuestions().stream()
                        .filter(q -> q.getLabels()
                                .stream()
                                .anyMatch(l -> l.getIndex() == pointer + 1))
                        .findFirst()
                        .map(DnsQuestion::getLabels)
                        .orElse(List.of());

                answer = answer.withLabels(labels.toArray(DnsLabel[]::new));

                if (!builder.isEmpty()) {
                    builder.append(".");
                }
                builder.append(labels.stream().map(DnsLabel::getContent).collect(Collectors.joining(".")));
            }

            DnsType dnsType = DnsType.findDnsType(buffer.getShort()).orElse(null);

            DnsClass dnsClass = DnsClass.findDnsClass(buffer.getShort()).orElse(null);

            int ttl = buffer.getInt();

            short _ = buffer.getShort();

            word = new byte[4];
            buffer.get(word, 0, 4);

            answer = answer
                    .withName(builder.toString())
                    .forDnsType(dnsType)
                    .forDnsClass(dnsClass)
                    .withTTL(ttl)
                    .withData(word);

            answers.add(answer.build());
        }

        bufferPosition = buffer.position();

        messageBuilder = messageBuilder.withAnswers(answers.toArray(DnsAnswer[]::new));
    }

}
