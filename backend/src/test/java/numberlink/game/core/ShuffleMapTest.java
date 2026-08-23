package numberlink.game.core;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ShuffleMapTest {
    private final ShuffleMap shuffleMap = new ShuffleMap();

    @Test
    void shuffle_whenAlwaysPicksTopLeftOn3x3_rotatesVerticalPairs() {
        int[][] table = {
                {0, 1, 2},
                {0, 1, 2},
                {3, 3, 4}
        };

        Random alwaysZero = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        shuffleMap.shuffle(table, alwaysZero);

        int[][] expected = {
                {0, 0, 2},
                {1, 1, 2},
                {3, 3, 4}
        };
        assertArrayEquals(expected, table);
    }

    @Test
    void oddCorner_whenOddBothGridDimensions_mergeLeftAndRightCellFromCorner() {
        int[][] table = {
                {0, 1, 2},
                {0, 1, 2},
                {3, 3, 4}
        };

        shuffleMap.oddCorner(table);

        int expected[][] = {
                {0, 1, 2},
                {0, 1, 2},
                {3, 3, 2}
        };

        assertArrayEquals(expected, table);
    }
}
