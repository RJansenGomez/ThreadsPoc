package org.rjansen.example.threads.strf.service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class WorkerEntityGenericBuilder<T> {
    String id;
    List<String> names = new ArrayList<>();
    List<String> surnames = new ArrayList<>();
    List<Integer> ages = new ArrayList<>();
    List<LocalDateTime> birthDates = new ArrayList<>();

    public WorkerEntityGenericBuilder<T> id(String id) {
        this.id = id;
        return this;
    }

    public WorkerEntityGenericBuilder<T> names(List<String> names) {
        this.names = names;
        return this;
    }

    public WorkerEntityGenericBuilder<T> surnames(List<String> surnames) {
        this.surnames = surnames;
        return this;
    }

    public WorkerEntityGenericBuilder<T> ages(List<Integer> ages) {
        this.ages = ages;
        return this;
    }

    public WorkerEntityGenericBuilder<T> birthDates(List<LocalDateTime> birthDates) {
        this.birthDates = birthDates;
        return this;
    }

    public abstract WorkerEntity build();

    public abstract WorkerEntity build(WorkerEntityGenericBuilder<?> builder);

}
