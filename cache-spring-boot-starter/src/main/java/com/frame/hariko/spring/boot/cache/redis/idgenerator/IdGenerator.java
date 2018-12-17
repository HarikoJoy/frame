package com.frame.hariko.spring.boot.cache.redis.idgenerator;

public interface IdGenerator<T> {
    T generateId();
    T doNextId();
}
