package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import aoc2024.utils.IO;


public class Day01 extends AbstractDay {

    List<List<Integer>> list;

    @Override
    public void prepare(String fn) throws IOException {
        list = IO
            .readLines(fn).stream()
            .map(line -> Stream.of(line.split("   "))
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    @Override
    public void part1Impl(boolean strict) {
        ArrayList<Integer> first = new ArrayList<>();
        ArrayList<Integer> second = new ArrayList<>();

        list.forEach(item -> {
            first.add(item.get(0));
            second.add(item.get(1));});

        first.sort(null);
        second.sort(null);

        int res = IntStream
            .range(0, list.size())
            .map(i -> Math.abs(first.get(i) - second.get(i)))
            .sum();

        check(res, 2000468, strict);
    }

    @Override
    public void part2Impl(boolean strict) {
        HashMap<Integer,Integer> first = new HashMap<>();
        HashMap<Integer,Integer> second = new HashMap<>();

        list.forEach(item -> {
            first.merge(item.get(0), 1, Integer::sum);
            second.merge(item.get(1), 1, Integer::sum);
        });

        Integer res = first.entrySet().stream()
            .map(e -> e.getKey() * e.getValue() * second.getOrDefault(e.getKey(), 0))
            .reduce(0, Integer::sum);

        check(res, 18567089, strict);
    }
}
