package io.cyphera.spring;

import io.cyphera.Cyphera;

/**
 * Main Cyphera client -- inject this into your Spring beans.
 *
 * <pre>
 * &#64;Autowired
 * private CypheraClient cyphera;
 *
 * String encrypted = cyphera.protect("123-45-6789", "ssn");
 * String decrypted = cyphera.access(encrypted);
 * </pre>
 *
 * Delegates to the real Cyphera SDK. Argument order matches the SDK
 * (value-first for protect; value-first for the 2-arg access escape hatch).
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
     * Protect a value using a named configuration.
     */
    public String protect(String value, String configurationName) {
        return cyphera.protect(value, configurationName);
    }

    /**
     * Access a header-prefixed protected value. Primary form — the embedded header
     * identifies which configuration to use.
     */
    public String access(String protectedValue) {
        return cyphera.access(protectedValue);
    }

    /**
     * Access a protected value with an explicit configuration name.
     * Escape hatch for headerless configurations.
     */
    public String access(String protectedValue, String configurationName) {
        return cyphera.access(protectedValue, configurationName);
    }
}
