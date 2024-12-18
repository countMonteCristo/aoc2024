package aoc2024.days;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day11 extends AbstractDay {

    private HashMap<Long, Long> stones;
    private HashMap<Integer, Long> powers;

    @Override
    public void prepare(String fn) throws IOException {
        stones = Stream.of(IO.readLines(fn).getFirst().split(" "))
            .map(Long::parseLong)
            .collect(Collectors.toMap(s -> s, s -> 1L, Long::sum, HashMap::new));
        powers = new HashMap<>();
    }

    private int countDigits(long stone) {
        int count = 0;
        while (stone > 0) {
            stone /= 10;
            count++;
        }
        return count;
    }

    private Pair<Long, Long> splitStone(long stone, int numDigits) {
        long power = 1;
        if (powers.containsKey(numDigits)) {
            power = powers.get(numDigits);
        } else {
            for (int i = 0; i < numDigits / 2; i++) {
                power *= 10;
            }
            powers.put(numDigits, power);
        }

        return new Pair<>(stone / power, stone % power);
    }

    private long solve(int N) {
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
