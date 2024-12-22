package aoc2024.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Array2d<T> {
    protected List<List<T>> data_;

    public Array2d(Stream<String> stream, String sep, Function<String,T> transform)  {
        data_ = stream
            .map(line -> Stream.of(line.split(sep)).map(transform).collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    public Array2d(String fn, String sep, Function<String,T> transform) throws IOException {
        this(IO.readLines(fn).stream(), sep, transform);
    }

    public Array2d(List<List<T>> data) {
        data_ = data;
    }

    public Array2d(T[][] data) {
        data_ = new ArrayList<>();
        for (int r = 0; r < data.length; r++) {
            ArrayList<T> row = new ArrayList<>();
            for (int c = 0; c < data[r].length; c++) {
                row.add(data[r][c]);
            }
            data_.add(row);
        }
    }

    public static Array2d<Character> parseCharTable(String fn) throws IOException {
        return new Array2d<>(fn, "", s -> s.charAt(0));
    }
    public static Array2d<Integer> parseIntTable(String fn, String sep) throws IOException {
        return new Array2d<>(fn, sep, Integer::parseInt);
    }
    public static Array2d<Long> parseLongTable(String fn, String sep) throws IOException {
        return new Array2d<>(fn, sep, Long::parseLong);
    }

    public static<T> Array2d<T> getTable(int width, int height, T value) {
        List<List<T>> data = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<T> row = new ArrayList<>(width);
            for (int j = 0; j < width; j++) {
                row.add(value);
            }
            data.add(row);
        }
        return new Array2d<>(data);
    }

    public final void set(int row, int col, T value) {
        data_.get(row).set(col, value);
    }
    public final void set(Vector2 p, T value) {
        set(p.y(), p.x(), value);
    }

    public final int width() {
        return data_.getFirst().size();
    }
    public final int height() {
        return data_.size();
    }

    public final T at(int row, int col) {
        return data_.get(row).get(col);
    }
    public final T at(Vector2 p) {
        return at(p.y(), p.x());
    }

    public final boolean contains(int row, int col) {
        return (0 <= row) && (row < height()) && (0 <= col) && (col < width());
    }
    public final boolean contains(Vector2 p) {
        return contains(p.y(), p.x());
    }

    public final void forEachRow(Consumer<List<T>> consumer) {
        data_.forEach(consumer);
    }

    public final Stream<List<T>> stream() {
        return data_.stream();
    }

    public final Stream<Pair<Vector2,T>> elementStream() {
        Vector2 start = new Vector2(0,0);
        var seed = new Pair<>(start, at(start));
        return Stream.iterate(seed, i -> {
            int row = i.first().y();
            int col = i.first().x();
            int index = row * width() + col + 1;
            Vector2 next = new Vector2(index % width(), index / width());
            return new Pair<>(next, at(next));
        }).limit(width() * height());
    }
}
