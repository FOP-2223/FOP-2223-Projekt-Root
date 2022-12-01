package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static org.tudalgo.algoutils.student.Student.crash;

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
     * Returns the previous component this Vehicle occupied.
     * @return the previous component or null if this vehicle has not moved yet.
     */
    @Nullable VehicleManager.Occupied<?> getPreviousOccupied();

    List<? extends Path> getPaths();

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

    default double getCurrentWeight() {
        return crash(); // TODO: H5.1 - remove if implemented
    }

    interface Path {

        Deque<Region.Node> getNodes();

        Consumer<? super Vehicle> getArrivalAction();
    }
}
