package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import aoc2024.utils.Array2d;
import aoc2024.utils.IO;
import aoc2024.utils.Pair;
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
    private HashMap<Character, Vector2> dirPos = new HashMap<>() {{
        put('<', new Vector2(0, 1));
        put('v', new Vector2(1, 1));
        put('>', new Vector2(2, 1));
        put('^', new Vector2(1, 0));
        put('A', new Vector2(2, 0));
    }};

    private HashMap<Pair<Character, Character>, Integer> pairToIdx;
    private HashMap<Integer, Pair<Character, Character>> idxToPair;

    private HashMap<Character, HashMap<Character, List<String>>> numPaths;
    private HashMap<Character, HashMap<Character, List<String>>> dirPaths;
    private int[][][] t;    // сколько раз пара p_k встречается в j-м пути пары p_i
    private int[][] u;
    private int[] du;

    int getQ(int index) {
        Pair<Character, Character> p = idxToPair.get(index);
        return dirPaths.get(p.first()).get(p.second()).getFirst().length() + 1; // + 1 из-за нажатия на 'A'
    }

    @Override
    public void prepare(String fn) throws IOException {
        lines = IO.readLines(fn);

        pairToIdx = new HashMap<>();
        idxToPair = new HashMap<>();
        String chars = "<^v>A";
        int index = 0;
        for (int i = 0; i < chars.length(); i++) {
            for (int j = 0; j < chars.length(); j++) {
                Pair<Character, Character> p = new Pair<>(chars.charAt(i), chars.charAt(j));
                pairToIdx.put(p, index);
                idxToPair.put(index, p);
                index++;
            }
        }

        numPaths = buildKeypad(numericKeypad);
        dirPaths = buildKeypad(directionalKeypad);

        t = new int[idxToPair.size()][][];
        for (int i = 0; i < idxToPair.size(); i++) {
            int[][] row_kj = new int[idxToPair.size()][];
            for (int k = 0; k < idxToPair.size(); k++) {
                row_kj[k] = new int[] {0, 0};                     // 2 - максимальное кол-во путей
            }
            t[i] = row_kj;
        }

        u = new int[idxToPair.size()][];
        for (int i = 0; i < idxToPair.size(); i++) {
            u[i] = new int[] {0, 0};
        }

        for (int i = 0; i < idxToPair.size(); i++) {
            Pair<Character, Character> p = idxToPair.get(i);
            List<String> paths = dirPaths.get(p.first()).get(p.second());
            for (int j = 0; j < paths.size(); j++) {
                String path = paths.get(j);
                int k;
                Pair<Character, Character> pq;
                if (!path.isEmpty()) {
                    pq = new Pair<>('A', path.charAt(0));
                    k = pairToIdx.get(pq);
                    t[i][k][j]++;
                    for (int q = 0; q < path.length() - 1; q++) {
                        pq = new Pair<>(path.charAt(q), path.charAt(q+1));
                        k = pairToIdx.get(pq);
                        t[i][k][j]++;
                    }
                    pq = new Pair<>(path.charAt(path.length() - 1), 'A');
                    k = pairToIdx.get(pq);
                    t[i][k][j]++;
                } else {
                    pq = new Pair<>('A', 'A');
                    k = pairToIdx.get(pq);
                    t[i][k][j]++;
                }
            }
        }

        for (int i = 0; i < idxToPair.size(); i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < idxToPair.size(); k++) {
                    u[i][j] += getQ(k) * t[i][k][j];
                }
            }
        }

        du = new int[idxToPair.size()];
        for (int i = 0; i < idxToPair.size(); i++) {
            du[i] = u[i][0] - u[i][1];
        }
    }

    private long getNextN(int k, HashMap<Integer, Long> s) {
        long n = 0;
        for (int i = 0; i < idxToPair.size(); i++) {
            long ni = s.getOrDefault(i, 0L);
            Pair<Character, Character> pi = idxToPair.get(i);
            if (dirPaths.get(pi.first()).get(pi.second()).size() == 1) {
                n += ni * t[i][k][0];
            } else {
                long xi = (du[i] <= 0) ? ni : 0;
                n += xi * t[i][k][0] + (ni - xi) * t[i][k][1];
            }
        }

        return n;
    }

    private int getTransformedSize(String line, HashMap<Character, HashMap<Character, List<String>>> dirPaths) {
        Character c = 'A';
        int len = 0;
        for (int i = 0; i < line.length(); i++) {
            len += dirPos.get(c).manhattan(dirPos.get(line.charAt(i))) + 1;
            c = line.charAt(i);
        }
        return len;
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

    private List<String> getPaths(String line, HashMap<Character, HashMap<Character, List<String>>> keypadPaths) {
        Character c = 'A';
        List<String> cur = Arrays.asList("");
        for (int i = 0; i < line.length(); i++) {
            List<String> nxt = new ArrayList<>();
            List<String> paths = keypadPaths.get(c).get(line.charAt(i));
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

    private String getOnePath(String line, HashMap<Character, HashMap<Character, List<String>>> keypadPaths) {
        Character c = 'A';
        String cur = "";
        for (int i = 0; i < line.length(); i++) {
            List<String> paths = keypadPaths.get(c).get(line.charAt(i));
            cur = cur + paths.getFirst() + 'A';
            c = line.charAt(i);
        }
        return cur;
    }

    void iterLine(String line, int N, HashMap<Character, HashMap<Character, List<String>>> numPaths, HashMap<Character, HashMap<Character, List<String>>> dirPaths) {
        System.out.println(line);
        String s = getOnePath(line, numPaths);
        System.out.println(s);
        for (int i = 0; i < N; i++) {
            s = getOnePath(s, dirPaths);
            System.out.printf("%d -> %s\n", getTransformedSize(s, dirPaths), s);
        }
    }

    private HashMap<Integer, Long> compress(String line) {
        HashMap<Integer, Long> res = new HashMap<>();
        char c = 'A';
        for (int i = 0; i < line.length(); i++) {
            Pair<Character, Character> pi = new Pair<>(c, line.charAt(i));
            int index = pairToIdx.get(pi);
            res.merge(index, 1L, Long::sum);
            c =  line.charAt(i);
        }
        return res;
    }

    private long compressedSize(HashMap<Integer, Long> map) {
        return map.values().stream().reduce(0L, Long::sum);
    }

    private long compressedTransformedSize(HashMap<Integer, Long> map) {
        long n = 0;
        for (int i = 0; i < idxToPair.size(); i++) {
            n += getQ(i) * map.getOrDefault(i, 0L);
        }
        return n;
    }

    private record Item(HashMap<Integer, Long> map, long size, long trSize) {}

    private long getMinInputSize(String line, int N) {
        List<Item> cur = getPaths(line, numPaths).stream()
            .map(s -> {
                HashMap<Integer, Long> map = compress(s);
                long size = compressedSize(map);
                long trSize = compressedTransformedSize(map);
                return new Item(map, size, trSize);
            })
            .collect(Collectors.toList());
        for (int it = 0; it < N; it++) {
            long min = cur.stream()
                .map(Item::trSize)
                .reduce(Long.MAX_VALUE, Long::min);

            List<Item> nxt = new ArrayList<>();
            for (Item item: cur) {
                // System.out.println(item);
                if (item.trSize > min) continue;
                HashMap<Integer, Long> nxtMap = new HashMap<>();
                for (int k = 0; k < idxToPair.size(); k++) {
                    nxtMap.put(k, getNextN(k, item.map));
                }
                nxt.add(new Item(nxtMap, compressedSize(nxtMap), compressedTransformedSize(nxtMap)));
            }
            // System.out.println();

            cur = nxt;
        }

        long min = cur.stream()
            .map(Item::trSize)
            .reduce(Long.MAX_VALUE, Long::min);
        long val = Long.parseLong(line.substring(0, line.length() - 1));

        // System.out.printf("%s: %d * %d = %d\n", line, min, val, min*val);
        return min * val;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        long res = lines.stream()
            .map(line -> getMinInputSize(line, 1))
            .reduce(0L, Long::sum);

        return check(res, 163086L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        long res = lines.stream()
            .map(line -> getMinInputSize(line, 24))
            .reduce(0L, Long::sum);

        // 226210762716922 - too high (N=24)
        // 226210762716921 - too high
        // 90368992427904  - too low (N=23)
        return check(res, 0, strict);
    }
}

