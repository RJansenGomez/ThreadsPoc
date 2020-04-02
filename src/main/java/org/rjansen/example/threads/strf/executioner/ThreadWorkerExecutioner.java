package org.rjansen.example.threads.strf.executioner;

import org.rjansen.example.threads.strf.service.entity.WorkerEntity;
import org.rjansen.example.threads.strf.service.entity.WorkerEntityResponse;
import org.rjansen.example.threads.strf.service.impl.WorkerServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class ThreadWorkerExecutioner extends ThreadExecutioner<WorkerEntity, WorkerEntityResponse, WorkerServiceImpl> {

    public ThreadWorkerExecutioner(WorkerServiceImpl worker) {
        super(worker);
    }
}
