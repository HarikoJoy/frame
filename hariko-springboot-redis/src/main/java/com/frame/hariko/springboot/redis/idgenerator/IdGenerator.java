package com.frame.hariko.springboot.redis.idgenerator;

public interface IdGenerator<T> {
    T generateId();
    T doNextId();
}
