package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import aoc2024.utils.Array2d;


public class Day01 extends AbstractDay {

    private Array2d<Integer> list;

    @Override
    public void prepare(String fn) throws IOException {
        list = Array2d.parseIntTable(fn, "   ");
    }

    @Override
    public boolean part1Impl(boolean strict) {
        ArrayList<Integer> first = new ArrayList<>();
        ArrayList<Integer> second = new ArrayList<>();

        list.forEachRow(item -> {
            first.add(item.get(0));
            second.add(item.get(1));});

        first.sort(null);
        second.sort(null);

        int res = IntStream
            .range(0, list.height())
            .map(i -> Math.abs(first.get(i) - second.get(i)))
            .sum();

        return check(res, 2000468, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        HashMap<Integer,Integer> first = new HashMap<>();
        HashMap<Integer,Integer> second = new HashMap<>();

        list.forEachRow(item -> {
            first.merge(item.get(0), 1, Integer::sum);
            second.merge(item.get(1), 1, Integer::sum);
        });

        Integer res = first.entrySet().stream()
            .map(e -> e.getKey() * e.getValue() * second.getOrDefault(e.getKey(), 0))
            .reduce(0, Integer::sum);

        return check(res, 18567089, strict);
    }
}
