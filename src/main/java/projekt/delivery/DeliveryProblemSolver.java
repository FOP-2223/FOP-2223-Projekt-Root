package projekt.delivery;

import projekt.base.DistanceCalculator;

public abstract class DeliveryProblemSolver {
    private ConfirmedOrder[] orders;
    private int[] capacities;
    private DistanceCalculator distanceCalculator;

    abstract long[][] solve();
}
