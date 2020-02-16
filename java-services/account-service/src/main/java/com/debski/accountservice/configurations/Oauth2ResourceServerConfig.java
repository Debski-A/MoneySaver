package com.debski.accountservice.configurations;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConfigurationProperties("security.oauth2.client")
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Setter
    private String checkTokenEndpointUrl;
    @Setter
    private String clientId;
    @Setter
    private String clientSecret;

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setClientId(clientId);
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
        tokenService.setClientSecret(clientSecret);
        return tokenService;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/create")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
