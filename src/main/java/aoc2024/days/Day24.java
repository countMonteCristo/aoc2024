package aoc2024.days;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import aoc2024.utils.IO;
import aoc2024.utils.Pair;


public class Day24 extends AbstractDay {

    private static final String AND = "AND";
    private static final String XOR = "XOR";
    private static final String OR = "OR";

    private record Gate(String name, String i1, String i2) {
        int proc(int v1, int v2) {
            switch (name) {
                case AND: return v1 & v2;
                case XOR: return v1 ^ v2;
                case OR: return v1 | v2;
            }
            return 0;
        }

        boolean hasInputs(String w1, String w2) {
            return (i1.equals(w1) && i2.equals(w2)) || (i1.equals(w2) && i2.equals(w1));
        }
    }

    private HashMap<String, Integer> inputWires;
    private HashMap<String, Gate> inputGates;
    List<String> xs;
    List<String> ys;
    List<String> zs;

    private long parseNum(String prefix, HashMap<String, Integer> wires) {
        List<String> outs = wires.keySet().stream()
            .filter(w -> w.startsWith(prefix))
            .collect(Collectors.toList());
        Collections.sort(outs, Collections.reverseOrder());

        String res = outs.stream()
            .map(w -> wires.get(w).toString())
            .collect(Collectors.joining());
        return Long.parseLong(res, 2);
    }

    private void run(HashMap<String, Integer> wires, HashMap<String, Gate> gates, HashSet<String> visited) {
        HashSet<String> unfinished = new HashSet<>(gates.keySet());
        while (!unfinished.isEmpty()) {
            HashSet<String> processed = new HashSet<>();
            for (String out: unfinished) {
                Gate g = gates.get(out);
                if (wires.containsKey(g.i1) && wires.containsKey(g.i2)) {
                    wires.put(out, g.proc(wires.get(g.i1), wires.get(g.i2)));
                    processed.add(out);
                    if (visited != null)
                        visited.add(out);
                }
            }
            unfinished.removeAll(processed);

            if (processed.isEmpty())
                break;
        }
    }

    private List<String> collectWires(Collection<String> iterable, String prefix) {
        List<String> res = iterable.stream()
            .filter(w -> w.startsWith(prefix))
            .collect(Collectors.toList());
        res.sort(null);
        return res;
    }

    @Override
    public void prepare(String fn) throws IOException {
        inputWires = new HashMap<>();
        inputGates = new HashMap<>();

        List<String> lines = IO.readLines(fn);
        Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            String line = it.next();
            if (line.isEmpty()) break;
            String[] parts = line.split(": ");
            inputWires.put(parts[0], Integer.parseInt(parts[1]));
        }

        while (it.hasNext()) {
            String line = it.next();
            String[] parts = line.split(" ");
            inputGates.put(parts[4], new Gate(parts[1], parts[0], parts[2]));
        }

        xs = collectWires(inputWires.keySet(), "x");
        ys = collectWires(inputWires.keySet(), "y");
        zs = collectWires(inputGates.keySet(), "z");
    }

    private record Summator(int id, String inputAnd, String inputXor, String tmpAnd, String out, String inCarry, String outCarry) {
        public static Summator getLast(String in) {
            return new Summator(-1, null, null, null, null, null, in);
        }

        public boolean ok() {
            return ((out != null) && out.startsWith("z") && (outCarry != null));
        }

        public List<Pair<String, String>> getVariants() {
            List<Pair<String, String>> variants = new ArrayList<>();
            List<String> all = new ArrayList<>(Arrays.asList(outCarry, out, tmpAnd, inputAnd, inputXor));
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i) == null) continue;
                for (int j = i + 1; j < all.size(); j++) {
                    if (all.get(j) == null) continue;
                    variants.add(new Pair<>(all.get(i), all.get(j)));
                }
            }
            return variants;
        }
    }

    private HashMap<String, String> getOutWires(String i1, String i2, HashMap<String, Gate> gates) {
        HashMap<String, String> out = new HashMap<>();
        for (String w: gates.keySet()) {
            Gate g = gates.get(w);
            if (g.hasInputs(i1, i2)) {
                out.put(g.name, w);
            }
        }
        return out;
    }

    private Summator getSummator(int i, String inCarry, HashMap<String, Gate> gates) {
        String ix = xs.get(i);
        String iy = ys.get(i);

        HashMap<String, String> p = getOutWires(ix, iy, gates);
        String inputXor = p.get(XOR);
        String inputAnd = p.get(AND);

        String outCarry = inputAnd;
        String out = inputXor;
        String tmpAnd = null;

        if (inCarry != null) {
            p = getOutWires(inputXor, inCarry, gates);
            out = p.get(XOR);
            tmpAnd = p.get(AND);
            outCarry = getOutWires(inputAnd, tmpAnd, gates).get(OR);
        }
        return new Summator(i, inputAnd, inputXor, tmpAnd, out, inCarry, outCarry);
    }

    private void swapGates(String w1, String w2, HashMap<String, Gate> gates) {
        Gate g = gates.get(w1);
        gates.put(w1, gates.get(w2));
        gates.put(w2, g);
    }

    private Summator findFirstBroken(int start, HashMap<String, Gate> gates, Summator last) {
        Summator item = last;
        for (int i = start; i < xs.size(); i++) {
            item = getSummator(i, item.outCarry, gates);
            if (!item.ok()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean part1Impl(boolean strict) {
        HashMap<String, Integer> wires = new HashMap<>(inputWires);
        run(wires, inputGates, null);
        return check(parseNum("z", wires), 52038112429798L, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        List<String> swapped = new ArrayList<>();

        HashMap<String, Gate> gates = new HashMap<>(inputGates);
        int idx = 0;
        Summator brokenItem = findFirstBroken(idx, gates, Summator.getLast(null));
        while (brokenItem != null) {
            for (Pair<String, String> p: brokenItem.getVariants()) {
                HashMap<String, Gate> tmpGates = new HashMap<>(gates);
                swapGates(p.first(), p.second(), tmpGates);

                Summator next = findFirstBroken(brokenItem.id, tmpGates, Summator.getLast(brokenItem.inCarry));
                if ((next == null ) || (next.id > brokenItem.id)) {
                    swapped.add(p.first());
                    swapped.add(p.second());
                    gates = tmpGates;
                    brokenItem = next;
                    if (next != null) {
                        idx = next.id;
                    }
                    break;
                }
            }
        }
        swapped.sort(null);

        return check(String.join(",", swapped), "cph,jqn,kwb,qkf,tgr,z12,z16,z24", strict);
    }
}

