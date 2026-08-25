package numberlink.controller;

import numberlink.entity.UserEntity;
import numberlink.exceptions.*;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.OauthService;
import numberlink.service.auth.TotpService;
import numberlink.service.user.EmailChangeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    @Mock private EmailChangeService emailChangeService;
    @Mock private OauthService oauthService;
    @Mock private TotpService totpService;

    @InjectMocks private AuthController authController;
    /**
    new AuthController(authService, emailChangeService, oauthService, totpService)
     */

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void login_whenCredentialsInvalid_returns401() throws Exception {
        when(authService.login(any(), any(), any()))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login":"player","password":"WrongPassword!1"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void login_whenEmailNotVerified_returns403() throws Exception {
        String email = "a@b.com";
        when(authService.login(any(), any(), any()))
                .thenThrow(new EmailNotVerifiedException(email));

        mockMvc.perform(post("/api/login")
        .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"login":"%s","password":"Password!1"}
                        """.formatted(email)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("EMAIL_NOT_VERIFIED"))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void login_whenTwoFactorRequired_returns200() throws Exception {
        when(authService.login(any(), any(), any()))
                .thenReturn(new AuthService.LoginOutcome(null, true));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login":"player","password":"Password!1"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.twoFactorRequired").value(true));
    }

    @Test
    void login_whenCredentialsValid_returns201() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("player");
        user.setEmail("a@b.com");
        when(authService.login(any(), any(), any()))
                .thenReturn(new AuthService.LoginOutcome(user, false));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login":"player","password":"Password!1"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void login_whenLoginBlank_returns400() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"login":"","password":"Password!1"}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void loginTwoFactor_whenInvalidTotpCode_returns400() throws Exception {
        when(authService.completeTwoFactorLogin(any(), any(), any()))
                .thenThrow(new InvalidTotpCodeException());

        mockMvc.perform(post("/api/login/2fa")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {"code":"123456"}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_TOTP_CODE"));
    }

    @Test
    void loginTwoFactor_whenTotpChallengeExpired_returns401() throws Exception {
        when(authService.completeTwoFactorLogin(any(), any(), any()))
                .thenThrow(new TotpChallengeExpiredException());

        mockMvc.perform(post("/api/login/2fa")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"code":"123456"}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("TOTP_CHALLENGE_EXPIRED"));
    }

    @Test
    void register_whenUsernameOrEmailAlreadyTaken_returns409() throws Exception {
        when(authService.register(any(), any(), any()))
                .thenThrow(new UsernameOrEmailTakenException());

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username":"user1234","email":"user1234@mail.com","password":"Password!1"}
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("USERNAME_OR_EMAIL_TAKEN"));
    }

    @Test
    void me_whenNotAuthenticated_returns401() throws Exception {
        when(authService.requireCurrentUser())
                .thenThrow(new NotAuthenticatedException());

        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("NOT_AUTHENTICATED"));
    }
}
