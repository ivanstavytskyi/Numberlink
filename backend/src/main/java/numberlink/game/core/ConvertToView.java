package numberlink.game.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConvertToView {
    private final FillMap fillMap;

    public void flatten(int[][] table, Random rand) {
        int w = table[0].length, h = table.length;
        int alpha = -1;
        List<Integer> perm = new ArrayList<>();
        for (int i = 0; i < w * h; i++)
            perm.add(i);
        Collections.shuffle(perm, rand);
        for (int p : perm) {
            int x = p % w;
            int y = p / w;
            if (table[y][x] >= 0) {
                fillMap.fill(x, y, alpha, table);
                alpha--;
            }
        }
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                table[y][x] = -table[y][x] - 1;
    }
}
