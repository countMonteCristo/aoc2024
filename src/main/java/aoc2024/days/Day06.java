package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;
import aoc2024.utils.Vector2;


public class Day06 extends AbstractDay {

    List<char[]> map;
    Vector2 start;
    Vector2[] vels = {new Vector2(0, -1), new Vector2(1, 0), new Vector2(0, 1), new Vector2(-1, 0)};

    @Override
    public void prepare(String fn) throws IOException {
        map = new ArrayList<>();
        List<String>input = IO.readLines(fn);
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            map.add(line.toCharArray());
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '^') {
                    map.get(row)[col] = '.';
                    start = new Vector2(col, row);
                }
            }
        }
    }

    boolean inside(Vector2 p) {
        return (p.x() >= 0) && (p.x() < map.get(0).length) && (p.y() >= 0) && (p.y() < map.size());
    }

    int rotateVelocity(int idx) {
        return (idx + 1) % vels.length;
    }

    char mapAt(Vector2 p) {
        return map.get(p.y())[p.x()];
    }

    int startPath(Vector2 pos, int velIdx, Set<Pair<Vector2, Vector2>> states, Vector2 obs, boolean collect_path) {
        Set<Pair<Vector2, Vector2>> local_states = new HashSet<>();
        Set<Vector2> points = collect_path ? new HashSet<>() : null;
        while (true) {
            Pair<Vector2, Vector2> p = new Pair<>(pos, vels[velIdx]);
            if (states.contains(p) || local_states.contains(p)) {
                return -1;
            }
            local_states.add(p);
            if (collect_path)
                points.add(pos);

            Vector2 next = pos.plus(vels[velIdx]);
            if (!inside(next)) {
                return collect_path ? points.size() : 0;
            }

            if ((mapAt(next) == '#') || next.equals(obs)) {
                velIdx = rotateVelocity(velIdx);
            } else {
                pos = next;
            }
        }
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(startPath(start, 0, new HashSet<>(), new Vector2(-1, -1), true), 5177, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        Set<Pair<Vector2, Vector2>> states = new HashSet<>();
        Set<Vector2> obstacles = new HashSet<>();
        Set<Vector2> path = new HashSet<>(List.of(start));
        Vector2 pos = start.copy();
        int velIdx = 0;

        while (true) {
            states.add(new Pair<>(pos, vels[velIdx]));
            Vector2 next = pos.plus(vels[velIdx]);
            if (!inside(next)) {
                break;
            }

            int newVelIdx = rotateVelocity(velIdx);
            if (mapAt(next) == '#') {
                velIdx = newVelIdx;
            } else {
                if (!path.contains(next) && (startPath(pos, newVelIdx, states, next, false) == -1)) {
                    obstacles.add(next);
                } else {
                    path.add(next);
                }
                pos = next;
            }
        }

        return check(obstacles.size(), 1686, strict);
    }
}
