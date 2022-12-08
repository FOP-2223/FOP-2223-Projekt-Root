package projekt;

import org.junit.jupiter.api.Assertions;

import java.util.function.Function;

public class ObjectUnitTests<T> {

    private final Function<Integer, T> testObjectFactory;
    private final Function<T, String> toString;

    private T[] testObjects;
    private T[] testObjectsReferenceEquality;
    private T[] testObjectsContentEquality;

    public ObjectUnitTests(Function<Integer, T> testObjectFactory, Function<T, String> toString) {
        this.testObjectFactory = testObjectFactory;
        this.toString = toString;
    }

    @SuppressWarnings("unchecked")
    public void initialize(int testObjectCount) {
        testObjects = (T[]) new Object[testObjectCount];
        testObjectsReferenceEquality = (T[]) new Object[testObjectCount];
        testObjectsContentEquality = (T[]) new Object[testObjectCount];

        for (int i = 0; i < testObjectCount; i++) {
            testObjects[i] = testObjectsReferenceEquality[i] = testObjectFactory.apply(i);
            testObjectsContentEquality[i] = testObjectFactory.apply(i);
        }
    }

    public void testEquals() {
        for (int i = 0; i < testObjects.length; i++) {
            Assertions.assertEquals(testObjects[i], testObjectsContentEquality[i]);
            Assertions.assertEquals(testObjects[i], testObjectsReferenceEquality[i]);
            Assertions.assertEquals(testObjectsReferenceEquality[i], testObjectsContentEquality[i]);
        }

        for (int i = 0; i < testObjects.length; i++) {
            for (int j = i + 1; j < testObjects.length; j++) {
                Assertions.assertNotEquals(testObjects[i], testObjects[j]);
            }
        }
    }

    public void testHashCode() {
        for (int i = 0; i < testObjects.length; i++) {
            Assertions.assertEquals(testObjects[i].hashCode(), testObjectsContentEquality[i].hashCode());
            Assertions.assertEquals(testObjects[i].hashCode(), testObjectsReferenceEquality[i].hashCode());
            Assertions.assertEquals(testObjectsReferenceEquality[i].hashCode(), testObjectsContentEquality[i].hashCode());
        }

        for (int i = 0; i < testObjects.length; i++) {
            for (int j = i + 1; j < testObjects.length; j++) {
                Assertions.assertNotEquals(testObjects[i].hashCode(), testObjects[j].hashCode());
            }
        }
    }

    public void testToString() {
        for (T testObject : testObjects) {
            Assertions.assertEquals(toString.apply(testObject), testObject.toString());
        }
    }

}
