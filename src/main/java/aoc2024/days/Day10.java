package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day10 extends AbstractDay {

    interface Scorer {
        int calc(int r, int c);
    }

    List<List<Integer>> map;
    final List<Vector2> dd = Arrays.asList(
        new Vector2(-1,  0), new Vector2( 0, -1),
        new Vector2( 1,  0), new Vector2( 0,  1));

    @Override
    public void prepare(String fn) throws IOException {
        map = IO.readLines(fn).stream()
            .map(s -> Stream.of(s.split("")).map(Integer::parseInt).collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    boolean mapContains(Vector2 p) {
        return (0 <= p.y()) && (p.y() < map.size()) && (0 <= p.x()) && (p.x() < map.get(0).size());
    }

    int mapAt(List<List<Integer>> table, Vector2 p) {
        return table.get(p.y()).get(p.x());
    }

    List<Vector2> getNeighbours(Vector2 point, int height) {
        List<Vector2> nbrs = new ArrayList<>(4);
        for (Vector2 d: dd) {
            Vector2 p = point.plus(d);
            if (mapContains(p) && (mapAt(map, p) == height)) {
                nbrs.add(p);
            }
        }
        return nbrs;
    }

    int calcTrailHeadScore(int r, int c) {
        HashSet<Vector2> current = new HashSet<>(Arrays.asList(new Vector2(c, r)));
        HashSet<Vector2> next = new HashSet<>();
        for (int height = 1; (height < 10) && (!current.isEmpty()); height++) {
            for (Vector2 p: current) {
                next.addAll(getNeighbours(p, height));
            }
            current = next;
            next = new HashSet<>();
        }
        return current.size();
    }

    int calcTrailHeadRaiting(int r, int c) {
        List<List<Integer>> dp = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            List<Integer> row = new ArrayList<>(map.get(i).size());
            for (int j = 0; j < map.get(i).size(); j++) {
                row.add(0);
            }
            dp.add(row);
        }
        dp.get(r).set(c, 1);

        HashSet<Vector2> current = new HashSet<>(Arrays.asList(new Vector2(c, r)));
        HashSet<Vector2> next = new HashSet<>();
        for (int height = 1; (height < 10) && (!current.isEmpty()); height++) {
            for (Vector2 p: current) {
                int q = mapAt(dp, p);
                for (Vector2 n: getNeighbours(p, height)) {
                    dp.get(n.y()).set(n.x(), mapAt(dp, n) + q);
                    next.add(n);
                }
            }

            current = next;
            next = new HashSet<>();
        }

        int res = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == 9) {
                    res += dp.get(i).get(j);
                }
            }
        }

        return res;
    }

    int solve(Scorer scorer) {
        int res = 0;
        for (int row = 0; row < map.size(); row++) {
            for (int col = 0; col < map.get(row).size(); col++) {
                if (map.get(row).get(col) == 0) {
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
