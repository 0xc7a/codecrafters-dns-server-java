package dns.writer;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.env.Environment;
import dns.message.DnsLabel;
import dns.message.DnsQuestion;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class DnsQuestionWriter extends Writer<DnsQuestion> {

    public DnsQuestionWriter(DnsQuestion data) {
        this.data = data;
    }

    @Override
    public byte[] write() {
        List<byte[]> labelsList = data.getLabels().stream().map(DnsLabel::getLabel).toList();

        int size = labelsList.stream().mapToInt(l -> l.length).sum()
                + 1 /* null byte */
                + DnsType.TYPE_SIZE_BYTES
                + DnsClass.CLASS_SIZE_BYTES;

        ByteBuffer buffer = ByteBuffer
                .allocate(size)
                .order(ByteOrder.BIG_ENDIAN);

        labelsList.forEach(buffer::put);

        return buffer
                .put(Environment.getInstance().getNullByte())
                .putShort(data.getDnsType().getValue())
                .putShort(data.getDnsClass().getValue())
                .array();
    }

}
