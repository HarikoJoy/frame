package com.frame.hariko.spring.boot.cache.redis.idgenerator;

public interface IdWorker<T> {
    T nextId();
}
