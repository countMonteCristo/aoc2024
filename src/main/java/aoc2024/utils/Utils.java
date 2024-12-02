package aoc2024.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Utils {
    public static<T> void check(int id, T actual, T expected, boolean strict) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Part %d: ans = %s", id, actual.toString()));
        if (!actual.equals(expected) && strict) {
            builder.append(String.format("\n  * [ERROR] Wrong answer at Part %d", id));
            builder.append(String.format("(`%s` != `%s`)", expected.toString(), actual.toString()));
        }

        System.out.println(builder.toString());
    }

    public static ArrayList<String> readFile(String fn) throws IOException {
        ArrayList<String> result = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(fn))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }

        return result;
    }
}
