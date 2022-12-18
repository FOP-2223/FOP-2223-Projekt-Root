package projekt;

import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;
import projekt.delivery.archetype.*;
import projekt.delivery.rating.*;
import projekt.delivery.routing.DijkstraPathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.BasicDeliveryService;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static ProblemGroup problemGroup;
    public static SimulationConfig simulationConfig;

    public static void main(String[] args) {

        // layer 1 - Region
        Region region1 = Region.builder()
            .addNeighborhood("Wiesbaden", new Location(-9, -4), 0.75)
            .addNeighborhood("Mainz", new Location(-8, 0), 0.6)
            .addNeighborhood("Frankfurt", new Location(8, -8), 1)
            .addNeighborhood("Darmstadt", new Location(6, 8), 0.5)
            .addNeighborhood("Ruesselsheim", new Location(-2, 0), 0.35)
            .addNeighborhood("Gross-Gerau", new Location(0, 5), 0.2)
            .addNeighborhood("Langen", new Location(6, 0), 0.25)
            .addNeighborhood("Offenbach", new Location(10, -7), 0.35)
            .addRestaurant(new Location(3, -1), Region.Restaurant.LOS_FOPBOTS_HERMANOS)
            .addNode("Mainspitzdreieck", new Location(-5, 0))
            .addNode("Wiesbadener Kreuz", new Location(-4, -5))
            .addNode("Moenchhof-Dreieck", new Location(1, -2))
            .addNode("Frankfurter Kreuz", new Location(4, -4))
            .addNode("Dreieck Mainz", new Location(-10, -1))
            .addEdge("A643", new Location(-10, -1), new Location(-9, -4))
            .addEdge("A60", new Location(-10, -1), new Location(-8, 0))
            .addEdge("A60_1", new Location(-5, 0), new Location(-8, 0))
            .addEdge("A671", new Location(-5, 0), new Location(-9, -4))
            .addEdge("A60_2", new Location(-5, 0), new Location(-2, 0))
            .addEdge("A66", new Location(-4, -5), new Location(-9, -4))
            .addEdge("A66_1", new Location(-4, -5), new Location(8, -8))
            .addEdge("A3", new Location(-4, -5), new Location(1, -2))
            .addEdge("A67", new Location(1, -2), new Location(-2, 0))
            .addEdge("A3_1", new Location(1, -2), new Location(4, -4))
            .addEdge("A5", new Location(4, -4), new Location(8, -8))
            .addEdge("A3_2", new Location(4, -4), new Location(10, -7))
            .addEdge("A5_1", new Location(4, -4), new Location(6, 0))
            .addEdge("A5_2", new Location(6, 0), new Location(6, 8))
            .addEdge("A67_1", new Location(0, 5), new Location(6, 8))
            .addEdge("A67_2", new Location(0, 5), new Location(-2, 0))
            .addEdge("Straße", new Location(3, -1), new Location(1, -2))
            .distanceCalculator(new EuclideanDistanceCalculator())
            .build();

        Region region2 = Region.builder()
            .addNeighborhood("Wiesbaden", new Location(-9, -4), 0.75)
            .addNeighborhood("Mainz", new Location(-8, 0), 0.6)
            .addNeighborhood("Frankfurt", new Location(8, -8), 1)
            .addNeighborhood("Darmstadt", new Location(6, 8), 0.5)
            .addNeighborhood("Ruesselsheim", new Location(-2, 0), 0.35)
            .addNeighborhood("Gross-Gerau", new Location(0, 5), 0.2)
            .addNeighborhood("Langen", new Location(6, 0), 0.25)
            .addNeighborhood("Offenbach", new Location(10, -7), 0.35)
            .addRestaurant(new Location(10, 8), Region.Restaurant.LOS_FOPBOTS_HERMANOS)
            .addNode("Mainspitzdreieck", new Location(-5, 0))
            .addNode("Wiesbadener Kreuz", new Location(-4, -5))
            .addNode("Moenchhof-Dreieck", new Location(1, -2))
            .addNode("Frankfurter Kreuz", new Location(4, -4))
            .addNode("Dreieck Mainz", new Location(-10, -1))
            .addEdge("A643", new Location(-10, -1), new Location(-9, -4))
            .addEdge("A60", new Location(-10, -1), new Location(-8, 0))
            .addEdge("A60_1", new Location(-5, 0), new Location(-8, 0))
            .addEdge("A671", new Location(-5, 0), new Location(-9, -4))
            .addEdge("A60_2", new Location(-5, 0), new Location(-2, 0))
            .addEdge("A66", new Location(-4, -5), new Location(-9, -4))
            .addEdge("A66_1", new Location(-4, -5), new Location(8, -8))
            .addEdge("A3", new Location(-4, -5), new Location(1, -2))
            .addEdge("A67", new Location(1, -2), new Location(-2, 0))
            .addEdge("A3_1", new Location(1, -2), new Location(4, -4))
            .addEdge("A5", new Location(4, -4), new Location(8, -8))
            .addEdge("A3_2", new Location(4, -4), new Location(10, -7))
            .addEdge("A5_1", new Location(4, -4), new Location(6, 0))
            .addEdge("A5_2", new Location(6, 0), new Location(6, 8))
            .addEdge("A67_1", new Location(0, 5), new Location(6, 8))
            .addEdge("A67_2", new Location(0, 5), new Location(-2, 0))
            .addEdge("Straße", new Location(6, 8), new Location(10, 8))
            .distanceCalculator(new EuclideanDistanceCalculator())
            .build();

        // layer 2 - VehicleManager
        VehicleManager vehicleManager1 = VehicleManager.builder()
            .region(region1)
            .pathCalculator(new DijkstraPathCalculator())
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .addVehicle(new Location(3, -1), 2)
            .build();

        // layer 2 - VehicleManager
        VehicleManager vehicleManager2 = VehicleManager.builder()
            .region(region2)
            .pathCalculator(new DijkstraPathCalculator())
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .addVehicle(new Location(10, 8), 2)
            .build();

        final int simulationLength = 700;

        //OrderGenerator
        OrderGenerator.Factory orderGeneratorFactory1 = new FridayOrderGenerator.FactoryBuilder()
            .setOrderCount(300)
            .setDeliveryInterval(15)
            .setVariance(0.5)
            .setMaxWeight(0.5)
            .setVehicleManager(vehicleManager1)
            .setLastTick(400)
            .setSeed(0)
            .build();

        OrderGenerator.Factory orderGeneratorFactory2 = new FridayOrderGenerator.FactoryBuilder()
            .setOrderCount(300)
            .setDeliveryInterval(15)
            .setVariance(0.5)
            .setMaxWeight(0.5)
            .setVehicleManager(vehicleManager2)
            .setLastTick(400)
            .setSeed(0)
            .build();

        //Rater
        Map<RatingCriteria, Rater.Factory> raterFactoryMap1 = new HashMap<>();
        raterFactoryMap1.put(RatingCriteria.IN_TIME, new InTimeRater.FactoryBuilder()
            .setIgnoredTicksOff(5)
            .setMaxTicksOff(25)
            .build());

        raterFactoryMap1.put(RatingCriteria.TRAVEL_DISTANCE, new TravelDistanceRater.FactoryBuilder()
            .setFactor(0.9)
            .setVehicleManager(vehicleManager1)
            .build());

        raterFactoryMap1.put(RatingCriteria.AMOUNT_DELIVERED, new AmountDeliveredRater.FactoryBuilder()
            .setFactor(0.99)
            .build());

        Map<RatingCriteria, Rater.Factory> raterFactoryMap2 = new HashMap<>();
        raterFactoryMap2.put(RatingCriteria.IN_TIME, new InTimeRater.FactoryBuilder()
            .setIgnoredTicksOff(5)
            .setMaxTicksOff(25)
            .build());

        raterFactoryMap2.put(RatingCriteria.TRAVEL_DISTANCE, new TravelDistanceRater.FactoryBuilder()
            .setFactor(0.9)
            .setVehicleManager(vehicleManager2)
            .build());

        raterFactoryMap2.put(RatingCriteria.AMOUNT_DELIVERED, new AmountDeliveredRater.FactoryBuilder()
            .setFactor(0.99)
            .build());

        //ProblemArchetype
        ProblemArchetype problemArchetype1 = new ProblemArchetypeImpl(orderGeneratorFactory1, vehicleManager1, raterFactoryMap1, simulationLength, "problem1");
        ProblemArchetype problemArchetype2 = new ProblemArchetypeImpl(orderGeneratorFactory2, vehicleManager2, raterFactoryMap2, simulationLength, "problem2");

        //layer 3 - DeliveryService
        DeliveryService deliveryService = new BasicDeliveryService(vehicleManager1);

        // SimulationConfig
        simulationConfig = new SimulationConfig(0);

        //ProblemGroup
        problemGroup = new ProblemGroupImpl(List.of(problemArchetype1, problemArchetype2), new ArrayList<>(raterFactoryMap1.keySet()));

        MyApplication.launch();
    }
}
