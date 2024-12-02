package aoc2024.days;

import java.io.IOException;


public interface IDay {
    public void prepare(String fn) throws IOException;
    public void part1(boolean strict);
    public void part2(boolean strict);
}
