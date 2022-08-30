package projekt.delivery.routing;

import org.junit.jupiter.api.Test;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class VehicleManagerTest {

    private Region createRegion1() {
        return Region.builder()
            .addNeighborhood("nodeA", new Location(-2, -2), 1.5)
            .addNode("nodeB", new Location(-2, 2))
            .addNeighborhood("nodeC", new Location(2, 2), 0.5)
            .addNode("nodeD", new Location(2, -2))
            .addEdge("edge1", new Location(-2, -2), new Location(-2, 2))
            .addEdge("edge2", new Location(-2, 2), new Location(-2, 2))
            .addEdge("edge3", new Location(-2, -2), new Location(-2, 2))
            .addEdge("edge4", new Location(-2, -2), new Location(-2, 2))
            .distanceCalculator(new EuclideanDistanceCalculator())
            .build();
    }

    @Test
    void testVehicleManagerBasic() {
        Region region = createRegion1();
        Region.Node warehouse = region.getNode(new Location(-2, -2)); // nodeA
        LocalDateTime startTime = LocalDateTime.now();
        VehicleManager vehicleManager = new VehicleManagerImpl(startTime, region, new DijkstraPathCalculator(), warehouse);

        assertTrue(vehicleManager.getVehicles().isEmpty(), "No vehicles added yet but found at least one.");
        assertEquals(startTime, vehicleManager.getCurrentTime(), "Start time was changed without any ticks.");
        assertEquals(vehicleManager.getWarehouse().getComponent(), warehouse, "Warehouse was changed without manipulation.");

        Collection<VehicleManager.Occupied<? extends Region.Edge>> occupiedEdges = vehicleManager.getOccupiedEdges();
        assertEquals(occupiedEdges.size(), 4, "Expected occupied edges count to fit edges count");
        occupiedEdges.forEach(occupied -> {
            assertTrue(occupied.getVehicles().isEmpty(), "No vehicles added yet but found at least one.");
        });

        Collection<VehicleManager.Occupied<? extends Region.Node>> occupiedNodes = vehicleManager.getOccupiedNodes();
        assertEquals(occupiedNodes.size(), 4, "Expected occupied nodes count to fit nodes count");
        occupiedNodes.forEach(occupied -> {
            assertTrue(occupied.getVehicles().isEmpty(), "No vehicles added yet but found at least one.");
        });

        String[] edgeNames = {"edge1", "edge2", "edge3", "edge4"};
        String[] nodeNames = {"nodeA", "nodeB", "nodeC", "nodeD"};

        // Check if all edges are in occupied edges
        Arrays.stream(edgeNames).forEach(edgeName ->
            assertTrue(occupiedEdges.stream().anyMatch(occupiedEdge -> occupiedEdge.getComponent().getName().equals(edgeName)),
                String.format("Edge %s was not found in occupied edges.", edgeName)));
        // Check if all nodes are in occupied nodes
        Arrays.stream(nodeNames).forEach(nodeName ->
            assertTrue(occupiedNodes.stream().anyMatch(occupiedNode -> occupiedNode.getComponent().getName().equals(nodeName)),
                String.format("Node %s was not found in occupied nodes.", nodeName)));
    }
}
