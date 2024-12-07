package aoc2024.days;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;


public class Day07 extends AbstractDay {
    List<TestCase> cases;

    record TestCase(Long result, List<Long> args){}

    static TestCase parseTestCase(String line) {
        String[] parts = line.split(": ");
        Long result = Long.parseLong(parts[0]);
        List<Long> args = Stream.of(parts[1].split(" "))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        return new TestCase(result, args);
    }

    static Long getMinPowerOf10(Long x) {
        long n = 10;
        while (n <= x) {
            n *= 10;
        }
        return n;
    }

    boolean checkSmart(TestCase test, int index, long res) {
        if (index == 0) {
            return res == test.args.getFirst();
        }
        if (res < 0) {
            return false;
        }
        return checkSmart(test, index-1, res - test.args.get(index))
            || ((res % test.args.get(index) == 0) && checkSmart(test, index-1, res / test.args.get(index)) );
    }
    boolean checkSmartEx(TestCase test, int index, long res) {
        if (index == 0) {
            return res == test.args.getFirst();
        }
        if (res < 0) {
            return false;
        }
        long x = getMinPowerOf10( test.args.get(index));
        return checkSmartEx(test, index - 1, res - test.args.get(index))
            || ((res % test.args.get(index) == 0) && checkSmartEx(test, index - 1, res / test.args.get(index)))
            || ((res % x == test.args.get(index)) && checkSmartEx(test, index - 1, res / x));
    }

    @Override
    public void prepare(String fn) throws IOException {
        cases = IO.readLines(fn)
            .stream()
            .map(Day07::parseTestCase)
            .collect(Collectors.toList());
    }

    @Override
    public void part1Impl(boolean strict) {
        long result = cases.stream()
            .filter(t -> checkSmart(t, t.args.size()-1, t.result))
            .map(t -> t.result)
            .reduce(0L, Long::sum);
        check(result, 4998764814652L, strict);
    }

    @Override
    public void part2Impl(boolean strict) {
        long result = cases.stream()
            .filter(t -> checkSmartEx(t, t.args.size()-1, t.result))
            .map(t -> t.result)
            .reduce(0L, Long::sum);

        check(result, 37598910447546L, strict);
    }
}
