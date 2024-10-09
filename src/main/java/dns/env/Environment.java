package dns.env;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public final class Environment {

    private static class LazyHolder {
        public static Environment INSTANCE = new Environment();
    }

    private static final int port = 2053;

    private static final int bufferSize = 512;

    private static final byte nullByte = 0;

    private SocketAddress forwardSocketAddress;

    private Environment() {
    }

    public static Environment getInstance() {
        return LazyHolder.INSTANCE;
    }

    public int getPort() {
        return port;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public byte getNullByte() {
        return nullByte;
    }

    public SocketAddress getForwardAddress() {
        return forwardSocketAddress;
    }

    public void parseArgs(String[] args) {
        if (args.length == 0) {
            return;
        }

        for (int i = 0; i < args.length; i += 2) {
            if (!args[i].isBlank() && args[i].equals("--resolver")) {
                String address = args[i + 1];
                String[] parts = address.split(":");
                try {
                    this.forwardSocketAddress = new InetSocketAddress(InetAddress.getByName(parts[0]), parts.length == 1 ? 53 : Integer.parseInt(parts[1]));
                } catch (UnknownHostException e) {
                    e.printStackTrace(System.err);
                }
                break;
            }
        }
    }

}
