package projekt.h12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoException;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;
import projekt.base.LocationUnitTests;
import projekt.solution.TutorComparableUnitTests;
import projekt.solution.TutorLocation;
import projekt.solution.TutorObjectUnitTests;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mockConstruction;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class TutorTests_H12_LocationUnitTests_Private {

    @BeforeEach
    public void setUp() {
        TutorComparableUnitTests.reset();
        TutorObjectUnitTests.reset();
    }

    @Test
    public void testInitializeObjectUnitTests() throws ReflectiveOperationException {

        Context context = contextBuilder().subject("LocationUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation)) {
            LocationUnitTests locationUnitTests = new LocationUnitTests();
            LocationUnitTests.initialize();

            assertNotNull(TutorObjectUnitTests.lastInstance, context,
                TR -> "No ObjectUnitTest Instance got created.");

            assertSame(TutorObjectUnitTests.lastInstance, getObjectUnitTests(locationUnitTests), context,
                TR -> "The objectUnitTests Attribute is not the last create ObjectUnitTests instance.");

            assertNotNull(TutorObjectUnitTests.testObjectFactory, context,
                TR -> "The testObjectFactory is null.");
            assertNotNull(TutorObjectUnitTests.toString, context,
                TR -> "The toString function is null.");

            assertEquals(10, TutorObjectUnitTests.testObjectCount, context,
                TR -> "The testObjectCount is not 10.");

            boolean xyDifferent = false;

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int finalI = i;
                    int finalJ = j;

                    TutorLocation locationI = TutorTests_H12_Private.locationMap.get((Location) TutorObjectUnitTests.testObjectFactory.apply(i));
                    TutorLocation locationJ = TutorTests_H12_Private.locationMap.get((Location) TutorObjectUnitTests.testObjectFactory.apply(j));

                    if (!xyDifferent && i < 10 && j < 10) {
                        if (locationI.getX() != locationJ.getX() && locationI.getY() != locationJ.getY()) {
                            xyDifferent = true;
                        }
                    }

                    if (i == j) {
                        assertEquals(locationI, locationJ, context, TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned false for i = " + finalI + " and j = " + finalJ);
                    } else {
                        assertNotEquals(locationI, locationJ, context, TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned true for i = " + finalI + " and j = " + finalJ);
                    }
                }
            }

            assertEquals(xyDifferent, true, context,
                TR -> "Found no location pair with i and j < 10 where x and y are both different.");

            assertEquals("(6,9)", TutorObjectUnitTests.toString.apply(new TutorLocation(6, 9)), context,
                TR -> "toString.apply() did not return the correct String when given a location with x=6 and y=9");
        } catch (MockitoException e) {
            throw new RuntimeException(e.getMessage() + ": " + e.getCause().getMessage());
        }
    }

    @Test
    public void testInitializeComparableUnitTests() throws ReflectiveOperationException {

        Context context = contextBuilder().subject("LocationUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation)) {

            LocationUnitTests locationUnitTests = new LocationUnitTests();
            LocationUnitTests.initialize();

            assertNotNull(TutorComparableUnitTests.lastInstance, context,
                TR -> "No ComparableUnitTests Instance got created.");

            assertSame(TutorComparableUnitTests.lastInstance, getComparableUnitTests(locationUnitTests), context,
                TR -> "The comparableUnitTests Attribute is not the last create ComparableUnitTests instance.");

            assertNotNull(TutorComparableUnitTests.testObjectFactory, context,
                TR -> "The testObjectFactory is null.");

            assertEquals(10, TutorComparableUnitTests.testObjectCount, context,
                TR -> "The testObjectCount is not 10.");

            boolean xyDifferent = false;

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int finalI = i;
                    int finalJ = j;

                    TutorLocation locationI = TutorTests_H12_Private.locationMap.get((Location) TutorObjectUnitTests.testObjectFactory.apply(i));
                    TutorLocation locationJ = TutorTests_H12_Private.locationMap.get((Location) TutorObjectUnitTests.testObjectFactory.apply(j));

                    if (!xyDifferent && i < 10 && j < 10) {
                        if (locationI.getX() != locationJ.getX() && locationI.getY() != locationJ.getY()) {
                            xyDifferent = true;
                        }
                    }

                    if (i == j) {
                        assertEquals(0, locationI.compareTo(locationJ), context,
                            TR -> "testObjectFactory.apply(i).compareTo(testObjectFactory.apply(j)) is not equal to 0 for i = " + finalI + " and j = " + finalJ);
                    } else if (i < j) {
                        assertTrue(locationI.compareTo(locationJ) < 0, context, TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) is not less than 0 for i = " + finalI + " and j = " + finalJ);
                    } else {
                        assertTrue(locationI.compareTo(locationJ) > 0, context, TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) is not greater than 0 for i = " + finalI + " and j = " + finalJ);
                    }
                }
            }

            assertEquals(xyDifferent, true, context,
                TR -> "Found no location pair with i and j < 10 where x and y are both different.");
        } catch (MockitoException e) {
            throw new RuntimeException(e.getMessage() + ": " + e.getCause().getMessage());
        }
    }

    @Test
    public void testTestMethods() throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder().subject("LocationUnitTests#test*()").build();

        LocationUnitTests locationUnitTests = new LocationUnitTests();

        setObjectUnitTests(locationUnitTests, new TutorObjectUnitTests<>(null, null));
        setComparableUnitTests(locationUnitTests, new TutorComparableUnitTests<>(null));

        locationUnitTests.testEquals();
        assertEquals(1, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testEquals() did not call ObjectUnitTests.testEquals() once.");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testEquals() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testEquals() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testEquals() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testEquals() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testEquals() called ComparableUnitTests.testLessThen().");

        TutorObjectUnitTests.reset();

        locationUnitTests.testHashCode();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testHashCode() called ObjectUnitTests.testEquals().");
        assertEquals(1, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testHashCode() did not call ObjectUnitTests.testHashCode() once.");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testHashCode() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testHashCode() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testHashCode() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testHashCode() called ComparableUnitTests.testLessThen().");

        TutorObjectUnitTests.reset();

        locationUnitTests.testToString();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testToString() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testToString() called ObjectUnitTests.testHashCode().");
        assertEquals(1, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testToString() did not call ObjectUnitTests.toString() once.");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testToString() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testToString() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testToString() called ComparableUnitTests.testLessThen().");

        TutorObjectUnitTests.reset();

        locationUnitTests.testBiggerThen();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testBiggerThen() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testBiggerThen() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testBiggerThen() called ObjectUnitTests.toString().");
        assertEquals(1, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testBiggerThen() did not call ComparableUnitTests.testBiggerThen() once.");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testBiggerThen() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testBiggerThen() called ComparableUnitTests.testLessThen().");

        TutorComparableUnitTests.reset();

        locationUnitTests.testAsBigAs();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testAsBigAs() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testAsBigAs() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testAsBigAs() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testAsBigAs() called ComparableUnitTests.testBiggerThen().");
        assertEquals(1, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testAsBigAs() did not call ComparableUnitTests.testAsBigAs() once.");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testAsBigAs() called ComparableUnitTests.testLessThen().");

        TutorComparableUnitTests.reset();

        locationUnitTests.testLessThen();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testLessThen() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testLessThen() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testLessThen() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testLessThen() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testLessThen() called ComparableUnitTests.testAsBigAs().");
        assertEquals(1, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testLessThen() did not call ComparableUnitTests.testLessThen() once.");
    }

    @SuppressWarnings("unchecked")
    public static ObjectUnitTests<Location> getObjectUnitTests(LocationUnitTests locationUnitTests) throws ReflectiveOperationException {
        Field objectUnitTests = LocationUnitTests.class.getDeclaredField("objectUnitTests");
        objectUnitTests.setAccessible(true);
        return (ObjectUnitTests<Location>) objectUnitTests.get(locationUnitTests);
    }

    @SuppressWarnings("unchecked")
    public static ComparableUnitTests<Location> getComparableUnitTests(LocationUnitTests locationUnitTests) throws ReflectiveOperationException {
        Field objectUnitTests = LocationUnitTests.class.getDeclaredField("comparableUnitTests");
        objectUnitTests.setAccessible(true);
        return (ComparableUnitTests<Location>) objectUnitTests.get(locationUnitTests);
    }

    public static void setObjectUnitTests(LocationUnitTests locationUnitTests, ObjectUnitTests<Location> objectUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = LocationUnitTests.class.getDeclaredField("objectUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(locationUnitTests, objectUnitTests);
    }

    public static void setComparableUnitTests(LocationUnitTests locationUnitTests, ComparableUnitTests<Location> comparableUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = LocationUnitTests.class.getDeclaredField("comparableUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(locationUnitTests, comparableUnitTests);
    }

}
