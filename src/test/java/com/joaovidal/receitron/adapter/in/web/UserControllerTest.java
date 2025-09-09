package com.joaovidal.receitron.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    String email = "tester@email.com";
    String password = "supersecretpass";

    @Test
    @WithMockUser(username = "tester@email.com", roles = {"USER"})
    void shouldAddCultures() throws Exception {
        User user = new User(UUID.randomUUID(),email, password, Set.of("USER"));
        var cultures = List.of("Brazilian", "Italian");

        when(userService.addCultures("tester@email.com", cultures)).thenReturn(user);

        mockMvc.perform(post("/user/add/cultures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(cultures)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "tester@email.com", roles = {"USER"})
    void shouldAddPreferences() throws Exception {
        User user = new User(UUID.randomUUID(),email, password, Set.of("USER"));
        var preferences = List.of("Vegan", "Vegetarian");

        when(userService.addPreferences("tester@email.com", preferences)).thenReturn(user);

        mockMvc.perform(post("/user/add/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(preferences)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "tester@email.com", roles = {"USER"})
    void shouldAddRestrictions() throws Exception {
        User user = new User(UUID.randomUUID(),email, password, Set.of("USER"));
        var restrictions = List.of("pork", "lamb");

        when(userService.addRestrictions("tester@email.com", restrictions)).thenReturn(user);

        mockMvc.perform(post("/user/add/restrictions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restrictions)))
                .andExpect(status().isOk());
    }

    //TODO: TEST ERRORS
}
