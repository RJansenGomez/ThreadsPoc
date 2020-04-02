package org.rjansen.example.threads.strf.service.entity;

import org.rjansen.example.threads.strf.service.entity.sizes.WorkerEntityLarge;
import org.rjansen.example.threads.strf.service.entity.sizes.WorkerEntityMedium;
import org.rjansen.example.threads.strf.service.entity.sizes.WorkerEntitySmall;
import org.rjansen.example.threads.strf.size.ExecutionerSize;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;


public class WorkerEntity implements ExecutionerSize{
    private static final int LARGE_SIZE = 1000;
    private static final int MEDIUM_SIZE = 500;
    private String id;
    private List<String> names;
    private List<String> surnames;
    private List<Integer> ages;
    private List<LocalDateTime> birthDates;
    private WorkerEntityGenericBuilder<?> builder;

    protected WorkerEntity(WorkerEntityGenericBuilder<?> builder) {
        this.builder = builder;
        assertNotNull(builder.id);
        this.id = builder.id;
        this.names = builder.names;
        this.surnames = builder.surnames;
        this.ages = builder.ages;
        this.birthDates = builder.birthDates;
    }

    public static class WorkerEntityBuilder extends WorkerEntityGenericBuilder<WorkerEntity> {
        @Override
        public WorkerEntity build() {
            return new WorkerEntity(this);
        }

        @Override
        public WorkerEntity build(WorkerEntityGenericBuilder<?> builder) {
            return new WorkerEntity(builder);
        }
    }

    @Override
    public ExecutionerSize getSize() {
        int totalAmount = names.size() + surnames.size() + ages.size() + birthDates.size();
        if (totalAmount >= LARGE_SIZE) {
            return new WorkerEntityLarge.WorkerEntityLargeBuilder().build(this.builder);
        } else if (totalAmount >= MEDIUM_SIZE) {
            return new WorkerEntityMedium.WorkerEntityMediumBuilder().build(this.builder);
        } else
            return new WorkerEntitySmall.WorkerEntitySmallBuilder().build(this.builder);
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
