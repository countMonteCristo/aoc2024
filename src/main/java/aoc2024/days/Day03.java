package aoc2024.days;

import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2024.utils.*;


public class Day03 extends AbstractDay {

    String content;
    Boolean enabled;
    final Pattern pat1 = Pattern.compile("mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\)");
    final Pattern pat2 = Pattern.compile("((?<do>do)\\(\\))|((?<dont>don't)\\(\\))|((?<mul>mul)\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\))");

    static Long evalMul(MatchResult r) {
        return Long.parseLong(r.group("first")) * Long.valueOf(r.group("second"));
    }

    @Override
    public void prepare(String fn) throws IOException {
        content = Utils.readAll(fn);
    }

    @Override
    public void part1Impl(boolean strict) {
        Matcher matcher = pat1.matcher(content);
        Long result = matcher.results()
            .map(Day03::evalMul)
            .reduce(0L, (a, b) -> a + b);
        check(result, 168539636L, strict);
    }

    @Override
    public void part2Impl(boolean strict) {
        Matcher matcher = pat2.matcher(content);
        enabled = true;
        Long result = matcher.results()
            .map(r -> {
                if ((r.group("mul") != null) && enabled)
                    return evalMul(r);
                enabled = (r.group("dont") == null) && (r.group("do") != null);
                return 0L;
            })
            .reduce(0L, (a, b) -> a + b);
        check(result, 97529391L, strict);
    }
}

