package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;


public class Day17 extends AbstractDay {

    private record Item(long n, int index) {}

    private class Computer {
        public long A;
        public long B;
        public long C;
        int ip;
        List<Integer> output;
        boolean loop;

        public static int maskSize = 3;
        public static int mask = (1 << maskSize) - 1;

        public Computer(long a, long b, long c) {
            A = a;
            B = b;
            C = c;
            ip = 0;
            output = new ArrayList<>();
        }
        public List<Integer> run(List<Integer> input) {
            while (ip < input.size()) step(input);
            return output;
        }
        public int iter(List<Integer> input) {
            loop = false;
            while (!loop) step(input);
            return output.getFirst();
        }
        public void step(List<Integer> input) {
            long op = getOperand(input.get(ip + 1));
            switch (input.get(ip)) {
                case 0: A >>= op;                                      break;
                case 1: B ^= op;                                       break;
                case 2: B = op & mask;                                 break;
                case 3: ip = (A != 0) ? (int)op - 2 : ip; loop = true; break;
                case 4: B ^= C;                                        break;
                case 5: output.add((int)(op & mask));                  break;
                case 6: B = A >> op;                                   break;
                case 7: C = A >> op;                                   break;
                default: System.err.printf("[ERROR]\n");
            }
            ip += 2;
        }

        Long getOperand(int op) {
            if (op == 4) return A;
            if (op == 5) return B;
            if (op == 6) return C;
            return (long)op;
        }
    }

    private Long regA;
    private Long regB;
    private Long regC;
    private List<Integer> program;

    private Computer newComp(long a, long b, long c) {
        return new Computer(a, b, c);
    }

    private String convert(List<Integer> nums) {
        return String.join(",", nums.stream().map(Object::toString).collect(Collectors.toList()));
    }

    @Override
    public void prepare(String fn) throws IOException {
        List<String> lines = IO.readLines(fn);
        regA = Long.parseLong(lines.get(0).split(" ")[2]);
        regB = Long.parseLong(lines.get(1).split(" ")[2]);
        regC = Long.parseLong(lines.get(2).split(" ")[2]);
        program = Stream.of(lines.get(4).split(" ")[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public boolean part1Impl(boolean strict) {
        return check(convert(newComp(regA, regB, regC).run(program)), "7,1,5,2,4,0,7,6,1", strict);
    }

    // Note: решил яасть 2 сначала вручную, затем уже реализовал в коде
    @Override
    public boolean part2Impl(boolean strict) {
        long answer = -1L;
        PriorityQueue<Item> q = new PriorityQueue<>((i1, i2) -> Long.compare(i1.n, i2.n));
        q.add(new Item(0L, program.size() - 1));
        while (!q.isEmpty()) {
            Item i = q.poll();
            if (i.index < 0) {
                answer = i.n;
                break;
            }
            for (long b = 0; b <= Computer.mask; b++) {
                long a = (i.n << Computer.maskSize) | b;
                int out = newComp(a, regB, regC).iter(program);
                if (out == program.get(i.index)) {
                    q.add(new Item(a, i.index - 1));
                }
            }
        }
        return check(answer, 37222273957364L, strict);
    }
}
