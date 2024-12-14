package aoc2024.utils;

import java.util.Objects;

public abstract class Vec2Base<T> implements IVec2<T> {
    protected final T x_;
    protected final T y_;

    Vec2Base(T x, T y) {
        x_ = x;
        y_ = y;
    }

    public final T x() {
        return x_;
    }
    public final T y() {
        return y_;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() ) {
            return false;
        }
        IVec2<?> other = (IVec2<?>) o;
        return x_.equals(other.x()) && y_.equals(other.y());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(x_, y_);
    }
}
