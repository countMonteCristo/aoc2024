package aoc2024.days;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.Pair;


public class Day04 extends AbstractDay {

    interface Findable {
        int call(String needle, int r, int c);
    }

    Array2d<Character> table;
    final List<Pair<Integer, Integer>> dirs = Arrays.asList(
        new Pair<>(0, 1), new Pair<>(0, -1), new Pair<>( 1, 0), new Pair<>(-1, 0),
        new Pair<>(1, 1), new Pair<>(1, -1), new Pair<>(-1, 1), new Pair<>(-1, -1));

    boolean contains(String needle, int r, int c, int dr, int dc) {
        for (int i = 0; i < needle.length(); i++) {
            if (table.contains(r, c) && (table.at(r, c) == needle.charAt(i))) {
                r += dr;
                c += dc;
            } else {
                return false;
            }
        }
        return true;
    }

    boolean contains_x(String needle, int r, int c) {
        int half = needle.length() / 2;
        boolean one = contains(needle, r - half, c - half, 1, 1) || contains(needle, r + half, c + half, -1, -1);
        boolean two = contains(needle, r - half, c + half, 1, -1) || contains(needle, r + half, c - half, -1, 1);
        return one && two;
    }

    int find(String needle, int r, int c) {
        int count = 0;
        for (var pair : dirs) {
            if (contains(needle, r, c, pair.first(), pair.second()))
                count++;
        }
        return count;
    }

    int find_x(String needle, int r, int c) {
        if ((table.at(r, c) == needle.charAt(needle.length() / 2)) && contains_x(needle, r, c))
            return 1;
        return 0;
    }

    int count(String needle, Findable func) {
        int n = 0;
        for (int row = 0; row < table.height(); row++) {
            for (int col = 0; col < table.width(); col++)
                n += func.call(needle, row, col);
        }
        return n;
    }

    @Override
    public void prepare(String fn) throws IOException {
        table = Array2d.parseCharTable(fn);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(count("XMAS", this::find), 2547, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(count("MAS", this::find_x), 1939, strict);
    }
}
