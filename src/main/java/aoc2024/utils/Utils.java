package aoc2024.utils;


public class Utils {
    public static<T> boolean check(int partId, T actual, T expected, boolean strict, Long elapsedMs) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Part %d: ans = %s (%d ms)", partId, actual.toString(), elapsedMs));
        if (strict && !actual.equals(expected)) {
            builder.append(String.format("\n  * [ERROR] Wrong answer at Part %d", partId));
            builder.append(String.format("(`%s` != `%s`)", actual.toString(), expected.toString()));
        }

        System.out.println(builder.toString());

        return !strict || actual.equals(expected);
    }
}
