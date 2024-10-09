package dns.server;

import dns.env.DnsClass;
import dns.env.DnsPacketIndicator;
import dns.env.DnsType;
import dns.message.DnsAnswer;
import dns.message.DnsHeader;
import dns.message.DnsMessage;
import dns.message.DnsQuestion;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public record DnsResolver(DnsMessage request) {

    public DnsResolver {
        Objects.requireNonNull(request, "Request must not be null.");
    }

    public DnsMessage resolve() {
        List<DnsQuestion> questions = request.getQuestions();

        List<DnsAnswer> answers = questions.stream()
                .map(question -> DnsAnswer.builder()
                        .withName(question.getDomainName())
                        .forDnsType(DnsType.A)
                        .forDnsClass(DnsClass.IN)
                        .withTTL(42)
                        .withData("8.8.8.8".getBytes(StandardCharsets.UTF_8))
                        .build())
                .toList();

        DnsHeader header = DnsHeader.builder()
                .withIdentifier(request.getHeader().getIdentifier())
                .withQRIndicator(DnsPacketIndicator.RESPONSE)
                .withOperationCode(request.getHeader().getOperationCode())
                .isAuthoritative(false)
                .isTruncated(false)
                .isRecursionDesired(request.getHeader().isRecursionDesired())
                .isRecursionAvailable(false)
                .withQuestionCount((short) questions.size())
                .withAnswerRecordsCount((short) answers.size())
                .build();

        return DnsMessage.builder()
                .withHeader(header)
                .withQuestions(questions.toArray(DnsQuestion[]::new))
                .withAnswers(answers.toArray(DnsAnswer[]::new))
                .build();
    }

}
