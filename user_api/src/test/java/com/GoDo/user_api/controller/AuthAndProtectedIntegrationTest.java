package com.GoDo.user_api.controller;

import com.GoDo.user_api.model.User;
import com.GoDo.user_api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthAndProtectedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void registerLoginAndGetMe() throws Exception {
        userRepository.deleteAll();

        String email = "ituser@example.local";
        String payload = String.format("{\"email\":\"%s\",\"name\":\"IT\",\"surname\":\"Test\",\"password\":\"pw\"}", email);

        // register
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());

        // login
        String loginPayload = String.format("{\"email\":\"%s\",\"password\":\"pw\"}", email);
        String loginResp = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> parsed = mapper.readValue(loginResp, Map.class);
        assertThat(parsed).containsKey("jwt");
        String jwt = (String) parsed.get("jwt");

        // call /api/users/me
        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    public void userCannotListAllUsers_butAdminCan() throws Exception {
        userRepository.deleteAll();

        // create normal user
        User normal = new User();
        normal.setEmail("normal@example.local");
        normal.setName("Normal");
        normal.setSurname("User");
        normal.setPassword(passwordEncoder.encode("pw"));
        normal.setRole("ROLE_USER");
        userRepository.save(normal);

        // login normal
        String loginPayload = "{\"email\":\"normal@example.local\",\"password\":\"pw\"}";
        String loginResp = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> parsed = mapper.readValue(loginResp, Map.class);
        String userJwt = (String) parsed.get("jwt");

        // normal user should receive 403 when listing all users
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + userJwt))
                .andExpect(status().isForbidden());

        // create admin user directly
        User admin = new User();
        admin.setEmail("admin@example.local");
        admin.setName("Admin");
        admin.setSurname("User");
        admin.setPassword(passwordEncoder.encode("adminpw"));
        admin.setRole("ROLE_ADMIN");
        userRepository.save(admin);

        // login admin
        String adminLogin = "{\"email\":\"admin@example.local\",\"password\":\"adminpw\"}";
        String adminResp = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminLogin))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> adminParsed = mapper.readValue(adminResp, Map.class);
        String adminJwt = (String) adminParsed.get("jwt");

        // admin can list users
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + adminJwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
    }
}
