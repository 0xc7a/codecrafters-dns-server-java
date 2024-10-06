package dns.util;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class Patterns {

    public static final Supplier<Pattern> PATTERN_DOMAIN_NAME =
            () -> Pattern.compile("^(?:https?://)?(?:[a-z]{3}\\.)?([a-z0-9]+(?:-[a-z0-9]+)*)\\.([a-z0-9]+)$");

    public static final Supplier<Pattern> PATTERN_IPV4 = () ->
            Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$");

}
