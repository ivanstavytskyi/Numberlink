package numberlink.game.core;
import org.springframework.stereotype.Service;

@Service
public class CheckSolution {

    public boolean check(int[][] actual, int[][] expected) {
        int w = actual[0].length, h = actual.length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (actual[y][x] != expected[y][x])
                    return false;
            }
        }
        return true;
    }
}
