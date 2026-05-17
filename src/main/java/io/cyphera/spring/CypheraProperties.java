package io.cyphera.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Cyphera.
 *
 * <pre>
 * cyphera:
 *   configuration-file: classpath:cyphera.json
 *   # or
 *   server-url: https://cyphera.company.com/configurations
 * </pre>
 */
@ConfigurationProperties(prefix = "cyphera")
public class CypheraProperties {

    /** Path to the configuration JSON file. Supports classpath: and file: prefixes. */
    private String configurationFile = "classpath:cyphera.json";

    /** URL of a remote Cyphera configuration server (future). */
    private String serverUrl;

    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
