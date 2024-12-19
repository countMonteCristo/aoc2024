package aoc2024.days;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import aoc2024.utils.IO;


public class Day19 extends AbstractDay {

    private HashSet<String> patterns;
    private List<String> designs;
    private HashMap<String, Long> cache;

    @Override
    public void prepare(String fn) throws IOException {
        List<String> lines = IO.readLines(fn);

        patterns = new HashSet<>(Arrays.asList(lines.get(0).split(", ")));
        designs = lines.subList(2, lines.size());

        cache = new HashMap<>();
    }

    private boolean isPossible(String design, int start) {
        if (start == design.length()) return true;

        for (String pat: patterns) {
            if (design.regionMatches(start, pat, 0, pat.length()) && isPossible(design, start + pat.length()))
                return true;
        }
        return false;
    }

    private long countPossible(String design, int start) {
        if (start == design.length()) return 1;

        String rem = design.substring(start);
        if (cache.containsKey(rem)) {
            return cache.get(rem);
        }

        long count = 0;
        for (String pat: patterns) {
            if (design.regionMatches(start, pat, 0, pat.length()))
                count += countPossible(design, start + pat.length());
        }
        cache.put(rem, count);
        return count;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(designs.stream().filter(d -> isPossible(d, 0)).count(), 263L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(designs.stream().map(d -> countPossible(d, 0)).reduce(0L, Long::sum), 723524534506343L, strict);
    }
}
