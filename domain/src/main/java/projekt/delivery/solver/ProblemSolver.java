package projekt.delivery.solver;


import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.List;
import java.util.Map;

public interface ProblemSolver {

    Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solve();

}
