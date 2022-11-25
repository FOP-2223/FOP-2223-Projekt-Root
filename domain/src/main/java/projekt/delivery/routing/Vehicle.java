package projekt.delivery.routing;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

public interface Vehicle extends Comparable<Vehicle> {

    /**
     * The current {@link Region.Component} that this entity is on.
     *
     * <p>
     * This may be a {@link Region.Edge} or a {@link Region.Node}.
     * </p>
     *
     * @return The current {@link Region.Component} that this entity is on
     */
    VehicleManager.Occupied<?> getOccupied();

    /**
     * Deletes the entire move queue and moves directly to the provided {@link Region.Node}.
     */
    default void moveDirect(Region.Node node) {
        moveDirect(node, v -> {
        });
    }

    void moveDirect(Region.Node node, Consumer<? super Vehicle> arrivalAction);

    /**
     * Adds the provided {@link Region.Node} to the move queue.
     */
    default void moveQueued(Region.Node node) {
        moveQueued(node, v -> {
        });
    }

    /**
     * Adds the provided {@link Region.Node} to the move queue.
     * As soon as the vehicle arrives at the specified node, {@code arrivalAction} is run.
     */
    void moveQueued(Region.Node node, Consumer<? super Vehicle> arrivalAction);

    int getId();

    /**
     * The maximum acceptable weight of the total cargo in KG.
     */
    double getCapacity();

    /**
     * Accessor for the vehicle manager that is responsible for movements of this vehicle
     *
     * @return the vehicle manager that is responsible for this vehicle
     */
    VehicleManager getVehicleManager();

    VehicleManager.Occupied<? extends Region.Node> getStartingNode();

    Collection<ConfirmedOrder> getOrders();

    void reset();

    // TODO: allow appending of own methods?
    default double getCurrentWeight() {
        return getOrders().stream().mapToDouble(ConfirmedOrder::getTotalWeight).sum();
    }

    // TODO: Gui exercise to draw paths?
    interface Path {

        Deque<Region.Node> getNodes();

        Consumer<? super Vehicle> getArrivalAction();
    }
}
