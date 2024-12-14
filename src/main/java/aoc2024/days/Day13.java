package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2024.utils.IO;
import aoc2024.utils.Vec2l;


public class Day13 extends AbstractDay {

    record Machine(Vec2l A, Vec2l B, Vec2l P) {
        static final Vec2l cost = new Vec2l(3L, 1L);

        Long calcTokens(long diff, long max) {
            long d = A.cross(B);
            if (d == 0L) return 0L;
            long sd = Long.signum(d);

            Vec2l p = new Vec2l(P.x() + diff, P.y() + diff);
            long nAd = p.cross(B);
            long nBd = A.cross(p);
            if ((Long.signum(nAd) * sd < 0L) || (Long.signum(nAd) * sd < 0L) || (nAd % d != 0L) || (nBd % d != 0L)) {
                return 0L;
            }

            Vec2l n = new Vec2l(nAd / d, nBd / d);
            if ((n.x() > max) || (n.y() > max)) return 0L;

            return n.dot(cost);
        }
    }

    List<Machine> machines;
    static final Pattern pat = Pattern.compile("^\\D+X.(?<x>\\d+), Y.(?<y>\\d+)$");

    static Vec2l parseLine(String line) {
        Matcher matcher = pat.matcher(line);
        matcher.find();
        long x = Long.parseLong(matcher.group("x"));
        long y = Long.parseLong(matcher.group("y"));
        return new Vec2l(x, y);
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
                machines.add(new Machine(parseLine(lineA), parseLine(lineB), parseLine(line)));
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
