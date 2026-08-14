package numberlink.service.user;

import numberlink.dto.user.profile.request.UpdateProfileRequestDto;
import numberlink.dto.user.profile.response.UserProfileDto;
import numberlink.entity.UserEntity;
import numberlink.exceptions.UsernameTakenException;
import numberlink.repository.UserRepository;
import numberlink.service.auth.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final AvatarStorageService avatarStorageService;

    public UserService(
            UserRepository userRepository,
            AuthService authService,
            AvatarStorageService avatarStorageService
    ) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.avatarStorageService = avatarStorageService;
    }

    @Transactional
    public UserProfileDto updateProfile(UpdateProfileRequestDto dto) {
        UserEntity user = authService.requireCurrentUser();

        String username = dto.username().trim();

        if (!user.getUsername().equalsIgnoreCase(username)
                && userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UsernameTakenException();
        }

        user.setUsername(username);
        userRepository.save(user);

        return toProfileDto(user);
    }

    @Transactional
    public UserProfileDto uploadAvatar(MultipartFile file) {
        return toProfileDto(avatarStorageService.store(file));
    }

    @Transactional
    public UserProfileDto deleteAvatar() {
        return toProfileDto(avatarStorageService.clear());
    }

    public static UserProfileDto toProfileDto(UserEntity user) {
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl()
        );
    }
}
