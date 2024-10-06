package dns.reader;

import dns.message.DnsHeader;
import dns.message.DnsMessage;
import dns.message.DnsQuestion;

import java.nio.ByteBuffer;

public class DnsMessageReader extends Reader<DnsMessage> {

    public DnsMessageReader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public DnsMessage read() {
        DnsMessage.Builder message = DnsMessage.builder();

        int index = 0;

        DnsHeaderReader headerReader = new DnsHeaderReader(buffer.slice(index, DnsHeader.HEADER_SIZE_BYTES));
        DnsHeader header = headerReader.read();
        message.withHeader(header);

        index += headerReader.bufferPosition;

        DnsQuestionsReader questionsReader = new DnsQuestionsReader(buffer.slice(index, buffer.limit() - index), header.getQuestionCount());
        DnsQuestion[] questions = questionsReader.read();
        message.withQuestions(questions);

        index += questionsReader.bufferPosition;

        return message.build();
    }

}
