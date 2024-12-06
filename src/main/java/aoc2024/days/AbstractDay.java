package aoc2024.days;

import java.io.IOException;

import aoc2024.utils.Utils;


public abstract class AbstractDay {
    final public Integer Part1 = 1;
    final public Integer Part2 = 2;
    protected Integer currentPart;
    protected Long currentStartMs;

    public abstract void prepare(String fn) throws IOException;
    public abstract void part1Impl(boolean strict);
    public abstract void part2Impl(boolean strict);

    public void part1(boolean strict) {
        setCurrent(Part1);
        part1Impl(strict);
    }
    public void part2(boolean strict) {
        setCurrent(Part2);
        part2Impl(strict);
    }

    private void setCurrent(Integer part) {
        currentPart = part;
        currentStartMs = System.currentTimeMillis();
    }
    protected <T> void check(T actual, T expected, boolean strict) {
        Utils.<T>check(currentPart, actual, expected, strict, System.currentTimeMillis() - currentStartMs);
    }
}
