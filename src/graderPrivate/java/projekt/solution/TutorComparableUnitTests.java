package projekt.solution;

import projekt.ComparableUnitTests;

import java.util.function.Function;

public class TutorComparableUnitTests<T extends Comparable<? super T>> extends ComparableUnitTests<T> {

    public static Function<Integer, Comparable<?>> testObjectFactory;
    public static int testObjectCount;
    public static int testBiggerThenCount;
    public static int testAsBigAsCount;
    public static int testLessThenCount;
    public static TutorComparableUnitTests<?> lastInstance;

    @SuppressWarnings("unchecked")
    public TutorComparableUnitTests(Function<Integer, T> testObjectFactory) {
        super(testObjectFactory);
        TutorComparableUnitTests.testObjectFactory = (Function<Integer, Comparable<?>>) testObjectFactory;
        lastInstance = this;
    }

    public void initialize(int testObjectCount) {
        TutorComparableUnitTests.testObjectCount = testObjectCount;
    }

    public void testBiggerThen() {
        testBiggerThenCount++;
    }

    public void testAsBigAs() {
        testAsBigAsCount++;
    }

    public void testLessThen() {
        testLessThenCount++;
    }

    public static void reset() {
        testObjectCount = 0;
        testBiggerThenCount = 0;
        testAsBigAsCount = 0;
        testLessThenCount = 0;
        lastInstance = null;
    }
}
