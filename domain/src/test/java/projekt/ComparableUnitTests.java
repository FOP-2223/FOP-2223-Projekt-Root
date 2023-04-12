package projekt;

import org.junit.jupiter.api.Assertions;

import java.util.function.Function;


public class ComparableUnitTests<T extends Comparable<? super T>> {

    private final Function<Integer, T> testObjectFactory;

    private T[] testObjects;

    public ComparableUnitTests(Function<Integer, T> testObjectFactory) {
        this.testObjectFactory = testObjectFactory;
    }

    @SuppressWarnings("unchecked")
    public void initialize(int testObjectCount) {
        testObjects = (T[]) new Comparable<?>[testObjectCount];

        for (int i = 0; i < testObjectCount; i++) {
            testObjects[i] = testObjectFactory.apply(i);
        }
    }

    public void testBiggerThen() {
        for (int i = 0; i < testObjects.length; i++) {
            for (int j = 0; j < i; j++) {
                Assertions.assertTrue(testObjects[i].compareTo(testObjects[j]) > 0);
            }
        }
    }

    @SuppressWarnings("EqualsWithItself")
    public void testAsBigAs() {
        for (T testObject : testObjects) {
            Assertions.assertEquals(0, testObject.compareTo(testObject));
        }
    }

    public void testLessThen() {
        for (int i = 0; i < testObjects.length; i++) {
            for (int j = i + 1; j < testObjects.length; j++) {
                Assertions.assertTrue(testObjects[i].compareTo(testObjects[j]) < 0);
            }
        }
    }
}
