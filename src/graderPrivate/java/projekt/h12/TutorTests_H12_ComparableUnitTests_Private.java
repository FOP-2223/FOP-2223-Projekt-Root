package projekt.h12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import projekt.ComparableUnitTests;
import spoon.reflect.declaration.CtMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;
import static projekt.h12.TutorTests_H12_Private.IntegerWrapper;
import static projekt.h12.TutorTests_H12_Private.TestObjectFactory;
import static projekt.h12.TutorTests_H12_Private.checkCompares;
import static projekt.h12.TutorTests_H12_Private.countInvocations;
import static projekt.h12.TutorTests_H12_Private.createTestObjectSpies;


@TestForSubmission
public class TutorTests_H12_ComparableUnitTests_Private {

    @Test
    public void testTestObjects() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ComparableUnitTests#initialize")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ComparableUnitTests<IntegerWrapper> comparableUnitTests = new ComparableUnitTests<>(testObjectFactory);

        comparableUnitTests.initialize(testObjectCount);

        Object[] testObjects = getComparableTestObjects(comparableUnitTests);

        assertEquals(testObjectCount, testObjects.length, context,
            TR -> "The array testObjects has the wrong length.");

        for (int i = 0; i < testObjectCount; i++) {
            int finalI = i;
            assertEquals(testObjects[i], new IntegerWrapper(finalI), context,
                TR -> "The elements at index " + finalI + " of testObjects and testObjectsReferenceEquality are not the same.");
        }
    }

    @Test
    public void testAsBigAs() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ComparableUnitTests#testBiggerThen")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ComparableUnitTests<IntegerWrapper> comparableUnitTests = new ComparableUnitTests<>(testObjectFactory);

        IntegerWrapper[] testObjects = createTestObjectSpies(testObjectCount);
        List<List<IntegerWrapper>> compareToCaptors = new ArrayList<>();

        for (int i = 0; i < testObjects.length; i++) {
            compareToCaptors.add(new ArrayList<>());

            int finalI = i;
            when(testObjects[i].compareTo(any())).thenAnswer(invocation -> {
                IntegerWrapper compareTo = invocation.getArgument(0);
                if (compareToCaptors.get(finalI).size() > 0) {
                    fail(context, TR -> "Expected only one call to compareTo() per test object but got 2.");
                }
                compareToCaptors.get(finalI).add(compareTo);
                return finalI + 1;
            });
        }

        setComparableTestObjects(comparableUnitTests, testObjects);

        try (MockedStatic<Assertions> mockUtils = mockStatic(Assertions.class)) {
            List<Integer> expectedValues = new ArrayList<>();
            List<Integer> actualValues = new ArrayList<>();

            mockUtils.when(() -> Assertions.assertEquals(anyInt(), anyInt())).thenAnswer(invocation -> {
                expectedValues.add(invocation.getArgument(0));
                actualValues.add(invocation.getArgument(1));
                return null;
            });

            comparableUnitTests.testAsBigAs();

            assertEquals(testObjectCount, expectedValues.size(), context,
                TR -> "assertEquals() should be invoked " + (testObjectCount) + " times.");

            for (int i = 0; i < testObjectCount; i++) {
                assertSame(testObjects[i], compareToCaptors.get(i).get(0), context,
                    TR -> "The compareTo() method should only be invoked with the object it was called on.");
            }

            Integer[] expectedExpected = Stream.generate(() -> 0).limit(testObjectCount).toArray(Integer[]::new);

            Integer[] expectedActual = Stream.iterate(1, i -> i + 1).limit(testObjectCount).toArray(Integer[]::new);

            if (!checkCompares(expectedExpected, expectedActual, expectedValues.toArray(), actualValues.toArray())) {
                fail(context, TR -> "Found no equals call that compares the comparison results and zeros.");
            }
        }
    }

    @Test
    public void testTestAsBigAsEqualsCount() {
        Context context = contextBuilder().subject("ComparableUnitTests#testAsBigAs").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(ComparableUnitTests.class).getMethod(identical("testAsBigAs"))).getCtElement();

        assertEquals(1, countInvocations(method, (name, target) -> name.equals("assertEquals") && target.startsWith("org.junit")), context,
            TR -> "The method testEquals() should contain exactly 1 assertEquals() calls.");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] getComparableTestObjects(ComparableUnitTests<T> objectUnitTests) throws ReflectiveOperationException {
        Field testObjectsField = ComparableUnitTests.class.getDeclaredField("testObjects");
        testObjectsField.setAccessible(true);
        return (T[]) testObjectsField.get(objectUnitTests);
    }

    public static <T extends Comparable<T>> void setComparableTestObjects(ComparableUnitTests<T> objectUnitTests, T[] testObjects) throws ReflectiveOperationException {
        Field testObjectsField = ComparableUnitTests.class.getDeclaredField("testObjects");
        testObjectsField.setAccessible(true);
        testObjectsField.set(objectUnitTests, testObjects);
    }

}
