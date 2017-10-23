package creatures;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;


/**
 * Environment for the creatures together with the visualization facility.
 */
@SuppressWarnings("serial")
public class Environment extends JPanel implements Runnable {

	/**
	 * The field of view (FOV) is the extent of the observable world that is
	 * seen at any given moment by a creature in radians.
	 */
	static double fieldOfView = (Math.PI / 4);

	/**
	 * The distance indicating how far a creature see in front of itself in
	 * pixels.
	 */
	static double visionDistance = 20;
	
	/**
	 * Maximum speed in pixels / cycle
	 */
	public static final float MAX_SPEED = 5;//10f

	/** List of creatures in the simulation. */
	private List<AbstractCreature> creatures;

	/** Number of creates in the simulator. */
	private int creaturesCount = 5;

	/** The thread in which the simulation runs. */
	private Thread simulation;

	private volatile boolean paused = false;

	public Environment(int width, int height) {
		// set the size of the panel that is represented by this class
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);

		// create creatures
		creatures = createCreatures(creaturesCount);

		// creates the simulation thread
		simulation = new Thread(this);
		// start the simulation
		// this after a little bookkeeping, calls the run()
		simulation.start();

		/* EXO 3 */
		System.out.println(creatures.get(0).toString());

		// set listeners
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}
		});

	}

	private void handleMousePressed(MouseEvent e) {
		pause();
	}

	private synchronized void pause() {
		paused = !paused;
		notifyAll();
	}

	/**
	 * Represents the simulation.
	 * 
	 * This method is running in its own thread.
	 */
	public void run() {
		while (true) {
			for (AbstractCreature c : creatures) {
				c.act();
			}

			synchronized (this) {
				while (paused) {
					try {
						wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			repaint();
		}
	}

	/**
	 * Creates creatures
	 * 
	 * @param number
	 *            of creatures to be created
	 * @return a list of size {@code number} containing newly created creatures
	 */
	public List<AbstractCreature> createCreatures(int number) {
		List<AbstractCreature> list = new ArrayList<AbstractCreature>();
		Random rand = new Random();

		// view the color space as a cube and then iterate over it using a small
		// steps
		float creaturesCountCubeRoot = (float) pow(number, 1.0 / 3.0);
		float colorPhase = (float) (1.0 / creaturesCountCubeRoot);
		float r = 0.0f;
		float g = 0.0f;
		float b = 0.0f;

		for (int i = 0; i < number; i++) {
			// X coordinate
			int x = (int) (rand.nextDouble() * getWidth()) - getWidth() / 2;

			// Y coordinate
			int y = (int) (rand.nextDouble() * getHeight()) - getHeight() / 2;

			// direction
			double direction = (rand.nextDouble() * 2 * PI);

			int speed = (int) (rand.nextDouble() * MAX_SPEED);
			r += colorPhase;
			if (r > 1.0) {
				r -= 1.0f;
				g += colorPhase;
				if (g > 1.0) {
					g -= 1.0f;
					b += colorPhase;
					if (b > 1.0)
						b -= 1.0f;
				}
			}
			list.add(new StupidCreature(this, x, y, direction, speed, new Color(
					r, g, b)));
		}
		creaturesCount = number;
		return list;
	}

	
	/**
	 * List all creatures that are in the FOV of the given {@code observer}.
	 * 
	 * @param observer
	 *            the creature observing other creatures around
	 * @return a list of all creatures within FOV of the given {@code observer}.
	 */
	public List<AbstractCreature> creaturesAround(AbstractCreature observer) {
		ArrayList<AbstractCreature> list = new ArrayList<AbstractCreature>();

		for (AbstractCreature c : creatures) {

			if (c != observer) {
				Point2D o = observer.position();
				double dirAngle = c.directionFromAPoint(o, observer.getDirection());

				if (Math.abs(dirAngle) < (fieldOfView / 2)
						&& observer.distanceFromAPoint(c.position()) <= visionDistance) {
					list.add(c);
				}
			}
		}

		return list;
	}

	public synchronized void setCreatures(List<AbstractCreature> creatures) {
		pause();
		this.creatures = creatures;
		this.creaturesCount = creatures.size();
		pause();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// translate the origin of the coordinate system
		AffineTransform pT = g2.getTransform();
		g2.translate(getWidth() / 2, getHeight() / 2);

		// draw all creatures
		for (Object obj : creatures) {
			// save the transformation so the creature can do anything
			AffineTransform cT = g2.getTransform();

			AbstractCreature c = (AbstractCreature) obj;
			c.paint(g2);

			// revert the transformation
			g2.setTransform(cT);
		}

		// revert the transformation
		g2.setTransform(pT);
	}

}
