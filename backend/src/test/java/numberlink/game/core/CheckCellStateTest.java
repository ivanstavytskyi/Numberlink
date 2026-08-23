package numberlink.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckCellStateTest {
    private final CheckCellState checkCellState = new CheckCellState();

    @Test
    void inside_whenCellsOnGrid_returnsTrue() {
        assertTrue(checkCellState.inside(0, 0, 3, 2));
        assertTrue(checkCellState.inside(2, 1, 3, 2));
    }

    @Test
    void inside_whenCoordinatesAreNegative_returnsFalse() {
        assertFalse(checkCellState.inside(-1, 0, 3, 2));
        assertFalse(checkCellState.inside(0, -1, 3, 2));
    }

    @Test
    void inside_whenCellsOutOfGrid_returnsFalse() {
        assertFalse(checkCellState.inside(3, 0, 3, 2));
        assertFalse(checkCellState.inside(0, 2, 3, 2));
    }
}
