package projekt.delivery.solver;


import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.List;
import java.util.Map;

public interface DeliveryProblemSolver {

    Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solve(ProblemArchetype problemArchetype);

}
