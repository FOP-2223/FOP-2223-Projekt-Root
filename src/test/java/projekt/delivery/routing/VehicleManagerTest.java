package projekt.delivery.routing;

import org.junit.jupiter.api.Test;
import projekt.base.Location;

import java.time.Duration;

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
            .build();
    }

    @Test
    void testVehicleManagerBasic() {
    }
}
