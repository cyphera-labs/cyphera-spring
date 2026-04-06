package dev.cyphera.spring;

/**
 * Placeholder cipher — reversible character shift within an alphabet.
 * Will be replaced with real FF1 FPE via cyphera-java.
 */
final class DummyCipher {

    private static final String DIGITS = "0123456789";
    private static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private DummyCipher() {}

    static String resolveAlphabet(String name) {
        return switch (name.toLowerCase()) {
            case "digits" -> DIGITS;
            case "alpha_lower" -> ALPHA_LOWER;
            case "alphanumeric" -> ALPHANUMERIC;
            default -> DIGITS;
        };
    }

    private static int deriveShift(String keyHex) {
        int h = 0;
        for (int i = 0; i < keyHex.length(); i++) {
            h = 31 * h + keyHex.charAt(i);
        }
        return Math.abs(h) % 256 + 1;
    }

    static String encrypt(String input, String alphabetName, String keyHex) {
        String alpha = resolveAlphabet(alphabetName);
        int shift = deriveShift(keyHex);
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int idx = alpha.indexOf(c);
            if (idx >= 0) {
                sb.append(alpha.charAt((idx + shift) % alpha.length()));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String decrypt(String input, String alphabetName, String keyHex) {
        String alpha = resolveAlphabet(alphabetName);
        int shift = deriveShift(keyHex);
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int idx = alpha.indexOf(c);
            if (idx >= 0) {
                sb.append(alpha.charAt(((idx - shift % alpha.length()) + alpha.length()) % alpha.length()));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
