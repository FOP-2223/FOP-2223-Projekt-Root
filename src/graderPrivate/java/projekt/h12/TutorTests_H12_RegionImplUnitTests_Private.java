package projekt.h12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import projekt.ObjectUnitTests;
import projekt.base.Location;
import projekt.delivery.routing.EdgeImpl;
import projekt.delivery.routing.NodeImpl;
import projekt.delivery.routing.RegionImpl;
import projekt.delivery.routing.RegionImplUnitTests;
import projekt.solution.TutorObjectUnitTests;
import projekt.solution.TutorRegion;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mockConstruction;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class TutorTests_H12_RegionImplUnitTests_Private {

    @BeforeEach
    public void setUp() {
        TutorObjectUnitTests.reset();
    }

    @Test
    public void testInitializeObjectUnitTests() throws ReflectiveOperationException {

        Context context = contextBuilder().subject("RegionImplUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation);
             MockedConstruction<RegionImpl> mocked2 = mockConstruction(RegionImpl.class, TutorTests_H12_Private::prepareRegion);
             MockedConstruction<NodeImpl> mocked3 = mockConstruction(NodeImpl.class, TutorTests_H12_Private::prepareNode);
             MockedConstruction<EdgeImpl> mocked4 = mockConstruction(EdgeImpl.class, TutorTests_H12_Private::prepareEdge)) {

            RegionImplUnitTests regionImplUnitTests = new RegionImplUnitTests();
            RegionImplUnitTests.initialize();

            assertNotNull(TutorObjectUnitTests.lastInstance, context,
                TR -> "No ObjectUnitTest Instance got created.");

            assertSame(TutorObjectUnitTests.lastInstance, getObjectUnitTests(regionImplUnitTests), context,
                TR -> "The objectUnitTests Attribute is not the last create ObjectUnitTests instance.");

            assertNotNull(TutorObjectUnitTests.testObjectFactory, context,
                TR -> "The testObjectFactory is null.");
            assertNull(TutorObjectUnitTests.toString, context,
                TR -> "The toString function is not null.");

            assertEquals(10, TutorObjectUnitTests.testObjectCount, context,
                TR -> "The testObjectCount is not 10.");

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int finalI = i;
                    int finalJ = j;

                    TutorRegion regionI = TutorTests_H12_Private.regionMap.get((RegionImpl) TutorObjectUnitTests.testObjectFactory.apply(i));
                    TutorRegion regionJ = TutorTests_H12_Private.regionMap.get((RegionImpl) TutorObjectUnitTests.testObjectFactory.apply(j));

                    assertTrue(regionI.nodes.size() >= 2, context,
                        TR -> "Expected that the region for i=%d has at least 2 nodes but it has %d".formatted(finalI, regionI.nodes.size()));
                    assertTrue(regionJ.nodes.size() >= 2, context,
                        TR -> "Expected that the region for j=%d has at least 2 nodes but it has %d".formatted(finalJ, regionJ.nodes.size()));

                    assertTrue(regionI.allEdges.size() >= 1, context,
                        TR -> "Expected that the region for i=%d has at least 2 edges but it has %d".formatted(finalI, regionI.allEdges.size()));
                    assertTrue(regionJ.allEdges.size() >= 1, context,
                        TR -> "Expected that the region for j=%d has at least 2 edges but it has %d".formatted(finalJ, regionJ.allEdges.size()));

                    if (i == j) {
                        assertEquals(regionI, regionJ, context,
                            TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned false for i = " + finalI + " and j = " + finalJ + "");
                    } else {
                        assertNotEquals(regionI, regionJ, context,
                            TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned true for i = " + finalI + " and j = " + finalJ + "");
                    }
                }
            }
        } catch (MockitoException e) {
            throw new RuntimeException(e.getMessage() + ": " + e.getCause().getMessage());
        }
    }

    @Test
    public void testTestMethods() throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder().subject("RegionImplUnitTests#test*()").build();

        RegionImplUnitTests regionImplUnitTests = new RegionImplUnitTests();

        setObjectUnitTests(regionImplUnitTests, new TutorObjectUnitTests<>(null, null));

        regionImplUnitTests.testEquals();
        assertEquals(1, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testEquals() did not call ObjectUnitTests.testEquals() once.");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testEquals() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testEquals() called ObjectUnitTests.toString().");

        TutorObjectUnitTests.reset();

        regionImplUnitTests.testHashCode();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testHashCode() called ObjectUnitTests.testEquals().");
        assertEquals(1, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testHashCode() did not call ObjectUnitTests.testHashCode() once.");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testHashCode() called ObjectUnitTests.toString().");
    }

    @SuppressWarnings("unchecked")
    public static ObjectUnitTests<RegionImpl> getObjectUnitTests(RegionImplUnitTests regionImplUnitTests) throws ReflectiveOperationException {
        Field field = RegionImplUnitTests.class.getDeclaredField("objectUnitTests");
        field.setAccessible(true);
        return (ObjectUnitTests<RegionImpl>) field.get(regionImplUnitTests);
    }

    public static void setObjectUnitTests(RegionImplUnitTests regionImplUnitTests, ObjectUnitTests<Location> objectUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = RegionImplUnitTests.class.getDeclaredField("objectUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(regionImplUnitTests, objectUnitTests);
    }
}
