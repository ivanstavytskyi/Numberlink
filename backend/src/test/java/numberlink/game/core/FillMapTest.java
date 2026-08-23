package numberlink.game.core;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FillMapTest {
    private final CheckCellState checkCellState = new CheckCellState();
    private final FillMap fillMap = new FillMap(new CheckFlowHead(checkCellState), checkCellState);

    @Test
    void canConnect_whenConnectionAcceptable_returnsTrue () {
        int map[][] = {
                {1, 1, 2, 2},
                {4, 4, 5, 3},
                {6, 6, 5, 3}
        };

        assertTrue(fillMap.canConnect(1, 0, 2, 0, map));
        assertTrue(fillMap.canConnect(2, 0, 2, 1, map));
    }

    @Test
    void canConnect_whenConnectionUnacceptable_returnsFalse () {
        int map[][] = {
                {1, 1, 2, 2},
                {4, 4, 5, 3},
                {6, 6, 5, 3}
        };

        assertFalse(fillMap.canConnect(3, 2, 2, 2, map));
        assertFalse(fillMap.canConnect(0, 1, 0, 2, map));
    }

    @Test
    void fill_whenPathCanBePainted_ChangeTheNeighbourPathColor() {
        int actual[][] = {
                {1, 1, 2, 2},
                {4, 4, 5, 3},
                {6, 6, 5, 3}
        };

        fillMap.fill(2, 0, 1, actual);

        int expected[][] = {
                {1, 1, 1, 1},
                {4, 4, 5, 3},
                {6, 6, 5, 3}
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    void fill_whenComponentHasSeveralCells_paintsEntireComponent() {
        int actual[][] = {
                {1, 1, 2, 2},
                {4, 4, 5, 2},
                {6, 6, 5, 2}
        };

        fillMap.fill(2, 0, 1, actual);

        int expected[][] = {
                {1, 1, 1, 1},
                {4, 4, 5, 1},
                {6, 6, 5, 1}
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    void follow_guideFromStartToEndOfPath() {
        int table [][] = {
                {1, 1, 1, 1},
                {4, 4, 5, 1},
                {6, 6, 5, 1}
        };

        assertArrayEquals(new int[]{3, 2}, fillMap.follow(1, 0, 0, 0, table));
        assertArrayEquals(new int[]{3, 2}, fillMap.follow(3, 2, 3, 1, table));
    }

    @Test
    void layFlow_whenAdjacentHeadsCanMerge_paintsOtherFlow() {
        int table[][] = {
                {1, 1, 2, 2},
                {4, 4, 5, 3},
                {6, 6, 5, 3}
        };

        Random alwaysZero = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        fillMap.layFlow(1, 0, table, alwaysZero);

        int expected[][] = {
                {1, 1, 1, 1},
                {4, 4, 5, 1},
                {6, 6, 5, 1}
        };

        assertArrayEquals(expected, table);
    }

    @Test
    void findFlows_whenAdjacentHeadsCanMerge_joinsFlows() {
        int table[][] = {
                {1, 1, 2, 2},
                {3, 6, 5, 4},
                {3, 6, 5, 4}
        };

        Random alwaysZero = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        fillMap.findFlows(table, alwaysZero);

        int[][] expected = {
                {3, 3, 3, 3},
                {3, 6, 5, 3},
                {3, 6, 5, 3}
        };

        assertArrayEquals(expected, table);
    }
}
