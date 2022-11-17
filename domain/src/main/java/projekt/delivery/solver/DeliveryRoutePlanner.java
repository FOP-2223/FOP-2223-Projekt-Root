package projekt.delivery.solver;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.HashMap;
import java.util.List;

public interface DeliveryRoutePlanner {

    HashMap<Long, List<ConfirmedOrder>> route();

}
