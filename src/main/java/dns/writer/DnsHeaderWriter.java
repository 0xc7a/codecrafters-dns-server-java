package dns.writer;

import dns.env.DnsPacketIndicator;
import dns.message.DnsHeader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static dns.message.DnsHeader.*;

public class DnsHeaderWriter extends Writer<DnsHeader> {

    public DnsHeaderWriter(DnsHeader data) {
        this.data = data;
    }

    @Override
    public byte[] write() {
        return ByteBuffer
                .allocate(HEADER_SIZE_BYTES)
                .order(ByteOrder.BIG_ENDIAN)
                .putShort(data.getIdentifier())
                .putShort(getFlags())
                .putShort(data.getQuestionCount())
                .putShort(data.getAnswerRecordsCount())
                .putShort(data.getAuthorityRecordsCount())
                .putShort(data.getAdditionalRecordsCount())
                .array();
    }

    private short getFlags() {
        short flags = 0;

        if (data.getQrIndicator() == DnsPacketIndicator.RESPONSE) {
            flags ^= FLAG_MASK_QR_INDICATOR;
        }

        flags ^= (short) ((data.getOperationCode() & FLAG_MASK_CODE) << 11);

        if (data.isAuthoritative()) {
            flags ^= FLAG_MASK_AUTHORITATIVE;
        }

        if (data.isTruncated()) {
            flags ^= FLAG_MASK_TRUNCATED;
        }

        if (data.isRecursionDesired()) {
            flags ^= FLAG_MASK_RECURSION_DESIRED;
        }

        if (data.isRecursionAvailable()) {
            flags ^= FLAG_MASK_RECURSION_AVAILABLE;
        }

        flags ^= (short) (data.getResponseCode() & FLAG_MASK_CODE);

        return flags;
    }

}
