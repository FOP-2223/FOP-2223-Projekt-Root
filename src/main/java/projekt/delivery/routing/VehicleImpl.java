package projekt.delivery.routing;

import projekt.food.Food;
import projekt.food.FoodType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

class VehicleImpl implements Vehicle {

    private final int id;
    private final double capacity;
    private final List<Food> foods = new ArrayList<>();
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
        moveQueued(node);
    }

    @Override
    public void moveQueued(Region.Node node) {
        final Deque<Region.Node> nodes = new LinkedList<>();
        nodes.add(node);
        moveQueue.add(new PathImpl(nodes, v ->
            System.out.println("Vehicle " + v.getId() + " arrived at node " + node.getName())));
    }

    void move() {
        if (moveQueue.isEmpty()) {
            return;
        }
        final PathImpl path = moveQueue.pop();
        final Region region = vehicleManager.getRegion();
        final Region.Node next = path.nodes.pop();
        if (occupied instanceof OccupiedNodeImpl) {
            vehicleManager.getOccupied(region.getEdge(((OccupiedNodeImpl) occupied).getComponent(), next)).addVehicle(this);
        } else if (occupied instanceof OccupiedEdgeImpl) {
            vehicleManager.getOccupied(next).addVehicle(this);
        } else {
            throw new AssertionError("Component must be either node or component");
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

    public List<Food> getFoods() {
        return foods;
    }

    @Override
    public Collection<Food> getFood() {
        return foods;
    }

    @Override
    public Collection<FoodType<?, ?>> getCompatibleFoodTypes() {
        return compatibleFoodTypes;
    }

    @Override
    public void addFood(Food food) throws VehicleOverloadedException {
        double capacityNeeded = getCurrentWeight() + food.getWeight();

        if (capacityNeeded > capacity) {
            throw new VehicleOverloadedException(this, capacityNeeded);
        }

        foods.add(food);
    }

    @Override
    public int compareTo(Vehicle o) {
        return Integer.compare(getId(), o.getId());
    }

    private class PathImpl implements Path {

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
