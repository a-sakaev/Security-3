package com.example.logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuditLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(AuditLogoutSuccessHandler.class);

    public AuditLogoutSuccessHandler() {
        setDefaultTargetUrl("/");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        String name = (authentication != null ? authentication.getName() : "anonymous");
        log.info("LOGOUT: principal={}", name);

        super.onLogoutSuccess(request, response, authentication);
    }
}
