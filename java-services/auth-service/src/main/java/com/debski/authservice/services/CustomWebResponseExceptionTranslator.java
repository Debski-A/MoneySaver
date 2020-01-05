package com.debski.authservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity responseEntity = super.translate(e);
        OAuth2Exception auth2Exception = (OAuth2Exception)responseEntity.getBody();
        if (auth2Exception != null) {
            String messageSourceKey = auth2Exception.getOAuth2ErrorCode();
            auth2Exception.addAdditionalInformation("errorMessage", messageSource.getMessage(messageSourceKey, null, LocaleContextHolder.getLocale()));
        }
        return new ResponseEntity<OAuth2Exception>(auth2Exception, responseEntity.getHeaders(), responseEntity.getStatusCode());
    }
}
