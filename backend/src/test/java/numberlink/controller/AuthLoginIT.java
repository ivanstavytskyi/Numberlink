package numberlink.controller;

import numberlink.entity.LocalUserEntity;
import numberlink.entity.UserEntity;
import numberlink.repository.LocalUserRepository;
import numberlink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Transactional
class AuthLoginIT {

    private static final String USERNAME = "itplayer";
    private static final String PASSWORD = "Password!1";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired LocalUserRepository localUserRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void insertVerifiedUser() {
        UserEntity user = new UserEntity();
        user.setUsername(USERNAME);
        user.setEmail("itplayer@test.local");
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(Instant.now());
        user.setCreatedAt(Instant.now());
        user = userRepository.saveAndFlush(user);

        LocalUserEntity local = new LocalUserEntity();
        local.setUser(user);
        local.setEncodedPassword(passwordEncoder.encode(PASSWORD));
        localUserRepository.saveAndFlush(local);
    }

    @Test
    void login_whenEmailVerified_establishesSessionAndReturnsMe() throws Exception {
        MvcResult login = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login":"%s","password":"%s"}
                                """.formatted(USERNAME, PASSWORD)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.emailVerified").value(true))
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession();

        mockMvc.perform(get("/api/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value("itplayer@test.local"));
    }
}
