package org.rjansen.example.threads.strf.executioner;

import org.rjansen.example.threads.strf.service.WorkerInterface;
import org.rjansen.example.threads.strf.service.entity.sizes.WorkerEntityLarge;
import org.rjansen.example.threads.strf.service.entity.sizes.WorkerEntityMedium;
import org.rjansen.example.threads.strf.service.entity.sizes.WorkerEntitySmall;
import org.rjansen.example.threads.strf.size.ExecutionerSize;
import org.rjansen.example.threads.strf.size.Large;
import org.rjansen.example.threads.strf.size.Medium;
import org.rjansen.example.threads.strf.size.Small;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ThreadExecutioner<E extends ExecutionerSize, R, S extends WorkerInterface<E, R>> {

    static final int FIXED_SMALL_OBJECTS_THREADS = 15;
    static final int FIXED_MEDIUM_OBJECTS_THREADS = 10;
    static final int FIXED_LARGE_OBJECTS_THREADS = 5;
    static final int MAX_THREADS = 30;
    private AtomicInteger usedThreads = new AtomicInteger(0);
    private AtomicInteger smallObjectsProcessed = new AtomicInteger(0);
    private AtomicInteger mediumObjectsProcessed = new AtomicInteger(0);
    private AtomicInteger largeObjectsProcessed = new AtomicInteger(0);
    private S worker;

    public ThreadExecutioner(S worker) {
        this.worker = Objects.requireNonNull(worker);
    }

    protected void processAsyncObjects(List<ExecutionerSize> objectsToProcess) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LocalDateTime start = LocalDateTime.now();
        List<E>[] sizesList = splitListIntoSizes(objectsToProcess);
        int numberOfMainThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<List<R>> executorCompletion = new ExecutorCompletionService<>(executor);
        executorCompletion.submit(() -> this.processSmallSize(sizesList[0]));
        executorCompletion.submit(() -> this.processMediumSize(sizesList[1]));
        executorCompletion.submit(() -> this.processLargeSize(sizesList[2]));
        try {
            int count = 0;
            while (count < numberOfMainThreads) {
                List<R> processed = executorCompletion.take().get();
                System.out.println("Number of processed objects:" + processed.size());
                System.out.println("Time consumed processing in thread "
                        + count + ": " + start.until(LocalDateTime.now(), ChronoUnit.NANOS));
                count++;
                System.out.println("Large objects processed: " + largeObjectsProcessed);
                System.out.println("Medium objects processed: " + mediumObjectsProcessed);
                System.out.println("Small objects processed: " + smallObjectsProcessed);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println("Time consumed processing: " + stopWatch.getLastTaskTimeNanos());
        executor.shutdownNow();
    }

    private synchronized void increaseAmountLarge() {
        largeObjectsProcessed.incrementAndGet();
    }

    private synchronized void increaseAmountMedium() {
        mediumObjectsProcessed.incrementAndGet();
    }

    private synchronized void increaseAmountSmall() {
        smallObjectsProcessed.incrementAndGet();
    }

    private List<R> processSmallSize(List<E> objectsToProcess) throws InterruptedException, ExecutionException {
        return processThreads(objectsToProcess, FIXED_SMALL_OBJECTS_THREADS, 0);
    }

    private List<R> processMediumSize(List<E> objectsToProcess) throws ExecutionException, InterruptedException {
        return processThreads(objectsToProcess, FIXED_MEDIUM_OBJECTS_THREADS, 1);
    }

    private List<R> processLargeSize(List<E> objectsToProcess) throws ExecutionException, InterruptedException {
        return processThreads(objectsToProcess, FIXED_LARGE_OBJECTS_THREADS, 2);
    }

    private List<R> processThreads(List<E> objectsToProcess, int numberOfThreads, int kindOfProcess) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(
                numberOfThreads
        );
        List<R> processedList = new ArrayList<>();
        CompletionService<R> cs = new ExecutorCompletionService<>(executor);
        for (E objectToProcess : objectsToProcess) {
            cs.submit(() -> worker.doWork(objectToProcess));
        }
        int count = 0;
        while (count < objectsToProcess.size()) {
            R processed = cs.take().get();
            processedList.add(processed);
            increaseAmount(kindOfProcess);
            count++;
        }
        return processedList;
    }

    private void increaseAmount(int kindOfProcess) {
        switch (kindOfProcess) {
            case 0: {
                increaseAmountSmall();
                break;
            }
            case 1: {
                increaseAmountMedium();
                break;
            }
            case 2: {
                increaseAmountLarge();
                break;
            }
        }
    }

    /**
     * Split the list by sizes excluding ExecutionerSize
     *
     * @param objectsToProcess list with objects to split by size
     * @return Position
     * 0 -> {@link Small} list
     * 1 -> {@link Medium} list
     * 2 -> {@link Large} list
     */
    private List<E>[] splitListIntoSizes(List<ExecutionerSize> objectsToProcess) {
        List<Small> smallList = new ArrayList<>();
        List<Medium> mediumList = new ArrayList<>();
        List<Large> largeList = new ArrayList<>();
        objectsToProcess.forEach(object -> {
            ExecutionerSize objectSized = object.getSize();
            if (objectSized instanceof WorkerEntitySmall) {
                smallList.add((WorkerEntitySmall) objectSized);
            } else if (objectSized instanceof WorkerEntityMedium) {
                mediumList.add((WorkerEntityMedium) objectSized);
            } else {
                largeList.add((WorkerEntityLarge) objectSized);
            }
        });
        return new List[]{smallList, mediumList, largeList};
    }
}
