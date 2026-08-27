package numberlink.controller;

import numberlink.entity.LocalUserEntity;
import numberlink.entity.UserEntity;
import numberlink.repository.LocalUserRepository;
import numberlink.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
@Testcontainers
class AuthLoginHttpIT {

    private static final String PASSWORD = "Password!1";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired RestTestClient http;
    @Autowired UserRepository userRepository;
    @Autowired LocalUserRepository localUserRepository;
    @Autowired PasswordEncoder passwordEncoder;

    private String username;
    private String email;

    @BeforeEach
    void insertVerifiedUser() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        username = "http" + suffix;
        email = username + "@test.local";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(Instant.now());
        user.setCreatedAt(Instant.now());
        user = userRepository.saveAndFlush(user);

        LocalUserEntity local = new LocalUserEntity();
        local.setUser(user);
        local.setEncodedPassword(passwordEncoder.encode(PASSWORD));
        localUserRepository.saveAndFlush(local);
    }

    @AfterEach
    void deleteUser() {
        userRepository.findByUsernameIgnoreCase(username).ifPresent(user -> {
            localUserRepository.findByUserId(user.getId()).ifPresent(localUserRepository::delete);
            userRepository.delete(user);
        });
    }

    @Test
    void login_whenEmailVerified_establishesSessionAndReturnsMe() {
        EntityExchangeResult<byte[]> login = http.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("login", username, "password", PASSWORD))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.username").isEqualTo(username)
                .jsonPath("$.emailVerified").isEqualTo(true)
                .returnResult();

        String cookie = cookieHeader(login.getResponseHeaders());
        assertThat(cookie).contains("JSESSIONID=");

        http.get()
                .uri("/api/me")
                .header(HttpHeaders.COOKIE, cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo(username)
                .jsonPath("$.email").isEqualTo(email);
    }

    private static String cookieHeader(HttpHeaders headers) {
        List<String> setCookies = headers.get(HttpHeaders.SET_COOKIE);
        assertThat(setCookies).isNotEmpty();
        return setCookies.stream()
                .map(value -> value.split(";", 2)[0])
                .collect(Collectors.joining("; "));
    }
}
