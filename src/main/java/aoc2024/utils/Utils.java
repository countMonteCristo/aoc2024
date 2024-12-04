package aoc2024.utils;

public class Utils {
    public static<T> void check(int id, T actual, T expected, boolean strict, Long elapsedMs) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Part %d: ans = %s (%d ms)", id, actual.toString(), elapsedMs));
        if (strict && !actual.equals(expected)) {
            builder.append(String.format("\n  * [ERROR] Wrong answer at Part %d", id));
            builder.append(String.format("(`%s` != `%s`)", expected.toString(), actual.toString()));
        }

        System.out.println(builder.toString());
    }
}
