package dev.cyphera.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoConfiguration
@EnableConfigurationProperties(CypheraProperties.class)
public class CypheraAutoConfiguration {

    private static final Logger LOG = Logger.getLogger(CypheraAutoConfiguration.class.getName());

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public CypheraClient cypheraClient(CypheraProperties properties) {
        Map<String, CypheraClient.PolicyEntry> policies = new HashMap<>();
        Map<String, CypheraClient.KeyEntry> keys = new HashMap<>();

        try {
            Resource resource = new DefaultResourceLoader().getResource(properties.getPolicyFile());
            try (InputStream in = resource.getInputStream()) {
                Yaml yaml = new Yaml();
                Map<String, Object> root = yaml.load(in);

                Map<String, Map<String, String>> rawKeys = (Map<String, Map<String, String>>)
                        root.getOrDefault("keys", Map.of());
                for (var entry : rawKeys.entrySet()) {
                    keys.put(entry.getKey(),
                            new CypheraClient.KeyEntry(entry.getValue().getOrDefault("material", "")));
                }

                Map<String, Map<String, String>> rawPolicies = (Map<String, Map<String, String>>)
                        root.getOrDefault("policies", Map.of());
                for (var entry : rawPolicies.entrySet()) {
                    Map<String, String> def = entry.getValue();
                    policies.put(entry.getKey(), new CypheraClient.PolicyEntry(
                            def.getOrDefault("engine", "ff1"),
                            def.getOrDefault("alphabet", "digits"),
                            def.getOrDefault("key_ref", "")));
                }
                LOG.info("Cyphera: loaded " + policies.size() + " policies from " + properties.getPolicyFile());
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Cyphera: could not load policy file: " + properties.getPolicyFile(), e);
        }

        return new CypheraClient(policies, keys);
    }
}
