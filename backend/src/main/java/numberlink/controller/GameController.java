package numberlink.controller;
import numberlink.game.core.CheckSolution;
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
    private final CheckSolution checkSolution;


    public GameController(CreateMap createMap, CheckSolution checkSolution) {
        this.createMap = createMap;
        this.checkSolution = checkSolution;
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
    public ResponseEntity<?> mapCheck(@RequestBody int[][] mapConnected,
                                      HttpSession session) {
        Object widthObj = session.getAttribute("width");
        Object heightObj = session.getAttribute("height");
        Object mapSolutionObj = session.getAttribute("map_solved");

        if (widthObj == null || heightObj == null || mapSolutionObj == null) {
            return ResponseEntity.ok(false);
        }

        int width = (int) widthObj;
        int height = (int) heightObj;
        int[][] mapSolution = (int[][]) mapSolutionObj;

        if (mapConnected == null || mapConnected.length != height || mapConnected[0].length != width) {
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(checkSolution.check(mapConnected, mapSolution));
    }
}
