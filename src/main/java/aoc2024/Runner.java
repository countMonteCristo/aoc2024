package aoc2024;

import aoc2024.days.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
            AbstractDay day = (AbstractDay) Class.forName(className).getDeclaredConstructor().newInstance();

            String label = config.strict ? "" : " (non-strict mode)";
            System.out.printf("Day %s%s:\n", config.dayId, label);

            day.prepare(config.inputFile);
            day.part1(config.strict);
            day.part2(config.strict);

            System.out.println();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                 InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.printf("[ERROR] Can not run day %s:\n", config.dayId);
            System.err.println(e);
            System.exit(1);
        }
    }

    static void runDays(List<DayConfig> configs, String message) {
        System.out.println("========================================");
        if (!message.isEmpty()) {
            System.out.println(message);
        }

        configs
            .stream()
            .forEach(config -> runDay(config));
        System.out.println("========================================");
    }

    public static void main(String[] args) throws Exception {
        String dayId = args[0];

        if (dayId.equals("days")) {
            Integer dayFrom = Integer.valueOf(args[1]);
            Integer dayTo = Integer.valueOf(args[2]);
            if (dayTo < dayFrom) {
                System.out.printf("Bad range: %d to %d\n", dayFrom, dayTo);
                System.exit(1);
            }

            String message = String.format("Check days %d-%d\n", dayFrom, dayTo);
            List<DayConfig> configs = IntStream
                .range(dayFrom, dayTo+1)
                .mapToObj(day -> new DayConfig(day, ""))
                .collect(Collectors.toList());

            runDays(configs, message);
        } else if (dayId.equals("all")) {
            Integer dayFrom = 1;
            Integer dayTo = 25;

            String message = String.format("Check days %d-%d\n", dayFrom, dayTo);
            List<DayConfig> configs = IntStream
                .range(dayFrom, dayTo+1)
                .mapToObj(day -> new DayConfig(day, ""))
                .collect(Collectors.toList());

            runDays(configs, message);
        } else {
            Integer id = Integer.valueOf(dayId);
            String inputFn = (args.length >= 2) ? args[1] : "";

            runDays(Arrays.asList(new DayConfig(id, inputFn)), "");
        }
    }
}
