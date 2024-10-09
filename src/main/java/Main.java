import dns.env.Environment;
import dns.server.DnsServer;

public class Main {

    public static void main(String[] args) {
        Environment.getInstance().parseArgs(args);
        DnsServer server = new DnsServer();
        server.start();
    }

}
