package org.rjansen.example.threads.strf.service.impl;

import org.rjansen.example.threads.strf.service.WorkerInterface;
import org.rjansen.example.threads.strf.service.entity.WorkerEntity;
import org.rjansen.example.threads.strf.service.entity.WorkerEntityResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerInterface<WorkerEntity, WorkerEntityResponse> {

    @Override
    public WorkerEntityResponse doWork(WorkerEntity workerObject) {
        return process(workerObject);
    }

    private WorkerEntityResponse process(WorkerEntity workerObject) {
        WorkerEntityResponse response = new WorkerEntityResponse();
        List<String> generateList = mapWorkerObject(workerObject);
        response.setSourceId(workerObject.getId());
        response.setListAllTogether(generateList);
        return response;
    }

    private List<String> mapWorkerObject(WorkerEntity workerObject) {
        List<String> response = new ArrayList<>();
        for (int i = 0; i < workerObject.getNames().size(); i++) {
            response.add(concatenateUserData(workerObject, i));
        }
        return response;
    }

    private String concatenateUserData(WorkerEntity workerObject, int position) {
        String name = workerObject.getNames().get(position);
        String surname = workerObject.getSurnames().get(position);
        Integer age = workerObject.getAges().get(position);
        LocalDateTime birthDate = workerObject.getBirthDates().get(position);
        String objectType = workerObject.getPriority().name();
        return "User name: " + name +
                ", Surname:" + surname +
                ", Age: " + age +
                ", BirthDate:" + birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", User belongs to a " + objectType;
    }
}
