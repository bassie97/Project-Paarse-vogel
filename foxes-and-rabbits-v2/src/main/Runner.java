package main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import view.AbstractView;
import view.SimulatorView;
import controller.*;
import model.*;

public class Runner extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// main method
	public static void main(String[] args) {
		new Runner();
	}

	private Simulator simulator;
	private AbstractController buttonController;
	private AbstractController menuController;
	private AbstractView simulatorview;

	public Runner() {

		simulator = new Simulator();
		// Create a view of the state of each location in the field.
		simulatorview = new SimulatorView(simulator);
		simulatorview.setColor(Rabbit.class, Color.orange);
		simulatorview.setColor(Fox.class, Color.blue);
		simulatorview.setColor(Bear.class, Color.red);
		simulatorview.setColor(Hunter.class, Color.MAGENTA);
		simulatorview.setColor(Plague.class, Color.green);
		buttonController = new ButtonController(simulator);
		menuController = new MenuController(simulator);

		// frame settings
		setTitle("Foxes and Rabbits");
		setSize(800, 800);
		setLocation(1, 1);

		JPanel container = (JPanel) getContentPane();
		container.setLayout(new BorderLayout());
		container.setBorder(new EmptyBorder(10, 10, 10, 10));

		setJMenuBar(menuController.getMenu());

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
		leftPanel.add(buttonController.getPanel(), BorderLayout.NORTH);

		// container.add(rightPanel, BorderLayout.EAST);
		container.add(simulatorview.getField(), BorderLayout.CENTER);
		container.add(leftPanel, BorderLayout.WEST);
		// container.add(VERSIELABEL, BorderLayout.SOUTH);
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);

	}
}
