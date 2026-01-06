package com.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class RevokeAuthorizedClientLogoutHandler implements LogoutHandler {

    private static final Logger log = LoggerFactory.getLogger(RevokeAuthorizedClientLogoutHandler.class);

    private final OAuth2AuthorizedClientService authorizedClientService;

    public RevokeAuthorizedClientLogoutHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauth) {
            String registrationId = oauth.getAuthorizedClientRegistrationId();
            String principalName = oauth.getName();
            authorizedClientService.removeAuthorizedClient(registrationId, principalName);
            log.info("REVOKE(local): removed authorized client. registrationId={}, principal={}", registrationId, principalName);
        }
    }
}