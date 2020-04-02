package org.rjansen.example.threads.strf.service.entity.sizes;

import org.rjansen.example.threads.strf.service.entity.WorkerEntity;
import org.rjansen.example.threads.strf.service.entity.WorkerEntityGenericBuilder;
import org.rjansen.example.threads.strf.size.Small;

public class WorkerEntitySmall extends WorkerEntity implements Small{

    private WorkerEntitySmall(WorkerEntityGenericBuilder<?> builder) {
        super(builder);
    }

    public static class WorkerEntitySmallBuilder extends WorkerEntityGenericBuilder<WorkerEntitySmall> {
        @Override
        public WorkerEntitySmall build(WorkerEntityGenericBuilder<?> builder) {
            return new WorkerEntitySmall(builder);
        }

        @Override
        public WorkerEntitySmall build() {
            return new WorkerEntitySmall(this);
        }
    }
}
