package projekt.base;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LocationUnitTests {

    private static ComparableUnitTests<Location> comparableUnitTests;
    private static ObjectUnitTests<Location> objectUnitTests;

    @BeforeAll
    public static void initialize() {
        Function<Integer, Location> testObjectFactory = i -> {
            if (i < 5) {
                return new Location(0, i);
            } else if (i < 10){
                return new Location(i - 5, i);
            }
            return new Location(i, 0);
        };

        objectUnitTests = new ObjectUnitTests<>(testObjectFactory, o ->
            String.format("(%d, %d)", o.getX(), o.getY()));
        comparableUnitTests = new ComparableUnitTests<>(testObjectFactory);

        objectUnitTests.initialize(10);
        comparableUnitTests.initialize(10);
    }

    @Test
    public void testEquals() {
        objectUnitTests.testEquals();
    }

    @Test
    public void testHashCode() {
        objectUnitTests.testHashCode();
    }

    @Test
    public void testToString() {
        objectUnitTests.testToString();
    }

    @Test
    public void testBiggerThen() {
        comparableUnitTests.testBiggerThen();
    }

    @Test
    public void testAsBigAs() {
        comparableUnitTests.testAsBigAs();
    }

    @Test
    public void testLessThen() {
        comparableUnitTests.testLessThen();
    }

}
