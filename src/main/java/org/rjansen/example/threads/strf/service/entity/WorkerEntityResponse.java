package org.rjansen.example.threads.strf.service.entity;

import java.util.List;

public class WorkerEntityResponse {
    private String sourceId;
    private List<String> listAllTogether;

    public List<String> getListAllTogether() {
        return listAllTogether;
    }

    public void setListAllTogether(List<String> listAllTogether) {
        this.listAllTogether = listAllTogether;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String toString() {
        return "Number of objects:" + listAllTogether.size();
    }

    public void printList() {
        listAllTogether.forEach(System.out::println);
    }
}
