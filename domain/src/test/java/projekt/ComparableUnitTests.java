package projekt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;

public class ComparableUnitTests {

    private final Function<Integer, Object> testObjectFactory;

    private Object[] testObjects;

    public ComparableUnitTests(Function<Integer, Object> testObjectFactory) {
        this.testObjectFactory = testObjectFactory;
    }

    @BeforeAll
    public void initialize(int testObjectCount) {
        crash(); // TODO: H12.2 - remove if implemented
    }

    @Test
    void testBiggerThen() {
        crash(); // TODO: H12.2 - remove if implemented
    }

    @Test
    void testAsBigAs() {
        crash(); // TODO: H12.2 - remove if implemented
    }

    @Test
    void testLessThen() {
        crash(); // TODO: H12.2 - remove if implemented
    }
}
