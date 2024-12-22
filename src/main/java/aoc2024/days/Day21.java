package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.Array2d;
import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day21 extends AbstractDay {

    static final Vector2[] dirs = {Vector2.RIGHT, Vector2.UP, Vector2.LEFT, Vector2.DOWN};
    static final HashMap<Vector2, String> dirMap = new HashMap<>() {{
        put(Vector2.RIGHT, ">");
        put(Vector2.LEFT,  "<");
        put(Vector2.UP,    "^");
        put(Vector2.DOWN,  "v");
    }};

    private List<String> lines;

    private Array2d<Character> numericKeypad = new Array2d<>(new Character[][] {
        {'7', '8', '9'},
        {'4', '5', '6'},
        {'1', '2', '3'},
        {'*', '0', 'A'}});
    private Array2d<Character> directionalKeypad = new Array2d<>(new Character[][] {
        {'*', '^', 'A'},
        {'<', 'v', '>'}});

    private HashMap<Character, HashMap<Character, List<String>>> numPaths;
    private HashMap<Character, HashMap<Character, List<String>>> dirPaths;

    @Override
    public void prepare(String fn) throws IOException {
        lines = IO.readLines(fn);
        cache = new HashMap<>();

        numPaths = buildKeypad(numericKeypad);
        dirPaths = buildKeypad(directionalKeypad);
    }

    private HashMap<Character, List<String>> buildAllKeypadPaths(Array2d<Character> keypad, Vector2 pos) {
        HashMap<Character, List<String>> paths = new HashMap<>();
        paths.put(keypad.at(pos), Arrays.asList(""));

        HashSet<Vector2> visited = new HashSet<>();
        HashSet<Vector2> edge = new HashSet<>();
        edge.add(pos);
        while (!edge.isEmpty()) {
            HashSet<Vector2> nextEdge = new HashSet<>();
            for (Vector2 p: edge) {
                Character curChar = keypad.at(p);
                for (Vector2 d: dirs) {
                    Vector2 next = p.plus(d);
                    if (!keypad.contains(next) || (keypad.at(next) == '*') || visited.contains(next)) continue;

                    Character nextChar = keypad.at(next);
                    if (!paths.containsKey(nextChar))
                        paths.put(nextChar, new ArrayList<>());

                    for (String path: paths.get(curChar)) {
                        paths.get(nextChar).add(path + dirMap.get(d));
                    }
                    nextEdge.add(next);
                }
                visited.add(p);
            }
            edge = nextEdge;
        }

        return paths;
    }

    private HashMap<Character, HashMap<Character, List<String>>> buildKeypad(Array2d<Character> keypad) {
        HashMap<Character, HashMap<Character, List<String>>> keypadPaths = new HashMap<>();
        keypad.elementStream()
            .forEach(p -> {
                if (p.second() != '*') {
                    keypadPaths.put(p.second(), buildAllKeypadPaths(keypad, p.first()));
                }
            });
        return keypadPaths;
    }

    private List<String> getPaths(String line) {
        Character c = 'A';
        List<String> cur = Arrays.asList("");
        for (int i = 0; i < line.length(); i++) {
            List<String> nxt = new ArrayList<>();
            List<String> paths = numPaths.get(c).get(line.charAt(i));
            for (String prefix: cur) {
                for (String p: paths) {
                    nxt.add(prefix + p + 'A');
                }
            }
            c = line.charAt(i);
            cur = nxt;
        }
        return cur;
    }

    private record Item(char from, char to, int level) {}
    private HashMap<Item, Long> cache;

    private long findShortestPathLen(char from, char to, int level) {
        if (level == 0) {
            return dirPaths.get(from).get(to).getFirst().length() + 1;
        }
        Item item = new Item(from, to, level);
        if (cache.containsKey(item)) {
            return cache.get(item);
        }

        long min = dirPaths.get(from).get(to).stream()
            .map(p -> findShortestPathLen(p + 'A', level-1))
            .reduce(Long.MAX_VALUE, Long::min);
        cache.put(item, min);
        return min;
    }

    private long findShortestPathLen(String input, int level) {
        char cur = 'A';
        long res = 0;
        for (char c: input.toCharArray()) {
            res += findShortestPathLen(cur, c, level);
            cur = c;
        }
        return res;
    }

    private long solve(int level) {
        return lines.stream()
            .map(line -> {
                long min = getPaths(line).stream()
                    .map(input -> findShortestPathLen(input, level))
                    .reduce(Long.MAX_VALUE, Long::min);
                return min * Long.parseLong(line.substring(0, line.length() - 1));
            })
            .reduce(0L, Long::sum);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(1), 163086L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(24), 198466286401228L, strict);
    }
}

