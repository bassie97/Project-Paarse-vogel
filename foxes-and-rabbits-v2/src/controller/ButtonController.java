package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Simulator;

public class ButtonController extends AbstractController implements
		ActionListener {

	private static final long serialVersionUID = 1L;
	public JButton eenStap;
	public JButton honderdStappen;
	public JButton jachtSeizoen;
	public JButton start;
	public JButton stop;
	public JButton spreadPlague;
	public JButton simulationSpeed;

	public ButtonController(Simulator simulator) {
		super(simulator);
		// TODO Auto-generated constructor stub
	}

	public JPanel getPanel() {
		// Maakt een "toolbar" links aan.
		// Creates start simulation button
		JPanel basicMenu = new JPanel();
		basicMenu.setBorder(new EmptyBorder(6, 6, 6, 6));
		basicMenu.setLayout(new BoxLayout(basicMenu, BoxLayout.PAGE_AXIS));

		start = new JButton("Start doorlopende simulatie");
		//start.setHorizontalAlignment(SwingConstants.LEFT);
		start.addActionListener(this);
		basicMenu.add(start);
		// Creates stop simulation button
		stop = new JButton("Stop simulatie");
		//stop.setHorizontalAlignment(SwingConstants.LEFT);
		stop.addActionListener(this);
		basicMenu.add(stop);
		// Creates simulation speed button
		simulationSpeed = new JButton("Simulatie snelheid");
		//simulationSpeed.setHorizontalAlignment(SwingConstants.LEFT);
		simulationSpeed.addActionListener(this);
		basicMenu.add(simulationSpeed);
		// maakt button in de toolbar
		eenStap = new JButton("1 stap vooruit");
		eenStap.setHorizontalAlignment(SwingConstants.LEFT);
		eenStap.addActionListener(this);
		basicMenu.add(eenStap);
		// Creates hundersteps button
		honderdStappen = new JButton("100 stappen vooruit");
		//honderdStappen.setHorizontalAlignment(SwingConstants.LEFT);
		honderdStappen.addActionListener(this);
		basicMenu.add(honderdStappen);
		// maakt een flow layout panel. Dit is nodig voor de toolbar.
		// JPanel flow = new JPanel();
		// voegt de toolbar toe aan de flow layout
		// flow.add(basicMenu);
		// Creates hunting season button
		jachtSeizoen = new JButton("Open jacht seizoen");
		//jachtSeizoen.setHorizontalAlignment(SwingConstants.LEFT);
		jachtSeizoen.addActionListener(this);
		basicMenu.add(jachtSeizoen);
		// Creates spread plague button
		spreadPlague = new JButton("Spread the plague");
		//spreadPlague.setHorizontalAlignment(SwingConstants.LEFT);
		spreadPlague.addActionListener(this);
		basicMenu.add(spreadPlague);

		
		
		return basicMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == start) {
			simulator.startThread();
		}
		if (e.getSource() == stop) {
			simulator.suspend();
		}
		if (e.getSource() == simulationSpeed) {
			simulator.simulationSpeed();
		}
		if (e.getSource() == eenStap) {
			simulator.simulateSingleStep();
		}
		if (e.getSource() == honderdStappen) {
			simulator.simulateHundredSteps();
		}
		if (e.getSource() == jachtSeizoen) {
			simulator.startHuntingSeason();
		}
		if (e.getSource() == spreadPlague) {
			simulator.startPlague();
		}

		// Setup a valid starting point.
		// reset();
	}

	@Override
	public JMenuBar getMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}