package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import model.Simulator;

public class MenuController extends AbstractController implements ActionListener{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuController(Simulator simulator) {
		super(simulator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenuBar getMenu() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu options = new JMenu("Options");
		menubar.add(options);
		return menubar;
	} 
	
}
