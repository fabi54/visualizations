package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import java.awt.geom.Point2D;

public class BoundedLinearFunction extends AbstractFunctionBase {
    
	protected double a;
    protected double b;
    protected double lower;
    protected double upper;
    
    public BoundedLinearFunction(double a, double b, double lower, double upper) {
    	this.a = a;
    	this.b = b;
    	this.lower = lower;
    	this.upper = upper;
    }
    
    public BoundedLinearFunction(Point2D p, Point2D q) {
        this.a = (p.getY() - q.getY()) / (p.getX() - q.getX());
        this.b = q.getY() - (a * q.getX());
        this.lower = Math.min(p.getX(), q.getX());
        this.upper = Math.max(p.getX(), q.getX());
    }

	@Override
	public double getValue(double x) {
		return (x < lower) ? (a * lower + b) : (x > upper) ? (a * upper + b) : (a * x + b);
	}
    
    
}
