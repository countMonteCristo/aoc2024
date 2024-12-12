package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aoc2024.utils.IO;


public class Day09 extends AbstractDay {

    class Block {
        public int start;
        public int width;

        public Block(int s, int w) {
            start = s;
            width = w;
        }
    }

    List<Integer> diskMap;

    @Override
    public void prepare(String fn) throws IOException {
        diskMap = Stream.of(IO.readLines(fn).getFirst().split(""))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    List<Integer> fillMemory() {
        int size = diskMap.stream().reduce(0, Integer::sum);
        List<Integer> mem = new ArrayList<>(size);
        int id = 0;
        int pos = 0;
        for (int i = 0; i < diskMap.size(); i++) {
            int blockWidth = diskMap.get(i);
            int blockId = (i % 2 == 0) ? id++ : -1;
            for (int j = pos; j < pos + blockWidth; j++) mem.add(j, blockId);
            pos += blockWidth;
        }
        return mem;
    }

    List<Integer> buildFilePositions() {
        List<Integer> filePositions = new ArrayList<>();

        int pos = 0;
        for (int i = 0; i < diskMap.size(); i++) {
            if (i % 2 == 0) {
                filePositions.add(pos);
            }
            pos += diskMap.get(i);
        }

        return filePositions;
    }

    LinkedList<Block> buildEmptyBlocks() {
        LinkedList<Block> blocks = new LinkedList<>();

        int pos = 0;
        for (int i = 0; i < diskMap.size(); i++) {
            if (i % 2 != 0) {
                blocks.add(new Block(pos, diskMap.get(i)));
            }
            pos += diskMap.get(i);
        }

        return blocks;
    }

    void defrag(List<Integer> memory) {
        int i = 0;
        int j = memory.size() - 1;
        while (i < j) {
            if (memory.get(j) == -1) {
                j--;
                continue;
            }
            if (memory.get(i) != -1) {
                i++;
                continue;
            }

            memory.set(i, memory.get(j));
            memory.set(j, -1);

            i++;
            j--;
        }
    }

    long checkSum(List<Integer> mem) {
        long s = 0;
        for (int i = 0; i < mem.size(); i++) {
            if (!mem.get(i).equals(-1)) {
                s += (long)i * (long)mem.get(i);
            }
        }
        return s;
    }

    long checkSumFiles(List<Integer> files) {
        long s = 0;
        for (int id = 0; id < files.size(); id++) {
            int fileStart = files.get(id);
            int fileSize = diskMap.get(2*id);
            s += (long)id * (long)(((2*fileStart + fileSize - 1) * fileSize) / 2);
        }
        return s;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        List<Integer> memory = fillMemory();
        defrag(memory);
        return check(checkSum(memory), 6432869891895L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        List<Integer> files = buildFilePositions();
        LinkedList<Block> emptyBlocks = buildEmptyBlocks();

        for (int id = files.size() - 1; id > 0; id--) {
            int fileStartPos = files.get(id);
            int fileSize = diskMap.get(2*id);
            for (Block b : emptyBlocks) {
                if ((b.start < fileStartPos) && (b.width >= fileSize)) {
                    files.set(id, b.start);
                    b.start += fileSize;
                    b.width -= fileSize;
                    break;
                }
            }
        }

        return check(checkSumFiles(files), 6467290479134L, strict);
    }
}
