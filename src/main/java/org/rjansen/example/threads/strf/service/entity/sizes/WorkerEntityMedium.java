package org.rjansen.example.threads.strf.service.entity.sizes;

import org.rjansen.example.threads.strf.service.entity.WorkerEntity;
import org.rjansen.example.threads.strf.service.entity.WorkerEntityGenericBuilder;
import org.rjansen.example.threads.strf.size.Medium;

public class WorkerEntityMedium extends WorkerEntity implements Medium{

    private WorkerEntityMedium(WorkerEntityGenericBuilder<?> builder) {
        super(builder);
    }

    public static class WorkerEntityMediumBuilder extends WorkerEntityGenericBuilder<WorkerEntityMedium> {
        @Override
        public WorkerEntityMedium build(WorkerEntityGenericBuilder<?> builder) {
            return new WorkerEntityMedium(builder);
        }

        @Override
        public WorkerEntityMedium build() {
            return new WorkerEntityMedium(this);
        }
    }
}
