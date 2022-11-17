package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

class VehicleImpl implements Vehicle {

    private final int id;
    private final double capacity;
    private final List<ConfirmedOrder> orders = new ArrayList<>();
    private final Collection<String> compatibleFoodTypes;
    private final VehicleManagerImpl vehicleManager;
    private final Deque<PathImpl> moveQueue = new LinkedList<>();
    private AbstractOccupied<?> occupied;

    public VehicleImpl(
        int id,
        double capacity,
        Collection<String> compatibleFoodTypes,
        AbstractOccupied<?> occupied,
        VehicleManagerImpl vehicleManager
    ) {
        this.id = id;
        this.capacity = capacity;
        this.compatibleFoodTypes = compatibleFoodTypes;
        this.occupied = occupied;
        this.vehicleManager = vehicleManager;
    }

    @Override
    public VehicleManager.Occupied<?> getOccupied() {
        return occupied;
    }

    void setOccupied(AbstractOccupied<?> occupied) {
        this.occupied = occupied;
    }

    @Override
    public void moveDirect(Region.Node node, Consumer<? super Vehicle> arrivalAction) {
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
            moveQueue.add(new PathImpl(new ArrayDeque<>(Collections.singleton(nextNode)), v -> {
            }));
        }
        moveQueued(node, arrivalAction);
    }

    @Override
    public void moveQueued(Region.Node node, Consumer<? super Vehicle> arrivalAction) {
        checkMoveToNode(node);
        Region.Node startNode = null;
        final Iterator<PathImpl> it = moveQueue.descendingIterator();
        while (it.hasNext() && startNode == null) {
            PathImpl path = it.next();
            if (!path.getNodes().isEmpty()) {
                startNode = path.getNodes().peekLast();
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
        moveQueue.add(new PathImpl(nodes, ((Consumer<Vehicle>) v ->
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
    public Collection<ConfirmedOrder> getOrders() {
        return orders;
    }

    @Override
    public boolean checkCompatibility(String food) {
        return true;
    }

    public boolean checkCompatibility(List<String> foods) {
        for (String food : foods) {
            if (!checkCompatibility(food)) return false;
        }
        return true;
    }

    private void checkMoveToNode(Region.Node node) {
        if (occupied.component.equals(node)) {
            throw new IllegalArgumentException("Vehicle " + getId() + " cannot move to own node " + node);
        }
    }

    void move(long currentTick) {
        final Region region = vehicleManager.getRegion();
        if (moveQueue.isEmpty()) {
            return;
        }
        final PathImpl path = moveQueue.peek();
        if (path.getNodes().isEmpty()) {
            moveQueue.pop();
            final @Nullable Consumer<? super Vehicle> action = path.getArrivalAction();
            if (action == null) {
                move(currentTick);
            } else {
                action.accept(this);
            }
        } else {
            Region.Node next = path.getNodes().peek();
            if (occupied instanceof OccupiedNodeImpl) {
                vehicleManager.getOccupied(region.getEdge(((OccupiedNodeImpl<?>) occupied).getComponent(), next)).addVehicle(this, currentTick);
            } else if (occupied instanceof OccupiedEdgeImpl) {
                vehicleManager.getOccupied(next).addVehicle(this, currentTick);
                path.getNodes().pop();
            } else {
                throw new AssertionError("Component must be either node or component");
            }
        }
    }

    void loadOrder(ConfirmedOrder order) {
        double capacityNeeded = getCurrentWeight() + order.getTotalWeight();

        if (capacityNeeded > capacity) {
            throw new VehicleOverloadedException(this, capacityNeeded);
        }

        Optional<String> incompatibleFood = order.getFoodList().stream().filter(f ->
            !checkCompatibility(f)).findFirst();
        if (incompatibleFood.isPresent()) {
            throw new FoodNotSupportedException(this, incompatibleFood.get());
        }

        orders.add(order);
    }

    void unloadOrder(ConfirmedOrder order) {
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

    /**
     * The path is expected to exclude the start node and to include the end node.
     * If the start node and the end node are the same, the path should be empty.
     */
    private static class PathImpl implements Path {

        private final Deque<Region.Node> nodes;
        private final Consumer<? super Vehicle> arrivalAction;

        private PathImpl(Deque<Region.Node> nodes, Consumer<? super Vehicle> arrivalAction) {
            this.nodes = nodes;
            this.arrivalAction = arrivalAction;
        }

        @Override
        public Deque<Region.Node> getNodes() {
            return nodes;
        }

        @Override
        public Consumer<? super Vehicle> getArrivalAction() {
            return arrivalAction;
        }
    }
}
