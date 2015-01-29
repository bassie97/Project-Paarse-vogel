package controller;

import javax.swing.*;

import model.Simulator;

/**
 * The abstract controller that serves for all it's subclasses.
 * It initiates a brain that can be used by all the subclasses.
 *
 */
public abstract class AbstractController extends JPanel {
       
        //private static final long serialVersionUID = 6997920280234060101L;
       
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		// The brain used for all the controllers
        protected Simulator simulator;
       
        /**
         * The constructor for the abstractcontroller
         * @param brain The brain that is used for all the controllers
         */
        public AbstractController(Simulator simulator){
                this.simulator = simulator;
        }
       
        // Method to get the button/slider panels
        public abstract JPanel getPanel();
        	//JPanel  = new JPanel();
        
       
        // Method to get the Menu
        public abstract JMenuBar getMenu();
}

