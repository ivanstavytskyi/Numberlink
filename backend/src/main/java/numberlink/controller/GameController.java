package numberlink.controller;
import numberlink.game.core.CreateMap;
import numberlink.game.core.GameConstants;
import numberlink.service.user.UsernameGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Map;

@RestController
public class GameController {

    private final CreateMap createMap;
    private final UsernameGenerator usernameGenerator;


    public GameController(CreateMap createMap, UsernameGenerator usernameGenerator) {
        this.createMap = createMap;
        this.usernameGenerator = usernameGenerator;
    }

    @GetMapping("/api/create-map")
    public ResponseEntity generateMap(
            @RequestParam int width,
            @RequestParam int height,
            HttpSession session)
    {
        if ((width < GameConstants.MIN_MAP_WIDTH || width > GameConstants.MAX_MAP_WIDTH) || (height < GameConstants.MIN_MAP_HEIGHT || width > GameConstants.MAX_MAP_HEIGHT)) {
            return ResponseEntity.badRequest().body("Error generating map. Incorrect map size.");
        }

        int map_solved[][], map_unsolved[][];

        try {
            map_solved = createMap.generateParallel(width, height);
            map_unsolved = createMap.convertToUnsolved(map_solved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error generating map");
        }

        if (session.getAttribute("username") == null) {
            String name = usernameGenerator.generate();
            if (name == null) return ResponseEntity.badRequest().body("Error generating name");
            session.setAttribute("username", name);
        }

        session.setAttribute("width", width);
        session.setAttribute("height", height);

        session.setAttribute("map_solved", map_solved);

        return ResponseEntity.ok(Arrays.deepToString(map_unsolved));
    }

    @GetMapping("/api/width")
    public ResponseEntity<?> getWidth(HttpSession session) {
        Object width = session.getAttribute("width");
        if (width == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No active map"));
        }
        return ResponseEntity.ok(width);
    }

    @GetMapping("/api/height")
    public ResponseEntity<?> getHeight(HttpSession session) {
        Object height = session.getAttribute("height");
        if (height == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No active map"));
        }
        return ResponseEntity.ok(height);
    }

    @PostMapping("/api/map-check")
    public ResponseEntity<?> mapCheck(@RequestBody int[][] map,
                                      HttpSession session) {
        Object widthObj = session.getAttribute("width");
        Object heightObj = session.getAttribute("height");
        Object mapSolvedObj = session.getAttribute("map_solved");

        if (widthObj == null || heightObj == null || mapSolvedObj == null) {
            return ResponseEntity.ok(false);
        }

        int width = (int) widthObj;
        int height = (int) heightObj;
        int[][] mapSolved = (int[][]) mapSolvedObj;

        if (map == null || map.length != height || map[0].length != width) {
            return ResponseEntity.ok(false);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] != mapSolved[i][j]) {
                    return ResponseEntity.ok(false);
                }
            }
        }

        return ResponseEntity.ok(true);
    }
}
