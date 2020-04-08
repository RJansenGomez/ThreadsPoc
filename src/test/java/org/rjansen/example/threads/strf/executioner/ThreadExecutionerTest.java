package org.rjansen.example.threads.strf.executioner;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.rjansen.example.threads.strf.service.entity.WorkerEntity;
import org.rjansen.example.threads.strf.service.impl.WorkerServiceImpl;
import org.rjansen.example.threads.strf.size.ExecutionerPriority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
public class ThreadExecutionerTest {

    ThreadWorkerExecutioner executioner = new ThreadWorkerExecutioner(new WorkerServiceImpl());

    @ParameterizedTest
    @ArgumentsSource(ThreadTestDataProvider.class)
    public void dataProviderTest(List<ExecutionerPriority> objectsToProcess) {
        executioner.processAsyncObjects(objectsToProcess);
    }

    static class ThreadTestDataProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(generateData1())
            );
        }
    }

    private static List<ExecutionerPriority> generateData1() {
        List<ExecutionerPriority> sumOfAllObjects = new ArrayList<>(largeObjects(1000));
        sumOfAllObjects.addAll(mediumObjects(2000));
        sumOfAllObjects.addAll(smallObjects(1500));
        return sumOfAllObjects;
    }

    private static List<ExecutionerPriority> largeObjects(int number) {
        List<ExecutionerPriority> objectList = new ArrayList<>();
        int count = 0;
        while (count < number) {
            objectList.add(generateLargeObject(count));
            count++;
        }
        return objectList;
    }

    private static List<ExecutionerPriority> mediumObjects(int number) {
        List<ExecutionerPriority> objectList = new ArrayList<>();
        int count = 0;
        while (count < number) {
            objectList.add(generateMediumObject(count));
            count++;
        }
        return objectList;
    }

    private static List<ExecutionerPriority> smallObjects(int number) {
        List<ExecutionerPriority> objectList = new ArrayList<>();
        int count = 0;
        while (count < number) {
            objectList.add(generateSmallObject(count));
            count++;
        }
        return objectList;
    }

    private static ExecutionerPriority generateLargeObject(int id) {
        //The large object is considered when the number is higher than 1000 -> sum of the 4 lists size
        //So we have here 1040 objects
        int amount = 310;
        List<Integer> listAges = createIntegerList(amount);
        return new WorkerEntity.WorkerEntityBuilder()
                .id("Large " + id)
                .names(createStringList("large", amount))
                .surnames(createStringList("surname Large", amount))
                .ages(listAges)
                .birthDates(
                        listAges.stream()
                                .map(ThreadExecutionerTest::createBirthDate)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static ExecutionerPriority generateMediumObject(int id) {
        //The medium object is considered when the number is higher than 500 -> sum of the 4 lists size
        //So we have here 600 objects
        int amount = 150;
        List<Integer> listAges = createIntegerList(amount);
        return new WorkerEntity.WorkerEntityBuilder()
                .id("Medium " + id)
                .names(createStringList("medium", amount))
                .surnames(createStringList("surname Medium", amount))
                .ages(listAges)
                .birthDates(
                        listAges.stream()
                                .map(ThreadExecutionerTest::createBirthDate)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static ExecutionerPriority generateSmallObject(int id) {
        //The medium object is considered when the number is lower than 500 -> sum of the 4 lists size
        //So we have here 480 objects
        int amount = 120;
        List<Integer> listAges = createIntegerList(amount);
        return new WorkerEntity.WorkerEntityBuilder()
                .id("Small " + id)
                .names(createStringList("small", amount))
                .surnames(createStringList("surname Small", amount))
                .ages(listAges)
                .birthDates(
                        listAges.stream()
                                .map(ThreadExecutionerTest::createBirthDate)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static List<Integer> createIntegerList(int number) {
        List<Integer> ages = new ArrayList<>();
        int count = 0;
        while (count <= number) {
            ages.add(Math.max(18, 50));
            count++;
        }
        return ages;
    }

    private static List<String> createStringList(String name, int number) {
        List<String> stringList = new ArrayList<>();
        int count = 0;
        while (count <= number) {
            stringList.add(name + " " + count);
            count++;
        }
        return stringList;
    }

    private static LocalDateTime createBirthDate(Integer age) {
        return LocalDateTime.now().minusYears(age);
    }
}
