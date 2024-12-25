package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import aoc2024.utils.Array2d;
import aoc2024.utils.IO;


public class Day25 extends AbstractDay {

    private enum SchemType {
        LOCK,
        KEY
    }
    private class Item {
        SchemType type;
        int[] values;
        int height;

        public Item(Array2d<Character> s) {
            boolean top = IntStream.range(0, s.width())
                .allMatch(c -> s.at(0, c) == '#');
            type = top ? SchemType.LOCK : SchemType.KEY;
            values = new int[s.width()];
            height = s.height() - 2;
            if (type == SchemType.LOCK) {
                for (int c = 0; c < s.width(); c++) {
                    for (int r = 1; r < s.height(); r++) {
                        if (s.at(r, c) == '.') {
                            values[c] = r - 1;
                            break;
                        }
                    }
                }
            } else {
                for (int c = 0; c < s.width(); c++) {
                    for (int r = s.height() - 2; r >= 0; r--) {
                        if (s.at(r, c) == '.') {
                            values[c] = s.height() - 2 - r;
                            break;
                        }
                    }
                }
            }
        }

        public int fit(Item key) {
            for (int i = 0; i < values.length; i++) {
                if ((values[i] + key.values[i]) > height)
                    return 0;
            }
            return 1;
        }
    }

    private List<Item> keys;
    private List<Item> locks;

    @Override
    public void prepare(String fn) throws IOException {
        keys = new ArrayList<>();
        locks = new ArrayList<>();
        List<Array2d<Character>> schematics = Stream.of(IO.readAll(fn).split("\n\n"))
            .map(p -> new Array2d<Character>(Stream.of(p.split("\n")), "", s -> s.charAt(0)))
            .collect(Collectors.toList());

        for (Array2d<Character> s: schematics) {
            Item i = new Item(s);
            if (i.type == SchemType.LOCK) {
                locks.add(i);
            } else {
                keys.add(i);
            }
        }

    }

    @Override
    public boolean part1Impl(boolean strict) {
        int fit = 0;
        for (Item lock: locks) {
            for (Item key: keys) {
                fit += lock.fit(key);
            }
        }
        return check(fit, 3133, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(0, 0, strict);
    }
}
