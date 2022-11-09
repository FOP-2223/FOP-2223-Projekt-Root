package projekt.pizzeria;

import projekt.delivery.DeliveryService;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.food.Food;
import projekt.food.Pizza;

import java.time.Instant;
import java.util.List;

public interface Pizzeria {

    Factory LOS_FOPBOTS_HERMANOS = (deliveryService) -> null;
    Factory JAVA_HUT = (deliveryService) -> new PizzeriaImpl("FopHut",
        List.of(
            Pizza.MARGHERITA
        ),
        deliveryService
    );
    Factory PASTAFAR = (deliveryService) -> null;
    Factory PALPAPIZZA = (deliveryService) -> null;
    Factory ISENJAR = (deliveryService) -> null;
    Factory MIDDLE_FOP = (deliveryService) -> null;
    Factory MOUNT_DOOM_PIZZA = (deliveryService) -> null;

    String getName();

    List<? extends Food.Variant<?, ?>> getMenu();

    ConfirmedOrder submitOrder(
        int x,
        int y,
        Instant earliestDelivery,
        List<? extends Food> food
    );

    void sendOutOrders();

    interface Factory {

        Pizzeria create(DeliveryService deliveryService);
    }
}
