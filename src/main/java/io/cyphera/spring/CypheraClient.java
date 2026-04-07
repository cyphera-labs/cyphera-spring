package io.cyphera.spring;

import io.cyphera.Cyphera;

/**
 * Main Cyphera client -- inject this into your Spring beans.
 *
 * <pre>
 * &#64;Autowired
 * private CypheraClient cyphera;
 *
 * String encrypted = cyphera.protect("ssn", "123-45-6789");
 * String decrypted = cyphera.access("ssn", encrypted);
 * </pre>
 *
 * Delegates to the real Cyphera SDK.
 */
public class CypheraClient {

    private final Cyphera cyphera;

    CypheraClient(Cyphera cyphera) {
        this.cyphera = cyphera;
    }

    /**
     * Returns the underlying Cyphera SDK instance for direct use.
     */
    public Cyphera sdk() {
        return cyphera;
    }

    /**
     * Protect a value using a named policy.
     */
    public String protect(String policyName, String value) {
        return cyphera.protect(value, policyName);
    }

    /**
     * Access a protected value using a named policy.
     */
    public String access(String policyName, String protectedValue) {
        return cyphera.access(protectedValue, policyName);
    }

    /**
     * Access a tag-encoded protected value (no policy name needed).
     */
    public String access(String protectedValue) {
        return cyphera.access(protectedValue);
    }
}
