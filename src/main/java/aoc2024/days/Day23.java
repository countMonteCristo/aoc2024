package aoc2024.days;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import aoc2024.utils.IO;


public class Day23 extends AbstractDay {

    private HashMap<String, Set<String>> net;
    private Set<String> maxClique;

    private<T> Set<T> intersection(Set<T> A, Set<T> B) {
        Set<T> i = new HashSet<>(A);
        i.retainAll(B);
        return i;
    }

    // https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
    private void BronKerbosh(Set<String> R, Set<String> P, Set<String> X) {
        if (P.isEmpty() && X.isEmpty()) {
            if ((maxClique == null) || (R.size() > maxClique.size()))
                maxClique = R;
            return;
        }
        for (String v: new HashSet<>(P)) {
            Set<String> newR = new HashSet<>(R);
            newR.add(v);
            BronKerbosh(newR, intersection(P, net.get(v)), intersection(X, net.get(v)));
            P.remove(v);
            X.add(v);
        }
    }


    @Override
    public void prepare(String fn) throws IOException {
        net = new HashMap<>();
        IO.readLines(fn)
            .forEach(line -> {
                String[] parts = line.split("-");
                Stream.of(parts).forEach(p -> net.putIfAbsent(p, new HashSet<>()));
                net.get(parts[0]).add(parts[1]);
                net.get(parts[1]).add(parts[0]);
            });
    }

    @Override
    public boolean part1Impl(boolean strict) {
        HashSet<Set<String>> triples = new HashSet<>();
        for (String first: net.keySet()) {
            if (!first.startsWith("t")) continue;

            for (String second: net.get(first)) {
                for (String third: net.get(first)) {
                    if (!second.equals(third) && net.get(second).contains(third)) {
                        triples.add(new HashSet<>(Arrays.asList(first, second, third)));
                    }
                }
            }
        }
        return check(triples.size(), 1238, strict);
    }

    @Override
    public boolean part2Impl(boolean strict) {
        BronKerbosh(new HashSet<>(), net.keySet(), new HashSet<>());
        return check(String.join(",", new TreeSet<String>(maxClique)), "bg,bl,ch,fn,fv,gd,jn,kk,lk,pv,rr,tb,vw", strict);
    }
}
