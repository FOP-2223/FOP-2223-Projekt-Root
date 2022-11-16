package projekt.delivery.simulation;

import projekt.delivery.DeliveryService;

public class BasicDeliverySimulation extends AbstractSimulation {

    private final DeliveryService deliveryService;

    public BasicDeliverySimulation(SimulationConfig simulationConfig, DeliveryService deliveryService) {
        super(simulationConfig);
        this.deliveryService = deliveryService;
    }

    @Override
    void tick() {
        deliveryService.tick(getCurrentTick());
    }

    @Override
    public void onStateUpdated() {
        //TODO notify MainFrame
        //SwingUtilities.invokeLater(mainFrame::onModelUpdate);
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
}
