package dns.message;

import dns.env.DnsPacketIndicator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

// https://www.rfc-editor.org/rfc/rfc1035#section-4.1.1
public class DnsHeader {

    private static final int HEADER_SIZE_BYTES = 12;

    private final short identifier; // Packet Identifier (ID) - 16 bits
    private final DnsPacketIndicator qrIndicator; // Query/Response Indicator (QR) - 1 bit
    private final byte operationCode; // Operation Code (OPCODE) - 4 bits
    private final boolean isAuthoritative; // Authoritative Answer (AA) - 1 bit
    private final boolean isTruncated; // Truncation (TC) - 1 bit
    private final boolean isRecursionDesired; // Recursion Desired (RD) - 1 bit
    private final boolean isRecursionAvailable; // Recursion Available (RA) - 1 bit
    private final byte responseCode; // Response Code (RCODE) - 4 bits
    private final short questionCount; // Question Count (QDCOUNT) - 16 bits
    private final short answerRecordsCount; // Answer Record Count (ANCOUNT) - 16 bits
    private final short authorityRecordsCount; // Authority Record Count (NSCOUNT) - 16 bits
    private final short additionalRecordsCount; // Additional Record Count (ARCOUNT) - 16 bits

    private DnsHeader(DnsHeader.Builder builder) {
        Objects.requireNonNull(builder.qrIndicator, "PacketIndicator must not be null.");

        this.identifier = builder.identifier;
        this.qrIndicator = builder.qrIndicator;
        this.operationCode = builder.operationCode;
        this.isAuthoritative = builder.isAuthoritative;
        this.isTruncated = builder.isTruncated;
        this.isRecursionDesired = builder.isRecursionDesired;
        this.isRecursionAvailable = builder.isRecursionAvailable;
        this.responseCode = builder.responseCode;
        this.questionCount = builder.questionCount;
        this.answerRecordsCount = builder.answerRecordsCount;
        this.authorityRecordsCount = builder.authorityRecordsCount;
        this.additionalRecordsCount = builder.additionalRecordsCount;
    }

    public byte[] getHeader() {
        return ByteBuffer
                .allocate(HEADER_SIZE_BYTES)
                .order(ByteOrder.BIG_ENDIAN)
                .putShort(identifier)
                .putShort(getFlags())
                .putShort(questionCount)
                .putShort(answerRecordsCount)
                .putShort(authorityRecordsCount)
                .putShort(additionalRecordsCount)
                .array();
    }

    private short getFlags() {
        short flags = 0;

        if (qrIndicator == DnsPacketIndicator.RESPONSE) {
            flags ^= (short) 0x8000;
        }

        flags ^= (short) ((operationCode & 0x0F) << 11);

        if (isAuthoritative) {
            flags ^= (short) 0x400;
        }

        if (isTruncated) {
            flags ^= (short) 0x200;
        }

        if (isRecursionDesired) {
            flags ^= (short) 0x100;
        }

        if (isRecursionAvailable) {
            flags ^= (short) 0x80;
        }

        flags ^= (short) (responseCode & 0x0F);

        return flags;
    }

    public static DnsHeader.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private short identifier;
        private DnsPacketIndicator qrIndicator;
        private byte operationCode;
        private boolean isAuthoritative;
        private boolean isTruncated;
        private boolean isRecursionDesired;
        private boolean isRecursionAvailable;
        private byte responseCode;
        private short questionCount;
        private short answerRecordsCount;
        private short authorityRecordsCount;
        private short additionalRecordsCount;

        public Builder withIdentifier(short identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder forQRIndicator(DnsPacketIndicator qrIndicator) {
            this.qrIndicator = qrIndicator;
            return this;
        }

        public Builder withOperationCode(byte operationCode) {
            this.operationCode = operationCode;
            return this;
        }

        public Builder isAuthoritative(boolean isAuthoritative) {
            this.isAuthoritative = isAuthoritative;
            return this;
        }

        public Builder isTruncated(boolean isTruncated) {
            this.isTruncated = isTruncated;
            return this;
        }

        public Builder isRecursionDesired(boolean isRecursionDesired) {
            this.isRecursionDesired = isRecursionDesired;
            return this;
        }

        public Builder isRecursionAvailable(boolean isRecursionAvailable) {
            this.isRecursionAvailable = isRecursionAvailable;
            return this;
        }

        public Builder withResponseCode(byte responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        public Builder withQuestionCount(short questionCount) {
            this.questionCount = questionCount;
            return this;
        }

        public Builder withAnswerRecordsCount(short answerRecordsCount) {
            this.answerRecordsCount = answerRecordsCount;
            return this;
        }

        public Builder withAuthorityRecordsCount(short authorityRecordsCount) {
            this.authorityRecordsCount = authorityRecordsCount;
            return this;
        }

        public Builder withAdditionalRecordsCount(short additionalRecordsCount) {
            this.additionalRecordsCount = additionalRecordsCount;
            return this;
        }

        public DnsHeader build() {
            return new DnsHeader(this);
        }

    }

}
