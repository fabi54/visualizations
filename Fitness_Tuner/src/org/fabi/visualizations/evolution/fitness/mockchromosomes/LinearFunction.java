/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import java.awt.geom.Point2D;

/**
 *
 * @author janf
 */
public class LinearFunction extends AbstractFunctionBase {

    protected double a;
    protected double b;
    
    /**
     * y = ax + b
     */
    public LinearFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }
    
    public LinearFunction(double x1, double y1, double x2, double y2) {
        this(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
    }
    
    public LinearFunction(Point2D p, Point2D q) {
        this.a = (p.getY() - q.getY()) / (p.getX() - q.getX());
        this.b = q.getY() - (a * q.getX());
    }
    
    @Override
    public double getValue(double x) {
        return a * x + b;
    }
    
}
