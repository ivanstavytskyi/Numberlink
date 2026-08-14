package numberlink.game.core;

public final class GameConstants {
    private GameConstants() {}

    public static final int MIN_MAP_WIDTH = 7;
    public static final int MAX_MAP_WIDTH = 11;
    public static final int MIN_MAP_HEIGHT = 7;
    public static final int MAX_MAP_HEIGHT = 11;
    public static final int MAX_ATTEMPTS = 10000;
    public static final int THREADS = 5;
    public static final int MIN_PATH_LENGTH = 3;
    public static final int[] DX = { 0, 1, 0, -1 };
    public static final int[] DY = { -1, 0, 1, 0 };
    public static final char[] SIGMA = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static int toInternalValue(int coordinate) {
        return coordinate -1;
    }

    public static int toExternalValue(int coordinate) {
        return coordinate + 1;
    }

    public static boolean lessOrEqual(int x, int y) {return x <= y;}
    public static boolean moreOrEqual(int x, int y) {return x >= y;}
}
