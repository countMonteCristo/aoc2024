package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import aoc2024.utils.Array2d;


public class Day02 extends AbstractDay {

    private Array2d<Integer> streams;

    public static boolean isSafe(List<Integer> stream) {
        Integer first = stream.getFirst();
        Integer last = stream.getLast();
        int sign = Integer.signum(last - first);
        for (int i = 1; i < stream.size(); i++) {
            int diff = stream.get(i) - stream.get(i-1);
            if ((Integer.signum(diff) != sign) || (Math.abs(diff) < 1) || (Math.abs(diff) > 3))
                return false;
        }
        return true;
    }

    public static boolean isSafeSkipped(List<Integer> stream) {
        if (Day02.isSafe(stream)) return true;

        for (int i = 0; i < stream.size(); i++) {
            List<Integer> x = new ArrayList<>(stream);
            x.remove(i);

            if (Day02.isSafe(x)) return true;
        }
        return false;
    }

    public static long processStreams(Array2d<Integer> streams, Predicate<? super List<Integer>> p) {
        return streams.stream().filter(p).count();
    }

    @Override
    public void prepare(String fn) throws IOException {
        streams = Array2d.parseIntTable(fn, " ");
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(processStreams(streams, Day02::isSafe), 224L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        return check(processStreams(streams, Day02::isSafeSkipped), 293L, strict);
    }
}
