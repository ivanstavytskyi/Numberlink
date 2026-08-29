package numberlink.game.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static numberlink.game.core.GameConstants.DX;
import static numberlink.game.core.GameConstants.DY;

@Service
@RequiredArgsConstructor
public class FillMap {
    private final CheckFlowHead checkFlowHead;
    private final CheckCellState checkCellState;

    public void findFlows(int[][] table, Random rand) {
        int w = table[0].length, h = table.length;
        List<Integer> perm = new ArrayList<>();
        for (int i = 0; i < w * h; i++)
            perm.add(i);
        Collections.shuffle(perm, rand);
        for (int p : perm) {
            int x = p % w, y = p / w;
            if (checkFlowHead.isFlowHead(x, y, table))
                layFlow(x, y, table, rand);
        }
    }

    // it prepares an array of 4 random directions, after that it goes in each of direction consistently,
    // then it checks whether the neighbour coordinates that lies near the cell that pending for connection,
    // is inside the grid, and it fulfills the conditions for connection.
    // If it does, then the color of primary cell stores to the variable, and the neighbour cell gets painted into
    // the color of primary cell (method `fill` goes recursively until all the path of the neighbour cell,
    // not yet get painted into the primary color of cell), after painting completely the path of neighbour cell,
    // method calls itself with specified coordinates of the end of neighbour path (head),
    // and tries to stretch as long as possible the path of the primary cell.
    public void layFlow(int x, int y, int[][] table, Random rand) {
        int w = table[0].length, h = table.length;
        List<Integer> dirs = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            dirs.add(i);
        Collections.shuffle(dirs, rand);
        for (int dir : dirs) {
            int nx = x + DX[dir], ny = y + DY[dir];
            if (checkCellState.inside(nx, ny, w, h) && canConnect(x, y, nx, ny, table)) {
                int color = table[y][x];
                fill(nx, ny, color, table);
                int[] next = follow(nx, ny, x, y, table);
                layFlow(next[0], next[1], table, rand);
                return;
            }
        }
    }

    // checks whether cells can be connected or not.
    // if cells have the same numbers then it can't be connected, or
    // if at least one of cells is not head of the flow.
    public boolean canConnect(int x1, int y1, int x2, int y2, int[][] table) {
        int w = table[0].length, h = table.length;
        if (table[y1][x1] == table[y2][x2])
            return false;
        if (!checkFlowHead.isFlowHead(x1, y1, table) || !checkFlowHead.isFlowHead(x2, y2, table))
            return false;

        for (int y3 = 0; y3 < h; y3++) {
            for (int x3 = 0; x3 < w; x3++) {
                for (int i = 0; i < 4; i++) {
                    int x4 = x3 + DX[i], y4 = y3 + DY[i];
    // check that neighbour cell is not outside the map, and
    // the cells that are pending for connection is not the selected neighbour cell, and
    // check that the selected neighbour cell number is equal to the first pending cell for connection, and
    // that the selected second neighbour cell is equal to the second pending cell for connection.
    // If pair like that was found, it means that the cells are already connected somewhere.
                    if (checkCellState.inside(x4, y4, w, h)
                            && !(x3 == x1 && y3 == y1 && x4 == x2 && y4 == y2)
                            && table[y3][x3] == table[y1][x1]
                            && table[y4][x4] == table[y2][x2]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // saves primary number (color) of cell to variable, then changes number (color) of this cell to defined color,
    // after that it goes in 4 directions from the painted cell, and searches for the leftovers with previous
    // primary color until it exists, after that drawing is finished.
    public void fill(int x, int y, int color, int[][] table) {
        int w = table[0].length, h = table.length;
        int primaryColor = table[y][x];
        table[y][x] = color;
        for (int i = 0; i < 4; i++) {
            int nx = x + DX[i], ny = y + DY[i];
            if (checkCellState.inside(nx, ny, w, h) && table[ny][nx] == primaryColor)
                fill(nx, ny, color, table);
        }
    }

    // goes through the path of one color and do not returns to the primary points (px, py),
    // when there is nowhere to go - this is the end of path.
    // Method returns coordinates of the end of that path (head).
    public int[] follow(int x, int y, int px, int py, int[][] table) {
        int w = table[0].length, h = table.length;
        for (int i = 0; i < 4; i++) {
            int nx = x + DX[i], ny = y + DY[i];
            if (checkCellState.inside(nx, ny, w, h) && !(nx == px && ny == py) && table[ny][nx] == table[y][x] )
                return follow(nx, ny, x, y, table);
        }
        return new int[] { x, y };
    }
}
