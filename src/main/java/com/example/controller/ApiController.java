package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "loginUrl", "/oauth2/authorization/github",
                "meUrl", "/api/me",
                "adminUrl", "/admin/ping",
                "logoutUrl", "/logout"
        );
    }

    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("login", principal.getAttribute("login"));
        res.put("id", principal.getAttribute("id"));
        res.put("name", principal.getAttribute("name"));
        res.put("roles", principal.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList()));
        return res;
    }

    @GetMapping("/admin/ping")
    public Map<String, Object> adminPing() {
        return Map.of("status", "ok", "message", "Вы админ");
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return me(principal);
    }

}