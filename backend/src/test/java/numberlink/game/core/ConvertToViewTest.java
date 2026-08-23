package numberlink.game.core;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvertToViewTest {
    CheckCellState cells= new CheckCellState();
    private final ConvertToView convertToView = new ConvertToView(new FillMap(new CheckFlowHead(cells), cells));

    @Test
    void flatten_relabelsComponents_keepsShapes() {
        int[][] original  = {
                {9, 9, 9, 9},
                {9, 4, 4, 4},
                {9, 0, 0, 4},
                {0, 0, 4, 4}
        };

        int[][] table = copy(original);

        // check that two cells were the same numbers (colors) before [boolean], and
        // checks whether the two new cells at the same positions as an old cells
        // are the same numbers (colors) after converting [boolean]

        convertToView.flatten(table, new Random());
        int h = table.length, w = table[0].length;
        for (int y1 = 0; y1 < h; y1++) {
            for (int x1 = 0; x1 < w; x1++) {
                for (int y2 = 0; y2 < h; y2++) {
                    for (int x2 = 0; x2 < w; x2++) {
                        boolean sameBefore = original[y1][x1] == original[y2][x2];
                        boolean sameAfter = table[y1][x1] == table[y2][x2];
                        assertEquals(sameBefore, sameAfter);
                    }
                }
            }
        }

        // check that only [0,1,2] numbers left on the field after converting

        Set<Integer> ids = new HashSet<>();
        for (int[] row : table) {
            for (int cell : row) {
                ids.add(cell);
            }
        }
        assertEquals(Set.of(0, 1, 2), ids);
    }

    private static int[][] copy(int[][] src) {
        int[][] dst = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i].clone();
        }
        return dst;
    }
}
