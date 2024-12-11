package aoc2024.days;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day11 extends AbstractDay {

    HashMap<Long, Long> stones;

    @Override
    public void prepare(String fn) throws IOException {
        stones = Stream.of(IO.readLines(fn).getFirst().split(" "))
            .map(Long::parseLong)
            .collect(Collectors.toMap(s -> s, s -> 1L, Long::sum, HashMap::new));
    }

    int countDigits(long stone) {
        int count = 0;
        while (stone > 0) {
            stone /= 10;
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
        var current = stones;

        for (int i = 0; i < N; i++) {
            HashMap<Long, Long> next = new HashMap<>();
            current.forEach((stone, count) -> {
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
            });
            current = next;
        }

        return current.values().stream().reduce(0L, Long::sum);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(25), 182081L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(75), 216318908621637L, strict);
    }
}