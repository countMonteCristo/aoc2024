package aoc2024.utils;

final public class Vec2i extends Vec2Base<Integer> {

    public Vec2i(int x, int y) {
        super(x, y);
    }

    public Vec2i mul(Integer k) {
        return new Vec2i(k * x(), k * y());
    }
    public Vec2i plus(IVec2<Integer> other) {
        return new Vec2i(x() + other.x(), y() + other.y());
    }
    public Vec2i minus(IVec2<Integer> other) {
        return new Vec2i(x() - other.x(), y() - other.y());
    }
    public Integer dot(IVec2<Integer> other) {
        return x() * other.x() + y() * other.y();
    }

    public Integer cross(IVec2<Integer> other) {
        return x()*other.y() - y()*other.x();
    }
}

