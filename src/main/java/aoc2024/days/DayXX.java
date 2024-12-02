package aoc2024.days;

import aoc2024.utils.*;
import java.io.IOException;


public class DayXX implements IDay {

    @Override
    public void prepare(String fn) throws IOException {
        // List<String> lines = Utils.readFile(fn);
    }

    @Override
    public void part1(boolean strict) {
        Utils.<Integer>check(1, 0, 0, strict);
    }

    @Override
    public void part2(boolean strict) {
        Utils.<Integer>check(2, 0, 0, strict);
    }
}
