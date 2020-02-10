package com.debski.calculationservice.configurations;

import feign.RequestInterceptor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.Arrays;

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
    @Setter
    private String accessTokenUri;
    @Setter
    private String grantType;
    @Setter
    private String scope;

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        ClientCredentialsResourceDetails ccrd = new ClientCredentialsResourceDetails();
        ccrd.setClientId(clientId);
        ccrd.setClientSecret(clientSecret);
        ccrd.setAccessTokenUri(accessTokenUri);
        ccrd.setGrantType(grantType);
        ccrd.setScope(Arrays.asList(scope));
        return ccrd;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor = new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
        return oAuth2FeignRequestInterceptor;
    }

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails());
        return oAuth2RestTemplate;
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setClientId(clientId);
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
        tokenService.setClientSecret(clientSecret);
        return tokenService;
    }

}
