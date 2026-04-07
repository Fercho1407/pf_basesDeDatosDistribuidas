package bdd.uam.mx.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.datasource.rural")
public class RuralDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
