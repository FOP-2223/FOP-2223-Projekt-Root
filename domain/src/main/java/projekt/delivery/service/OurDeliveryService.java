package projekt.delivery.service;

import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.ArrayList;
import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

public class OurDeliveryService extends AbstractDeliveryService {

    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    public OurDeliveryService(
        ProblemArchetype problemArchetype
    ) {
        super(problemArchetype.vehicleManager());
    }

    @Override
    List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {
        return crash(); // TODO: H9.2 - remove if implemented
    }

    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        return pendingOrders;
    }

    @Override
    public void reset() {
        super.reset();
        pendingOrders.clear();
    }
}
