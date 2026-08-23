package numberlink.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckSolutionTest {
    private final CheckSolution checkSolution = new CheckSolution();

    private final int expected[][] = {
            {1, 1, 2, 2},
            {1, 2, 2, 3},
            {1, 3, 3, 3}

    };

    private final int actual[][] = {
            {2, 1, 2, 2},
            {2, 2, 2, 3},
            {1, 3, 3, 3}
    };

    @Test
    public void check_whenGridsDiffer_returnsFalse() {
        assertFalse(checkSolution.check(actual, expected));
    }

    @Test
    void check_whenGridsMatch_returnsTrue() {
        assertTrue(checkSolution.check(expected, expected));
    }
}
