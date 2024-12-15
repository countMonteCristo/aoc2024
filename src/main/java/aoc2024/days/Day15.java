package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day15 extends AbstractDay {

    class Box {
        public int id;
        ArrayList<Vector2> parts;

        Box(int i, Vector2 start, int count) {
            id = i;
            parts = new ArrayList<>();
            for (int n = 0; n < count; n++) {
                parts.add(new Vector2(start.x() + n, start.y()));
            }
        }

        void move(Vector2 d) {
            parts.replaceAll(p -> p.plus(d));
        }
    }

    class Warehouse {
        HashMap<Vector2, Integer> boxes;
        HashMap<Integer, Box> storage;
        HashSet<Vector2> walls;
        public Vector2 bot;
        int xScale;

        Warehouse(int scale) {
            boxes = new HashMap<>();
            storage = new HashMap<>();
            walls = new HashSet<>();
            xScale = scale;
        }

        void addWall(int row, int col) {
            for (int i = 0; i < xScale; i++) {
                walls.add(new Vector2(xScale * col + i, row));
            }
        }
        void addBox(int row, int col, int id) {
            Box box = new Box(id, new Vector2(xScale * col, row), xScale);
            box.parts.forEach(p -> boxes.put(p, box.id));
            storage.put(box.id, box);
        }
        void addBot(int row, int col) {
            bot = new Vector2(xScale * col, row);
        }

        void popBox(int id) {
            getBox(id).parts.forEach(p -> boxes.remove(p));
        }
        void insertBox(int id) {
            getBox(id).parts.forEach(p -> boxes.put(p, id));
        }

        boolean hasBoxAt(Vector2 pos) {
            return boxes.containsKey(pos);
        }
        boolean hasWallAt(Vector2 pos) {
            return walls.contains(pos);
        }

        int getBoxIdAt(Vector2 pos) {
            return boxes.get(pos);
        }
        Box getBox(int id) {
            return storage.get(id);
        }

        int getGPS() {
            return storage.values().stream()
                .map(b -> b.parts.getFirst())
                .map(b -> b.x() + b.y() * 100)
                .reduce(0, Integer::sum);
        }
    }

    final static List<Vector2> dd = Arrays.asList(
        new Vector2(-1,  0), new Vector2( 0, -1),
        new Vector2( 1,  0), new Vector2( 0,  1));

    List<Vector2> moves;
    HashMap<Integer, Warehouse> wh;

    static Vector2 moveCharToVector2(String c) {
        if (c.equals("v")) return dd.get(3);
        if (c.equals(">")) return dd.get(2);
        if (c.equals("^")) return dd.get(1);
        if (c.equals("<")) return dd.get(0);
        System.out.printf("Wrong direction: %s\n", c.toString());
        return new Vector2(0,0);
    }

    @Override
    public void prepare(String fn) throws IOException {
        String[] inputParts = IO.readAll(fn).split("\n\n");

        String[] map = inputParts[0].split("\n");

        wh = new HashMap<>();
        for (int i = 1; i <= 2; i++)
            wh.put(i, new Warehouse(i));

        int boxId = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length(); col++) {
                if (map[row].charAt(col) == '.') continue;

                for (Warehouse w: wh.values()) {
                    if (map[row].charAt(col) == '#') {
                        w.addWall(row, col);
                    }
                    if (map[row].charAt(col) == 'O') {
                        w.addBox(row, col, boxId);
                        boxId++;
                    }
                    if (map[row].charAt(col) == '@') {
                        w.addBot(row, col);
                    }
                }
            }
        }

        moves = Stream.of(String.join("", inputParts[1].split("\n")).split(""))
            .map(Day15::moveCharToVector2)
            .collect(Collectors.toList());
    }

    HashSet<Integer> getMovingBoxIds(Vector2 pos, Vector2 d, Warehouse wh) {
        if (!wh.hasBoxAt(pos)) return null;

        HashSet<Integer> ids = new HashSet<>();

        int id = wh.getBoxIdAt(pos);
        Stack<Integer> stack = new Stack<>();
        stack.push(id);
        while (!stack.isEmpty()) {
            id = stack.pop();
            if (ids.contains(id))
                continue;

            ids.add(id);

            Box box = wh.getBox(id);
            for (Vector2 p: box.parts) {
                Vector2 next = p.plus(d);
                if (wh.hasWallAt(next))
                    return null;

                if (wh.hasBoxAt(next))
                    stack.push(wh.getBoxIdAt(next));
            }
        }
        return ids;
    }

    void moveBot(Vector2 d, Warehouse wh) {
        Vector2 first = wh.bot.plus(d);
        if (!wh.hasBoxAt(first) && !wh.hasWallAt(first)) {
            wh.bot = first;
            return;
        }

        HashSet<Integer> movingBoxesIds = getMovingBoxIds(first, d, wh);
        if (movingBoxesIds == null) {
            return;
        }

        movingBoxesIds.forEach(wh::popBox);
        movingBoxesIds.forEach(i -> wh.getBox(i).move(d));
        movingBoxesIds.forEach(wh::insertBox);

        wh.bot = first;
    }

    void show(Warehouse wh, Vector2 mapSize) {
        for (int row = 0; row < mapSize.y(); row++) {
            for (int col = 0; col < mapSize.x(); col++) {
                Vector2 p = new Vector2(col, row);
                String c = ".";
                if (wh.hasWallAt(p)) c = "#";
                if (wh.hasBoxAt(p)) c = "O";
                if (wh.bot.equals(p)) c = "@";
                System.out.print(c);
            }
            System.out.println();
        }
    }

    int solve(int whId) {
        moves.forEach(d -> moveBot(d, wh.get(whId)));
        return wh.get(whId).getGPS();
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(1), 1437174, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(2), 1437468, strict);
    }
}
