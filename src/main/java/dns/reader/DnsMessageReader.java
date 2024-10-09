package dns.reader;

import dns.message.DnsHeader;
import dns.message.DnsMessage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class DnsMessageReader {

    public static DnsMessage read(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);

        DnsMessage.Builder messageBuilder = DnsMessage.builder();

        int index = 0;
        int length = DnsHeader.HEADER_SIZE_BYTES;

        DnsHeaderReader headerReader = new DnsHeaderReader(buffer.slice(index, length));
        headerReader.messageBuilder = messageBuilder;
        headerReader.read();

        index += headerReader.bufferPosition;
        length = buffer.limit() - index;

        DnsQuestionsReader questionsReader = new DnsQuestionsReader(buffer.slice(index, length));
        questionsReader.messageBuilder = messageBuilder;
        questionsReader.read();

        index += questionsReader.bufferPosition;
        length = buffer.limit() - index;

        DnsAnswersReader answersReader = new DnsAnswersReader(buffer.slice(index, length));
        answersReader.messageBuilder = messageBuilder;
        answersReader.read();

        return messageBuilder.build();
    }

}
