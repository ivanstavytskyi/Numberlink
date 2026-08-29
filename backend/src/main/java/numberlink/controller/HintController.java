package numberlink.controller;

import numberlink.game.core.CheckCellState;
import numberlink.game.core.CheckFlowHead;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import static numberlink.game.core.GameConstants.DX;
import static numberlink.game.core.GameConstants.DY;

@RestController
public class HintController {

    private CheckCellState checkCellState;
    private CheckFlowHead checkFlowHead;

    public HintController(CheckCellState checkCellState, CheckFlowHead checkFlowHead) {
        this.checkCellState = checkCellState;
        this.checkFlowHead = checkFlowHead;
    }

    @PostMapping("/api/hint-check")
    public ResponseEntity<?> hintCheck(
            @RequestBody int[][] map,
            HttpSession session
    ) {
        Object widthObj = session.getAttribute("width");
        Object heightObj = session.getAttribute("height");
        Object mapSolvedObj = session.getAttribute("map_solved");

        if (widthObj == null || heightObj == null || mapSolvedObj == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No active map"));
        }

        int width = (int) widthObj;
        int height = (int) heightObj;
        int[][] mapSolved = (int[][]) mapSolvedObj;

        if (map == null || map.length != height || map[0].length != width) {
            return ResponseEntity.badRequest().body("Error. Transfered map size, not answer original size.");
        }

        boolean helpToClear = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] != mapSolved[i][j] && map[i][j] != -1) {
                    map[i][j] = -1;
                    helpToClear = true;
                }
            }
        }

        if (helpToClear) {
            return ResponseEntity.ok(Map.of("status", "success", "map", map));
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (map[i][j] == -1) continue;

                boolean isHeadOnOrigin = checkFlowHead.isFlowHead(j, i, mapSolved);
                boolean isHeadOnRealTime = checkFlowHead.isFlowHead(j, i, map);

                if (isHeadOnOrigin) {
                    for (int k = 0; k < 4; k++) {
                        int nx = j + DX[k], ny = i + DY[k];

                        if (
                                checkCellState.inside(nx, ny, width, height) &&
                                        map[ny][nx] == -1 &&
                                        mapSolved[ny][nx] == map[i][j]) {
                            map[ny][nx] = map[i][j];
                            return ResponseEntity.ok(Map.of("status", "success", "map", map));
                        }
                    }
                } else if (isHeadOnRealTime) {
                    for (int k = 0; k < 4; k++) {
                        int nx = j + DX[k], ny = i + DY[k];

                        if (
                                checkCellState.inside(nx, ny, width, height) &&
                                        map[ny][nx] == -1 &&
                                        mapSolved[ny][nx] == map[i][j]) {
                            map[ny][nx] = map[i][j];
                            return ResponseEntity.ok(Map.of("status", "success", "map", map));
                        }
                    }
                }

            }
        }

        return ResponseEntity.badRequest().body("Unable to use hint!");
    }
}
