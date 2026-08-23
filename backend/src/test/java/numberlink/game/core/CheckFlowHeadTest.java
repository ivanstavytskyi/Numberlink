package numberlink.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckFlowHeadTest {

    private final CheckFlowHead checkFlowHead = new CheckFlowHead(new CheckCellState());
    
    private int table[][] = {
            {7, 7, 7}
        };

    @Test
    void isFlowHead_whenCellIsMiddleOfPath_returnsFalse() {
        assertFalse(checkFlowHead.isFlowHead(1, 0, table));
    }

    @Test
    void isFlowHead_whenCellIsHeadOfPath_returnsTrue() {
        assertTrue(checkFlowHead.isFlowHead(0, 0,  table));
        assertTrue(checkFlowHead.isFlowHead(2, 0, table));
    }
}
