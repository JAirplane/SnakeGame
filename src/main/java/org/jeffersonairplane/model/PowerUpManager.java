package org.jeffersonairplane.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface PowerUpManager {
    /**
     * Getter for power ups collection.
     * @return list of power ups existed.
     */
    List<PowerUp> getPowerUps();

    /**
     * One frame passed for all power ups waiting for creation.
     * Leave implementation empty if you do not use creation delays.
     */
    void countdownWaitingPowerUps();

    /**
     * Creates new {@link org.jeffersonairplane.model.PowerUp} instance in the game.
     * @param type particular type of PowerUp to create.
     * @param coordinate of created Power Up, should be on the playing field.
     * @return true if power up successfully created and added to power up collection.
     */
    public boolean createPowerUp(PowerUpTypes type, Coordinate coordinate);

    /**
     * Creates one or few {@link org.jeffersonairplane.model.PowerUp} instance in the game.
     * @param coordinateSupplier provide points for power ups created.
     */
    void createPowerUps(Supplier<Coordinate> coordinateSupplier);

    /**
     * Adds new countdown in frames until power up created.
     */
    void runNewPowerUpCountdown();

    /**
     * Checks if any power up exists on particular point and returns it.
     * @param point to find power up at.
     * @return Optional<PowerUp> power up at arg point or empty Optional otherwise.
     */
    Optional<PowerUp> getPowerUpByPoint(Coordinate point);

    /**
     * Removes power up from game.
     * @param powerUp to remove.
     * @return true if removed.
     */
    boolean removePowerUp(PowerUp powerUp);
}
