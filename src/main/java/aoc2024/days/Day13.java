package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day13 extends AbstractDay {

    record Machine(long Ax, long Ay, long Bx, long By, long Px, long Py) {
        static final long costA = 3L;
        static final long costB = 1L;

        Long calcTokens(long diff, long max) {
            long px = Px + diff;
            long py = Py + diff;

            long d = Ax * By - Ay * Bx;
            if (d == 0L) return 0L;
            long sd = Long.signum(d);

            long nAd = By * px - Bx * py;
            long nBd = Ax * py - Ay * px;
            if ((Long.signum(nAd) * sd < 0) || (Long.signum(nAd) * sd < 0) || (nAd % d != 0) || (nBd % d != 0)) {
                return 0L;
            }

            long nA = nAd / d;
            long nB = nBd / d;
            if ((nA > max) || (nB > max)) return 0L;

            return nA * costA + nB * costB;
        }
    }

    List<Machine> machines;
    static final Pattern pat = Pattern.compile("^\\D+X.(?<x>\\d+), Y.(?<y>\\d+)$");

    static Pair<Long, Long> parseLine(String line) {
        Matcher matcher = pat.matcher(line);
        matcher.find();
        long x = Long.parseLong(matcher.group("x"));
        long y = Long.parseLong(matcher.group("y"));
        return new Pair<>(x, y);
    }

    @Override
    public void prepare(String fn) throws IOException {
        machines = new ArrayList<>();

        String lineA = "", lineB = "";
        for (String line: IO.readLines(fn)) {
            if (line.isEmpty()) {
                continue;
            } else if (line.startsWith("Button A")) {
                lineA = line;
                continue;
            } else if (line.startsWith("Button B")) {
                lineB = line;
                continue;
            } else if (line.startsWith("Prize")) {
                Pair<Long, Long> a = parseLine(lineA);
                Pair<Long, Long> b = parseLine(lineB);
                Pair<Long, Long> p = parseLine(line);
                machines.add(new Machine(a.first(), a.second(), b.first(), b.second(), p.first(), p.second()));
                continue;
            }
        }
    }

    long solve(long diff, long max) {
        return machines.stream().map(m -> m.calcTokens(diff, max)).reduce(0L, Long::sum);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(solve(0L, 100L), 39290L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(solve(10000000000000L, Long.MAX_VALUE), 73458657399094L, strict);
    }
}
