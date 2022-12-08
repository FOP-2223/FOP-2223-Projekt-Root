package projekt;

import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;

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

    public void initialize(int testObjectCount) {
        crash(); // TODO: H12.1 - remove if implemented
    }

    void testEquals() {
        crash(); // TODO: H12.1 - remove if implemented
    }

    void testHashCode() {
        crash(); // TODO: H12.1 - remove if implemented
    }

    void testToString() {
        crash(); // TODO: H12.1 - remove if implemented
    }

}
