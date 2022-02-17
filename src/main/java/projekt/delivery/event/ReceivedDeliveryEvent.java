package projekt.delivery.event;

import projekt.delivery.routing.Region;

public interface ReceivedDeliveryEvent extends ArrivedAtNodeEvent {
    @Override
    Region.Neighborhood getNode();
}
