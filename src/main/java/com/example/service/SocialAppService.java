package com.example.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String ADMIN_LOGIN = "a-sakaev";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        Map<String, Object> attrs = oauth2User.getAttributes();
        String login = (String) attrs.get("login");

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (ADMIN_LOGIN.equalsIgnoreCase(login)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new DefaultOAuth2User(authorities, attrs, "id");
    }
}
