package com.debski.authservice.configurations;

import com.debski.authservice.services.CustomWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService defaultUserDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Environment env;

    @Autowired
    private CustomWebResponseExceptionTranslator oauth2ResponseExceptionTranslator;

    @Autowired
    @Qualifier("tokenDataSource")
    private DataSource tokenDataSource;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient("browser")
                // password grant is not supported by default, see configure(...) method below
                .authorizedGrantTypes("refresh_token", "password")
                .scopes("ui")
                .and()
                .withClient("account-service")
                .secret(encoder.encode(env.getProperty("ACCOUNT_SERVICE_PASSWORD")))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(tokenDataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .exceptionTranslator(oauth2ResponseExceptionTranslator)
                .tokenStore(tokenStore())
                // By default all grant types are supported except password
                // password grants are switched on by injecting an AuthenticationManager
                .authenticationManager(authenticationManager)
                // userDetailsService is needed for refreshToken, otherwise it wont work
                .userDetailsService(defaultUserDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        OAuth2AccessDeniedHandler auth2AccessDeniedHandler = new OAuth2AccessDeniedHandler();
        auth2AccessDeniedHandler.setExceptionTranslator(oauth2ResponseExceptionTranslator);
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionTranslator(oauth2ResponseExceptionTranslator);

        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .accessDeniedHandler(auth2AccessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .passwordEncoder(encoder);
    }

}
