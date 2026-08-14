package numberlink.game.core;

import org.springframework.stereotype.Service;

import static numberlink.game.core.GameConstants.DX;
import static numberlink.game.core.GameConstants.DY;

@Service
public class CheckCellState {
    public boolean isNextToNeighbourCell(int x, int y, int color, int[][] table) {
        for (int i = 0;  i < 4; i++) {
                int nx = DX[i] + x, ny = DY[i] + y;
                if (inside(nx, ny, table[0].length, table.length))
                if (color == table[ny][nx]) {
                    return true;
            }
        }
        return false;
    }

    public boolean inside(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }
}
