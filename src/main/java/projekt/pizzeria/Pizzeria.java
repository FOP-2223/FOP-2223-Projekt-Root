package projekt.pizzeria;

import projekt.delivery.ConfirmedOrder;
import projekt.delivery.DeliveryService;
import projekt.food.Food;
import projekt.food.Pizza;

import java.time.Instant;
import java.util.List;

public interface Pizzeria {

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

    Pizzeria.Factory LOS_FOPBOTS_HERMANOS = (deliveryService) -> null;

    Pizzeria.Factory JAVA_HUT = (deliveryService) -> new PizzeriaImpl("FopHut",
        List.of(
            Pizza.MARGHERITA
        ),
        deliveryService
    );

    Pizzeria.Factory PASTAFAR = (deliveryService) -> null;

    Pizzeria.Factory PALPAPIZZA = (deliveryService) -> null;

    Pizzeria.Factory ISENJAR = (deliveryService) -> null;

    Pizzeria.Factory MIDDLE_FOP = (deliveryService) -> null;

    Pizzeria.Factory MOUNT_DOOM_PIZZA = (deliveryService) -> null;
}
