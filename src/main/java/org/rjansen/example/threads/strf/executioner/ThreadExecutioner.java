package org.rjansen.example.threads.strf.executioner;

import org.rjansen.example.threads.strf.service.WorkerInterface;
import org.rjansen.example.threads.strf.size.ExecutionerPriority;
import org.rjansen.example.threads.strf.size.ProcessPriority;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ThreadExecutioner<E extends ExecutionerPriority, R, S extends WorkerInterface<E, R>> {

    static final int FIXED_SMALL_OBJECTS_THREADS = 15;
    static final int FIXED_MEDIUM_OBJECTS_THREADS = 10;
    static final int FIXED_LARGE_OBJECTS_THREADS = 5;
    static final int MAX_THREADS = 30;
    private AtomicInteger usedThreads = new AtomicInteger(0);
    private AtomicBoolean processFIRSTActive = new AtomicBoolean(true);
    private AtomicBoolean processSECONDActive = new AtomicBoolean(true);
    private AtomicBoolean processTHIRDActive = new AtomicBoolean(true);
    private AtomicInteger smallObjectsProcessed = new AtomicInteger(0);
    private AtomicInteger mediumObjectsProcessed = new AtomicInteger(0);
    private AtomicInteger largeObjectsProcessed = new AtomicInteger(0);
    private S worker;

    public ThreadExecutioner(S worker) {
        this.worker = Objects.requireNonNull(worker);
    }

    protected void processAsyncObjects(List<ExecutionerPriority> objectsToProcess) {
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
                        + count + ": " + start.until(LocalDateTime.now(), ChronoUnit.MILLIS));
                count++;
                System.out.println("Large objects processed: " + largeObjectsProcessed);
                System.out.println("Medium objects processed: " + mediumObjectsProcessed);
                System.out.println("Small objects processed: " + smallObjectsProcessed);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println("Time consumed processing: " + stopWatch.getLastTaskTimeMillis());
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
        return processThreads(objectsToProcess, FIXED_SMALL_OBJECTS_THREADS, ProcessPriority.FIRST);
    }

    private List<R> processMediumSize(List<E> objectsToProcess) throws ExecutionException, InterruptedException {
        return processThreads(objectsToProcess, FIXED_MEDIUM_OBJECTS_THREADS, ProcessPriority.SECOND);
    }

    private List<R> processLargeSize(List<E> objectsToProcess) throws ExecutionException, InterruptedException {
        return processThreads(objectsToProcess, FIXED_LARGE_OBJECTS_THREADS, ProcessPriority.THIRD);
    }

    private List<R> processThreads(List<E> objectsToProcess, int numberOfThreads, ProcessPriority priority) throws InterruptedException, ExecutionException {
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
            increaseAmount(priority);
            count++;
        }
        return processedList;
    }

    private void increaseAmount(ProcessPriority priority) {
        switch (priority) {
            case FIRST: {
                increaseAmountSmall();
                break;
            }
            case SECOND: {
                increaseAmountMedium();
                break;
            }
            case THIRD: {
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
     * 0 -> {@link org.rjansen.example.threads.strf.size.ProcessPriority} FIRST list
     * 1 -> {@link org.rjansen.example.threads.strf.size.ProcessPriority} SECOND list
     * 2 -> {@link org.rjansen.example.threads.strf.size.ProcessPriority} THIRD list
     */
    private List<E>[] splitListIntoSizes(List<ExecutionerPriority> objectsToProcess) {
        List<ExecutionerPriority> smallList = new ArrayList<>();
        List<ExecutionerPriority> mediumList = new ArrayList<>();
        List<ExecutionerPriority> largeList = new ArrayList<>();
        objectsToProcess.forEach(object -> {
            ProcessPriority priority = object.getPriority();
            switch (priority) {
                case FIRST:
                    smallList.add(object);
                    break;
                case SECOND:
                    mediumList.add(object);
                    break;
                default:
                    largeList.add(object);
                    break;
            }
        });
        return new List[]{smallList, mediumList, largeList};
    }
}
