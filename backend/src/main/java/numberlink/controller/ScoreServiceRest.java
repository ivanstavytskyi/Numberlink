package numberlink.controller;

import numberlink.dto.score.request.ScoreRequestDto;
import numberlink.dto.score.response.ScoreResponseDto;
import numberlink.dto.score.response.ScoreResponseSelfDto;
import numberlink.entity.UserEntity;
import numberlink.game.core.GameConstants;
import numberlink.service.auth.AuthService;
import numberlink.service.jpa.ScoreService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    private final ScoreService scoreService;
    private final AuthService authService;

    public ScoreServiceRest(ScoreService scoreService, AuthService authService) {
        this.scoreService = scoreService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> addScore(
            HttpSession session,
            @Valid @RequestBody ScoreRequestDto scoreRequestDto
    ) {
        UserEntity user = authService.requireCurrentUser();

        Object widthObj = session.getAttribute("width");
        Object heightObj = session.getAttribute("height");
        if (widthObj == null || heightObj == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "No active map in session"
            ));
        }

        int width = (int) widthObj;
        int height = (int) heightObj;
        if (width < GameConstants.MIN_MAP_WIDTH || width > GameConstants.MAX_MAP_WIDTH
                || height < GameConstants.MIN_MAP_HEIGHT || height > GameConstants.MAX_MAP_HEIGHT) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Invalid map size in session"
            ));
        }

        scoreService.addScore(user, scoreRequestDto.getElapsedSeconds(), width, height);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/sort")
    public List<ScoreResponseDto> getTopScoresOrderBy(
            @RequestParam(required = false, defaultValue = "score") String criterion,
            @RequestParam(required = false, defaultValue = "all-time") String period,
            @RequestParam(required = false, defaultValue = "") String mapSize
    ) {
        return scoreService.getTopScoresByCriterion(criterion, period, mapSize);
    }

    @GetMapping
    public ResponseEntity<ScoreResponseSelfDto> getTopScore() {
        UserEntity user = authService.requireCurrentUser();
        ScoreResponseSelfDto dto = scoreService.getTopScore(user.getId(), user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());
        return ResponseEntity.ok(dto);
    }
}
