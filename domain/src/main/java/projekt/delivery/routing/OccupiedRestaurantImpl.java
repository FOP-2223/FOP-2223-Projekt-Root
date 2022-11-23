package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtRestaurantEvent;
import projekt.delivery.event.LoadOrderEvent;

import java.util.Collections;
import java.util.List;

class OccupiedRestaurantImpl extends OccupiedNodeImpl<Region.Node> implements VehicleManager.OccupiedRestaurant {

    private final String name;
    private final List<String> availableFood;

    OccupiedRestaurantImpl(Region.Node component, VehicleManager vehicleManager, String name,  List<String> availableFood) {
        super(component, vehicleManager);
        this.name = name;
        this.availableFood = availableFood;
    }

    @Override
    public void loadOrder(Vehicle vehicle, ConfirmedOrder order, long currentTick) {
        if (vehicle.getOccupied() != this) {
            throw new IllegalArgumentException("The specified vehicle is not located on this node!");
        }

        ((VehicleImpl) vehicle).loadOrder(order);
        vehicleManager.getEventBus().queuePost(LoadOrderEvent.of(
                currentTick,
                vehicle,
                order,
                this
            )
        );
    }

    @Override
    public List<String> getAvailableFood() {
        return Collections.unmodifiableList(availableFood);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void emitArrivedEvent(VehicleImpl vehicle, OccupiedEdgeImpl previousEdge, long currentTick) {
        vehicleManager.getEventBus().queuePost(ArrivedAtRestaurantEvent.of(
                currentTick,
                vehicle,
                this,
                previousEdge.getComponent()
            )
        );
    }
}
