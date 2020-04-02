package org.rjansen.example.threads.strf.service;

public interface WorkerInterface<E, R> {
    R doWork(E smallObject);
}
