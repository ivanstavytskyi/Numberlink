package numberlink.game.core;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ShuffleMap {

    public void shuffle(int[][] table, Random rand) {
        int w = table[0].length, h = table.length;
        if (w <= 1 || h <= 1)
            return;
        int attempts = w * h * w * h;
        for (int i = 0; i < attempts; i++) {
            int x = rand.nextInt(w - 1);
            int y = rand.nextInt(h - 1);
            if (table[y][x] == table[y][x + 1] && table[y + 1][x] == table[y + 1][x + 1]) {
                table[y + 1][x] = table[y][x];
                table[y][x + 1] = table[y + 1][x + 1];
            } else if (table[y][x] == table[y + 1][x] && table[y][x + 1] == table[y + 1][x + 1]) {
                table[y][x + 1] = table[y][x];
                table[y + 1][x] = table[y + 1][x + 1];
            }
        }
    }

    public void oddCorner(int[][] table) {
        int w = table[0].length, h = table.length;
        if (w % 2 == 1 && h % 2 == 1) {
            if (w > 2 && table[h - 1][w - 3] == table[h - 1][w - 2])
                table[h - 1][w - 1] = table[h - 1][w - 2];
            if (h > 2 && table[h - 3][w - 1] == table[h - 2][w - 1])
                table[h - 1][w - 1] = table[h - 2][w - 1];
        }

        // System.out.println("OddCorner: ");
        // for (int i = 0; i < h; i++) {
        // for (int j = 0; j < w; j++) {
        // System.out.print(table[i][j] + " ");
        // }
        // System.out.println();
        // }

        // System.out.println();
    }
}
