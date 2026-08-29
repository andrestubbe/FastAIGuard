package fastaiguard.pii;

import java.util.regex.Pattern;

public final class PIIMasker {
    private static final Pattern EMAIL_PAT = Pattern.compile("(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}");
    private static final Pattern CREDIT_CARD_PAT = Pattern.compile("\\b(?:\\d{4}[ -]?){3}\\d{4}\\b");
    private static final Pattern IPV4_PAT = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
    private static final Pattern API_KEY_PAT = Pattern.compile("(?i)(?:sk-[a-zA-Z0-9]{20,}|ghp_[a-zA-Z0-9]{20,}|bearer\\s+[a-zA-Z0-9_.-]{20,})");

    public static String maskAll(String input) {
        if (input == null || input.isEmpty()) return input;
        String s = EMAIL_PAT.matcher(input).replaceAll("[REDACTED_EMAIL]");
        s = CREDIT_CARD_PAT.matcher(s).replaceAll("[REDACTED_CARD]");
        s = API_KEY_PAT.matcher(s).replaceAll("[REDACTED_SECRET]");
        s = IPV4_PAT.matcher(s).replaceAll("[REDACTED_IP]");
        return s;
    }
}