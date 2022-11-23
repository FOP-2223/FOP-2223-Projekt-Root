package projekt.delivery.rating;

import projekt.delivery.simulation.SimulationListener;

public interface Rater extends SimulationListener {

    /**
     * Gets the score of the entire simulation up to the current tick.
     * This method has no side effects
     * @return the rating
     */
    double getScore();

    interface Factory {

        Rater create();
    }
}
