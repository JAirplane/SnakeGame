package org.jeffersonairplane.model;

/**
 * Registers, removes and notifies {@link org.jeffersonairplane.model.PowerUpTakenObserver} if power up eaten by snake.
 */
public interface PowerUpTakenObservable {
    /**
     * Registers {@link org.jeffersonairplane.model.PowerUpTakenObserver}.
     * @param obs is an observer to register.
     */
    void registerPowerUpTakenObserver(PowerUpTakenObserver obs);

    /**
     * Removes {@link org.jeffersonairplane.model.PowerUpTakenObserver}.
     * @param obs is an observer to remove.
     */
    void removePowerUpTakenObserver(PowerUpTakenObserver obs);

    /**
     * Notifies {@link org.jeffersonairplane.model.PowerUpTakenObserver} if power up eaten by snake.
     * @param powerUp is a power up eaten.
     */
    void notifyPowerUpTakenObservers(PowerUp powerUp);
}
