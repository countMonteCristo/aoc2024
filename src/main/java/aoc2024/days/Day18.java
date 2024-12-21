package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day18 extends AbstractDay {

    private record Item(List<Vector2> path, Vector2 last) {}

    private List<Vector2> bytes;
    private static final Vector2[] dirs = {Vector2.RIGHT, Vector2.UP, Vector2.LEFT, Vector2.DOWN};

    private static final Vector2 testSize = new Vector2(7, 7);
    private static final Vector2 prdSize = new Vector2(71, 71);

    private static final int testCount = 12;
    private static final int prdCount = 1024;

    private static final Vector2 begin = new Vector2(0, 0);

    @Override
    public void prepare(String fn) throws IOException {
        bytes = IO.readLines(fn).stream()
            .map(line -> {
                String[] parts = line.split(",");
                return new Vector2(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            })
            .collect(Collectors.toList());
    }

    private boolean within(Vector2 p, Vector2 size) {
        return (0 <= p.x()) && (p.x() < size.x()) && (0 <= p.y()) && (p.y() < size.y());
    }

    private Item findShortestPath(Vector2 begin, Vector2 end, HashSet<Vector2> fallen, Vector2 size) {
        Item minItem = null;
        PriorityQueue<Item> q = new PriorityQueue<>(
            (p1, p2) -> Integer.compare(p1.path.size(), p2.path.size()));

        HashSet<Vector2> visited = new HashSet<>();

        List<Vector2> path = new ArrayList<>();
        path.add(begin);
        q.add(new Item(path, begin));

        while (!q.isEmpty()) {
            Item i = q.poll();
            if (i.last.equals(end)) {
                minItem = i;
                break;
            };

            if (visited.contains(i.last)) continue;

            for (Vector2 d: dirs) {
                Vector2 next = i.last.plus(d);
                if (!within(next, size) || fallen.contains(next) || visited.contains(next)) continue;

                List<Vector2> newPath = new ArrayList<>(i.path);
                newPath.add(next);
                q.add(new Item(newPath, next));
            }
            visited.add(i.last);
        }
        return minItem;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        Vector2 size = strict ? prdSize : testSize;
        int count = strict ? prdCount : testCount;
        Vector2 end = new Vector2(size.x() - 1, size.y() - 1);
        HashSet<Vector2> fallen = new HashSet<>(bytes.subList(0, count));
        return check(findShortestPath(begin, end, fallen, size).path.size() - 1, 340, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        Vector2 size = strict ? prdSize : testSize;
        Vector2 end = new Vector2(size.x() - 1, size.y() - 1);

        HashSet<Vector2> fallen = new HashSet<>();
        Item i = findShortestPath(begin, end, fallen, size);
        Vector2 lastByte = new Vector2(-1, -1);
        for (Vector2 p: bytes) {
            fallen.add(p);
            if (i.path.contains(p)) i = findShortestPath(begin, end, fallen, size);

            if (i == null) {
                lastByte = p;
                break;
            }
        }
        String res = String.format("%d,%d", lastByte.x(), lastByte.y());

        return check(res, "34,32", strict);
    }
}
