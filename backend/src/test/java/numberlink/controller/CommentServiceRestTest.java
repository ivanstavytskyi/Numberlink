package numberlink.controller;

import numberlink.entity.UserEntity;
import numberlink.exceptions.RestExceptionHandler;
import numberlink.repository.RatingRepository;
import numberlink.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentServiceRestTest {

    @Mock private RatingRepository ratingRepository;
    @Mock private AuthService authService;

    @InjectMocks private CommentServiceRest commentServiceRest;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(commentServiceRest)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void addComment_whenTextBlank_returns400() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        when(authService.requireCurrentUser()).thenReturn(user);

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"comment":"   "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failed"))
                .andExpect(jsonPath("$.message").value("empty comment specified"));
    }

    @Test
    void addComment_whenNoStarRating_returns400() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        when(authService.requireCurrentUser()).thenReturn(user);
        when(ratingRepository.findByUser_Id(user.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"comment":"nice game"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failed"))
                .andExpect(jsonPath("$.message").value("Choose a star rating before posting a review."));
    }
}
