package actors;
import view.*;

import java.util.List;
/**
 * Write a description of class Actor here.
 * 
 * @author () 
 * @version (a version number or a date)
 */
public interface Actor 
{
	public void act(List<Actor> newAnimals);
	
	//public void act2(List<Actor> newHunters);
    
	//public boolean isActive();
    
	//public void setDead();
 
	//public Location getLocation();
    
	public void setLocation(Location newLocation);


	public boolean isActive();
    
	//public Field getField();
    
}

