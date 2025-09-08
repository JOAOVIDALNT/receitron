package com.joaovidal.receitron.adapter.in.web;

import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.TokenProviderPort;
import com.joaovidal.receitron.domain.service.AuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Pra que cada teste tenha uma instância individual e única;
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService service;

    @Autowired
    private TokenProviderPort tokenProvider;

    String email = "tester@email.com";
    String password = "supersecretpass";

    @BeforeEach
    void setup() {
        User user = new User(UUID.randomUUID(),email, password, Set.of("USER"));
        service.register(user);
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        var json = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                """, email, password
        );
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowInvalidLogin() throws Exception {
        var json = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                """, "notatester@email.com", password
        );
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRegisterUser() throws Exception {
        var json = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                """, "othertester@email.com", "strongpass"
        );

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowUserAlreadyExistsOnRegistration() throws Exception {
        var json = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                """, email, "strongpass"
        );

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
