import dns.server.DnsServer;

public class Main {

    public static void main(String[] args) {
        DnsServer server = new DnsServer();
        server.start();
    }

}
