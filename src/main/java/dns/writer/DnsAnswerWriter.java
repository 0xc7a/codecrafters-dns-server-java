package dns.writer;

import dns.env.DnsClass;
import dns.env.DnsType;
import dns.env.Environment;
import dns.message.DnsAnswer;
import dns.message.DnsLabel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class DnsAnswerWriter extends Writer<DnsAnswer> {

    public DnsAnswerWriter(DnsAnswer data) {
        this.data = data;
    }

    @Override
    public byte[] write() {
        List<byte[]> labelsList = data.getLabels().stream().map(DnsLabel::getLabel).toList();

        int size = labelsList.stream().mapToInt(l -> l.length).sum()
                + 1 /* null byte */
                + DnsType.TYPE_SIZE_BYTES
                + DnsClass.CLASS_SIZE_BYTES
                + 4 /* ttl */
                + 2 /* rdlength */
                + 4; /* rdata */

        ByteBuffer buffer = ByteBuffer
                .allocate(size)
                .order(ByteOrder.BIG_ENDIAN);

        labelsList.forEach(buffer::put);

        return buffer
                .put(Environment.NULL_BYTE)
                .putShort(data.getDnsType().getValue())
                .putShort(data.getDnsClass().getValue())
                .putInt(data.getTtl())
                .putShort((short) data.getData().length)
                .put(data.getData())
                .array();
    }

}
