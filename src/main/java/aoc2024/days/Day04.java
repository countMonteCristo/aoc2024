package aoc2024.days;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.Vector2;


public class Day04 extends AbstractDay {

    private interface Findable {
        long call(String needle, int r, int c);
    }

    private Array2d<Character> table;
    private final List<Vector2> dirs = Arrays.asList(
        Vector2.DOWN, Vector2.UP, Vector2.RIGHT, Vector2.LEFT,
        Vector2.BOTTOM_RIGHT, Vector2.TOP_RIGHT, Vector2.BOTTOM_LEFT, Vector2.TOP_LEFT);

    private boolean contains(String needle, int r, int c, Vector2 d) {
        for (int i = 0; i < needle.length(); i++) {
            if (table.contains(r, c) && (table.at(r, c) == needle.charAt(i))) {
                r += d.y();
                c += d.x();
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean contains_x(String needle, int r, int c) {
        int half = needle.length() / 2;
        boolean one = contains(needle, r - half, c - half, dirs.get(4)) || contains(needle, r + half, c + half, dirs.get(7));
        boolean two = contains(needle, r - half, c + half, dirs.get(6)) || contains(needle, r + half, c - half, dirs.get(5));
        return one && two;
    }

    private long find(String needle, int r, int c) {
        return dirs.stream().filter(d -> contains(needle, r, c, d)).count();
    }

    private long find_x(String needle, int r, int c) {
        if ((table.at(r, c) == needle.charAt(needle.length() / 2)) && contains_x(needle, r, c))
            return 1L;
        return 0L;
    }

    private long count(String needle, Findable func) {
        return table.elementStream()
            .map(e -> func.call(needle, e.first().y(), e.first().x()))
            .reduce(0L, Long::sum);
    }

    @Override
    public void prepare(String fn) throws IOException {
        table = Array2d.parseCharTable(fn);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(count("XMAS", this::find), 2547L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(count("MAS", this::find_x), 1939L, strict);
    }
}
