package aoc2024.utils;

final public class Vec2l extends Vec2Base<Long> {

    public Vec2l(long x, long y) {
        super(x, y);
    }

    public Vec2l mul(Long k) {
        return new Vec2l(k * x(), k * y());
    }
    public Vec2l plus(IVec2<Long> other) {
        return new Vec2l(x() + other.x(), y() + other.y());
    }
    public Vec2l minus(IVec2<Long> other) {
        return new Vec2l(x() - other.x(), y() - other.y());
    }
    public Long dot(IVec2<Long> other) {
        return x() * other.x() + y() * other.y();
    }
    public Long cross(IVec2<Long> other) {
        return x()*other.y() - y()*other.x();
    }
}
