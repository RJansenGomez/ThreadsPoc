package org.rjansen.example.threads.strf.service.entity;

import org.rjansen.example.threads.strf.size.ExecutionerPriority;
import org.rjansen.example.threads.strf.size.ProcessPriority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;


public class WorkerEntity implements ExecutionerPriority {
    private static final int LARGE_SIZE = 1000;
    private static final int MEDIUM_SIZE = 500;
    private String id;
    private List<String> names;
    private List<String> surnames;
    private List<Integer> ages;
    private List<LocalDateTime> birthDates;

    protected WorkerEntity(WorkerEntityBuilder builder) {
        assertNotNull(builder.id);
        this.id = builder.id;
        this.names = builder.names;
        this.surnames = builder.surnames;
        this.ages = builder.ages;
        this.birthDates = builder.birthDates;
    }

    public static class WorkerEntityBuilder {
        String id;
        List<String> names = new ArrayList<>();
        List<String> surnames = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();
        List<LocalDateTime> birthDates = new ArrayList<>();

        public WorkerEntityBuilder id(String id) {
            this.id = id;
            return this;
        }

        public WorkerEntityBuilder names(List<String> names) {
            this.names = names;
            return this;
        }

        public WorkerEntityBuilder surnames(List<String> surnames) {
            this.surnames = surnames;
            return this;
        }

        public WorkerEntityBuilder ages(List<Integer> ages) {
            this.ages = ages;
            return this;
        }

        public WorkerEntityBuilder birthDates(List<LocalDateTime> birthDates) {
            this.birthDates = birthDates;
            return this;
        }

        public WorkerEntity build() {
            return new WorkerEntity(this);
        }
    }

    @Override
    public ProcessPriority getPriority() {
        int totalAmount = names.size() + surnames.size() + ages.size() + birthDates.size();
        if (totalAmount >= LARGE_SIZE) {
            return ProcessPriority.THIRD;
        } else if (totalAmount >= MEDIUM_SIZE) {
            return ProcessPriority.SECOND;
        } else
            return ProcessPriority.FIRST;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getSurnames() {
        return surnames;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public List<LocalDateTime> getBirthDates() {
        return birthDates;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        WorkerEntity oCasted = (WorkerEntity) o;
        if (this.id == null && oCasted.id == null) {
            return false;
        } else if (this.id != null) {
            return this.id.equals(oCasted.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
