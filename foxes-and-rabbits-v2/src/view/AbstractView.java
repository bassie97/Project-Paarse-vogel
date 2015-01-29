package view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import model.*;



public abstract class AbstractView extends JPanel {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Simulator simulator;
	
    public AbstractView(Simulator simulator){
        this.simulator = simulator;
        
        simulator.addView(this);
}
    
    public abstract void showStatus();

    public abstract void setColor(Class<?> animalClass, Color color);
    
    /**
     * Get the views
     * @return JPanel with the view
     */
    public abstract JPanel getField();

    public Component getFieldView() {
            // TODO Auto-generated method stub
            return null;
    }     
}
