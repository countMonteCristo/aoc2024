package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.Vector2;


public class Day10 extends AbstractDay {

    private interface Scorer {
        int calc(Vector2 p);
    }

    private Array2d<Integer> map;
    private final List<Vector2> dd = Arrays.asList(Vector2.LEFT, Vector2.UP, Vector2.RIGHT, Vector2.DOWN);

    @Override
    public void prepare(String fn) throws IOException {
        map = Array2d.parseIntTable(fn, "");
    }

    // Tried to use streams, but they seem to work longer than plain for-loop
    private List<Vector2> getNeighbours(Vector2 point, int height) {
        List<Vector2> nbrs = new ArrayList<>(4);
        for (Vector2 d: dd) {
            Vector2 p = point.plus(d);
            if (map.contains(p) && (map.at(p) == height)) {
                nbrs.add(p);
            }
        }
        return nbrs;
    }

    private int calcTrailHeadScore(Vector2 p) {
        HashSet<Vector2> current = new HashSet<>(Arrays.asList(p));
        for (int height = 1; (height < 10) && !current.isEmpty(); height++) {
            HashSet<Vector2> next = new HashSet<>();
            for (Vector2 q: current) {
                next.addAll(getNeighbours(q, height));
            }
            current = next;
        }
        return current.size();
    }

    private int calcTrailHeadRaiting(Vector2 p) {
        Array2d<Integer> dp = Array2d.getTable(map.width(), map.height(), 0);
        dp.set(p, 1);

        HashSet<Vector2> current = new HashSet<>(Arrays.asList(p));
        for (int height = 1; (height < 10) && !current.isEmpty(); height++) {
            HashSet<Vector2> next = new HashSet<>();
            for (Vector2 q: current) {
                for (Vector2 n: getNeighbours(q, height)) {
                    dp.set(n,  dp.at(n) + dp.at(q));
                    next.add(n);
                }
            }
            current = next;
        }

        return map.elementStream()
            .filter(i -> i.second() == 9)
            .map(i -> dp.at(i.first()))
            .reduce(0, Integer::sum);
    }

    private int solve(Scorer scorer) {
        return map.elementStream()
            .filter(i -> i.second() == 0)
            .map(i -> scorer.calc(i.first()))
            .reduce(0, Integer::sum);
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
