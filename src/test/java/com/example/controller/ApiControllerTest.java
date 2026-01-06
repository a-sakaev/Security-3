package com.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void home_permitAll_ok200() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void me_unauthenticated_401() throws Exception {
        mvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void me_oauth2User_ok200_andHasFields() throws Exception {
        mvc.perform(get("/api/me")
                        .with(oauth2Login()
                                .attributes(a -> {
                                    a.put("login", "testuser");
                                    a.put("id", 123);
                                    a.put("name", "Test User");
                                })
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"))
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    void userAlias_oauth2User_ok200() throws Exception {
        mvc.perform(get("/user")
                        .with(oauth2Login()
                                .attributes(a -> {
                                    a.put("login", "testuser");
                                    a.put("id", 123);
                                    a.put("name", "Test User");
                                })
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    void adminPing_asUser_forbidden403() throws Exception {
        mvc.perform(get("/admin/ping")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "user"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPing_asAdmin_ok200() throws Exception {
        mvc.perform(get("/admin/ping")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "admin"))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }
}