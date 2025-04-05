package dev.hail.create_simple_generator.mixin;

public interface SelfGetter<T> {
    default T self(){
        return (T) this;
    }
}