package numberlink.controller;

import numberlink.entity.UserEntity;
import numberlink.exceptions.RestExceptionHandler;
import numberlink.service.auth.AuthService;
import numberlink.service.jpa.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ScoreServiceRestTest {

    @Mock private ScoreService scoreService;
    @Mock private AuthService authService;

    @InjectMocks private ScoreServiceRest scoreServiceRest;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(scoreServiceRest)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void addScore_whenNoMapInSession_returns400() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("player");
        when(authService.requireCurrentUser()).thenReturn(user);

        mockMvc.perform(post("/api/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"elapsedSeconds":10}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failed"))
                .andExpect(jsonPath("$.message").value("No active map in session"));
    }
}
