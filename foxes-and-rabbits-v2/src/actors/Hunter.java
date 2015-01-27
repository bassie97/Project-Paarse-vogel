package actors;
import java.util.Iterator;
import java.util.List;

import view.Field;
import view.Location;

public class Hunter implements Actor {
	
    // Characteristics shared by all Hunteres (class variables).

    // The age to which a Hunter can live.
    private static final int MAX_AGE = 80;

    // Individual characteristics (instance fields).
    // The Hunter's age.
    private int age;
    
    // Characteristics shared by all hunters (static fields).
    
    // The hunters field.
    private Field field;
    // The hunters position in the field.
    private Location location;
    // Whether the actor is alive or not.
    private boolean active; 	
    private boolean alive;
    /**
     * Create a Hunter. A Hunter can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Hunter will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hunter(boolean randomAge, Field field, Location location)
    {
    	active = true;
    	this.field = field;
    	setLocation(location);
    }
    
    /**	
     * This is what the Hunter does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHunters A list to return newly born Hunters.
     */
    public void act(List<Actor> newHunters)
    {
    	incrementAge();
        if(isActive()) {   
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }

        }
    }

    /*
     * Increase the age. This could result in the Hunter's death.
     */
    private void incrementAge()
    {
        age++;
        if(age >= MAX_AGE) {
        	setGone();
        }
   }
   
    
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
           
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isActive()) { 
                    rabbit.setDead();
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isActive()) { 
                	fox.setDead();
                    // Remove the dead rabbit from the field.
                	return where;
                }
            }
            if(animal instanceof Bear) {
            	Bear bear = (Bear) animal;
                if(bear.isActive()) { 
                	bear.setDead();
                    // Remove the dead rabbit from the field.
                	return where;
                }
            }	
        }
        return null;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    public Field getField()
    {
        return field;
    }
    
    /**
     * Return the actors location.
     * @return The actors location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
        
        /**
         * Check whether the animal is alive or not.
         * @return true if the animal is still alive.
         */
        public boolean isActive()
        {
            return active;
        }
        
        /**
         * Indicate that the animal is no longer alive.
         * It is removed from the field.
         */
        void setGone()
        {
            active = false;
            if(location != null) {
                field.clear(location);
                location = null;
                field = null;
            }
        }	
        
 
}

