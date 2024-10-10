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

        DnsHeaderReader headerReader = new DnsHeaderReader(buffer.slice(index, DnsHeader.HEADER_SIZE_BYTES), messageBuilder);
        index += headerReader.read();

        DnsQuestionsReader questionsReader = new DnsQuestionsReader(buffer.slice(index, buffer.limit() - index), messageBuilder);
        index += questionsReader.read();

        DnsAnswersReader answersReader = new DnsAnswersReader(buffer.slice(index, buffer.limit() - index), messageBuilder);
        answersReader.read();

        return messageBuilder.build();
    }

}
