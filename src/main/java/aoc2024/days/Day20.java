package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import aoc2024.utils.Array2d;
import aoc2024.utils.Vector2;


public class Day20 extends AbstractDay {

    private static final Vector2[] dirs = {Vector2.RIGHT, Vector2.UP, Vector2.LEFT, Vector2.DOWN};

    private Array2d<Character> maze;
    private Vector2 start;
    private Vector2 finish;

    private ArrayList<Vector2> path;
    private int[][] cache;

    private int getMinSavedTime(boolean strict) {
        if (strict) return 100;
        return currentPart.equals(Part1) ? 1 : 50;
    }

    @Override
    public void prepare(String fn) throws IOException {
        maze = Array2d.parseCharTable(fn);
        // NOTE: elementStream crashes when array2d is empty
        maze.elementStream()
            .forEach(pair -> {
                if (pair.second() == 'S') start = pair.first();
                if (pair.second() == 'E') finish = pair.first();
            });

        path = findShortestPath(start, finish);
        cache = new int[maze.height()][maze.width()];
        Arrays.stream(cache).forEach(a -> Arrays.fill(a, -1));
        for (int i = 0; i < path.size(); i++) {
            cache[path.get(i).y()][path.get(i).x()] = i;
        }
    }

    private ArrayList<Vector2> findShortestPath(Vector2 begin, Vector2 end) {
        Vector2 current = begin;
        ArrayList<Vector2> path = new ArrayList<>();
        path.add(begin);
        while (!current.equals(end)) {
            for (Vector2 d: dirs) {
                Vector2 next = current.plus(d);
                if (!maze.contains(next) || (maze.at(next) == '#') || (path.size() > 1 && path.get(path.size() - 2).equals(next))) continue;
                path.add(next);
                current = next;
            }
        }
        return path;
    }

    private int solve(int minSavedTime, int maxCheatTime) {
        HashMap<Integer, Integer> cheats = new HashMap<>();
        for (int i = 0; i < path.size() - minSavedTime; i++) {
            Vector2 p = path.get(i);
            int minX = Integer.max(0, p.x() - maxCheatTime);
            int minY = Integer.max(0, p.y() - maxCheatTime);
            int maxX = Integer.min(maze.width() - 1, p.x() + maxCheatTime);
            int maxY = Integer.min(maze.height() - 1, p.y() + maxCheatTime);
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    int j = cache[y][x];
                    if ((cache[y][x] < 0) || (j <= i))
                        continue;

                    int straightLen = j - i;
                    int cheatedLen = p.manhattan(path.get(j));
                    int saved = straightLen - cheatedLen;
                    if ((cheatedLen <= maxCheatTime) && (saved >= minSavedTime)) {
                        cheats.merge(saved, 1, Integer::sum);
                    }
                }
            }
        }
        return cheats.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(getMinSavedTime(strict), 2), 1402, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(getMinSavedTime(strict), 20), 1020244, strict);
    }
}
