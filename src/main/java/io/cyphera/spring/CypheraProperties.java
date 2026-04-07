package io.cyphera.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Cyphera.
 *
 * <pre>
 * cyphera:
 *   policy-file: classpath:cyphera.yaml
 *   # or
 *   server-url: https://cyphera.company.com/policies
 * </pre>
 */
@ConfigurationProperties(prefix = "cyphera")
public class CypheraProperties {

    /** Path to the policy YAML file. Supports classpath: and file: prefixes. */
    private String policyFile = "classpath:cyphera.yaml";

    /** URL of a remote Cyphera policy server (future). */
    private String serverUrl;

    public String getPolicyFile() {
        return policyFile;
    }

    public void setPolicyFile(String policyFile) {
        this.policyFile = policyFile;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
