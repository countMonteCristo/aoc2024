package aoc2024;

import java.util.Arrays;
import java.util.stream.IntStream;

import aoc2024.days.*;


class Runner {

    static class DayConfig {
        public String dayId;
        public String inputFile;
        public boolean strict;

        DayConfig(Integer id, String inputFn) {
            dayId = String.format("%02d", id);
            strict = inputFn.equals("");
            inputFile = strict ?
                        String.format("src/main/resources/day%s.txt", dayId) :
                        inputFn;
        }
    }

    static void runDay(DayConfig config) {
        String className = String.format("aoc2024.days.Day%s", config.dayId);

        try {
            IDay day = (IDay) Class.forName(className).newInstance();

            String label = config.strict ? "" : " (non-strict mode)";
            System.out.printf("Day %s%s:\n", config.dayId, label);

            day.prepare(config.inputFile);
            day.part1(config.strict);
            day.part2(config.strict);

            System.out.println();
        } catch (Exception e) {
            System.err.printf("[ERROR] Can not run day %s:\n", config.dayId);
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void main(String[] args) throws Exception {
        String dayId = args[0];

        if (dayId.equals("check")) {
            Integer dayFrom = Integer.parseInt(args[1]);
            Integer dayTo = Integer.parseInt(args[2]);

            System.out.printf("Check days %d-%s\n\n", dayFrom, dayTo);
            IntStream
                .range(dayFrom, dayTo+1)
                .forEach(day -> runDay(new DayConfig(day, "")));
        } else {
            Integer id = Integer.parseInt(dayId);
            String inputFn = (args.length >= 2) ? args[1] : "";
            runDay(new DayConfig(id, inputFn));
        }
    }
}
