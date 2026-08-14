package numberlink.game.core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static numberlink.game.core.GameConstants.DX;
import static numberlink.game.core.GameConstants.DY;

@Service
public class CheckFlowHead {
    @Autowired
    private CheckCellState checkCellState;

    public boolean isFlowHead(int x, int y, int[][] table) {
        int w = table[0].length, h = table.length;
        int deg = 0;
        for (int i = 0; i < 4; i++) {
            int nx = x + DX[i], ny = y + DY[i];
            if (checkCellState.inside(nx, ny, w, h) && table[ny][nx] == table[y][x])
                deg++;
        }
        return deg < 2;
    }
}
