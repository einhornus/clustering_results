package data.vertex;

import java.util.ArrayList;
import java.util.Arrays;

public class Vertex2D extends Vertex {
	public Vertex2D(double x, double y) {
		super(new ArrayList<Double>(Arrays.asList( x, y )));
	}
	
	public static double getDistance(Vertex2D firstVertex, Vertex2D secondVertex) {
		double deltaX = firstVertex.getCoordinates(0) - secondVertex.getCoordinates(0);
		double deltaY = firstVertex.getCoordinates(1) - secondVertex.getCoordinates(1);
		double distance = Math
							.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		return distance;
	}
}

--------------------

package cab.springdemo08;

public class Triangle {

	private Point pointA;
	private Point pointB;
	private Point pointC;

	public Point getPointA() {
		return pointA;
	}

	public void setPointA(Point pointA) {
		this.pointA = pointA;
	}

	public Point getPointB() {
		return pointB;
	}

	public void setPointB(Point pointB) {
		this.pointB = pointB;
	}

	public Point getPointC() {
		return pointC;
	}

	public void setPointC(Point pointC) {
		this.pointC = pointC;
	}

	public void draw() {
		System.out.println("Point A: (" + getPointA().getX() + ", " + getPointA().getY() + ")");
		System.out.println("Point B: (" + getPointB().getX() + ", " + getPointB().getY() + ")");
		System.out.println("Point C: (" + getPointC().getX() + ", " + getPointC().getY() + ")");
	}

}

--------------------

package linearEquations;

import storageObjects.Point;

public class standard
{
	private int d,e;
	private double f;
	
	public standard()
	{
		d=1;
		e=1;
		f=1;
	}
	/*
	 * d*y + e*x = f
	 */
	public standard(int dx, int ex, double fx)
	{
		d=dx;
		e=ex;
		f=fx;
	}
	
	public int getD()
	{
		return d;
	}
	
	public int getE()
	{
		return e;
	}
	
	public double getf()
	{
		return f;
	}
	
	public slopeIntercept toSlopeIntercept()
	{
		double b = -e/d;
		double c = f/d;
		slopeIntercept formated = new slopeIntercept(b,c);
		return formated;
	}
	
	public pointSlope toPointSlope(Point userPoint)
	{
		slopeIntercept transFormated = this.toSlopeIntercept();
		pointSlope formated = transFormated.toPointSlope(userPoint);
		return formated;
	}
}

--------------------

