package projekt.delivery;

import projekt.base.DistanceCalculator;
import projekt.base.Location;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleDeliveryService implements DeliveryService {

    private final DistanceCalculator distanceCalculator;

    public SimpleDeliveryService(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public void deliver(List<ConfirmedOrder> confirmedOrders) {
        List<ConfirmedOrder> sortedOrders = sortOrders(confirmedOrders);
        Location currentLocation = new Location(0, 0);
        LocalDateTime timeAtLastStop = LocalDateTime.now();

        for (ConfirmedOrder order : sortedOrders) {
            Duration deliveryDuration = Duration.ofMinutes((long) distanceCalculator.calculateDistance(currentLocation, order.getLocation()));

            timeAtLastStop = timeAtLastStop.plus(deliveryDuration);
            order.setActualDeliveryTime(timeAtLastStop);
            currentLocation = order.getLocation();
        }
    }

    private List<ConfirmedOrder> sortOrders(List<ConfirmedOrder> confirmedOrders) {
        List<ConfirmedOrder> result = new ArrayList<>();
        ConfirmedOrder lastOrder = new ConfirmedOrder(0, 0, -1, null, null);

        while (!confirmedOrders.isEmpty()) {
            double lastDistance = Double.MAX_VALUE;
            ConfirmedOrder nearestOrder = null;

            for (ConfirmedOrder order : confirmedOrders) {
                if (nearestOrder == null || distanceCalculator.calculateDistance(lastOrder.getLocation(), order.getLocation()) < lastDistance) {
                    nearestOrder = order;
                    lastDistance = distanceCalculator.calculateDistance(lastOrder.getLocation(), order.getLocation());
                }
            }

            result.add(nearestOrder);
            confirmedOrders.remove(nearestOrder);
            lastOrder = nearestOrder;
        }

        return result;
    }
}
