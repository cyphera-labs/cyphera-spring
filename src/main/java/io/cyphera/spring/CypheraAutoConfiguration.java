package io.cyphera.spring;

import io.cyphera.Cyphera;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoConfiguration
@EnableConfigurationProperties(CypheraProperties.class)
public class CypheraAutoConfiguration {

    private static final Logger LOG = Logger.getLogger(CypheraAutoConfiguration.class.getName());

    @Bean
    @ConditionalOnMissingBean
    public Cyphera cyphera(CypheraProperties properties) {
        try {
            Resource resource = new DefaultResourceLoader().getResource(properties.getConfigurationFile());
            try (InputStream in = resource.getInputStream()) {
                String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                Cyphera instance = Cyphera.fromFile(properties.getConfigurationFile());
                LOG.info("Cyphera SDK loaded from " + properties.getConfigurationFile());
                return instance;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to load Cyphera config: " + properties.getConfigurationFile(), e);
            throw new RuntimeException("Failed to load Cyphera config: " + properties.getConfigurationFile(), e);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public CypheraClient cypheraClient(Cyphera cyphera) {
        return new CypheraClient(cyphera);
    }
}
