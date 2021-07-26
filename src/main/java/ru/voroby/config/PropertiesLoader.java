package ru.voroby.config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.util.Properties;

@ApplicationScoped
public class PropertiesLoader {

    private final Properties properties = new Properties();

    @PostConstruct
    private void loadProperties() {
        try {
            properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Configuration properties were not loaded properly");
        }
    }

    @Produces
    @Config("")
    public String exposeConfig(InjectionPoint injectionPoint) {
        Config config = injectionPoint.getAnnotated().getAnnotation(Config.class);
        return config != null ?
                properties.getProperty(config.value()) : null;
    }

}
