package aoc2024.days;

import java.io.IOException;
import java.util.List;

import aoc2024.utils.*;
import aoc2024.days.IDay;


public class DayXX implements IDay {

    public void prepare(String fn) throws IOException {
        List<String> lines = Utils.readFile(fn);
    }

    public void part1(boolean strict) {
        Utils.<Integer>check(1, 0, 0, strict);
    }

    public void part2(boolean strict) {
        Utils.<Integer>check(2, 0, 0, strict);
    }
}
