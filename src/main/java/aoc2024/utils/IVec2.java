package aoc2024.utils;

public interface IVec2<T> {
    public T x();
    public T y();

    public IVec2<T> mul(T k);
    public IVec2<T> plus(IVec2<T> other);
    public IVec2<T> minus(IVec2<T> other);
    public T dot(IVec2<T> other);
    public T cross(IVec2<T> other);
}
