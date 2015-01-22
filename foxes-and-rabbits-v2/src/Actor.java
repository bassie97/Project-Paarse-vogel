import java.util.List;
/**
 * Write a description of class Actor here.
 * 
 * @author () 
 * @version (a version number or a date)
 */
public interface Actor
{
    abstract void act(List<Animal> newAnimals);
    
    abstract boolean isActive();
}

