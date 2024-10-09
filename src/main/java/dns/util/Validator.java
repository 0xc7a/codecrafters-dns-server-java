package dns.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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

}
