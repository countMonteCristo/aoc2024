package aoc2024.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class IO {

    public static String readAll(String fn) throws IOException {
        return Files.readString(Path.of(fn));
    }

    public static ArrayList<String> readLines(String fn) throws IOException {
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
