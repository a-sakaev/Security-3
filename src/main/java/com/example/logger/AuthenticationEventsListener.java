package com.example.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationEventsListener {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationEventsListener.class);


    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event){
        log.info("AUTH SUCCESS: principal={}", event.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event){
        log.warn("AUTH FAILURE: principal={}, error={}",
                event.getAuthentication().getName(),
                event.getException().getClass().getSimpleName());
    }

}
