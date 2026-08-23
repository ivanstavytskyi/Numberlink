package numberlink.game.core;

import org.springframework.stereotype.Service;

import static numberlink.game.core.GameConstants.DX;
import static numberlink.game.core.GameConstants.DY;

@Service
public class CheckCellState {
    public boolean inside(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }
}
