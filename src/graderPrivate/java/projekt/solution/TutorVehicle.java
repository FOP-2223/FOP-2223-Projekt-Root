package projekt.solution;

import org.jetbrains.annotations.Nullable;
import projekt.delivery.routing.AbstractOccupied;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.OccupiedEdgeImpl;
import projekt.delivery.routing.OccupiedNodeImpl;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleImpl;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.routing.VehicleManagerImpl;
import projekt.delivery.routing.VehicleOverloadedException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

@SuppressWarnings("DuplicatedCode")
public class TutorVehicle extends VehicleImpl {

    public TutorVehicle(
        int id,
        double capacity,
        VehicleManagerImpl vehicleManager,
        VehicleManager.OccupiedRestaurant startingNode) {
        super(id, capacity, vehicleManager, startingNode);
    }

    @Override
    public VehicleManager.Occupied<?> getOccupied() {
        return occupied;
    }

    @Override
    public @Nullable VehicleManager.Occupied<?> getPreviousOccupied() {
        AbstractOccupied.VehicleStats stats = occupied.vehicles.get(this);
        return stats == null ? null : stats.previous;
    }

    @Override
    public List<? extends Path> getPaths() {
        return new LinkedList<>(moveQueue);
    }

    public void setOccupied(AbstractOccupied<?> occupied) {
        this.occupied = occupied;
    }

    @Override
    public void moveDirect(Region.Node node, BiConsumer<? super Vehicle, Long> arrivalAction) {
        checkMoveToNode(node);
        moveQueue.clear();
        if (occupied instanceof OccupiedEdgeImpl) {
            // if a vehicle is on an edge, keep the movement to the next node
            final @Nullable VehicleManager.Occupied<?> previousOccupied = occupied.vehicles.get(this).previous;
            if (!(previousOccupied instanceof OccupiedNodeImpl<?>)) {
                throw new AssertionError("Previous component must be a node");
            }
            final Region.Node previousNode = ((OccupiedNodeImpl<?>) previousOccupied).component;
            final Region.Node nodeA = ((Region.Edge) occupied.component).getNodeA();
            final Region.Node nodeB = ((Region.Edge) occupied.component).getNodeB();
            final Region.Node nextNode = previousNode.equals(nodeA) ? nodeB : nodeA;
            moveQueue.add(new PathImpl(new ArrayDeque<>(Collections.singleton(nextNode)), (v, t) -> {
            }));
        }
        moveQueued(node, arrivalAction);
    }

    @Override
    public void moveQueued(Region.Node node, BiConsumer<? super Vehicle, Long> arrivalAction) {
        checkMoveToNode(node);
        Region.Node startNode = null;
        final Iterator<PathImpl> it = moveQueue.descendingIterator();
        while (it.hasNext() && startNode == null) {
            PathImpl path = it.next();
            if (!path.nodes().isEmpty()) {
                startNode = path.nodes().peekLast();
            }
        }
        // if no queued node could be found
        if (startNode == null) {
            if (occupied instanceof OccupiedNodeImpl<?>) {
                startNode = ((OccupiedNodeImpl<?>) occupied).getComponent();
            } else {
                throw new AssertionError("It is not possible to be on an edge if the move queue is naturally empty");
            }
        }
        final Deque<Region.Node> nodes = vehicleManager.getPathCalculator().getPath(startNode, node);
        moveQueue.add(new PathImpl(nodes, ((BiConsumer<Vehicle, Long>) (v, t) ->
            System.out.println("Vehicle " + v.getId() + " arrived at node " + node)).andThen(arrivalAction)));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    @Override
    public VehicleManager.Occupied<? extends Region.Node> getStartingNode() {
        return startingNode;
    }

    @Override
    public Collection<ConfirmedOrder> getOrders() {
        return orders;
    }

    @Override
    public void reset() {
        occupied = (AbstractOccupied<?>) startingNode;
        moveQueue.clear();
        orders.clear();
    }

    private void checkMoveToNode(Region.Node node) {
        if (occupied.component.equals(node) && moveQueue.isEmpty()) {
            throw new IllegalArgumentException("Vehicle " + getId() + " cannot move to own node " + node);
        }
    }

    public void move(long currentTick) {
        final Region region = vehicleManager.getRegion();
        if (moveQueue.isEmpty()) {
            return;
        }
        final PathImpl path = moveQueue.peek();
        if (path.nodes().isEmpty()) {
            moveQueue.pop();
            final @Nullable BiConsumer<? super Vehicle, Long> action = path.arrivalAction();
            if (action == null) {
                move(currentTick);
            } else {
                action.accept(this, currentTick);
            }
        } else {
            Region.Node next = path.nodes().peek();
            if (occupied instanceof OccupiedNodeImpl) {
                vehicleManager.getOccupied(region.getEdge(((OccupiedNodeImpl<?>) occupied).getComponent(), next)).addVehicle(this, currentTick);
            } else if (occupied instanceof OccupiedEdgeImpl) {
                vehicleManager.getOccupied(next).addVehicle(this, currentTick);
                path.nodes().pop();
            } else {
                throw new AssertionError("Component must be either node or component");
            }
        }
    }

    public void loadOrder(ConfirmedOrder order) {
        double capacityNeeded = getCurrentWeight() + order.getWeight();

        if (capacityNeeded > capacity) {
            throw new VehicleOverloadedException(this, capacityNeeded);
        }

        orders.add(order);
    }

    public void unloadOrder(ConfirmedOrder order) {
        orders.remove(order);
    }

    @Override
    public int compareTo(Vehicle o) {
        return Integer.compare(getId(), o.getId());
    }

    @Override
    public String toString() {
        return "VehicleImpl("
            + "id=" + id
            + ", capacity=" + capacity
            + ", orders=" + orders
            + ", component=" + occupied.component
            + ')';
    }

}
