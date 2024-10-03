package dns.reply;

import dns.env.DnsPacketIndicator;

import java.nio.ByteBuffer;

// https://datatracker.ietf.org/doc/html/rfc1035#section-4.1.1
public class DnsHeader {

    private static final int HEADER_SIZE_BYTES = 12;

    private final short[] header;

    private DnsHeader(DnsHeader.Builder builder) {
        header = new short[HEADER_SIZE_BYTES >> 1];

        // Packet Identifier (ID) - 16 bits
        header[0] = builder.identifier;

        short flags = 0;

        // Query/Response Indicator (QR) - 1 bit
        if (builder.indicator == DnsPacketIndicator.REPLY) {
            flags |= (short) 0x8000;
        }

        // Operation Code (OPCODE) - 4 bits
        flags |= (short) ((builder.operationCode & 0x0F) << 11);

        // Authoritative Answer (AA) - 1 bit
        if (builder.isAuthoritative) {
            flags |= (short) 0x400;
        }

        // Truncation (TC) - 1 bit
        if (builder.isTruncated) {
            flags |= (short) 0x200;
        }

        // Recursion Desired (RD) - 1 bit
        if (builder.isRecursionDesired) {
            flags |= (short) 0x100;
        }

        // Recursion Available (RA) - 1 bit
        if (builder.isRecursionAvailable) {
            flags |= (short) 0x80;
        }

        // Response Code (RCODE) - 4 bits
        flags |= (short) (builder.responseCode & 0x0F);

        header[1] = flags;

        // Question Count (QDCOUNT) - 16 bits
        header[2] = builder.questionCount;

        // Answer Record Count (ANCOUNT) - 16 bits
        header[3] = builder.answerRecordsCount;

        // Authority Record Count (NSCOUNT) - 16 bits
        header[4] = builder.authorityRecordsCount;

        // Additional Record Count (ARCOUNT) - 16 bits
        header[5] = builder.additionalRecordsCount;
    }

    public byte[] getHeader() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE_BYTES);
        buffer.asShortBuffer().put(header);
        return buffer.array();
    }

    public static DnsHeader.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private short identifier;
        private DnsPacketIndicator indicator;
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

        public Builder forIndicator(DnsPacketIndicator indicator) {
            this.indicator = indicator;
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
