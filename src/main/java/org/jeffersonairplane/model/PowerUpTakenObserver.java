package org.jeffersonairplane.model;

/**
 * Observes if snake eaten power up via  {@link org.jeffersonairplane.model.PowerUpTakenObservable}.
 */
public interface PowerUpTakenObserver {
    /**
     * Observes if snake eaten power up via  {@link org.jeffersonairplane.model.PowerUpTakenObservable}.
     * @param powerUp is a power up eaten.
     */
    void powerUpTakenUpdate(PowerUp powerUp);
}
