package aoc2024.days;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import aoc2024.utils.Array2d;
import aoc2024.utils.IO;
import aoc2024.utils.Vector2;


public class Day14 extends AbstractDay {

    final Vector2 smallSize = new Vector2(11, 7);
    final Vector2 bigSize = new Vector2(101, 103);

    class Robot {
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
        Vector2 vel() {
            return v_;
        }
    }
    List<Robot> robots1;
    List<Robot> robots2;

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

    void display(List<Robot> robots, Vector2 size) {
        HashMap<Vector2, Integer> botsCount = new HashMap<>();
        robots.forEach(bot -> botsCount.merge(bot.pos(), 1, Integer::sum));

        int rStart = (size.y() > 100) ? 24 : 0;
        int cStart = (size.x() > 100) ? 38 : 0;
        int h = (size.y() > 100) ? 33 : size.y();
        int w = (size.x() > 100) ? 31 : size.y();

        for (int r = rStart; r < rStart + h; r++) {
            for (int c = cStart; c < cStart + w; c++) {
                int n = botsCount.getOrDefault(new Vector2(c, r), 0);
                System.out.print((n == 0) ? " " : Integer.toString(n));
            }
            System.out.println();
        }
    }

    @Override
    public boolean part1Impl(boolean strict) {
        Vector2 size = strict ? bigSize : smallSize;
        run(robots1, 100, size);

        Array2d<Long> quads = Array2d.getTable(2, 2, 0L);
        for (int r = 0; r < size.y(); r++) {
            if (r == size.y() / 2) continue;
            int qr = (r < size.y() / 2) ? 0 : 1;
            for (int c = 0; c < size.x(); c++) {
                if (c == size.x() / 2) continue;
                int qc = (c < size.x() / 2) ? 0 : 1;
                Vector2 p = new Vector2(c, r);
                long n = robots1.stream().filter(bot -> bot.pos().equals(p)).count();
                quads.set(qr, qc, quads.at(qr, qc) + n);
            }
        }
        long res = quads.at(0,0) * quads.at(0,1) * quads.at(1,0) * quads.at(1,1);

        return check(res, 230686500L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        Vector2 size = strict ? bigSize : smallSize;
        int res = strict ? 7672 : 100;

        for (int i = 0; i < res; i++) {
            robots2.forEach(r -> r.step(size));
        }
        display(robots2, size);

        return check(res, 7672, strict);
    }
}
