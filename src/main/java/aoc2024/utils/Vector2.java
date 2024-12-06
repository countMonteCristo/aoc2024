package aoc2024.utils;


public record Vector2 (int x, int y) {
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
}
