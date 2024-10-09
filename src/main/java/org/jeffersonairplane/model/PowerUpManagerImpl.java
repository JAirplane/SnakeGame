package org.jeffersonairplane.model;

import lombok.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.*;

public class PowerUpManagerImpl implements PowerUpManager {
	@Getter
    private final List<PowerUp> powerUps;
	@Getter
	private final Map<PowerUpTypes, List<Integer>> powerUpCreationCountdowns;
	@Getter @Setter
	private int powerUpNumberLimit;
	@Getter @Setter
	private int minPowerUpCreationDelay;
	@Getter @Setter
	private int maxPowerUpCreationDelay;
	@Getter @Setter
	private int waitingAndExistsPowerUpsNumber;
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	* Args powerUpNumberLimit, minPowerUpCreationDelay and maxPowerUpCreationDelay should be greater than or equal to zero.
	* @param powerUpNumberLimit is a number of power ups exists on the playing field at the same time.
	* @param minPowerUpCreationDelay is a min delay until power up created in milliseconds.
	* @param maxPowerUpCreationDelay is a max delay until power up created in milliseconds.
	 */
	public PowerUpManagerImpl(int powerUpNumberLimit, int minPowerUpCreationDelay, int maxPowerUpCreationDelay) {
		powerUps = new ArrayList<>();
		powerUpCreationCountdowns = new HashMap<>();
		
		this.powerUpNumberLimit = Math.max(powerUpNumberLimit, 0);
		this.minPowerUpCreationDelay = Math.max(minPowerUpCreationDelay, 0);
		this.maxPowerUpCreationDelay = Math.max(maxPowerUpCreationDelay, 0);
	}
	
	/**
	 * One frame passed for all power ups waiting for creation.
	 */
	@Override
	public void countdownWaitingPowerUps() {
		for(var entry: powerUpCreationCountdowns.entrySet()) {
			for(Integer framesLeft: entry.getValue()) {
				--framesLeft;
			}
		}
	}
	
	/**
	 * Adds new countdown in frames until power up created only if power ups number limit do not reached.
	 * Map key is a type of {@link org.jeffersonairplane.model.PowerUpTypes} - particular power up type.
	 * Value is a list of countdowns represented in frames.
	 * Delay is a random int between minPowerUpCreationDelay and maxPowerUpCreationDelay.
	 * Do nothing if power ups limit reached.
	 */
	@Override
	public void runNewPowerUpCountdown() {
		if(waitingAndExistsPowerUpsNumber < powerUpNumberLimit) {
			PowerUpTypes powerUpType = getRandomPowerUpType();
			if(!powerUpCreationCountdowns.containsKey(powerUpType)) {
				powerUpCreationCountdowns.put(powerUpType, new ArrayList<Integer>());
			}
			Random rnd = new Random();
			int delay = rnd.nextInt(maxPowerUpCreationDelay - minPowerUpCreationDelay) + minPowerUpCreationDelay;
			powerUpCreationCountdowns.get(powerUpType).add(delay);
			++waitingAndExistsPowerUpsNumber;
		}
	}
	
	/**
	 * Chooses type randomly using it predefined weight.
	 * @return type of power up.
	 */
	public PowerUpTypes getRandomPowerUpType() {
		Random rnd = new Random();
		int percentage = rnd.nextInt(101);
		for(PowerUpTypes type: PowerUpTypes.values()) {
			if(type.getCreationChance() <= percentage) {
				return type;
			}
		}
		logger.log(Level.SEVERE, "No appropriate Power Up type found.");
		throw new IllegalStateException("No appropriate Power Up type found.");
	}

	/**
	 * Checks if any power up exists on particular point and returns it.
	 * @param point to find power up at.
	 * @return Optional<PowerUp> power up at arg point or empty Optional otherwise.
	 */
	@Override
	public Optional<PowerUp> getPowerUpByPoint(Coordinate point) {
		for(PowerUp power: powerUps) {
			if(power.getPoint().equals(point)) {
				return Optional.of(power);
			}
		}
		return Optional.empty();
	}

	/**
	 * Creates new {@link org.jeffersonairplane.model.PowerUp} instance in the game.
	 * @param type particular type of PowerUp to create.
	 * @param coordinate of created Power Up, should be on the playing field.
	 * @return true if power up successfully created and added to power up collection.
	 */
	public boolean createPowerUp(PowerUpTypes type, Coordinate coordinate) {
		if(type == null || coordinate == null) return false;
		PowerUp powerUp = switch (type) {
			case APPLE -> new Apple(coordinate);
			default -> null;
		};
		if(powerUp == null) return false;
		powerUps.add(powerUp);
		return true;
	}

	/**
	 * Creates every power up which waiting time has passed.
	 */
	@Override
	public void createPowerUps(Supplier<Coordinate> coordinateSupplier) {
		for(var entry: powerUpCreationCountdowns.entrySet()) {
			for(Integer framesLeft: entry.getValue()) {
				if(framesLeft < 1) {
					boolean created = createPowerUp(entry.getKey(), coordinateSupplier.get());
					entry.getValue().remove(framesLeft);
					if(!created) --waitingAndExistsPowerUpsNumber;
				}
			}
		}
	}

	/**
	 * Removes power up from game.
	 * @param powerUp to remove.
	 * @return true if removed.
	 */
	@Override
	public boolean removePowerUp(PowerUp powerUp) {
		if(powerUps.remove(powerUp)) {
			--waitingAndExistsPowerUpsNumber;
			return true;
		}
		return false;
	}
}