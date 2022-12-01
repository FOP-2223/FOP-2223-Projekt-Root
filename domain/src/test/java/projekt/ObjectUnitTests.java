package projekt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.tudalgo.algoutils.student.Student.crash;

public class ObjectUnitTests {

    private final Function<Integer, Object> testObjectFactory;
    private final Function<Object, String> toString;

    private Object[] testObjects;
    private Object[] testObjectsReferenceEquality;
    private Object[] testObjectsContentEquality;

    public ObjectUnitTests(Function<Integer, Object> testObjectFactory, Function<Object, String> toString) {
        this.testObjectFactory = testObjectFactory;
        this.toString = toString;
    }

    @BeforeAll
    public void initialize(int testObjectCount) {
        crash(); // TODO: H12.1 - remove if implemented
    }

    @Test
    void testEquals() {
        crash(); // TODO: H12.1 - remove if implemented
    }

    @Test
    void testHashCode() {
        crash(); // TODO: H12.1 - remove if implemented
    }

    @Test
    void testToString() {
        crash(); // TODO: H12.1 - remove if implemented
    }

}
