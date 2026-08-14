package numberlink.service.user;

import numberlink.entity.UserEntity;
import numberlink.exceptions.AvatarTooLargeException;
import numberlink.exceptions.InvalidAvatarException;
import numberlink.repository.UserRepository;
import numberlink.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final Map<String, String> EXT_BY_TYPE = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private final AuthService authService;
    private final UserRepository userRepository;
    private final Path uploadsRoot;
    private final long maxBytes;

    public AvatarStorageService(
            AuthService authService,
            UserRepository userRepository,
            @Value("${app.uploads.dir:uploads}") String uploadsDir,
            @Value("${app.avatar.max-bytes:1048576}") long maxBytes
    ) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.uploadsRoot = Path.of(uploadsDir).toAbsolutePath().normalize();
        this.maxBytes = maxBytes;
    }

    @Transactional
    public UserEntity store(MultipartFile file) {
        validate(file);
        UserEntity user = authService.requireCurrentUser();

        String contentType = normalizeContentType(file.getContentType());
        String ext = EXT_BY_TYPE.get(contentType);
        if (ext == null) {
            throw new InvalidAvatarException("Use a PNG, JPG, or WebP image.");
        }

        Path avatarsDir = uploadsRoot.resolve("avatars");
        try {
            Files.createDirectories(avatarsDir);
        } catch (IOException e) {
            throw new InvalidAvatarException("Could not prepare avatar storage.");
        }

        deleteLocalFile(user.getAvatarUrl());

        Path target = avatarsDir.resolve(user.getId() + ext);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InvalidAvatarException("Could not save avatar.");
        }

        // Drop any previous extension for the same user id.
        deleteOtherExtensions(user.getId(), ext);

        String publicUrl = "/uploads/avatars/" + user.getId() + ext;
        user.setAvatarUrl(publicUrl);
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity clear() {
        UserEntity user = authService.requireCurrentUser();
        deleteLocalFile(user.getAvatarUrl());
        user.setAvatarUrl(null);
        return userRepository.save(user);
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidAvatarException("Choose an image file.");
        }

        long size = file.getSize();
        if (size <= 0) {
            throw new InvalidAvatarException("Choose an image file.");
        }
        if (size > maxBytes) {
            throw new AvatarTooLargeException();
        }

        String contentType = normalizeContentType(file.getContentType());
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidAvatarException("Use a PNG, JPG, or WebP image.");
        }
    }

    private static String normalizeContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return "";
        }
        String normalized = contentType.trim().toLowerCase(Locale.ROOT);
        int semi = normalized.indexOf(';');
        return semi >= 0 ? normalized.substring(0, semi).trim() : normalized;
    }

    private void deleteOtherExtensions(UUID userId, String keepExt) {
        for (String ext : EXT_BY_TYPE.values()) {
            if (ext.equals(keepExt)) {
                continue;
            }
            Path other = uploadsRoot.resolve("avatars").resolve(userId + ext);
            try {
                Files.deleteIfExists(other);
            } catch (IOException ignored) {
                /* best-effort cleanup */
            }
        }
    }

    private void deleteLocalFile(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return;
        }
        if (!avatarUrl.startsWith("/uploads/avatars/")) {
            return;
        }
        String name = avatarUrl.substring("/uploads/avatars/".length());
        if (name.contains("..") || name.contains("/") || name.contains("\\")) {
            return;
        }
        Path file = uploadsRoot.resolve("avatars").resolve(name).normalize();
        if (!file.startsWith(uploadsRoot.resolve("avatars"))) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
            /* best-effort cleanup */
        }
    }
}
