package aoc2024.days;

import java.io.IOException;


public class DayXX extends AbstractDay {
    // List<String> lines;

    @Override
    public void prepare(String fn) throws IOException {
        // lines = IO.readLines(fn);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(0, 0, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(0, 0, strict);
    }
}
