package aoc2024.utils;


public record Vector2 (int x, int y) {
    public static Vector2 UP = new Vector2(0, -1);
    public static Vector2 DOWN = new Vector2(0, 1);
    public static Vector2 LEFT = new Vector2(-1, 0);
    public static Vector2 RIGHT = new Vector2(1, 0);

    public static Vector2 TOP_LEFT = new Vector2(-1, -1);
    public static Vector2 TOP_RIGHT = new Vector2(1, -1);
    public static Vector2 BOTTOM_LEFT = new Vector2(-1, 1);
    public static Vector2 BOTTOM_RIGHT = new Vector2(1, 1);

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public Vector2 plus(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 minus(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 mul(Integer k) {
        return new Vector2(x * k, y * k);
    }

    public Vector2 div(Integer k) {
        return new Vector2(x / k, y / k);
    }

    public int manhattan(Vector2 other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public String toStringRC() {
        return String.format("(%d,%d)", y, x);
    }
    public String toStringXY() {
        return String.format("(%d,%d)", x, y);
    }
}
