package aoc2024.days;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day14 extends AbstractDay {

    private final Vector2 smallSize = new Vector2(11, 7);
    private final Vector2 bigSize = new Vector2(101, 103);

    private class Robot {
        Vector2 p_;
        Vector2 v_;

        Vector2 parse(String line) {
            String[] parts = line.split(",");
            return new Vector2(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        Robot(String line) {
            String[] parts = line.split(" ");
            String pos = parts[0].split("=")[1];
            String vel = parts[1].split("=")[1];
            p_ = parse(pos);
            v_ = parse(vel);
        }

        void step(Vector2 size) {
            p_ = new Vector2((p_.x() + v_.x() + size.x()) % size.x(), (p_.y() + v_.y() + size.y()) % size.y());
        }

        Vector2 pos() {
            return p_;
        }
    }
    private List<Robot> robots1;
    private List<Robot> robots2;

    @Override
    public void prepare(String fn) throws IOException {
        robots1 = IO.readLines(fn).stream()
            .map(line -> new Robot(line))
            .collect(Collectors.toList());
        robots2 = IO.readLines(fn).stream()
            .map(line -> new Robot(line))
            .collect(Collectors.toList());
    }

    void run(List<Robot> robots, int N, Vector2 size) {
        for (int i = 0; i < N; i++) {
            robots.forEach(r -> r.step(size));
        }
    }

    private long calc(List<Robot> robots, Vector2 halfSize) {
        Long[][] quads = {{0L, 0L}, {0L, 0L}};
        for (Robot r: robots) {
            Vector2 p = r.pos();
            if (p.x() == halfSize.x() || p.y() == halfSize.y()) continue;
            int qr = (p.y() < halfSize.y()) ? 0 : 1;
            int qc = (p.x() < halfSize.x()) ? 0 : 1;
            quads[qr][qc]++;
        }
        return quads[0][0] * quads[0][1] * quads[1][0] * quads[1][1];
    }

    @Override
    public boolean part1Impl(boolean strict) {
        Vector2 size = strict ? bigSize : smallSize;
        run(robots1, 100, size);
        return check(calc(robots1, size.div(2)), 230686500L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        Vector2 size = strict ? bigSize : smallSize;
        Vector2 halfSize = size.div(2);

        int minIt = -1;
        long minScore = Long.MAX_VALUE;
        for (int i = 0; i < size.x() * size.y(); i++) {
            robots2.forEach(r -> r.step(size));

            long score = calc(robots2, halfSize);
            if (score < minScore) {
                minScore = score;
                minIt = i + 1;
            }
        }
        return check(minIt, 7672, strict);
    }
}
