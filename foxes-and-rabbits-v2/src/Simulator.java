import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple predator-prey simulator, based on a rectangular field containing
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Simulator implements Runnable {
	// Constants representing configuration information for the simulation.
	// The default width for the grid.
	private static final int DEFAULT_WIDTH = 120;
	// The default depth of the grid.
	private static final int DEFAULT_DEPTH = 80;
	// The probability that a fox will be created in any given grid position.
	private static final double FOX_CREATION_PROBABILITY = 0.05;
	// The probability that a rabbit will be created in any given grid position.
	private static final double RABBIT_CREATION_PROBABILITY = 0.08;
	// The probability that a bear will be created in any given grid position.
	private static final double BEAR_CREATION_PROBABILITY = 0.005;
	// The probability that a hunter will be created in any given grid position.
	private static final double HUNTER_CREATION_PROBABILITY = 0.005;

	// List of animals in the field.
	private List<Actor> animals;
	// The current state of the field.
	private Field field;
	// The current step of the simulation.
	private int step;
	// A graphical view of the simulation.
	private SimulatorView view;
	// Thread class variable
	private static Thread thread;
	// Boolean triggers to stop or suspend (pause) the thread
	private volatile boolean isRunning = true, suspend = false;
	// Lock used to sync the thread, do NOT tamper with!!
	private final Object lock = new Object();
	// Counter used in setting max number of steps
	private int count;
	// Boolean trigger for number of steps limit
	private boolean numberStepsSet = false;
	// Number of steps before simulation stops
	private int numberOfSteps = 100;
	// Boolean trigger for adding hunter to simulation
	private boolean addHunter;
	// Delay in MS between steps
	private int stepDelay = 50;
	// Counter used to count how many times simulation speed has been changed
	private int timesPressed = 0;

	/**
	 * Construct a simulation field with default size.
	 */
	public Simulator() {
		this(DEFAULT_DEPTH, DEFAULT_WIDTH);
	}

	/**
	 * Create a simulation field with the given size.
	 * 
	 * @param depth
	 *            Depth of the field. Must be greater than zero.
	 * @param width
	 *            Width of the field. Must be greater than zero.
	 */
	public Simulator(int depth, int width) {
		if (width <= 0 || depth <= 0) {
			System.out.println("The dimensions must be greater than zero.");
			System.out.println("Using default values.");
			depth = DEFAULT_DEPTH;
			width = DEFAULT_WIDTH;
		}

		animals = new ArrayList<Actor>();
		field = new Field(depth, width);

		// Create a view of the state of each location in the field.
		view = new SimulatorView(depth, width);
		view.setColor(Rabbit.class, Color.orange);
		view.setColor(Fox.class, Color.blue);
		view.setColor(Bear.class, Color.red);
		view.setColor(Hunter.class, Color.MAGENTA);

		view.start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startThread();
				numberStepsSet = false;
			}
		});

		view.stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				suspend = true;
			}
		});

		view.eenStap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				suspend = true;
				simulateOneStep();
			}
		});

		view.honderdStappen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulateHundredSteps();
			}
		});

		view.jachtSeizoen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addHunter = true;
			}
		});

		view.simulationSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulationSpeed();
			}
		});

		// Setup a valid starting point.
		reset();
	}

	/**
	 * Starts or resumes the thread.
	 */
	public void startThread() {
		if (!thread.isAlive()) {
			thread.start();
		} else {
			resume();
		}
	}

	/**
	 * Suspends the simulation after the given number of steps.
	 * 
	 * @param limit
	 */
	public void stepLimit(int limit) {
		if (numberStepsSet) {
			count++;
			if (count >= limit) {
				suspend = true;
				count = 0;
			}
		}
	}

	/**
	 * Thread run method
	 */
	public void run() {
		while (isRunning) {
			while (!suspend) {
				stepLimit(numberOfSteps);
				simulateOneStep();
				threadSleep(stepDelay);

			}
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException s) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}

	/**
	 * Puts the thread to sleep for given number of ms.
	 * 
	 * @param delay
	 */
	public void threadSleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException s) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Resumes the thread when paused
	 */
	public void resume() {
		suspend = false;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	/**
	 * Stops the running thread !NOT used!
	 */
	public void stop() {
		isRunning = false;

	}

	/**
	 * Runs the simulation 100 steps.
	 */
	public void simulateHundredSteps() {
		startThread();
		numberStepsSet = true;
	}

	/**
	 * Run the simulation from its current state for a single step. Iterate over
	 * the whole field updating the state of each fox and rabbit and bear.
	 */
	public void simulateOneStep() {
		step++;

		if (addHunter == true) {
			startHuntingSeason();
			addHunter = false;
			view.repaint();
		}

		List<Actor> newAnimals = new ArrayList<Actor>();
		// Let all rabbits act.
		Iterator<Actor> it = animals.iterator();

		for (animals.iterator(); it.hasNext();) {
			Actor animal = it.next();
			animal.act(newAnimals);
			if (!animal.isActive()) {
				it.remove();
			}

		}

		// Add the newly born foxes and rabbits to the main lists.
		animals.addAll(newAnimals);

		view.showStatus(step, field);
	}

	/**
	 * Reset the simulation to a starting position.
	 */
	public void reset() {
		step = 0;
		animals.clear();
		populate();

		// Show the starting state in the view.
		view.showStatus(step, field);
	}

	/**
	 * Randomly populate the field with foxes and rabbits.
	 */
	private void populate() {
		Random rand = Randomizer.getRandom();
		field.clear();
		for (int row = 0; row < field.getDepth(); row++) {
			for (int col = 0; col < field.getWidth(); col++) {
				if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
					Location location = new Location(row, col);
					Fox fox = new Fox(true, field, location);
					animals.add(fox);
				} else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
					Location location = new Location(row, col);
					Rabbit rabbit = new Rabbit(true, field, location);
					animals.add(rabbit);
				} else if (rand.nextDouble() <= BEAR_CREATION_PROBABILITY) {
					Location location = new Location(row, col);
					Bear bear = new Bear(true, field, location);
					animals.add(bear);
				}

				// else leave the location empty.
			}
		}
	}

	/**
	 * Adds hunters to the simulation.
	 */
	public void startHuntingSeason() {
		Random rand = Randomizer.getRandom();
		for (int row = 0; row < field.getDepth(); row++) {
			for (int col = 0; col < field.getWidth(); col++)
				if (rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
					Location location = new Location(row, col);
					Hunter hunter = new Hunter(true, field, location);
					animals.add(hunter);
				}

		}
	}

	/**
	 * Changes the speed of the simulation
	 */
	public void simulationSpeed() {
		if (timesPressed == 4) {
			timesPressed = 0;
		}
		if (timesPressed == 0) {
			stepDelay = 25;
			timesPressed++;
		} else if (timesPressed == 1) {
			stepDelay = 0;
			timesPressed++;
		} else if (timesPressed == 2) {
			stepDelay = 100;
			timesPressed++;
		} else if (timesPressed == 3) {
			stepDelay = 50;
			timesPressed++;
		}
	}

	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		thread = new Thread(new Simulator());
	}

}
