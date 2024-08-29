package ee.smit.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiConfig {
    private List<ApiEndpointConfig> endpoints;

    public List<ApiEndpointConfig> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<ApiEndpointConfig> endpoints) {
        this.endpoints = endpoints;
    }
}
