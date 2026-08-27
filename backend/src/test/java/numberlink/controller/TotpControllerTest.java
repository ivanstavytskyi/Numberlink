package numberlink.controller;

import numberlink.entity.UserEntity;
import numberlink.exceptions.InvalidTotpCodeException;
import numberlink.exceptions.NotAuthenticatedException;
import numberlink.exceptions.RestExceptionHandler;
import numberlink.exceptions.TotpAlreadyEnabledException;
import numberlink.exceptions.TotpNotEnabledException;
import numberlink.exceptions.TotpSetupRequiredException;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.TotpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TotpControllerTest {

    @Mock private AuthService authService;
    @Mock private TotpService totpService;

    @InjectMocks private TotpController totpController;

    private MockMvc mockMvc;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(totpController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("player");
        user.setEmail("a@b.com");
    }

    @Test
    void setup_whenNotAuthenticated_returns401() throws Exception {
        when(authService.requireCurrentUser()).thenThrow(new NotAuthenticatedException());

        mockMvc.perform(post("/api/me/2fa/setup"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("NOT_AUTHENTICATED"));
    }

    @Test
    void setup_whenAlreadyEnabled_returns409() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);
        when(totpService.startSetup(user)).thenThrow(new TotpAlreadyEnabledException());

        mockMvc.perform(post("/api/me/2fa/setup"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("TOTP_ALREADY_ENABLED"));
    }

    @Test
    void setup_whenOk_returnsSecret() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);
        when(totpService.startSetup(user)).thenReturn(Map.of(
                "secret", "ABCD EFGH",
                "otpauthUrl", "otpauth://totp/NumberLink:a@b.com"
        ));

        mockMvc.perform(post("/api/me/2fa/setup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secret").value("ABCD EFGH"));
    }

    @Test
    void confirm_whenCodeBlank_returns400() throws Exception {
        mockMvc.perform(post("/api/me/2fa/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void confirm_whenSetupMissing_returns400() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);
        doThrow(new TotpSetupRequiredException()).when(totpService).confirmSetup(eq(user), eq("123456"));

        mockMvc.perform(post("/api/me/2fa/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"123456"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TOTP_SETUP_REQUIRED"));
    }

    @Test
    void confirm_whenCodeInvalid_returns400() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);
        doThrow(new InvalidTotpCodeException()).when(totpService).confirmSetup(any(), any());

        mockMvc.perform(post("/api/me/2fa/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"000000"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_TOTP_CODE"));
    }

    @Test
    void confirm_whenOk_returnsEnabled() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);

        mockMvc.perform(post("/api/me/2fa/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"123456"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.twoFactorEnabled").value(true));
    }

    @Test
    void disable_whenNotEnabled_returns400() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);
        doThrow(new TotpNotEnabledException()).when(totpService).disable(any(), any());

        mockMvc.perform(post("/api/me/2fa/disable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"123456"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TOTP_NOT_ENABLED"));
    }

    @Test
    void cancelSetup_whenOk_returnsDisabled() throws Exception {
        when(authService.requireCurrentUser()).thenReturn(user);

        mockMvc.perform(delete("/api/me/2fa/setup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.twoFactorEnabled").value(false));
    }
}
