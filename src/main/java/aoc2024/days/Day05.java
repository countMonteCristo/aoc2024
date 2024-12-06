package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day05 extends AbstractDay {

    List<List<Integer>> updates;
    HashSet<Pair<Integer,Integer>> rules;

    @Override
    public void prepare(String fn) throws IOException {
        rules = new HashSet<>();
        updates = new ArrayList<>();

        boolean parse_rules = true;
        for (String line : IO.readLines(fn)) {
            if (line.isEmpty()) {
                parse_rules = false;
                continue;
            }
            if (parse_rules) {
                String[] parts = line.split("\\|");
                rules.add(new Pair<>(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                updates.add(Stream.of(line.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            }
        }
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
    public void part1Impl(boolean strict) {
        check(updates.stream().map(this::processUpdate).reduce(0, Integer::sum), 5713, strict);
    }

    @Override
    public void part2Impl(boolean strict) {
        int result = updates.stream()
            .filter(u -> processUpdate(u).equals(0))
            .map(this::processsBrokenUpdate)
            .reduce(0, Integer::sum);
        check(result, 5180, strict);
    }
}
