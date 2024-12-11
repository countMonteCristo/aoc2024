package aoc2024.days;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day11 extends AbstractDay {

    Map<Long, Long> stones;

    @Override
    public void prepare(String fn) throws IOException {
        stones = Stream.of(IO.readLines(fn).getFirst().split(" "))
            .map(Long::parseLong)
            .collect(Collectors.toMap(s -> s, s -> 1L));
    }

    int countDigits(long num) {
        int count = 0;
        while (num > 0) {
            num /= 10;
            count++;
        }
        return count;
    }

    Pair<Long, Long> splitStone(long stone, int numDigits) {
        long power = 1;
        for (int i = 0; i < numDigits / 2; i++) {
            power *= 10;
        }
        return new Pair<>(stone / power, stone % power);
    }

    long solve(int N) {
        HashMap<Long, Long> current = new HashMap<>(stones);

        for (int i = 0; i < N; i++) {
            HashMap<Long, Long> next = new HashMap<>();
            for (Map.Entry<Long, Long> entry: current.entrySet()) {
                long stone = entry.getKey();
                long count = entry.getValue();

                int numDigits = countDigits(stone);

                if (stone == 0L) {
                    next.merge(1L, count, Long::sum);
                } else if (numDigits % 2 == 0) {
                    Pair<Long, Long> p = splitStone(stone, numDigits);
                    next.merge(p.first(), count, Long::sum);
                    next.merge(p.second(), count, Long::sum);
                } else {
                    next.merge(stone * 2024, count, Long::sum);
                }
            }
            current = next;
        }

        return current.values().stream().reduce(0L, Long::sum);
    }

    @Override
    public void part1Impl(boolean strict) {
        check(solve(25), 182081L, strict);
    }

    @Override
    public void part2Impl(boolean strict) {
        check(solve(75), 216318908621637L, strict);
    }
}
