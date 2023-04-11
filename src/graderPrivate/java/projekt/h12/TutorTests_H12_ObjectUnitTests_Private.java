package projekt.h12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import projekt.ObjectUnitTests;
import spoon.reflect.declaration.CtMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;
import static projekt.h12.TutorTests_H12_Private.IntegerWrapper;
import static projekt.h12.TutorTests_H12_Private.TestObjectFactory;
import static projekt.h12.TutorTests_H12_Private.checkCompares;
import static projekt.h12.TutorTests_H12_Private.countInvocations;
import static projekt.h12.TutorTests_H12_Private.createTestObjects;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class TutorTests_H12_ObjectUnitTests_Private {

    @BeforeEach
    public void reset() {
        IntegerWrapper.reset();
    }

    @Test
    public void testReferenceArray() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#initialize")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Object::toString);

        objectUnitTests.initialize(testObjectCount);

        Object[] testObjects = getObjectTestObjects(objectUnitTests);
        Object[] testObjectsReferenceEquality = getTestObjectsReferenceEquality(objectUnitTests);

        assertEquals(testObjectCount, testObjects.length, context,
            TR -> "The array testObjects has the wrong length.");
        assertEquals(testObjectCount, testObjectsReferenceEquality.length, context,
            TR -> "The array testObjectsReferenceEquality has the wrong length.");

        for (int i = 0; i < testObjectCount; i++) {
            int finalI = i;
            assertSame(testObjects[i], testObjectsReferenceEquality[i], context,
                TR -> "The elements at index " + finalI + " of testObjects and testObjectsReferenceEquality are not the same.");
        }

    }

    @Test
    public void testAllArrays() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#initialize")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = spy(new TestObjectFactory());
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Object::toString);

        objectUnitTests.initialize(testObjectCount);

        Object[] testObjects = getObjectTestObjects(objectUnitTests);
        Object[] testObjectsReferenceEquality = getTestObjectsReferenceEquality(objectUnitTests);
        Object[] testObjectsContentEquality = getTestObjectsContentEquality(objectUnitTests);

        assertEquals(testObjectCount, testObjects.length, context,
            TR -> "The array testObjects has the wrong length.");
        assertEquals(testObjectCount, testObjectsReferenceEquality.length, context,
            TR -> "The array testObjectsReferenceEquality has the wrong length.");
        assertEquals(testObjectCount, testObjectsContentEquality.length, context,
            TR -> "The array testObjectsContentEquality has the wrong length.");

        for (int i = 0; i < testObjectCount; i++) {
            int finalI = i;
            assertSame(testObjects[i], testObjectsReferenceEquality[i], context,
                TR -> "The elements at index " + finalI + " of testObjects and testObjectsReferenceEquality are not the same.");
            assertNotSame(testObjects[i], testObjectsContentEquality[i], context,
                TR -> "The elements at index " + finalI + " of testObjects and testObjectsContentEquality are the same.");
            assertEquals(testObjects[i], testObjectsContentEquality[i], context,
                TR -> "The elements at index " + finalI + " of testObjects and testObjectsContentEquality are not equal.");
        }

        verify(testObjectFactory, times(2 * testObjectCount)).apply(anyInt());
    }

    @Test
    public void testTestEquals() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#testEquals")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Object::toString);


        IntegerWrapper[] testObjects = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectReferenceEquality = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectsContentEquality = createTestObjects(testObjectCount);

        setTestObjects(objectUnitTests, testObjects);
        setTestObjectsReferenceEquality(objectUnitTests, testObjectReferenceEquality);
        setTestObjectsContentEquality(objectUnitTests, testObjectsContentEquality);

        try (MockedStatic<Assertions> mockUtils = mockStatic(Assertions.class, CALLS_REAL_METHODS)) {
            ArgumentCaptor<Object> expectedCapture = ArgumentCaptor.forClass(Object.class);
            ArgumentCaptor<Object> actualCapture = ArgumentCaptor.forClass(Object.class);

            mockUtils.when(() -> Assertions.assertEquals(expectedCapture.capture(), actualCapture.capture())).thenCallRealMethod();

            objectUnitTests.testEquals();

            assertEquals(3 * testObjectCount, expectedCapture.getAllValues().size(), context,
                TR -> "assertEquals() should be invoked " + (3 * testObjectCount) + " times.");

            IntegerWrapper[] expected1Values = new IntegerWrapper[testObjectCount];
            IntegerWrapper[] expected2Values = new IntegerWrapper[testObjectCount];
            IntegerWrapper[] expected3Values = new IntegerWrapper[testObjectCount];

            IntegerWrapper[] actual1Values = new IntegerWrapper[testObjectCount];
            IntegerWrapper[] actual2Values = new IntegerWrapper[testObjectCount];
            IntegerWrapper[] actual3Values = new IntegerWrapper[testObjectCount];

            for (int i = 0; i < testObjectCount; i++) {
                expected1Values[i] = (IntegerWrapper) expectedCapture.getAllValues().get(3 * i);
                expected2Values[i] = (IntegerWrapper) expectedCapture.getAllValues().get(3 * i + 1);
                expected3Values[i] = (IntegerWrapper) expectedCapture.getAllValues().get(3 * i + 2);

                actual1Values[i] = (IntegerWrapper) actualCapture.getAllValues().get(3 * i);
                actual2Values[i] = (IntegerWrapper) actualCapture.getAllValues().get(3 * i + 1);
                actual3Values[i] = (IntegerWrapper) actualCapture.getAllValues().get(3 * i + 2);
            }

            if (!checkCompares(testObjects, testObjectReferenceEquality, expected1Values, actual1Values) &&
                !checkCompares(testObjects, testObjectReferenceEquality, expected2Values, actual2Values) &&
                !checkCompares(testObjects, testObjectReferenceEquality, expected3Values, actual3Values)) {
                fail(context, TR -> "Found no equals call that compares testObjects and testObjectsReferenceEquality.");
            }

            if (!checkCompares(testObjects, testObjectsContentEquality, expected1Values, actual1Values) &&
                !checkCompares(testObjects, testObjectsContentEquality, expected2Values, actual2Values) &&
                !checkCompares(testObjects, testObjectsContentEquality, expected3Values, actual3Values)) {
                fail(context, TR -> "Found no equals call that compares testObjects and testObjectsContentEquality.");
            }

            if (!checkCompares(testObjectReferenceEquality, testObjectsContentEquality, expected1Values, actual1Values) &&
                !checkCompares(testObjectReferenceEquality, testObjectsContentEquality, expected2Values, actual2Values) &&
                !checkCompares(testObjectReferenceEquality, testObjectsContentEquality, expected3Values, actual3Values)) {
                fail(context, TR -> "Found no equals call that compares testObjectsReferenceEquality and testObjectsContentEquality.");
            }
        }
    }

    @Test
    public void testTestEqualsCount() {
        Context context = contextBuilder().subject("ObjectUnitTests#testEquals").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(ObjectUnitTests.class).getMethod(identical("testEquals"))).getCtElement();

        assertEquals(3, countInvocations(method, (name, target) -> name.equals("assertEquals") && target.startsWith("org.junit")), context,
            TR -> "The method testEquals() should contain exactly 3 assertEquals() calls.");
    }

    @Test
    public void testTestNotEquals() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#testEquals")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Object::toString);


        IntegerWrapper[] testObjects = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectReferenceEquality = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectsContentEquality = createTestObjects(testObjectCount);

        setTestObjects(objectUnitTests, testObjects);
        setTestObjectsReferenceEquality(objectUnitTests, testObjectReferenceEquality);
        setTestObjectsContentEquality(objectUnitTests, testObjectsContentEquality);

        try (MockedStatic<Assertions> mockUtils = mockStatic(Assertions.class, CALLS_REAL_METHODS)) {
            ArgumentCaptor<Object> expectedCapture = ArgumentCaptor.forClass(Object.class);
            ArgumentCaptor<Object> actualCapture = ArgumentCaptor.forClass(Object.class);

            mockUtils.when(() -> Assertions.assertNotEquals(expectedCapture.capture(), actualCapture.capture())).thenCallRealMethod();

            objectUnitTests.testEquals();

            int expectedCount = 0;
            for (int i = 0; i < testObjectCount; i++) {
                loop2: for (int j = i + 1; j < testObjectCount; j++) {
                    expectedCount++;
                    for (int k = 0; k < expectedCapture.getAllValues().size(); k++) {
                        if (checkCompares(testObjects[i], testObjects[j], (IntegerWrapper) expectedCapture.getAllValues().get(k), (IntegerWrapper) actualCapture.getAllValues().get(k))) {
                            continue loop2;
                        }

                    }
                    int finalI = i;
                    int finalJ = j;
                    fail(context, TR -> "Found no notEquals call that compares testObjects[" + finalI + "] and testObjects[" + finalJ + "].");
                }

            }

            int finalExpectedCount = expectedCount;
            assertEquals(expectedCount, expectedCapture.getAllValues().size(), context,
                TR -> "assertNotEquals should be invoked " + finalExpectedCount + " times.");
        }
    }

    @Test
    public void testTestNotEqualsCount() {
        Context context = contextBuilder().subject("ObjectUnitTests#testEquals").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(ObjectUnitTests.class).getMethod(identical("testEquals"))).getCtElement();

        assertEquals(1, countInvocations(method, (name, target) -> name.equals("assertNotEquals") && target.startsWith("org.junit")), context,
            TR -> "The method testEquals() should contain exactly 1 assertNotEquals() calls.");
    }

    @Test
    public void testTestHashCodeEqual() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#testHashCode")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Object::toString);


        IntegerWrapper[] testObjects = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectReferenceEquality = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectsContentEquality = createTestObjects(testObjectCount);

        setTestObjects(objectUnitTests, testObjects);
        setTestObjectsReferenceEquality(objectUnitTests, testObjectReferenceEquality);
        setTestObjectsContentEquality(objectUnitTests, testObjectsContentEquality);

        try (MockedStatic<Assertions> mockUtils = mockStatic(Assertions.class)) {

            //we cant call the real methods and use argument captors because the custom hashCodes are not equal

            AtomicInteger invocationCount = new AtomicInteger(0);
            List<Integer> expectedValues = new ArrayList<>();
            List<Integer> actualValues = new ArrayList<>();
            mockUtils.when(() -> Assertions.assertEquals(anyInt(), anyInt()))
                .thenAnswer(invocation ->  {
                    invocationCount.incrementAndGet();
                    expectedValues.add(invocation.getArgument(0));
                    actualValues.add(invocation.getArgument(1));
                    return null;
                });

            objectUnitTests.testHashCode();

            assertEquals(3 * testObjectCount, invocationCount.get(), context,
                TR -> "testHashCode() should be invoked " + (3 * testObjectCount) + " times.");

            Integer[] expected1Values = new Integer[testObjectCount];
            Integer[] expected2Values = new Integer[testObjectCount];
            Integer[] expected3Values = new Integer[testObjectCount];

            Integer[] actual1Values = new Integer[testObjectCount];
            Integer[] actual2Values = new Integer[testObjectCount];
            Integer[] actual3Values = new Integer[testObjectCount];

            for (int i = 0; i < testObjectCount; i++) {
                expected1Values[i] = expectedValues.get(3 * i);
                expected2Values[i] = expectedValues.get(3 * i + 1);
                expected3Values[i] = expectedValues.get(3 * i + 2);

                actual1Values[i] = actualValues.get(3 * i);
                actual2Values[i] = actualValues.get(3 * i + 1);
                actual3Values[i] = actualValues.get(3 * i + 2);
            }

            if (!checkCompares(mapToHash(testObjects), mapToHash(testObjectReferenceEquality), expected1Values, actual1Values) &&
                !checkCompares(mapToHash(testObjects), mapToHash(testObjectReferenceEquality), expected2Values, actual2Values) &&
                !checkCompares(mapToHash(testObjects), mapToHash(testObjectReferenceEquality), expected3Values, actual3Values)) {
                fail(context, TR -> "Found no equals call that compares testObjects and testObjectsReferenceEquality.");
            }

            if (!checkCompares(mapToHash(testObjects), mapToHash(testObjectsContentEquality), expected1Values, actual1Values) &&
                !checkCompares(mapToHash(testObjects), mapToHash(testObjectsContentEquality), expected2Values, actual2Values) &&
                !checkCompares(mapToHash(testObjects), mapToHash(testObjectsContentEquality), expected3Values, actual3Values)) {
                fail(context, TR -> "Found no equals call that compares testObjects and testObjectsContentEquality.");
            }

            if (!checkCompares(mapToHash(testObjectReferenceEquality), mapToHash(testObjectsContentEquality), expected1Values, actual1Values) &&
                !checkCompares(mapToHash(testObjectReferenceEquality), mapToHash(testObjectsContentEquality), expected2Values, actual2Values) &&
                !checkCompares(mapToHash(testObjectReferenceEquality), mapToHash(testObjectsContentEquality), expected3Values, actual3Values)) {
                fail(context, TR -> "Found no equals call that compares testObjectsReferenceEquality and testObjectsContentEquality.");
            }
        }
    }

    @Test
    public void testTestHashCodeEqualCount() {
        Context context = contextBuilder().subject("ObjectUnitTests#testHashCode").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(ObjectUnitTests.class).getMethod(identical("testHashCode"))).getCtElement();

        assertEquals(3, countInvocations(method, (name, target) -> name.equals("assertEquals") && target.startsWith("org.junit")), context,
            TR -> "The method testEquals() should contain exactly 3 assertEquals() calls.");
    }

    @Test
    public void testTestHashCodeNotEquals() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#testHashCode")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, Object::toString);


        IntegerWrapper[] testObjects = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectReferenceEquality = createTestObjects(testObjectCount);
        IntegerWrapper[] testObjectsContentEquality = createTestObjects(testObjectCount);

        setTestObjects(objectUnitTests, testObjects);
        setTestObjectsReferenceEquality(objectUnitTests, testObjectReferenceEquality);
        setTestObjectsContentEquality(objectUnitTests, testObjectsContentEquality);

        try (MockedStatic<Assertions> mockUtils = mockStatic(Assertions.class)) {

            //we cant call the real methods and use argument captors because the custom hashCodes are not equal

            AtomicInteger invocationCount = new AtomicInteger(0);
            List<Integer> expectedValues = new ArrayList<>();
            List<Integer> actualValues = new ArrayList<>();
            mockUtils.when(() -> Assertions.assertNotEquals(anyInt(), anyInt()))
                .thenAnswer(invocation ->  {
                    invocationCount.incrementAndGet();
                    expectedValues.add(invocation.getArgument(0));
                    actualValues.add(invocation.getArgument(1));
                    return null;
                });

            objectUnitTests.testHashCode();

            int expectedCount = 0;
            for (int i = 0; i < testObjectCount; i++) {
                loop2: for (int j = i + 1; j < testObjectCount; j++) {
                    expectedCount++;
                    for (int k = 0; k < invocationCount.get(); k++) {
                        if (checkCompares(testObjects[i].hashCode(), testObjects[j].hashCode(), expectedValues.get(k), actualValues.get(k))) {
                            continue loop2;
                        }

                    }
                    int finalI = i;
                    int finalJ = j;
                    fail(context, TR -> "Found no notEquals call that compares testObjects[" + finalI + "] and testObjects[" + finalJ + "].");
                }

            }

            int finalExpectedCount = expectedCount;
            assertEquals(expectedCount, invocationCount.get(), context,
                TR -> "assertNotEquals should be invoked " + finalExpectedCount + " times.");
        }
    }

    @Test
    public void testTestHashCodeNotEqualCount() {
        Context context = contextBuilder().subject("ObjectUnitTests#testHashCode").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(ObjectUnitTests.class).getMethod(identical("testHashCode"))).getCtElement();

        assertEquals(1, countInvocations(method, (name, target) -> name.equals("assertNotEquals") && target.startsWith("org.junit")), context,
            TR -> "The method testEquals() should contain exactly 1 assertNotEquals() calls.");
    }

    @Test
    public void testTestToString() throws ReflectiveOperationException {
        int testObjectCount = 10;
        Context context = contextBuilder().subject("ObjectUnitTests#testToString")
            .add("testObjectCount", testObjectCount).build();

        TestObjectFactory testObjectFactory = new TestObjectFactory();
        ObjectUnitTests<IntegerWrapper> objectUnitTests = new ObjectUnitTests<>(testObjectFactory, integerWrapper -> "Actual: " + integerWrapper.value);


        IntegerWrapper[] testObjects = createTestObjects(testObjectCount);

        setTestObjects(objectUnitTests, testObjects);

        try (MockedStatic<Assertions> mockUtils = mockStatic(Assertions.class)) {
            //we cant call the real methods and use argument captors because the custom hashCodes are not equal

            AtomicInteger invocationCount = new AtomicInteger(0);
            List<String> expectedValues = new ArrayList<>();
            List<String> actualValues = new ArrayList<>();
            mockUtils.when(() -> Assertions.assertEquals((Object) any(), any()))
                .thenAnswer(invocation ->  {
                    invocationCount.incrementAndGet();
                    expectedValues.add(invocation.getArgument(0));
                    actualValues.add(invocation.getArgument(1));
                    return null;
                });

            objectUnitTests.testToString();

            assertEquals(testObjectCount, invocationCount.get(), context,
                TR -> "assertEquals() should be invoked " + testObjectCount + " times.");

            if (!checkCompares(mapToString(testObjects, ""), mapToString(testObjects, "Actual: "), expectedValues.toArray(new String[0]), actualValues.toArray(new String[0]))) {
                fail(context, TR -> "Found no equals call that compares testObjects and toString results.");
            }
        }
    }

    @Test
    public void testTestToStringCount() {
        Context context = contextBuilder().subject("ObjectUnitTests#testToString").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(ObjectUnitTests.class).getMethod(identical("testToString"))).getCtElement();

        assertEquals(1, countInvocations(method, (name, target) -> name.equals("assertEquals") && target.startsWith("org.junit")), context,
            TR -> "The method testEquals() should contain exactly 1 assertEquals() calls.");
    }

    private Integer[] mapToHash(IntegerWrapper[] testObjects) {
        return Arrays.stream(testObjects).map(IntegerWrapper::hashCode).toArray(Integer[]::new);
    }

    public String[] mapToString(IntegerWrapper[] testObjects, String prefix) {
        return Arrays.stream(testObjects).map(integerWrapper -> prefix + integerWrapper.toString()).toArray(String[]::new);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] getObjectTestObjects(ObjectUnitTests<T> objectUnitTests) throws ReflectiveOperationException {
        Field testObjectsField = ObjectUnitTests.class.getDeclaredField("testObjects");
        testObjectsField.setAccessible(true);
        return (T[]) testObjectsField.get(objectUnitTests);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] getTestObjectsReferenceEquality(ObjectUnitTests<T> objectUnitTests) throws ReflectiveOperationException {
        Field testObjectsField = ObjectUnitTests.class.getDeclaredField("testObjectsReferenceEquality");
        testObjectsField.setAccessible(true);
        return (T[]) testObjectsField.get(objectUnitTests);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] getTestObjectsContentEquality(ObjectUnitTests<T> objectUnitTests) throws ReflectiveOperationException {
        Field testObjectsField = ObjectUnitTests.class.getDeclaredField("testObjectsContentEquality");
        testObjectsField.setAccessible(true);
        return (T[]) testObjectsField.get(objectUnitTests);
    }

    public static <T> void setTestObjects(ObjectUnitTests<T> objectUnitTests, T[] testObjects) throws ReflectiveOperationException {
        Field testObjectsField = ObjectUnitTests.class.getDeclaredField("testObjects");
        testObjectsField.setAccessible(true);
        testObjectsField.set(objectUnitTests, testObjects);
    }

    public static <T> void setTestObjectsReferenceEquality(ObjectUnitTests<T> objectUnitTests, T[] testObjectsReferenceEquality) throws ReflectiveOperationException {
        Field testObjectsField = ObjectUnitTests.class.getDeclaredField("testObjectsReferenceEquality");
        testObjectsField.setAccessible(true);
        testObjectsField.set(objectUnitTests, testObjectsReferenceEquality);
    }

    public static <T> void setTestObjectsContentEquality(ObjectUnitTests<T> objectUnitTests, T[] testObjectsContentEquality) throws ReflectiveOperationException {
        Field testObjectsField = ObjectUnitTests.class.getDeclaredField("testObjectsContentEquality");
        testObjectsField.setAccessible(true);
        testObjectsField.set(objectUnitTests, testObjectsContentEquality);
    }

}
