package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface OrderGenerator {

    List<ConfirmedOrder> generateOrders(long time);
}
