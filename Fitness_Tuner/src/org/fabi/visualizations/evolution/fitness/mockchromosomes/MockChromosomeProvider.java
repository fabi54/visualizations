/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fabi.visualizations.Global;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;

/**
 *
 * @author janf
 */
public class MockChromosomeProvider {
    
    public static MockVisualizationChromosome BASE_LINEAR;
    public static MockVisualizationChromosome BASE_LINEAR_WITH_CONSTANT_PROLONGATION;
    public static MockVisualizationChromosome BASE_LINEAR_SMALL;
    public static MockVisualizationChromosome BASE_LINEAR_UNSIMILAR;
    public static MockVisualizationChromosome FLAT_LINEAR;
    public static MockVisualizationChromosome FLAT_LINEAR_SMALL;
    public static MockVisualizationChromosome FLAT_LINEAR_UNSIMILAR;
    public static MockVisualizationChromosome VERY_FLAT_LINEAR;
    public static MockVisualizationChromosome STEEP_LINEAR_UNSIMILAR;
    public static MockVisualizationChromosome BASE_CONSTANT;
    public static MockVisualizationChromosome FLAT_HANDFAN;
    public static MockVisualizationChromosome FLAT_BUTTERFLY;
    public static MockVisualizationChromosome BASE_BUTTERFLY;
    public static MockVisualizationChromosome BASE_LINEAR_PARALLEL_OUTLIER;
    public static MockVisualizationChromosome VERY_STEEP_LINEAR_VERY_UNSIMILAR;
    public static MockVisualizationChromosome BASE_LINEAR_CONSTANT_OUTLIER;
    public static MockVisualizationChromosome BASE_LINEAR_NAN_OUTLIER;
    public static MockVisualizationChromosome BASE_LINEAR_STRIKETHRU_OUTLIER;
    public static MockVisualizationChromosome BASE_LINEAR_VERY_UNSIMILAR;
    public static MockVisualizationChromosome BASE_SINE;
    public static MockVisualizationChromosome BASE_SINE_WITH_CONSTANT_PROLONGATION;
    public static MockVisualizationChromosome DOUBLE_BASE_SINE;
    public static MockVisualizationChromosome DOUBLE_BASE_SINE_SHORT;
    public static MockVisualizationChromosome DOUBLE_BASE_SINE_SHORT_UNSIMILAR;
    public static MockVisualizationChromosome STEEP_SINE;
    
    static {
        AbstractFunction[] models = new AbstractFunction[4];
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(1.0, -0.015 + (i * .01));
        }
        BASE_LINEAR = new MockVisualizationChromosome(0.0, 1.0, models);
        FLAT_LINEAR_SMALL = new MockVisualizationChromosome(0.0, 0.5, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(2.0, -0.015 + (i * .01));
        }
        BASE_LINEAR_SMALL = new MockVisualizationChromosome(0.0, 0.5, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(0.5, -0.015 + (i * .01));
        }
        FLAT_LINEAR = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new ConstantFunction(((double) i) / 100.0);
        }
        BASE_CONSTANT = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(1.27, -0.15 + (i * 0.1));
        }
        BASE_LINEAR_UNSIMILAR = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(10.0, -0.15 + (i * 0.1));
        }
        STEEP_LINEAR_UNSIMILAR = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(0.7, -0.15 + (i * 0.1));
        }
        FLAT_LINEAR_UNSIMILAR = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(i * (0.5 / 3.0), 0.0);
        }
        FLAT_HANDFAN = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(0.0, 0.5 - (((double) 0.5 * i) / 3.0), 1.0, 0.0 + (((double) 0.5 * i) / 3.0));
        }
        FLAT_BUTTERFLY = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(0.0, 1.0 - (((double) 1.0 * i) / 3.0), 1.0, 0.0 + (((double) 1.0 * i) / 3.0));
        }
        BASE_BUTTERFLY = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length - 1; i++) {
            models[i] = new LinearFunction(1.0, -0.015 + (i * .01));
        }
        models[models.length - 1] = new LinearFunction(1.0, 10.0);
        BASE_LINEAR_PARALLEL_OUTLIER = new MockVisualizationChromosome(0.0, 1.0, models);
        models[models.length - 1] = new ConstantFunction(10.0);
        BASE_LINEAR_CONSTANT_OUTLIER = new MockVisualizationChromosome(0.0, 1.0, models);
        models[models.length - 1] = new ConstantFunction(Double.NaN);
        BASE_LINEAR_NAN_OUTLIER = new MockVisualizationChromosome(0.0, 1.0, models);
        models[models.length - 1] = new LinearFunction(10.0, -5.0);
        BASE_LINEAR_STRIKETHRU_OUTLIER = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(0.025, -0.015 + (i * .01));
        }
        VERY_FLAT_LINEAR = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new LinearFunction(1.0, i * (10.0 / 3.0));
        }
        VERY_STEEP_LINEAR_VERY_UNSIMILAR = new MockVisualizationChromosome(0.0, 1.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new BoundedLinearFunction(1.0, -0.015 + (i * .01), 0.0, 1.0);
        }
        BASE_LINEAR_WITH_CONSTANT_PROLONGATION = new MockVisualizationChromosome(0.0, 2.0, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new SineFunction(1.0, 0.47, 0.5 + i * (.01));
        }
        BASE_SINE = new MockVisualizationChromosome(-0.25, 0.75, models);
        DOUBLE_BASE_SINE = new MockVisualizationChromosome(-0.25, 1.75, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new SineFunction(1.0, 0.97, 1 + i * (.01));
        }
        STEEP_SINE = new MockVisualizationChromosome(-0.25, 0.75, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new BoundedSineFunction(1.0, 0.47, 0.5 + i * (.01), -0.25, 0.75);
        }
        BASE_SINE_WITH_CONSTANT_PROLONGATION = new MockVisualizationChromosome(-0.25,1.75, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new SineFunction(0.5, 0.47, 0.5 + i * (.01));
        }
        DOUBLE_BASE_SINE_SHORT = new MockVisualizationChromosome(-0.125, 0.875, models);
        for (int i = 0; i < models.length; i++) {
            models[i] = new SineFunction(0.47, 0.62, 0.5 + i * (.1));
        }
        DOUBLE_BASE_SINE_SHORT_UNSIMILAR = new MockVisualizationChromosome(-0.125, 0.875, models);
    }
    
    protected MockChromosomeProvider() {}
    
    public static List<Rule> getRules() {
        List<Rule> rules = new LinkedList<Rule>();
/*0*/   rules.add(new Rule(BASE_LINEAR, BASE_CONSTANT, "ascending better than constant"));
/*1*/   rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_SMALL, "longer range better than smaller"));
/*2*/   rules.add(new Rule(BASE_LINEAR, FLAT_LINEAR_SMALL, "longer increase better than shorter although steeper"));
/*3*/   rules.add(new Rule(BASE_LINEAR_UNSIMILAR, FLAT_LINEAR_UNSIMILAR, "longer increase better than shorter algtough steeper also for unsimilar"));
/*4*/   rules.add(new Rule(BASE_LINEAR, FLAT_LINEAR, "steeper increase better than slow"));
/*5*/   rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_UNSIMILAR, "similar better than unsimilar"));
/*6*/   rules.add(new Rule(BASE_LINEAR_UNSIMILAR, FLAT_HANDFAN, "unsimilar ascending better than \"handfan\""));
/*7*/   rules.add(new Rule(FLAT_LINEAR_UNSIMILAR, FLAT_HANDFAN, "unsimilar ascending (even when a bit slower) better than 'handfan'"));
/*8*/   rules.add(new Rule(BASE_LINEAR_UNSIMILAR, FLAT_BUTTERFLY, "ascending better than 'butterfly'"));
/*9*/   rules.add(new Rule(BASE_LINEAR_UNSIMILAR, FLAT_BUTTERFLY, "unsimilar ascending better than 'butterfly'"));
/*10*/  rules.add(new Rule(FLAT_HANDFAN, FLAT_BUTTERFLY, "'handfan' better than 'butterfly'"));
/*11*/  rules.add(new Rule(FLAT_BUTTERFLY, BASE_BUTTERFLY, "flat 'butterfly' better than higher 'butterfly'"));
/*12*/  rules.add(new Rule(BASE_CONSTANT, FLAT_BUTTERFLY, "constant better than 'butterfly'"));
/*13*/  rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_PARALLEL_OUTLIER, "no outlier not worse than a parallel one", false));
/*14*/  rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_CONSTANT_OUTLIER, "no outlier not worse than a constant one", false));
/*15*/  rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_NAN_OUTLIER, "no outlier not worse than NaN one", false));
/*16*/  rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_STRIKETHRU_OUTLIER, "no outlier not worse than a 'strike-thru' one", false));
/*17*/  rules.add(new Rule(BASE_LINEAR_PARALLEL_OUTLIER, VERY_FLAT_LINEAR, "ascending with parallel outlier better than almost flat"));
/*18*/  rules.add(new Rule(BASE_LINEAR_CONSTANT_OUTLIER, VERY_FLAT_LINEAR, "ascending with constant outlier better than almost flat"));
/*19*/  rules.add(new Rule(BASE_LINEAR_NAN_OUTLIER, VERY_FLAT_LINEAR, "ascending with a NaN better than almost flat"));
/*20*/  rules.add(new Rule(BASE_LINEAR_STRIKETHRU_OUTLIER, VERY_FLAT_LINEAR, "ascending with 'strike-thru' outlier better than almost flat"));
/*21*/  rules.add(new Rule(BASE_LINEAR_PARALLEL_OUTLIER, VERY_STEEP_LINEAR_VERY_UNSIMILAR, "ascending with parallel outlier better than ascending unsimilar"));
/*22*/  rules.add(new Rule(BASE_LINEAR_CONSTANT_OUTLIER, VERY_STEEP_LINEAR_VERY_UNSIMILAR, "ascending with constant outlier better than ascending unsimilar"));
/*23*/  rules.add(new Rule(BASE_LINEAR_STRIKETHRU_OUTLIER, VERY_STEEP_LINEAR_VERY_UNSIMILAR, "ascending with 'strike-thru' outlier better than ascending unsimilar"));
/*24*/  rules.add(new Rule(BASE_LINEAR, BASE_LINEAR_WITH_CONSTANT_PROLONGATION, "shorter only ascending better than longer partially ascending, partially constant"));
/*25*/  rules.add(new Rule(BASE_SINE, BASE_LINEAR, "steeper nonlinear (sine peek) better than linear"));
/*26*/  rules.add(new Rule(BASE_SINE, BASE_SINE_WITH_CONSTANT_PROLONGATION, "shorter sine peak only better than longer partially sine peak, partially constant"));
/*27*/  rules.add(new Rule(DOUBLE_BASE_SINE, BASE_SINE, "two sine peaks better than one sine peak on half space"));
/*28*/  rules.add(new Rule(DOUBLE_BASE_SINE, DOUBLE_BASE_SINE_SHORT, "two sine peaks better than two sine peaks on half space"));
/*30*/  rules.add(new Rule(DOUBLE_BASE_SINE_SHORT, BASE_SINE, "two sine peaks better one sine peak on same space"));
/*31*/  rules.add(new Rule(DOUBLE_BASE_SINE_SHORT, DOUBLE_BASE_SINE_SHORT_UNSIMILAR, "similar better than unsimilar also for sine"));
/*32*/  rules.add(new Rule(BASE_SINE, DOUBLE_BASE_SINE_SHORT_UNSIMILAR, "one sine peak better than two unsimilar sine peaks on same space"));
/*33*/  rules.add(new Rule(STEEP_SINE, BASE_SINE, "sine with bigger amplitude better than with smaller"));
        return rules;
    }
    
    protected static int DIMENSION_CONST = 200;
    
    protected static JComponent getComponent(MockVisualizationChromosome c) {
    	VisualizationConfig cfg = (VisualizationConfig) c.getPhenotype();
    	ScatterplotVisualization vis = new ScatterplotVisualization(new DatasetGenerator(c, null), cfg);
    	double x = cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER) - cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER);
    	double y = cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_UPPER) - cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER);
    	vis.setProperty(ScatterplotVisualization.PROPERTY_LEGEND_VISIBLE, false);
    	JComponent comp = vis.getVisualizationAsComponent();
    	comp.setPreferredSize(new Dimension((int) (x * DIMENSION_CONST), (int) (y * DIMENSION_CONST)));
    	return comp;
    }
    
    protected static JPanel getPanel(Rule r) {
    	JFrame frame = new JFrame();
    	JPanel panel = new JPanel();
    	frame.add(panel);
    	panel.add(getComponent(r.better));
    	JLabel l = new JLabel(">");
    	l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
    	panel.add(l);
    	panel.add(getComponent(r.worse));
    	return panel;
    }
    
    public static String OUTPUT_PATH = "D:/Data/Dokumenty/Skola/FIT-MI/misc/fakegame/outputs/visrules";
    
    public static void main(String[] args) throws IOException {
		Global.getInstance().init();
		int i = 0;
		for (Rule r : getRules()) {
			JFrame frame = new JFrame();
			JPanel p = getPanel(r);
			frame.add(p);
	    	frame.pack();
	    	frame.addWindowListener(new WindowAdapter() {
	    		@Override
				public void windowClosed(WindowEvent e) {
					System.exit(0);
				}
			});
	    	frame.setVisible(true);
	    	BufferedImage img = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();
            p.paint(g);
            ImageIO.write(img, "png", new File(OUTPUT_PATH + "/rule_" + ++i + ".png"));
            frame.setVisible(false);
		}
		System.exit(0);
    }
}
