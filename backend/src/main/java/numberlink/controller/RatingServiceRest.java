package numberlink.controller;

import numberlink.dto.rating.general.RatingPercentDto;
import numberlink.dto.rating.request.RatingRequestDto;
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
@RequestMapping("/api/rating")
public class RatingServiceRest {

    private final RatingRepository ratingRepository;
    private final AuthService authService;

    public RatingServiceRest(RatingRepository ratingRepository, AuthService authService) {
        this.ratingRepository = ratingRepository;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> getRating() {
        UserEntity user = authService.requireCurrentUser();
        RatingEntity rating = ratingRepository.findByUser_Id(user.getId()).orElse(null);
        String comment = rating != null && rating.getContent() != null ? rating.getContent() : "";
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "value", rating != null ? rating.getValue() : 0,
                "comment", comment
        ));
    }

    @GetMapping("/avg")
    public double getAverageRating() {
        Double avg = ratingRepository.findAverageRating();
        return avg != null ? avg : 0.0;
    }

    @GetMapping("/amount")
    public long getRatingAmount() {
        return ratingRepository.count();
    }

    @GetMapping("/percentage")
    public RatingPercentDto getStarPercentage() {
        RatingPercentDto dto = new RatingPercentDto();
        List<Object[]> result = ratingRepository.getRatingPercentage();

        for (Object[] row : result) {
            Integer rating = ((Number) row[0]).intValue();
            double percent = ((Number) row[1]).doubleValue();
            switch (rating) {
                case 1 -> dto.setOneStar(percent);
                case 2 -> dto.setTwoStar(percent);
                case 3 -> dto.setThreeStar(percent);
                case 4 -> dto.setFourStar(percent);
                case 5 -> dto.setFiveStar(percent);
            }
        }
        return dto;
    }

    @PostMapping
    public ResponseEntity<?> setRating(@Valid @RequestBody RatingRequestDto ratingRequestDto) {
        UserEntity user = authService.requireCurrentUser();

        RatingEntity ratingEntity = ratingRepository.findByUser_Id(user.getId())
                .orElseGet(RatingEntity::new);
        ratingEntity.setUser(user);
        ratingEntity.setValue(ratingRequestDto.getRating());
        ratingEntity.setRatedAt(Instant.now());
        ratingRepository.save(ratingEntity);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "value", ratingEntity.getValue()
        ));
    }
}
