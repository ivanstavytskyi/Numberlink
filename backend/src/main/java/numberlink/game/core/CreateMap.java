package numberlink.game.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static numberlink.game.core.GameConstants.*;

@Service
@RequiredArgsConstructor
public class CreateMap {
    private final ConvertToView convertToView;
    private final ShuffleMap shuffleMap;
    private final FillMap fillMap;
    private final CheckFlowHead checkFlowHead;

    public int[][] generateParallel(int width, int height) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        List<Callable<int[][]>> tasks = new ArrayList<>();

        for (int i = 0; i < THREADS; i++) {
            tasks.add(() -> generate(width, height));
        }

        try {
            int[][] result = executor.invokeAny(tasks);
            return result;
        } finally {
            executor.shutdown();
        }
    }


    public int[][] generate(int width, int height) {
        Random rand = new Random();
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int[][] table = tile(width, height); // if map size is even -> fill map from left to right,
    // with vertical positioning of the pairs of cells,
    // for example if map 4x4, output of this method will be:
    // 0 1 2 3
    // 0 1 2 3
    // 4 5 6 7
    // 4 5 6 7
            shuffleMap.shuffle(table, rand);
    // placing the cells that way, that if the same cells are near each other in one row,
    // so they will be placed let's say hypothetically turned on the 90 degrees to the left, if based on the view
    // of the square of 4 cells, and cells from the row will be placed as column. From the other hand
    // if other condition executes, if looking at cells the same hypothetically way as a square of four cells,
    // the square will be turned to the right on the 90 degrees, and the cells from the columns will be placed in rows.
            shuffleMap.oddCorner(table);
    // Only if both w and h are odd:
    // In the last row, the two cells to the left of the right corner still form a pair
    // ([w-3] and [w-2] are identical)—the corner takes their color.
    // In the last column, the two cells above the corner still form a pair—the corner takes their color.
    // If both `if` conditions are met, the second one wins: the corner attaches upwards.

    // If the shuffle breaks up one of the pairs, only the intact one will attach.
    // If both are broken, the corner remains orphaned, and the generation process
    // will likely discard the board based on `checkMinPathLength`.
            fillMap.findFlows(table, rand);
    // creates an array of linear numbers of all cells,
    // then picks random number from array, calculates (w, h) coordinates,
    // and tries to connect neighbour cells that is near the head of the flow (picked coordinates)
            convertToView.flatten(table, rand);
    // due to map generation, numbers (colors) are not consistent (which means number numeration
    // may start not from zero and as wel may be spaces between numbers e.g 1,2,4,5,7), thus this method fills
    // the map from the random map position with negative numbers (staring from -1). It decreases after
    // completely filling the path (filling the whole path consistently each cell by cell with same negative number),
    // value of number by one, until there are positive numbers that exists on map,
    // after there no positive numbers on map, negative numbers are converted to positive, and
    // properly generated and solved map is ready.
            if (checkMinPathLength(table))
    // it checks whether the condition passes, the minimal path can be at least 3 cells and more,
    // then map generated properly, if condition has not passed, then map
    // generation continues until 10000 attempts. Generation occurs in
    // multiple threads (5) for faster response time.
                return table;
        }
        System.out.println("Failed to generate puzzle for " + MAX_ATTEMPTS + " attempts" +
                "+ in each of " + THREADS + "threads.");
        return null;
    }

    public int[][] tile(int width, int height) {
        int[][] table = new int[height][width];
        int alpha = 0;
        for (int y = 0; y < height - 1; y += 2) {
            for (int x = 0; x < width; x++) {
                int id = alpha;
                table[y][x] = id;
                table[y + 1][x] = id;
                alpha++;
            }
        }
        if (height % 2 == 1) {
            for (int x = 0; x < width - 1; x += 2) {
                int id = alpha;
                table[height - 1][x] = id;
                table[height - 1][x + 1] = id;
                alpha++;
            }
            if (width % 2 == 1) {
                table[height - 1][width - 1] = alpha;
            }
        }

        return table;
    }

    public boolean checkMinPathLength(int[][] table) {
        int w = table[0].length, h = table.length;
        Map<Integer, Integer> counts = new HashMap<>();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                counts.merge(table[y][x], 1, Integer::sum);
        for (int count : counts.values())
            if (count < MIN_PATH_LENGTH)
                return false;
        return true;
    }

    // converts the table to unsolved view,
    // by iterating through the map, and checking whether the cell is head-cell or not.
    public int[][] convertToUnsolved(int[][] table) {
        int w = table[0].length, h = table.length;
        int[][] putTable = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++)
                putTable[y][x] = (checkFlowHead.isFlowHead(x, y, table) ? table[y][x] : -1);
        }
        return putTable;
    }
}
