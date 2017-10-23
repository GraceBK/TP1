package creatures;

import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.Arrays;

import static java.lang.Math.*;


public abstract class AbstractCreature {

	public static final int DEFAULT_SIZE = 40;

	/** Position */
	protected double x, y;

	/** Speed in pixels */
	protected double speed;

	/** Direction in radians (0,2*pi) */
	protected double direction;

	/** Color of the creature */
	protected Color color;

	/** Reference to the environment */
	protected final Environment environment;

	/** Size of the creature in pixels */
	protected final int size = DEFAULT_SIZE;

	public AbstractCreature(Environment environment, double newX, double newY) {
		this.environment = environment;

		if (newX > environment.getWidth() / 2)
			newX -= size;

		if (newY > environment.getHeight() / 2)
			newY -= size;

		setX(newX);
		setY(newY);
	}

	/**
	 * The core method of a creature. It is suppose to modify its internal state
	 * (position, etc.) in a response to an environment. It can use methods
	 * defined in the {@link #environment}.
	 */
	public abstract void act();

	// ----------------------------------------------------------------------------
	// Getters and Setters
	// ----------------------------------------------------------------------------


	public double getX() {
		return x;
	}

	public void setX(double newX) {
		if (newX > environment.getWidth() / 2) {
			newX = -environment.getWidth() / 2;
		} else if (newX < -environment.getWidth() / 2) {
			newX = environment.getWidth() / 2;
		}

		this.x = newX;
	}

	public double getY() {
		return y;
	}

	public void setY(double newY) {
		if (newY > environment.getHeight() / 2) {
			newY = -environment.getHeight() / 2;
		} else if (newY < -environment.getHeight() / 2) {
			newY = environment.getHeight() / 2;
		}

		this.y = newY;
	}

	public double getSpeed() {
		return speed;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction % (PI * 2);
	}

	public Color getColor() {
		return color;
	}

	public int getSize() {
		return size;
	}

	/**
	 * Position of the creature.
	 * 
	 * @return position of the creature as a {@link Point}
	 */
	public Point2D position() {
		return new Point2D.Double(x, y);
	}

	// ----------------------------------------------------------------------------
	// Positioning methods
	// ----------------------------------------------------------------------------

	protected void move(double incX, double incY) {
		setX(x + incX);
		setY(y + incY);
	}

	protected void rotate(double angle) {
		this.direction += angle;
	}

	// ----------------------------------------------------------------------------
	// Methods for calculating the direction
	// ----------------------------------------------------------------------------

	/**
	 * Computes the direction between the given point {@code (x1, y1)} and the
	 * current position in respect to a given {@code axis}.
	 * 
	 * @return direction in radians between given point and current position in
	 *         respect to a given {@code axis}.
	 */
	public double directionFromAPoint(Point2D p, double axis) {
		double b = 0d;

		// use a inverse trigonometry to get the angle in an orthogonal triangle
		// formed by the points (x,y) and (x1,y1)
		if (x != p.getX()) {
			// if we are not in the same horizontal axis
			b = atan((y - p.getY()) / (x - p.getX()));
		} else if (y < p.getY()) {
			// below -pi/2
			b = -PI / 2;
		} else {
			// above +pi/2
			b = PI / 2;
		}

		// make a distinction between the case when the (x1, y1)
		// is right from the (x,y) or left
		if (x < p.getX()) {
			b += PI;
		}

		// align with the axis of the origin (x1,y1)
		b = b - axis;

		// make sure we always take the smaller angle
		// keeping the range between (-pi, pi)
		if (b >= PI)
			b = b - PI * 2;
		else if (b < -PI)
			b = b + PI * 2;

		return b % (PI * 2);
	}

	/**
	 * Distance between the current position and a given point {@code(x1, y1)}.
	 * 
	 * @return distance between the current position and a given point.
	 */
	public double distanceFromAPoint(Point2D p) {
		return position().distance(p);
	}

	// ----------------------------------------------------------------------------
	// Painting
	// ----------------------------------------------------------------------------

	/**
	 * Draws creature to a given canvas.
	 * 
	 * @param g2
	 *            canvas where to draw the creature.
	 */
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(x, y);
		// center the surrounding rectangle
		g2.translate(-size / 2, -size / 2);
		// center the arc
		// rotate towards the direction of our vector
		g2.rotate(-direction, size / 2, size / 2);

		// set the color
		g2.setColor(color);
		// we need to do PI - FOV since we want to mirror the arc
		g2.fillArc(0, 0, size, size, (int) toDegrees(-Environment.fieldOfView / 2),
				(int) toDegrees(Environment.fieldOfView));

	}

	/* EXO 3 */

	@Override
	public String toString() {
		Class cl = getClass().getSuperclass(); // getSuperclass permet de recuperer ce qui est dans AbstractCreature
		// getClass il recupere dans Stupid creature

		System.out.println("--> ATTRIBUTS");
		cl.getDeclaredFields(); // retourne un tableau
		cl.getTypeName();
		cl.getName();
		Field[] a = cl.getDeclaredFields();
		StringBuilder res = null;
		for (Field anA : a) {
			try {
				res.append(anA.getName()).append(anA.getDeclaredAnnotations());
			} catch (Exception ignored) {

			}
		}

		return super.toString();
	}
}
