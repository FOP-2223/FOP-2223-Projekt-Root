package projekt.delivery.event;

import projekt.delivery.vehicle.Region;

public interface ReceivedDeliveryEvent extends ArrivedAtNodeEvent {
    @Override
    Region.Neighborhood getNode();
}
