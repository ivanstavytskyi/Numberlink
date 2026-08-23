package numberlink.game.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateMapTest {

    @Mock ConvertToView convertToView;
    @Mock ShuffleMap shuffleMap;
    @Mock FillMap fillMap;

    @InjectMocks
    CreateMap createMap;

    @Test
    void tile_whenBothDimensionsEven_placesVerticalPairs() {
        int[][] actual = createMap.tile(4, 4);

        int[][] expected = {
                {0, 1, 2, 3},
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {4, 5, 6, 7}
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    void tile_whenHeightDimensionIsOdd_placesVerticalPairsAndHorizontalPairs() {
        int[][] actual = createMap.tile(4, 5);

        int[][] expected = {
                {0, 1, 2, 3},
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {4, 5, 6, 7},
                {8, 8, 9, 9}
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    void tile_whenWidthDimensionIsOdd_placesVerticalPairs() {
        int[][] actual = createMap.tile(5, 4);

        int[][] expected = {
                {0, 1, 2, 3, 4},
                {0, 1, 2, 3, 4},
                {5, 6, 7, 8, 9},
                {5, 6, 7, 8, 9}
        };

        assertArrayEquals(expected, actual);
    }


    @Test
    void tile_whenBothDimensionsOdd_placesVerticalPairsAndHorizontalPairsAndOrphanedCornerNumber() {
        int[][] actual = createMap.tile(5, 5);

        int[][] expected = {
                {0, 1, 2, 3, 4},
                {0, 1, 2, 3, 4},
                {5, 6, 7, 8, 9},
                {5, 6, 7, 8, 9},
                {10, 10, 11, 11, 12}
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    void checkMinPathLength_whenPathLengthTooShort_returnsFalse() {
        int table[][] = {
                {1, 1, 1, 1},
                {3, 2, 2, 1},
                {3, 3, 3, 3}
        };

        assertFalse(createMap.checkMinPathLength(table));
    }

    @Test
    void checkMinPathLength_whenPathLengthValid_returnsTrue() {
        int table[][] = {
                {1, 1, 1, 1},
                {3, 2, 2, 2},
                {3, 3, 3, 3}
        };

        assertTrue(createMap.checkMinPathLength(table));
    }

    @Test
    void convertToUnsolved_whenPathHasEnds_keepsHeadsAndClearsMiddle() {
        CreateMap mapWithRealHeads = new CreateMap(
                convertToView,
                shuffleMap,
                fillMap,
                new CheckFlowHead(new CheckCellState())
        );

        int map[][] = {
                {1, 1, 1, 1},
                {3, 2, 2, 2},
                {3, 3, 3, 3}
        };

        int [][] expected = {
                {1, -1, -1, 1},
                {3, 2, -1, 2},
                {-1, -1, -1, 3}
        };

        int [][] actual = mapWithRealHeads.convertToUnsolved(map);

        assertArrayEquals(expected, actual);
    }

    @Test
    void generate_whenSizeIsPlayable_returnsSolvedMapMeetingInvariants() {
        CheckCellState cells = new CheckCellState();
        CheckFlowHead heads = new CheckFlowHead(cells);
        FillMap fillMap = new FillMap(heads, cells);
        CreateMap generator = new CreateMap(
                new ConvertToView(fillMap),
                new ShuffleMap(),
                fillMap,
                heads
        );

        int[][] table = generator.generate(7, 7);

        assertNotNull(table);
        assertEquals(7, table.length);
        assertEquals(7, table[0].length);
        assertTrue(generator.checkMinPathLength(table));

        Set<Integer> ids = new HashSet<>();
        for (int[] row : table) {
            for (int cell : row) {
                assertTrue(cell >= 0);
                ids.add(cell);
            }
        }
        int maxId = Collections.max(ids);
        assertEquals(maxId + 1, ids.size());
        assertTrue(ids.contains(0));
    }
}
