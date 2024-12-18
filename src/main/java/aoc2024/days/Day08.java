package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day08 extends AbstractDay {

    private interface AntinodesGetter {
        HashSet<Vector2> collect(Vector2 a, Vector2 b);
    }

    private HashMap<Character,List<Vector2>> antennas;
    private int width = 0;
    private int height = 0;

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

    private int gcd(int x, int y) {
        if (x < y) {
            int t = x;
            x = y;
            y = t;
        }

        if (y == 0) return x;
        return gcd(y, x % y);
    }

    private boolean mapContains(int x, int y) {
        return (0 <= y) && (y < height) && (0 <= x) && (x < width);
    }

    private HashSet<Vector2> getAntinodes(Vector2 a, Vector2 b) {
        HashSet<Vector2> result = new HashSet<>();
        Vector2 r = b.minus(a);

        Vector2 p1 = new Vector2(a.x() - r.x(), a.y() - r.y());
        if (mapContains(p1.x(), p1.y())) result.add(p1);

        Vector2 p2 = new Vector2(b.x() + r.x(), b.y() + r.y());
        if (mapContains(p2.x(), p2.y())) result.add(p2);

        return result;
    }

    private HashSet<Vector2> getAllAntinodes(Vector2 a, Vector2 b) {
        HashSet<Vector2> result = new HashSet<>();

        Vector2 r = b.minus(a);
        int d = gcd(Math.abs(r.y()), Math.abs(r.x()));
        int dr = r.y() / d;
        int dc = r.x() / d;

        for (int n = -Integer.max(width, height); n <= Integer.max(width, height); n++) {
            Vector2 p = new Vector2(a.x() + n*dc, a.y() + n*dr);
            if (mapContains(p.x(), p.y())) {
                result.add(p);
            }
        }

        return result;
    }

    private int solve(AntinodesGetter getter) {
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
