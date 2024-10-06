package dns.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;

public final class Validator {

    /**
     * @param value Domain name in question.
     * @return List containing the subdomains (if any), the second-level domain and the top-level domain.
     * @throws IllegalArgumentException Throws IllegalArgumentException if the domain is invalid.
     */
    public static List<String> validateDomain(String value) {
        String[] parts = value.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid domain name %s.".formatted(value));
        }
        return Arrays.stream(parts)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .toList();
    }

    /**
     * @param value IP in question.
     * @return byte array containing the IP octets.
     * @throws IllegalArgumentException Throws IllegalArgumentException if the IP is invalid.
     */
    public static byte[] validateIPv4(String value) {
        Matcher matcher = Patterns.PATTERN_IPV4.get().matcher(value);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid IPv4 address.");
        }
        return ByteBuffer.allocate(4)
                .order(ByteOrder.BIG_ENDIAN)
                .put((byte) Integer.parseInt(matcher.group(1)))
                .put((byte) Integer.parseInt(matcher.group(2)))
                .put((byte) Integer.parseInt(matcher.group(3)))
                .put((byte) Integer.parseInt(matcher.group(4)))
                .array();
    }

}
