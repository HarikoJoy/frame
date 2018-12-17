package com.frame.hariko.springboot.redis.idgenerator;

public interface IdWorker<T> {
    T nextId();
}
