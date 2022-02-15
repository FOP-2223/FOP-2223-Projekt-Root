package projekt.pizzeria;

import projekt.delivery.ConfirmedOrder;
import projekt.delivery.DeliveryService;
import projekt.food.Food;
import projekt.food.Pizza;
import projekt.rating.Rater;

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

        Pizzeria create(DeliveryService deliveryService, Rater rater);
    }

    Pizzeria.Factory LOS_FOPBOTS_HERMANOS = (deliveryService, rater) -> null;

    Pizzeria.Factory JAVA_HUT = (deliveryService, rater) -> new PizzeriaImpl("FopHut",
        List.of(
            Pizza.MARGHERITA
        ),
        deliveryService,
        rater
    );

    Pizzeria.Factory PASTAFAR = (deliveryService, rater) -> null;

    Pizzeria.Factory PALPAPIZZA = (deliveryService, rater) -> null;

    Pizzeria.Factory ISENJAR = (deliveryService, rater) -> null;

    Pizzeria.Factory MIDDLE_FOP = (deliveryService, rater) -> null;

    Pizzeria.Factory MOUNT_DOOM_PIZZA = (deliveryService, rater) -> null;
}
