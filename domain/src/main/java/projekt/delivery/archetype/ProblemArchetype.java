package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface ProblemArchetype {

    void start();

    void start(long maxTickCount);

    void end();

    List<List<ConfirmedOrder>> getAllOrders();

    List<ConfirmedOrder> getOrdersForTick(long tick);
}
