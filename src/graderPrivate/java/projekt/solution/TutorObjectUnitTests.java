package projekt.solution;

import projekt.ObjectUnitTests;

import java.util.function.Function;

public class TutorObjectUnitTests<T> extends ObjectUnitTests<T> {

    public static Function<Integer, Object> testObjectFactory;
    public static Function<Object, String> toString;
    public static int testObjectCount;
    public static int testEqualsCount;
    public static int testHashCodeCount;
    public static int testToStringCount;
    public static TutorObjectUnitTests<?> lastInstance;

    @SuppressWarnings("unchecked")
    public TutorObjectUnitTests(Function<Integer, T> testObjectFactory, Function<T, String> toString) {
        super(testObjectFactory, toString);
        TutorObjectUnitTests.testObjectFactory = (Function<Integer, Object>) testObjectFactory;
        TutorObjectUnitTests.toString = (Function<Object, String>) toString;
        lastInstance = this;
    }

    @Override
    public void initialize(int testObjectCount) {
        TutorObjectUnitTests.testObjectCount = testObjectCount;
    }

    @Override
    public void testEquals() {
        testEqualsCount++;
    }

    @Override
    public void testHashCode() {
        testHashCodeCount++;
    }

    @Override
    public void testToString() {
        testToStringCount++;
    }

    public static void reset() {
        testObjectCount = 0;
        testEqualsCount = 0;
        testHashCodeCount = 0;
        testToStringCount = 0;
        lastInstance = null;
    }
}
