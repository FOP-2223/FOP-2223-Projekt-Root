package projekt.h8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import projekt.base.Location;
import projekt.base.TickInterval;
import projekt.delivery.event.ArrivedAtNodeEvent;
import projekt.delivery.event.ArrivedAtRestaurantEvent;
import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.TravelDistanceRater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.DijkstraPathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.solution.TutorEdge;
import projekt.solution.TutorLocation;
import projekt.solution.TutorNeighborhood;
import projekt.solution.TutorNode;
import projekt.solution.TutorRegion;
import projekt.solution.TutorRestaurant;
import projekt.solution.TutorVehicle;
import projekt.solution.TutorVehicleManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static projekt.util.Utils_Private.addEdgesAttributeToRegion;
import static projekt.util.Utils_Private.addEdgesToRegion;
import static projekt.util.Utils_Private.addNodesToRegion;
import static projekt.util.Utils_Private.createOccupiedRestaurant;

@SuppressWarnings({"FieldCanBeLocal", "DuplicatedCode"})
@TestForSubmission
public class TutorTests_H8_TravelDistanceRaterTest_Private {

    private Location neighborhoodLocation;
    private Location restaurantLocation;
    private Location nodeLocation;
    private Region region;
    private String food;
    private List<String> foodList;
    private Region.Restaurant restaurant;
    private Region.Neighborhood neighborhood;
    private Region.Node node;
    private Region.Edge edgeNeR;
    private Region.Edge edgeNoR;
    private TutorVehicleManager vehicleManager;
    private Vehicle vehicle;
    private VehicleManager.OccupiedRestaurant occupiedRestaurant;

    @BeforeEach
    public void setup() throws ReflectiveOperationException {
        neighborhoodLocation = new TutorLocation(0, 0);
        restaurantLocation = new TutorLocation(1, 1);
        nodeLocation = new TutorLocation(2, 2);
        region = new TutorRegion();
        food = "food";
        foodList = List.of(food);
        restaurant = new TutorRestaurant(region, "R", restaurantLocation, Set.of(neighborhoodLocation, nodeLocation), List.of(food));
        neighborhood = new TutorNeighborhood(region, "Ne", neighborhoodLocation, Set.of(restaurantLocation));
        node = new TutorNode(region, "No", nodeLocation, Set.of(restaurantLocation));
        addNodesToRegion(region, restaurant, neighborhood, node);
        edgeNeR = new TutorEdge(region, "ENeR", neighborhoodLocation, restaurantLocation, 10);
        edgeNoR = new TutorEdge(region, "ENoR", restaurantLocation, nodeLocation, 1);
        addEdgesToRegion(region, edgeNeR, edgeNoR);
        addEdgesAttributeToRegion(region, neighborhoodLocation, Map.of(restaurantLocation, edgeNeR));
        addEdgesAttributeToRegion(region, restaurantLocation, Map.of(nodeLocation, edgeNoR));
        vehicleManager = new TutorVehicleManager(region, new DijkstraPathCalculator());
        occupiedRestaurant = (VehicleManager.OccupiedRestaurant) createOccupiedRestaurant(vehicleManager, restaurant);
        vehicle = new TutorVehicle(1, 10, vehicleManager, occupiedRestaurant);
    }

    @ParameterizedTest
    @CsvSource({"0.5, 1.0", "0.25, 1.0", "0.99, 1.0"})
    public void testNoDistanceTraveled(double factor, double expected) {
        Rater travelDistanceRater = TravelDistanceRater.Factory.builder()
            .setVehicleManager(vehicleManager)
            .setFactor(factor)
            .build()
            .create();

        Context context = contextBuilder()
            .subject("TravelDistanceRater#getScore")
            .add("factor", factor)
            .build();

        ConfirmedOrder order1 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(0, 5), foodList, 1);
        ConfirmedOrder order2 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(1, 6), foodList, 1);
        ConfirmedOrder order3 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(2, 7), foodList, 1);
        ConfirmedOrder order4 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(3, 8), foodList, 1);

        travelDistanceRater.onTick(List.of(
            OrderReceivedEvent.of(0, order1),
            OrderReceivedEvent.of(0, order2)
        ), 0);

        order1.setActualDeliveryTick(1);
        order2.setActualDeliveryTick(1);

        travelDistanceRater.onTick(List.of(
            OrderReceivedEvent.of(1, order3),
            OrderReceivedEvent.of(1, order4),
            DeliverOrderEvent.of(1, vehicle, neighborhood, order1),
            DeliverOrderEvent.of(1, vehicle, neighborhood, order2)
        ), 1);

        order3.setActualDeliveryTick(2);
        order4.setActualDeliveryTick(2);

        travelDistanceRater.onTick(List.of(
            DeliverOrderEvent.of(2, vehicle, neighborhood, order3),
            DeliverOrderEvent.of(2, vehicle, neighborhood, order4)
        ), 2);

        assertEquals(expected, travelDistanceRater.getScore(), context,
            TR -> "method did not return correct score");

    }

    @ParameterizedTest
    @CsvSource({"0.5, 0.0", "0.25, 0.0"})
    public void testWorstDistanceTraveled(double factor, double expected) throws ReflectiveOperationException {

        Rater travelDistanceRater = TravelDistanceRater.Factory.builder()
            .setVehicleManager(vehicleManager)
            .setFactor(factor)
            .build()
            .create();

        Context context = contextBuilder()
            .subject("TravelDistanceRater#getScore")
            .add("factor", factor)
            .build();

        ConfirmedOrder order1 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(0, 5), foodList, 1);
        ConfirmedOrder order2 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(1, 6), foodList, 1);
        ConfirmedOrder order3 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(2, 7), foodList, 1);
        ConfirmedOrder order4 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(3, 8), foodList, 1);

        travelDistanceRater.onTick(List.of(
            OrderReceivedEvent.of(0, order1),
            OrderReceivedEvent.of(0, order2),
            ArrivedAtRestaurantEvent.of(0, vehicle, (VehicleManager.OccupiedRestaurant) createOccupiedRestaurant(vehicleManager, restaurant), edgeNeR)
        ), 0);

        order1.setActualDeliveryTick(1);
        order2.setActualDeliveryTick(1);

        travelDistanceRater.onTick(List.of(
            OrderReceivedEvent.of(1, order3),
            OrderReceivedEvent.of(1, order4),
            DeliverOrderEvent.of(1, vehicle, neighborhood, order1),
            DeliverOrderEvent.of(1, vehicle, neighborhood, order2)
        ), 1);

        for (int i = 2; i < 20; i += 2) {
            travelDistanceRater.onTick(List.of(
                ArrivedAtNodeEvent.of(i, vehicle, node, edgeNoR)
            ), i);

            travelDistanceRater.onTick(List.of(
                ArrivedAtRestaurantEvent.of(i + 1, vehicle, (VehicleManager.OccupiedRestaurant) createOccupiedRestaurant(vehicleManager, restaurant), edgeNeR)
            ), i + 1);
        }

        order3.setActualDeliveryTick(20);
        order4.setActualDeliveryTick(20);

        travelDistanceRater.onTick(List.of(
            DeliverOrderEvent.of(20, vehicle, neighborhood, order3)
        ), 20);

        assertEquals(expected, travelDistanceRater.getScore(), context,
            TR -> "method did not return correct score");
    }

    @ParameterizedTest
    @CsvSource({"0.75, 0.7333", "0.95, 0.7894"})
    public void testLessThanWorstDistanceTraveled(double factor, double expected) throws ReflectiveOperationException {

        Rater travelDistanceRater = TravelDistanceRater.Factory.builder()
            .setVehicleManager(vehicleManager)
            .setFactor(factor)
            .build()
            .create();

        Context context = contextBuilder()
            .subject("TravelDistanceRater#getScore")
            .add("factor", factor)
            .build();

        ConfirmedOrder order1 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(0, 5), foodList, 1);
        ConfirmedOrder order2 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(1, 6), foodList, 1);
        ConfirmedOrder order3 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(2, 7), foodList, 1);
        ConfirmedOrder order4 = new ConfirmedOrder(neighborhoodLocation, occupiedRestaurant, new TickInterval(3, 8), foodList, 1);

        travelDistanceRater.onTick(List.of(
            OrderReceivedEvent.of(0, order1),
            OrderReceivedEvent.of(0, order2),
            ArrivedAtRestaurantEvent.of(0, vehicle, (VehicleManager.OccupiedRestaurant) createOccupiedRestaurant(vehicleManager, restaurant), edgeNeR)
        ), 0);

        order1.setActualDeliveryTick(1);
        order2.setActualDeliveryTick(1);

        travelDistanceRater.onTick(List.of(
            OrderReceivedEvent.of(1, order3),
            OrderReceivedEvent.of(1, order4),
            DeliverOrderEvent.of(1, vehicle, neighborhood, order1),
            DeliverOrderEvent.of(1, vehicle, neighborhood, order2)
        ), 1);

        for (int i = 2; i < 4; i += 2) {
            travelDistanceRater.onTick(List.of(
                ArrivedAtNodeEvent.of(i, vehicle, node, edgeNoR)
            ), i);

            travelDistanceRater.onTick(List.of(
                ArrivedAtRestaurantEvent.of(i + 1, vehicle, (VehicleManager.OccupiedRestaurant) createOccupiedRestaurant(vehicleManager, restaurant), edgeNoR)
            ), i + 1);
        }

        order3.setActualDeliveryTick(20);
        order4.setActualDeliveryTick(20);

        travelDistanceRater.onTick(List.of(
            DeliverOrderEvent.of(20, vehicle, neighborhood, order3)
        ), 20);

        assertTrue(Math.abs(expected - travelDistanceRater.getScore()) < 0.001, context,
            TR -> "method did not return correct score. Expected %f but was %f".formatted(expected, travelDistanceRater.getScore()));
    }


}