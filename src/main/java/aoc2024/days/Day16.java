package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import aoc2024.utils.Array2d;
import aoc2024.utils.Vector2;


public class Day16 extends AbstractDay {

    private record Vertex(Vector2 pos, int dir) {}
    private record Item(ArrayList<Vertex> path, int score) {}

    static final Vector2[] dirs = {Vector2.RIGHT, Vector2.UP, Vector2.LEFT, Vector2.DOWN};
    static final int STEP_COST = 1;
    static final int TURN_COST = 1001;

    private Array2d<Character> maze;
    private Vector2 start;
    private Vector2 finish;

    @Override
    public void prepare(String fn) throws IOException {
        maze = Array2d.parseCharTable(fn);
        maze.elementStream()
            .forEach(p -> {
                if (p.second() == 'S') start = p.first();
                if (p.second() == 'E') finish = p.first();
            });
    }

    private Item findShortestPath(Vector2 begin, int dir, Vector2 end, HashMap<Vertex, Item> vCache) {
        Item minItem = null;
        PriorityQueue<Item> q = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.score, p2.score));

        HashSet<Vertex> visited = new HashSet<>();

        ArrayList<Vertex> path = new ArrayList<>();
        path.add(new Vertex(begin, dir));
        q.add(new Item(path, 0));

        while (!q.isEmpty()) {
            Item i = q.poll();
            Vertex last = i.path.getLast();

            if (last.pos.equals(end) && minItem == null) minItem = i;

            if (visited.contains(last)) continue;

            for (int d = -1; d <= 1; d++) {
                int nextDir = (last.dir + d + dirs.length) % dirs.length;
                Vector2 next = last.pos.plus(dirs[nextDir]);
                Vertex nv = new Vertex(next, nextDir);
                if (maze.at(next) == '#' || visited.contains(nv)) continue;

                ArrayList<Vertex> newPath = new ArrayList<>(i.path);
                newPath.add(nv);

                int stepPrice = (d == 0) ? STEP_COST : TURN_COST;
                q.add(new Item(newPath, i.score + stepPrice));
            }
            visited.add(last);

            if (vCache != null) {
                vCache.merge(last, i, (i1, i2) -> {
                    if (i1.score < i2.score)
                        return i1;
                    return i2;
                });
            }
        }
        return minItem;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(findShortestPath(start, 0, finish, null).score, 90440, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        HashMap<Vertex, Item> startCache = new HashMap<>();
        Item i = findShortestPath(start, 0, finish, startCache);

        HashMap<Vertex, Item> finishCache = new HashMap<>();
        findShortestPath(finish, 2, start, finishCache);
        findShortestPath(finish, 3, start, finishCache);

        HashSet<Vector2> seats = new HashSet<>();
        maze.elementStream()
            .forEach(pair -> {
                Vector2 p = pair.first();
                if (p.equals(start) ||  pair.second() == '#') return;
                if (p.equals(finish)) {
                    for (Vertex v: i.path) seats.add(v.pos);
                }

                for (int d = 0; d < dirs.length; d++) {
                    Item v1 = startCache.get(new Vertex(p, d));
                    if (v1 == null) continue;

                    Item v2 = finishCache.get(new Vertex(p, (v1.path.getLast().dir + dirs.length / 2) % dirs.length));
                    if (v2 == null) continue;

                    if ((v1.score + v2.score) == i.score) {
                        for (Vertex v: v1.path) seats.add(v.pos);
                        for (Vertex v: v2.path) seats.add(v.pos);
                    }
                }
            });
        return check(seats.size(), 479, strict);
    }
}
