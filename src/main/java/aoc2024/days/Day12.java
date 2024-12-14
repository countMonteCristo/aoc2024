package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.Pair;
import aoc2024.utils.Vector2;


public class Day12 extends AbstractDay {

    record Region(HashSet<Vector2> points, char label, int perimeter, int sides) {}

    Array2d<Character> garden;
    List<Region> regions;

    final List<Vector2> dd = Arrays.asList(
        new Vector2(-1,  0), new Vector2( 0, -1),
        new Vector2( 1,  0), new Vector2( 0,  1));

    @Override
    public void prepare(String fn) throws IOException {
        garden = Array2d.parseCharTable(fn);
        regions = new ArrayList<>();

        for (int row = 0; row < garden.height(); row++) {
            for (int col = 0; col < garden.width(); col++) {
                Vector2 p = new Vector2(col, row);
                if (regions.stream().anyMatch(r -> r.points.contains(p))) {
                    continue;
                }
                regions.add(buildRegion(p));
            }
        }
    }

    Pair<Integer, Boolean> checkNewSide(HashSet<Vector2> points, char label, Vector2 current, Vector2 next, boolean onSide) {
        int sides = 0;
        if (!points.contains(current)) {
            onSide = false;
        } else {
            boolean isSidePoint = (garden.at(current) == label) && (!garden.contains(next) || (garden.at(next) != label));
            if (!onSide && isSidePoint) sides++;
            onSide = (!onSide && isSidePoint) ? true : (onSide && !isSidePoint) ? false : onSide;
        }
        return new Pair<>(sides, onSide);
    }

    int countSides(HashSet<Vector2> points, char label, int minCol, int minRow, int maxCol, int maxRow) {
        int sides = 0;

        for (int dx = -1; dx < 2; dx += 2) {
            // count vertical sides
            for (int col = minCol; col <= maxCol; col++) {
                boolean onSide = false;
                for (int row = minRow; row <= maxRow; row++) {
                    var q = checkNewSide(points, label, new Vector2(col, row), new Vector2(col + dx, row), onSide);
                    sides += q.first();
                    onSide = q.second();
                }
            }

            // count vertical sides
            for (int row = minRow; row <= maxRow; row++) {
                boolean onSide = false;
                for (int col = minCol; col <= maxCol; col++) {
                    var q = checkNewSide(points, label, new Vector2(col, row), new Vector2(col, row + dx), onSide);
                    sides += q.first();
                    onSide = q.second();
                }
            }
        }

        return sides;
    }

    Region buildRegion(Vector2 p) {
        HashSet<Vector2> points = new HashSet<>();
        char label = garden.at(p);

        HashSet<Vector2> edge = new HashSet<>(List.of(p));
        while (!edge.isEmpty()) {
            HashSet<Vector2> next = new HashSet<>();
            for (Vector2 e: edge) {
                for (Vector2 d: dd) {
                    Vector2 n = e.plus(d);
                    if (garden.contains(n) && (garden.at(n) == label) && !points.contains(n)) {
                        next.add(n);
                    }
                }
            }
            points.addAll(edge);
            edge = next;
        }

        int perimeter = 0;
        for (Vector2 point: points) {
            for (Vector2 d: dd) {
                Vector2 n = point.plus(d);
                if (!points.contains(n)) {
                    perimeter++;
                }
            }
        }

        int minRow = points.stream().map(q -> q.y()).reduce(Integer.MAX_VALUE, Integer::min);
        int maxRow = points.stream().map(q -> q.y()).reduce(Integer.MIN_VALUE, Integer::max);
        int minCol = points.stream().map(q -> q.x()).reduce(Integer.MAX_VALUE, Integer::min);
        int maxCol = points.stream().map(q -> q.x()).reduce(Integer.MIN_VALUE, Integer::max);
        int sides = countSides(points, label, minCol, minRow, maxCol, maxRow);

        return new Region(points, label, perimeter, sides);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        int score = regions.stream()
            .map(r -> r.points.size() * r.perimeter)
            .reduce(0, Integer::sum);

        return check(score, 1387004, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        int score = regions.stream()
            .map(r -> r.points.size() * r.sides)
            .reduce(0, Integer::sum);

        return check(score, 844198, strict);
    }
}
