package aoc2024.days;

import java.io.IOException;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import aoc2024.utils.IO;


public class Day03 extends AbstractDay {

    private String content;
    private Boolean enabled;
    private final Pattern pat1 = Pattern.compile("mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\)");
    private final Pattern pat2 = Pattern.compile("((?<do>do)\\(\\))|((?<dont>don't)\\(\\))|((?<mul>mul)\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\))");

    private Function<MatchResult, Long> evalMul = r -> Long.parseLong(r.group("first")) * Long.valueOf(r.group("second"));

    @Override
    public void prepare(String fn) throws IOException {
        content = IO.readAll(fn);
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(pat1.matcher(content).results().map(evalMul).reduce(0L, Long::sum), 168539636L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        enabled = true;
        Long result = pat2.matcher(content).results()
            .map(r -> {
                if (enabled && (r.group("mul") != null))
                    return evalMul.apply(r);
                enabled = (r.group("dont") == null) && (r.group("do") != null);
                return 0L;})
            .reduce(0L, Long::sum);
        return check(result, 97529391L, strict);
    }
}

