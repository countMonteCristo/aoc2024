package aoc2024.days;

import aoc2024.utils.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Day01 implements IDay {

    List<String> list;

    @Override
    public void prepare(String fn) throws IOException {
        list = Utils.readFile(fn);
    }

    @Override
    public void part1(boolean strict) {
        ArrayList<Integer> first = new ArrayList<>();
        ArrayList<Integer> second = new ArrayList<>();

        list.stream()
            .map(line -> Stream.of(line.split("   "))
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .forEach(item -> {
                first.add(item.get(0));
                second.add(item.get(1));
            });

        first.sort(null);
        second.sort(null);

        int res = IntStream
            .range(0, list.size())
            .map(i -> Math.abs(first.get(i) - second.get(i)))
            .sum();

        Utils.<Integer>check(1, res, 2000468, strict);
    }

    @Override
    public void part2(boolean strict) {
        ArrayList<Integer> first = new ArrayList<>();
        HashMap<Integer,Integer> second = new HashMap<>();

        list.stream()
            .map(line -> Stream.of(line.split("   "))
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .forEach(item -> {
                first.add(item.get(0));
                second.merge(item.get(1), 1, Integer::sum);
            });

        Integer res = first.stream()
            .map(x -> x * second.getOrDefault(x, 0))
            .reduce(0, Integer::sum);

        Utils.<Integer>check(2, res, 18567089, strict);
    }
}
