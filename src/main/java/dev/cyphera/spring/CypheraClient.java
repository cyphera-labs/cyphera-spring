package dev.cyphera.spring;

import java.util.Map;

/**
 * Main Cyphera client — inject this into your Spring beans.
 *
 * <pre>
 * &#64;Autowired
 * private CypheraClient cyphera;
 *
 * String encrypted = cyphera.protect("ssn", "123-45-6789");
 * String decrypted = cyphera.unprotect("ssn", encrypted);
 * </pre>
 *
 * Currently uses a dummy cipher. Will delegate to cyphera-java when available.
 */
public class CypheraClient {

    private final Map<String, PolicyEntry> policies;
    private final Map<String, KeyEntry> keys;

    CypheraClient(Map<String, PolicyEntry> policies, Map<String, KeyEntry> keys) {
        this.policies = policies;
        this.keys = keys;
    }

    /**
     * Encrypt a value using a named policy.
     */
    public String protect(String policyName, String value) {
        PolicyEntry policy = policies.get(policyName);
        if (policy == null) {
            throw new IllegalArgumentException("Unknown policy: " + policyName);
        }
        String keyMaterial = resolveKey(policy.keyRef());
        return DummyCipher.encrypt(value, policy.alphabet(), keyMaterial);
    }

    /**
     * Decrypt a value using a named policy.
     */
    public String unprotect(String policyName, String value) {
        PolicyEntry policy = policies.get(policyName);
        if (policy == null) {
            throw new IllegalArgumentException("Unknown policy: " + policyName);
        }
        String keyMaterial = resolveKey(policy.keyRef());
        return DummyCipher.decrypt(value, policy.alphabet(), keyMaterial);
    }

    /**
     * Direct FF1 encrypt (no policy lookup).
     */
    public String ff1Encrypt(String value, String keyHex, String alphabet) {
        return DummyCipher.encrypt(value, alphabet, keyHex);
    }

    /**
     * Direct FF1 decrypt (no policy lookup).
     */
    public String ff1Decrypt(String value, String keyHex, String alphabet) {
        return DummyCipher.decrypt(value, alphabet, keyHex);
    }

    private String resolveKey(String keyRef) {
        KeyEntry key = keys.get(keyRef);
        return key != null ? key.material() : "";
    }

    record PolicyEntry(String engine, String alphabet, String keyRef) {}
    record KeyEntry(String material) {}
}
