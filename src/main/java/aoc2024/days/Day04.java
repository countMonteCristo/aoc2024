package aoc2024.days;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.Vector2;


public class Day04 extends AbstractDay {

    interface Findable {
        long call(String needle, int r, int c);
    }

    Array2d<Character> table;
    final List<Vector2> dirs = Arrays.asList(
        new Vector2(0, 1), new Vector2(0, -1), new Vector2( 1, 0), new Vector2(-1, 0),
        new Vector2(1, 1), new Vector2(1, -1), new Vector2(-1, 1), new Vector2(-1, -1));

    boolean contains(String needle, int r, int c, Vector2 d) {
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

    boolean contains_x(String needle, int r, int c) {
        int half = needle.length() / 2;
        boolean one = contains(needle, r - half, c - half, dirs.get(4)) || contains(needle, r + half, c + half, dirs.get(7));
        boolean two = contains(needle, r - half, c + half, dirs.get(6)) || contains(needle, r + half, c - half, dirs.get(5));
        return one && two;
    }

    long find(String needle, int r, int c) {
        return dirs.stream().filter(d -> contains(needle, r, c, d)).count();
    }

    long find_x(String needle, int r, int c) {
        if ((table.at(r, c) == needle.charAt(needle.length() / 2)) && contains_x(needle, r, c))
            return 1L;
        return 0L;
    }

    long count(String needle, Findable func) {
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
