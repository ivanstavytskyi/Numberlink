package numberlink.controller;

import numberlink.dto.user.password.request.ChangePasswordRequestDto;
import numberlink.dto.user.profile.request.UpdateProfileRequestDto;
import numberlink.dto.user.profile.response.UserProfileDto;
import numberlink.service.auth.AuthService;
import numberlink.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
public class ProfileController {

    private final UserService userService;
    private final AuthService authService;

    public ProfileController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDto dto
    ) {
        return ResponseEntity.ok(userService.updateProfile(dto));
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto dto,
            HttpServletRequest request
    ) {
        authService.changePassword(dto.currentPassword(), dto.newPassword(), request);
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "message", "Password updated."
        ));
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDto> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadAvatar(file));
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<UserProfileDto> deleteAvatar() {
        return ResponseEntity.ok(userService.deleteAvatar());
    }
}
