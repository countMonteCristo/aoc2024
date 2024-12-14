package aoc2024.days;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.Array2d;
import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day05 extends AbstractDay {

    Array2d<Integer> updates;
    Set<Pair<Integer,Integer>> rules;

    @Override
    public void prepare(String fn) throws IOException {
        String[] inputParts = IO.readAll(fn).split("\n\n");

        rules = Stream.of(inputParts[0].split("\n"))
            .map(line -> {
                String[] parts = line.split("\\|");
                return new Pair<Integer,Integer>(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));})
            .collect(Collectors.toSet());
        updates = new Array2d<>(Stream.of(inputParts[1].split("\n")), ",", Integer::parseInt);
    }

    Integer processUpdate(List<Integer> update) {
        for (int i = 0; i < update.size(); i++) {
            for (int j = i + 1; j < update.size(); j++) {
                if (rules.contains(new Pair<>(update.get(j), update.get(i)))) {
                    return 0;
                }
            }
        }
        return update.get(update.size() / 2);
    }

    Integer processsBrokenUpdate(List<Integer> broken) {
        for (int i = 0; i <= broken.size() / 2; i++) {
            for (int j = i + 1; j < broken.size(); j++) {
                if (rules.contains(new Pair<>(broken.get(j), broken.get(i)))) {
                    Collections.swap(broken, i, j);
                }
            }
        }
        return broken.get(broken.size() / 2);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(updates.stream().map(this::processUpdate).reduce(0, Integer::sum), 5713, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        int result = updates.stream()
            .filter(u -> processUpdate(u).equals(0))
            .map(this::processsBrokenUpdate)
            .reduce(0, Integer::sum);
        return check(result, 5180, strict);
    }
}
