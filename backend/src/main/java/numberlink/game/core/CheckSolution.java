package numberlink.game.core;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CheckSolution {
    private final RestClient restClient = RestClient.create();

    public boolean checkSolution(int[][] table, int[][] putTable) {
        int w = table[0].length, h = table.length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (table[y][x] != putTable[y][x])
                    return false;
            }
        }
        return true;
    }
}
