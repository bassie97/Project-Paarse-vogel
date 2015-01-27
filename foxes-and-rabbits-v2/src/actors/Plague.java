package actors;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import simulator.Randomizer;
import view.Field;
import view.Location;

public class Plague implements Actor {

	// Characteristics shared by all Plagues (class variables).
	// The age at which a plague can start to breed.
	private static final int SPLIT_AGE = 149;
	// The age to which a plague can live.
	private static final int MAX_CELL_AGE = 150;
	// The plagues field.
	private Field field;
	// The plagues position in the field.
	private Location location;
	// Whether the actor is alive or not.
	private boolean active;
	// set age for cell
	private int age;
	// A shared random number generator to control breeding.
	private static final Random rand = Randomizer.getRandom();

	/**
	 * Create a Plague. A Plague can be created as a new born (age zero and not
	 * hungry) or with a random age and food level.
	 * 
	 * @param randomAge
	 *            If true, the Plague will have random age and hunger level.
	 * @param field
	 *            The field currently occupied.
	 * @param location
	 *            The location within the field.
	 */
	public Plague(boolean randomAge, Field field, Location location) {
		active = true;
		this.field = field;
		setLocation(location);
		age = 0;
	}

	/**
	 * This is what the Plague does most of the time: it hunts for rabbits. In
	 * the process, it might breed, die of hunger, or die of old age.
	 * 
	 * @param field
	 *            The field currently occupied.
	 * @param newPlagues
	 *            A list to return newly born Plagues.
	 */
	public void act(List<Actor> newPlagues)

	{
		incrementAge();
		if (isActive()) {
			giveBirth(newPlagues);
			// Move towards a source of food if found.
			Location newLocation = findFood();
			if (newLocation == null) {
				// No food found - try to move to a free location.
				newLocation = getField().freeAdjacentLocation(getLocation());
			}
			// See if it was possible to move.
			if (newLocation != null) {
				setLocation(newLocation);
			}

		}
	}

	/**
	 * Increase the age. This could result in the plague's death.
	 */
	private void incrementAge() {
		age++;
		if (age > MAX_CELL_AGE) {
			setDestroyed();
		}
	}

	/**
	 * Indicate that the animal is no longer alive. It is removed from the
	 * field.
	 */
	public void setDestroyed() {
		active = false;
		if (location != null) {
			field.clear(location);
			location = null;
			field = null;
		}
	}

	/**
	 * Look for something to spread on at the adjacent to the current location.
	 * Only the first live rabbit is eaten.
	 * 
	 * @return Where food was found, or null if it wasn't.
	 */
	private Location findFood() {
		Field field = getField();
		List<Location> adjacent = field.adjacentLocations(getLocation());
		Iterator<Location> it = adjacent.iterator();
		while (it.hasNext()) {
			Location where = it.next();
			Object animal = field.getObjectAt(where);

			if (animal instanceof Rabbit) {
				Rabbit rabbit = (Rabbit) animal;
				if (rabbit.isActive()) {
					rabbit.setDead();

					// Remove the dead rabbit from the field.
					return where;
				}
			}
			if (animal instanceof Fox) {
				Fox fox = (Fox) animal;
				if (fox.isActive()) {
					fox.setDead();
					// Remove the dead rabbit from the field.
					return where;
				}
			}
			if (animal instanceof Bear) {
				Bear bear = (Bear) animal;
				if (bear.isActive()) {
					bear.setDead();
					// Remove the dead rabbit from the field.
					return where;
				}
			}
			if (animal instanceof Hunter) {
				Hunter hunter = (Hunter) animal;
				if (hunter.isActive()) {
					hunter.setGone();
					// Remove the dead rabbit from the field.
					return where;
				}
			}
		}
		return null;
	}

	/**
	 * Check whether or not this plague is to give birth at this step. New
	 * births will be made into free adjacent locations.
	 * 
	 * @param newplaguees
	 *            A list to return newly born plagues.
	 */
	private void giveBirth(List<Actor> newPlagues) {
		// New plaguees are born into adjacent locations.
		// Get a list of adjacent free locations.
		Field field = getField();
		List<Location> free = field.getFreeAdjacentLocations(getLocation());
		int births = breed();
		for (int b = 0; b < births && free.size() > 0; b++) {
			Location loc = free.remove(0);
			Plague cell = new Plague(false, field, loc);
			newPlagues.add(cell);
		}
	}

	/**
	 * Generate a number representing the number of births, if it can breed.
	 * 
	 * @return The number of births (may be zero).
	 */
	private int breed() {
		int births = 0;
		if (canBreed()) {
			births = 2;
		}
		return births;
	}

	/**
	 * Return the animal's field.
	 * 
	 * @return The animal's field.
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Return the actors location.
	 * 
	 * @return The actors location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Place the animal at the new location in the given field.
	 * 
	 * @param newLocation
	 *            The animal's new location.
	 */
	public void setLocation(Location newLocation) {
		if (location != null) {
			field.clear(location);
		}
		location = newLocation;
		field.place(this, newLocation);
	}

	/**
	 * Check whether the animal is alive or not.
	 * 
	 * @return true if the animal is still alive.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * A plague can breed if it has reached the breeding age.
	 */
	private boolean canBreed() {
		return age >= SPLIT_AGE;
	}
}
