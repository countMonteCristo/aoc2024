package aoc2024;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aoc2024.days.AbstractDay;


class Runner {

    static class DayConfig {
        public String dayId;
        public String inputFile;
        public boolean strict;

        DayConfig(Integer id, String inputFnId) {
            dayId = String.format("%02d", id);
            strict = inputFnId.equals("");
            inputFile = strict ?
                        String.format("src/main/resources/day%s.txt", dayId) :
                        String.format("src/test/resources/day%s_test%s.txt", dayId, inputFnId);
        }
    }

    static boolean runDay(DayConfig config) {
        String className = String.format("aoc2024.days.Day%s", config.dayId);

        try {
            AbstractDay day = (AbstractDay) Class.forName(className).getDeclaredConstructor().newInstance();

            String label = config.strict ? "" : " (non-strict mode)";
            System.out.printf("Day %s%s:\n", config.dayId, label);

            day.prepare(config.inputFile);
            boolean ok1 = day.part1(config.strict);
            boolean ok2 = day.part2(config.strict);

            System.out.println();
            return ok1 && ok2;
        } catch (IOException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                 InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.printf("[ERROR] Can not run day %s:\n", config.dayId);
            System.err.println(e);
            return false;
        }
    }

    static void runDays(List<DayConfig> configs, String message) {
        System.out.println("========================================");
        if (!message.isEmpty()) {
            System.out.println(message);
        }

        long countFails = configs
            .stream()
            .map(Runner::runDay)
            .filter(ok -> !ok)
            .count();
        System.out.println("========================================");
        System.out.printf("Failed days: %d\n", countFails);
        System.out.println("========================================");
    }

    static void runRange(Integer dayFrom, Integer dayTo) {
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
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            runRange(1, 25);
            return;
        }
        String dayId = args[0];

        if (dayId.equals("days")) {
            Integer dayFrom = Integer.parseInt(args[1]);
            Integer dayTo = Integer.parseInt(args[2]);
            runRange(dayFrom, dayTo);
        } else if (dayId.equals("all")) {
            runRange(1, 25);
        } else {
            Integer id = Integer.parseInt(dayId);
            String inputFnId = (args.length >= 2) ? args[1] : "";

            runDays(Arrays.asList(new DayConfig(id, inputFnId)), "");
        }
    }
}
