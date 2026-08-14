package numberlink.game.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static numberlink.game.core.GameConstants.*;

@Service
public class CreateMap {
    @Autowired
    private ConvertToView convertToView;
    @Autowired
    private ShuffleMap shuffleMap;
    @Autowired
    private FillMap fillMap;
    @Autowired
    private CheckFlowHead checkFlowHead;

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
            int[][] table = tile(width, height);
            shuffleMap.shuffle(table, rand);
            shuffleMap.oddCorner(table);
            fillMap.findFlows(table, rand);
            convertToView.flatten(table, rand);
            if (checkMinPathLength(table))
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
