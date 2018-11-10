package me.whiteship.ksug201811restapi.accounts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("my-app")
@Getter @Setter
public class MyAppProperties {

    private String clientId;

    private String clientSecret;

}
