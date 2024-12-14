package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.Vector2;


public class Day10 extends AbstractDay {

    interface Scorer {
        int calc(int r, int c);
    }

    Array2d<Integer> map;
    final List<Vector2> dd = Arrays.asList(
        new Vector2(-1,  0), new Vector2( 0, -1),
        new Vector2( 1,  0), new Vector2( 0,  1));

    @Override
    public void prepare(String fn) throws IOException {
        map = Array2d.parseIntTable(fn, "");
    }

    List<Vector2> getNeighbours(Vector2 point, int height) {
        List<Vector2> nbrs = new ArrayList<>(4);
        for (Vector2 d: dd) {
            Vector2 p = point.plus(d);
            if (map.contains(p) && (map.at(p) == height)) {
                nbrs.add(p);
            }
        }
        return nbrs;
    }

    int calcTrailHeadScore(int r, int c) {
        HashSet<Vector2> current = new HashSet<>(Arrays.asList(new Vector2(c, r)));
        for (int height = 1; (height < 10) && !current.isEmpty(); height++) {
            HashSet<Vector2> next = new HashSet<>();
            for (Vector2 p: current) {
                next.addAll(getNeighbours(p, height));
            }
            current = next;
        }
        return current.size();
    }

    int calcTrailHeadRaiting(int r, int c) {
        Array2d<Integer> dp = Array2d.getTable(map.width(), map.height(), 0);
        dp.set(r, c, 1);

        HashSet<Vector2> current = new HashSet<>(Arrays.asList(new Vector2(c, r)));
        for (int height = 1; (height < 10) && !current.isEmpty(); height++) {
            HashSet<Vector2> next = new HashSet<>();
            for (Vector2 p: current) {
                for (Vector2 n: getNeighbours(p, height)) {
                    dp.set(n,  dp.at(n) + dp.at(p));
                    next.add(n);
                }
            }
            current = next;
        }

        int res = 0;
        for (int row = 0; row < map.height(); row++) {
            for (int col = 0; col < map.width(); col++) {
                if (map.at(row, col) == 9) {
                    res += dp.at(row, col);
                }
            }
        }

        return res;
    }

    int solve(Scorer scorer) {
        int res = 0;
        for (int row = 0; row < map.height(); row++) {
            for (int col = 0; col < map.width(); col++) {
                if (map.at(row, col) == 0) {
                    res += scorer.calc(row, col);
                }
            }
        }
        return res;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(this::calcTrailHeadScore), 629, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(this::calcTrailHeadRaiting), 1242, strict);
    }
}
