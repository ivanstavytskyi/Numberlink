package numberlink.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static numberlink.game.core.GameConstants.DX;
import static numberlink.game.core.GameConstants.DY;

@Service
public class FillMap {
    @Autowired
    private CheckFlowHead checkFlowHead;
    @Autowired
    private CheckCellState checkCellState;

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

    public void layFlow(int x, int y, int[][] table, Random rand) {
        int w = table[0].length, h = table.length;
        boolean[][] visited = new boolean[h][w];
        List<Integer> dirs = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            dirs.add(i);
        Collections.shuffle(dirs, rand);
        for (int dir : dirs) {
            int nx = x + DX[dir], ny = y + DY[dir];
            if (checkCellState.inside(nx, ny, w, h) && canConnect(x, y, nx, ny, table)) {
                int color = table[y][x];
                fill(nx, ny, color, table);
                visited[y][x] = true;
                visited[y][x] = true;
                int[] next = follow(nx, ny, x, y, table);
                layFlow(next[0], next[1], table, rand);
                return;
            }
        }
    }

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

    public void fill(int x, int y, int color, int[][] table) {
        int w = table[0].length, h = table.length;
        int orig = table[y][x];
        table[y][x] = color;
        for (int i = 0; i < 4; i++) {
            int nx = x + DX[i], ny = y + DY[i];
            if (checkCellState.inside(nx, ny, w, h) && table[ny][nx] == orig)
                fill(nx, ny, color, table);
        }
    }

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
