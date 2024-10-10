package dns.reader;

import dns.env.DnsPacketIndicator;
import dns.message.DnsHeader;
import dns.message.DnsMessage;

import java.nio.ByteBuffer;

import static dns.message.DnsHeader.*;

public class DnsHeaderReader extends Reader {

    public DnsHeaderReader(ByteBuffer buffer, DnsMessage.Builder messageBuilder) {
        this.buffer = buffer;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public int read() {
        DnsHeader.Builder header = DnsHeader.builder();

        header.withIdentifier(buffer.getShort(0));
        short flags = buffer.getShort(2);
        header.withQRIndicator(readPacketIndicator(flags));
        header.withOperationCode(readOperationCode(flags));
        header.isAuthoritative(readIsAuthoritative(flags));
        header.isTruncated(readIsTruncated(flags));
        header.isRecursionDesired(readIsRecursionDesired(flags));
        header.isRecursionAvailable(readIsRecursionAvailable(flags));
        header.withResponseCode(readResponseCode(flags));
        header.withQuestionCount(buffer.getShort(4));
        header.withAnswerRecordsCount(buffer.getShort(6));
        header.withAuthorityRecordsCount(buffer.getShort(8));
        header.withAdditionalRecordsCount(buffer.getShort(10));

        messageBuilder = messageBuilder.withHeader(header.build());

        return 12;
    }

    private DnsPacketIndicator readPacketIndicator(short flags) {
        return (flags & FLAG_MASK_QR_INDICATOR) != 0 ? DnsPacketIndicator.RESPONSE : DnsPacketIndicator.QUERY;
    }

    private byte readOperationCode(short flags) {
        return (byte) ((flags >> 11) & FLAG_MASK_CODE);
    }

    private boolean readIsAuthoritative(short flags) {
        return (flags & FLAG_MASK_AUTHORITATIVE) != 0;
    }

    private boolean readIsTruncated(short flags) {
        return (flags & FLAG_MASK_TRUNCATED) != 0;
    }

    private boolean readIsRecursionDesired(short flags) {
        return (flags & FLAG_MASK_RECURSION_DESIRED) != 0;
    }

    private boolean readIsRecursionAvailable(short flags) {
        return (flags & FLAG_MASK_RECURSION_AVAILABLE) != 0;
    }

    private byte readResponseCode(short flags) {
        return (byte) (flags & FLAG_MASK_CODE);
    }

}
