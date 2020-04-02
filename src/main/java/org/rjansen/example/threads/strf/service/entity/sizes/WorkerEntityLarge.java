package org.rjansen.example.threads.strf.service.entity.sizes;

import org.rjansen.example.threads.strf.service.entity.WorkerEntity;
import org.rjansen.example.threads.strf.service.entity.WorkerEntityGenericBuilder;
import org.rjansen.example.threads.strf.size.Large;

public class WorkerEntityLarge extends WorkerEntity implements Large {

    private WorkerEntityLarge(WorkerEntityGenericBuilder<?> builder) {
        super(builder);
    }

    public static class WorkerEntityLargeBuilder extends WorkerEntityGenericBuilder<WorkerEntityLarge> {
        @Override
        public WorkerEntity build() {
            return new WorkerEntityLarge(this);
        }

        @Override
        public WorkerEntityLarge build(WorkerEntityGenericBuilder<?> builder) {
            return new WorkerEntityLarge(builder);
        }

    }
}
