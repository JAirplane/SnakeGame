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
	private int waitingAndExistingPowerUpsNumber;
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	* Args powerUpNumberLimit, minPowerUpCreationDelay and maxPowerUpCreationDelay should be greater than or equal to zero.
	* @param powerUpNumberLimit is a number of power ups exists on the playing field at the same time.
	* @param minPowerUpCreationDelay is a min delay until power up created in frames.
	* @param maxPowerUpCreationDelay is a max delay until power up created in frames.
	 */
	public PowerUpManagerImpl(int powerUpNumberLimit, int minPowerUpCreationDelay, int maxPowerUpCreationDelay) {
		powerUps = new ArrayList<>();
		powerUpCreationCountdowns = new HashMap<>();
		
		this.powerUpNumberLimit = Math.max(powerUpNumberLimit, 0);
		this.minPowerUpCreationDelay = Math.max(minPowerUpCreationDelay, 0);
		this.maxPowerUpCreationDelay = Math.max(maxPowerUpCreationDelay, 0);
		logger.log( Level.FINE, "PowerUpManager created." );
	}
	
	/**
	 * One frame passed for all power ups waiting for creation.
	 */
	@Override
	public void countdownWaitingPowerUps() {
		logger.log( Level.FINE, "processing {0} power up countdowns in loop", powerUpCreationCountdowns.entrySet().size());
		for(var entry: powerUpCreationCountdowns.entrySet()) {
			var countdowns = entry.getValue();
			for(int i = 0; i < countdowns.size(); i++) {
				logger.log( Level.FINER, "processing[{0}]: {1}", new Object[]{ entry.getValue(), countdowns.get(i) } );
				countdowns.set(i, countdowns.get(i) - 1);
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
		if(waitingAndExistingPowerUpsNumber < powerUpNumberLimit) {
			PowerUpTypes powerUpType = getRandomPowerUpType();
			logger.log( Level.FINE, "Chosen type: {0}.", powerUpType);
			if(!powerUpCreationCountdowns.containsKey(powerUpType)) {
				powerUpCreationCountdowns.put(powerUpType, new ArrayList<>());
				logger.log( Level.FINE, "New collection for type {0} created.", powerUpType);
			}
			Random rnd = new Random();
			int delay = rnd.nextInt(maxPowerUpCreationDelay - minPowerUpCreationDelay) + minPowerUpCreationDelay;
			logger.log( Level.FINE, "Chosen delay: {0}.", delay);
			powerUpCreationCountdowns.get(powerUpType).add(delay);
			++waitingAndExistingPowerUpsNumber;
			logger.log( Level.FINE, "Overall power ups: {0}.", waitingAndExistingPowerUpsNumber);
		}
		else
		{
			logger.log( Level.FINE, "New countdown wasn't run. Current power ups - {0}. Limit - {1}.",
					new Object[] {waitingAndExistingPowerUpsNumber, powerUpNumberLimit});
		}
	}
	
	/**
	 * Chooses type randomly using it predefined weight.
	 * @return type of power up.
	 */
	public PowerUpTypes getRandomPowerUpType() {
		Random rnd = new Random();
		int chance = rnd.nextInt(101);
		logger.log( Level.FINE, "Chosen percentage: {0}.", chance);
		for(PowerUpTypes type: PowerUpTypes.values()) {
			if(chance >= type.getMinChance() && chance <= type.getMaxChance()) {
				logger.log( Level.FINE, "Chosen Power Up type: {0}.", type);
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
				logger.log( Level.FINE, "Chosen Power Up: {0}.", power);
				return Optional.of(power);
			}
		}
		logger.log( Level.FINE, "No Power Up found.");
		return Optional.empty();
	}

	/**
	 * Creates new {@link org.jeffersonairplane.model.PowerUp} instance in the game.
	 * @param type particular type of PowerUp to create.
	 * @param coordinate of created Power Up, should be on the playing field.
	 * @return true if power up successfully created and added to power up collection.
	 */
	@Override
	public boolean createPowerUp(PowerUpTypes type, Coordinate coordinate) {
		if(type == null || coordinate == null) {
			logger.log( Level.FINE, "Bad arguments.");
			return false;
		}
		PowerUp powerUp = switch (type) {
			case APPLE -> new Apple(coordinate);
			case TAILCUTTER -> new TailCutter(coordinate);
			default -> null;
		};
		if(powerUp == null) return false;
		logger.log( Level.FINE, "Power Up created: {0}.", powerUp);
		powerUps.add(powerUp);
		return true;
	}

	/**
	 * Creates every power up which waiting time has passed.
	 */
	@Override
	public void createPowerUps(Supplier<Coordinate> coordinateSupplier) {
		for(var entry: powerUpCreationCountdowns.entrySet()) {
			logger.log( Level.FINE, "Processing Power Up type: {0}, elements: {1}.", new Object[] {entry.getKey(), entry.getValue().size()});
			var countdowns = entry.getValue();
			for (var iterator = countdowns.listIterator(); iterator.hasNext(); ) {
				Integer framesLeft = iterator.next();
				if(framesLeft < 1) {
					boolean created = createPowerUp(entry.getKey(), coordinateSupplier.get());
					iterator.remove();
					if(!created) {
						--waitingAndExistingPowerUpsNumber;
						logger.log( Level.FINE, "New Power Up creation failed. Overall amount of power ups left: {0}.", waitingAndExistingPowerUpsNumber);
					}
					else {
						logger.log( Level.FINE, "New Power Up created. Type: {0}.", entry.getKey());
					}
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
			--waitingAndExistingPowerUpsNumber;
			logger.log(Level.FINE, "Power Up removed: {0}. Overall amount of power ups left: {1}.", new Object[] {powerUp, waitingAndExistingPowerUpsNumber});
			return true;
		}
		logger.log(Level.FINE, "Power Up wasn't removed: {0}.", powerUp);
		return false;
	}
	
	/**
     * <p>Resets state to initial.</p>
     */
	@Override
	public void resetState() {
		logger.log(Level.FINE, "Power up manager state reset.");
		powerUpCreationCountdowns.clear();
		powerUps.clear();
		waitingAndExistingPowerUpsNumber = 0;
	}
}