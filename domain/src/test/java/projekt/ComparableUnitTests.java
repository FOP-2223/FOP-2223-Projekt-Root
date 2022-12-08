package projekt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;

public class ComparableUnitTests<T extends Comparable<? super T>> {

    private final Function<Integer, T> testObjectFactory;

    private T[] testObjects;

    public ComparableUnitTests(Function<Integer, T> testObjectFactory) {
        this.testObjectFactory = testObjectFactory;
    }

    public void initialize(int testObjectCount) {
        crash(); // TODO: H12.2 - remove if implemented
    }

    void testBiggerThen() {
        crash(); // TODO: H12.2 - remove if implemented
    }

    void testAsBigAs() {
        crash(); // TODO: H12.2 - remove if implemented
    }

    void testLessThen() {
        crash(); // TODO: H12.2 - remove if implemented
    }
}
