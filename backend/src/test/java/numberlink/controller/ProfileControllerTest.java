package numberlink.controller;

import numberlink.exceptions.AvatarTooLargeException;
import numberlink.exceptions.RestExceptionHandler;
import numberlink.service.auth.AuthService;
import numberlink.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock private UserService userService;
    @Mock private AuthService authService;

    @InjectMocks private ProfileController profileController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(profileController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void uploadAvatar_whenFileTooLarge_returns413() throws Exception {
        when(userService.uploadAvatar(any())).thenThrow(new AvatarTooLargeException());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                new byte[] {1, 2, 3});

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/me/avatar").file(file))
                .andExpect(status().isContentTooLarge())
                .andExpect(jsonPath("$.code").value("AVATAR_TOO_LARGE"));
    }
}
