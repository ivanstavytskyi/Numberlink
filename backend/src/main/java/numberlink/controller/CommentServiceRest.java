package numberlink.controller;

import numberlink.dto.comment.request.CommentRequestDto;
import numberlink.dto.comment.response.CommentResponseDto;
import numberlink.entity.RatingEntity;
import numberlink.entity.UserEntity;
import numberlink.repository.RatingRepository;
import numberlink.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    private final RatingRepository ratingRepository;
    private final AuthService authService;

    public CommentServiceRest(RatingRepository ratingRepository, AuthService authService) {
        this.ratingRepository = ratingRepository;
        this.authService = authService;
    }

    @GetMapping
    public List<CommentResponseDto> getComments() {
        return ratingRepository.findAllWithComments().stream()
                .map(CommentServiceRest::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        UserEntity user = authService.requireCurrentUser();

        String text = commentRequestDto.getComment() == null ? "" : commentRequestDto.getComment().trim();
        if (text.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "empty comment specified"
            ));
        }

        RatingEntity rating = ratingRepository.findByUser_Id(user.getId()).orElse(null);
        if (rating == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Choose a star rating before posting a review."
            ));
        }

        rating.setContent(text);
        rating.setCommentedOn(Instant.now());
        ratingRepository.save(rating);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    private static CommentResponseDto toDto(RatingEntity rating) {
        CommentResponseDto dto = new CommentResponseDto();
        UserEntity user = rating.getUser();
        dto.setPlayer(user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setComment(rating.getContent());
        dto.setCommentedOn(rating.getCommentedOn());
        dto.setRating(rating.getValue());
        return dto;
    }
}
