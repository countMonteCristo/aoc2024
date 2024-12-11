package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day08 extends AbstractDay {

    interface AntinodesGetter {
        HashSet<Vector2> collect(Vector2 a, Vector2 b);
    }

    HashMap<Character,List<Vector2>> antennas;
    int width = 0;
    int height = 0;

    @Override
    public void prepare(String fn) throws IOException {
        antennas = new HashMap<>();

        List<String> lines = IO.readLines(fn);
        width = lines.get(0).length();
        height = lines.size();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char c = lines.get(row).charAt(col);
                if (c == '.') continue;

                List<Vector2> points = antennas.getOrDefault(c, new ArrayList<>());
                points.add(new Vector2(col, row));

                antennas.put(c, points);
            }
        }
    }

    int gcd(int x, int y) {
        if (x < y) {
            int t = x;
            x = y;
            y = t;
        }

        if (y == 0) return x;
        return gcd(y, x % y);
    }

    boolean mapContains(int x, int y) {
        return (0 <= y) && (y < height) && (0 <= x) && (x < width);
    }

    HashSet<Vector2> getAntinodes(Vector2 a, Vector2 b) {
        HashSet<Vector2> result = new HashSet<>();

        int dRow = b.y() - a.y();
        int dCol = b.x() - a.x();

        Vector2 p1 = new Vector2(a.x() - dCol, a.y() - dRow);
        if (mapContains(p1.x(), p1.y())) result.add(p1);

        Vector2 p2 = new Vector2(b.x() + dCol, b.y() + dRow);
        if (mapContains(p2.x(), p2.y())) result.add(p2);

        return result;
    }

    HashSet<Vector2> getAllAntinodes(Vector2 a, Vector2 b) {
        HashSet<Vector2> result = new HashSet<>();

        int dRow = b.y() - a.y();
        int dCol = b.x() - a.x();
        int d = gcd(Math.abs(dRow), Math.abs(dCol));
        int dr = dRow / d;
        int dc = dCol / d;

        for (int n = -Integer.max(width, height); n <= Integer.max(width, height); n++) {
            Vector2 p = new Vector2(a.x() + n*dc, a.y() + n*dr);
            if (mapContains(p.x(), p.y())) {
                result.add(p);
            }
        }

        return result;
    }

    int solve(AntinodesGetter getter) {
        HashSet<Vector2> res = new HashSet<>();

        antennas.values().stream()
            .forEach(points -> {
                for (int i = 0; i < points.size(); i++) {
                    for (int j = i + 1; j < points.size(); j++) {
                        res.addAll(getter.collect(points.get(i), points.get(j)));
                    }
                }});
        return res.size();
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(this::getAntinodes), 249, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(this::getAllAntinodes), 905, strict);
    }
}
