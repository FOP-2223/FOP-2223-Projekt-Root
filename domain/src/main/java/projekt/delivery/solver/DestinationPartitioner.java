package projekt.delivery.solver;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.List;
import java.util.Map;

public interface DestinationPartitioner {

    Map<Vehicle, List<ConfirmedOrder>> partition();
}
