package aoc2024.days;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import aoc2024.utils.IO;


public class Day19 extends AbstractDay {

    private List<String> patterns;
    private List<String> designs;
    private HashMap<String, Long> cache;

    @Override
    public void prepare(String fn) throws IOException {
        List<String> lines = IO.readLines(fn);

        patterns = Arrays.asList(lines.get(0).split(", "));
        patterns.sort(null);
        designs = lines.subList(2, lines.size());
        cache = new HashMap<>();
        cache.put("", 1L);
    }

    private long countPossible(String rem) {
        if (cache.containsKey(rem)) return cache.get(rem);

        long count = patterns.stream()
            .filter(pat -> rem.startsWith(pat))
            .map(pat -> countPossible(rem.substring(pat.length())))
            .reduce(0L, Long::sum);

        cache.put(rem, count);

        return count;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(designs.stream().filter(d -> countPossible(d) > 0).count(), 263L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(designs.stream().map(d -> countPossible(d)).reduce(0L, Long::sum), 723524534506343L, strict);
    }
}
