package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderGenerator {

    List<ConfirmedOrder> generateOrders(LocalDateTime time);
}
