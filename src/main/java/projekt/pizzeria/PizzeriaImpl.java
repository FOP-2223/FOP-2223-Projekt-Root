package projekt.pizzeria;

import projekt.delivery.ConfirmedOrder;
import projekt.delivery.DeliveryService;
import projekt.food.Food;
import projekt.rating.Rater;

import java.time.Instant;
import java.util.List;

public class PizzeriaImpl implements Pizzeria {

    private final String name;
    private final List<? extends Food.Variant<?, ?>> menu;
    private final DeliveryService deliveryService;

    public PizzeriaImpl(String name, List<? extends Food.Variant<?, ?>> menu, DeliveryService deliveryService) {
        this.name = name;
        this.menu = menu;
        this.deliveryService = deliveryService;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<? extends Food.Variant<?, ?>> getMenu() {
        return null;
    }

    @Override
    public ConfirmedOrder submitOrder(
        int x,
        int y,
        Instant earliestDelivery,
        List<? extends Food> food
    ) {
        return null;
    }

    @Override
    public void sendOutOrders() {

    }
}
