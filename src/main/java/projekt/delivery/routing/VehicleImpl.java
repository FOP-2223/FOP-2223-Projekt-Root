package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.delivery.ConfirmedOrder;
import projekt.delivery.FoodNotSupportedException;
import projekt.food.Food;
import projekt.food.FoodType;

import java.util.*;
import java.util.function.Consumer;

class VehicleImpl implements Vehicle {

    private final int id;
    private final double capacity;
    private final List<ConfirmedOrder> orders = new ArrayList<>();
    private final Collection<FoodType<?, ?>> compatibleFoodTypes;
    private AbstractOccupied<?> occupied;
    private final VehicleManagerImpl vehicleManager;
    private final Deque<PathImpl> moveQueue = new LinkedList<>();

    public VehicleImpl(
        int id,
        double capacity,
        Collection<FoodType<?, ?>> compatibleFoodTypes,
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
    public void moveDirect(Region.Node node) {
        moveQueue.clear();
        // if a vehicle is on an edge, keep the movement to the next node
        if (occupied.component instanceof Region.Edge) {
            final @Nullable VehicleManager.Occupied<?> notThisOne = occupied.vehicles.get(this).previous;
            if (!(notThisOne instanceof OccupiedNodeImpl<?>)) {
                throw new AssertionError("Previous component must be a node");
            }
            Region.Node previousNode = ((OccupiedNodeImpl<?>) notThisOne).component;
            Region.Node nodeA = ((Region.Edge) occupied.component).getNodeA();
            Region.Node nodeB = ((Region.Edge) occupied.component).getNodeB();
            Region.Node nextNode = previousNode.equals(nodeA) ? nodeB : nodeA;
            moveQueue.add(new PathImpl(new ArrayDeque<>(Collections.singleton(nextNode)), v -> {}));
        }
        moveQueued(node);
    }

    @Override
    public void moveQueued(Region.Node node) {
        moveQueued(node, t -> {});
    }

    @Override
    public void moveQueued(Region.Node node, Consumer<? super Vehicle> arrivalAction) {
        Region.Node startNode = null;
        Iterator<PathImpl> it = moveQueue.descendingIterator();
        while (it.hasNext() && startNode == null) {
            PathImpl path = it.next();
            if (!path.getNodes().isEmpty()) {
                startNode = path.getNodes().peekLast();
            }
        }
        if (startNode == null) {
            if (occupied.getComponent() instanceof Region.Node) {
                startNode = (Region.Node) occupied.getComponent();
            } else {
                throw new IllegalStateException("The vehicle is on an edge and does not have any movements queued!");
            }
        }
        final Deque<Region.Node> nodes = vehicleManager.getPathCalculator().getPath(startNode, node);
        moveQueue.add(new PathImpl(nodes, ((Consumer<Vehicle>) v ->
            System.out.println("Vehicle " + v.getId() + " arrived at node " + node.getName())).andThen(arrivalAction)));
    }

    void move() {
        final Region region = vehicleManager.getRegion();
        if (moveQueue.isEmpty()) {
            return;
        }
        final PathImpl path = moveQueue.peek();
        if (path.getNodes().isEmpty()) {
            moveQueue.pop();
            final @Nullable Consumer<? super Vehicle> action = path.getArrivalAction();
            if (action == null) {
                move();
            } else {
                action.accept(this);
            }
        } else {
            Region.Node next = path.getNodes().peek();
            if (occupied instanceof OccupiedNodeImpl) {
                vehicleManager.getOccupied(region.getEdge(((OccupiedNodeImpl<?>) occupied).getComponent(), next)).addVehicle(this);
            } else if (occupied instanceof OccupiedEdgeImpl) {
                vehicleManager.getOccupied(next).addVehicle(this);
                path.getNodes().pop();
            } else {
                throw new AssertionError("Component must be either node or component");
            }
        }
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
    public Collection<FoodType<?, ?>> getCompatibleFoodTypes() {
        return compatibleFoodTypes;
    }

    void loadOrder(ConfirmedOrder order) {
        double capacityNeeded = getCurrentWeight() + order.getTotalWeight();

        if (capacityNeeded > capacity) {
            throw new VehicleOverloadedException(this, capacityNeeded);
        }

        Optional<Food> incompatibleFood = order.getFoodList().stream().filter(f ->
            !compatibleFoodTypes.contains(f.getFoodVariant().getFoodType())).findFirst();
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
