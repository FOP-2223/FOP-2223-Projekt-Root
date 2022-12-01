package projekt.delivery.routing;

import projekt.base.Location;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RestaurantImpl extends NodeImpl implements Region.Restaurant {

    private final List<String> availableFood;

    RestaurantImpl(
        Region region,
        String name,
        Location location,
        Set<Location> connections,
        List<String> availableFood
    ) {
        super(region, name, location, connections);
        this.availableFood = availableFood;
    }

    @Override
    public List<String> getAvailableFood() {
        return Collections.unmodifiableList(availableFood);
    }

}
