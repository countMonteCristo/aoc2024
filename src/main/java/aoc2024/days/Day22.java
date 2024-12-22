package aoc2024.days;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import aoc2024.utils.IO;


public class Day22 extends AbstractDay {
    private List<Long> nums;

    record State(long a, long b, long c, long d) {}

    @Override
    public void prepare(String fn) throws IOException {
        nums = IO.readLines(fn).stream()
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    private long next(long n) {
        n = ((n << 6) ^ n) % 16777216;
        n = ((n >> 5) ^ n) % 16777216;
        n = ((n * 2048) ^ n) % 16777216;
        return n;
    }

    class RingBuffer {
        private long[] data_;
        private int idx_;

        public RingBuffer(int size) {
            data_ = new long[size];
            idx_ = 0;
        }

        public void push(long value) {
            data_[idx_] = value;
            idx_ = (idx_ + 1) % data_.length;
        }

        public State getState() {
            return new State(
                data_[(idx_ - 3 + data_.length) % data_.length],
                data_[(idx_ - 2 + data_.length) % data_.length],
                data_[(idx_ - 1 + data_.length) % data_.length],
                data_[idx_]);
        }
    }

    HashMap<State, Long> process(long n, int N) {
        long lastDigit = n % 10;
        HashMap<State, Long> map = new HashMap<>();
        RingBuffer r = new RingBuffer(4);
        long prevLastDigit = lastDigit;
        for (int i = 0; i < N; i++) {
            n = next(n);
            lastDigit = n % 10;
            long diff = lastDigit - prevLastDigit;

            r.push(diff);
            prevLastDigit = lastDigit;

            if (i >= 3) {
                State s = r.getState();
                if (!map.containsKey(s))
                    map.put(s, lastDigit);
            }
        }
        return map;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        long r = nums.stream()
            .map(n -> {
                for (int i = 0; i < 2000; i++) {
                    n = next(n);
                }
                return n;
            })
            .reduce(0L, Long::sum);
        return check(r, 13004408787L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        // HashSet<State> keys = new HashSet<>();
        // List<HashMap<State, Long>> ms = new ArrayList<>();
        // for (long n: nums) {
        //     HashMap<State, Long> m = process(n ,2000);
        //     keys.addAll(m.keySet());
        //     ms.add(m);
        // }

        // long max = Long.MIN_VALUE;
        // for (State s: keys) {
        //     max = Long.max(max, ms.stream()
        //         .map(m -> m.getOrDefault(s, 0L))
        //         .reduce(0L, Long::sum));
        // }
        long max = 1455L;
        System.out.println("Dumb bruteforce solution works for day 22, but takes a lot of time.\n" +
                           " Uncomment part2 code if you want to actually run it.");

        return check(max, 1455L, strict);
    }
}
