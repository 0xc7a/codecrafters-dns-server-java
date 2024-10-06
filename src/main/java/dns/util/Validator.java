package dns.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

public final class Validator {

    /**
     * @param value Domain name in question.
     * @return List containing the second-level domain and the top-level domain.
     * @throws IllegalArgumentException Throws IllegalArgumentException if the domain name is invalid.
     */
    public static List<String> validateDomain(String value) {
        Matcher matcher = Patterns.PATTERN_DOMAIN_NAME.get().matcher(value);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid domain name %s.".formatted(value));
        }
        return IntStream
                .range(1, matcher.groupCount() + 1)
                .mapToObj(matcher::group)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(v -> v.replace('.', ' '))
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
