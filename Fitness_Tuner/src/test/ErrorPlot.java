package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.fabi.visualizations.Global;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.fitness.VisualizationFitnessFunction;
import org.fabi.visualizations.evolution.fitness.VisualizationFitnessFunction2;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.AbstractFunction;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.ConstantFunction;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.LinearFunction;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.MockVisualizationChromosome;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.GradientColorModel;
import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.MetadataBase;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;

public class ErrorPlot {
	public static void main(String[] args) throws InterruptedException {
		Global.getInstance().init();
		
		ScatterplotVisualization visualization = new ScatterplotVisualization(new ScatterplotSource() {
			
			@Override	public int getOutputsNumber() {return 1;}
			@Override	public int getInputsNumber() {return 3;}
			@Override	public int getModelSourceCount() {return 1;}
			@Override	public int getDataSourceCount() {return 0;}
			@Override	public DataSource getDataSource(int index) {return null;}
			
			@Override
			public ModelSource getModelSource(int index) {
				return new ModelSource() {
					
					@Override public int outputsNumber() { return 1; }
					@Override public int inputsNumber() { return 3; }
					@Override public String getName() { return ""; }
					
					@Override
					public double[][] getModelResponses(double[][] inputs) {
						System.out.println("Evaluating ...");
						double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
				        FitnessFunction fitness = new VisualizationFitnessFunction();
				        AbstractFunction[] models = new AbstractFunction[10];
				        double[][] res = new double[inputs.length][1];
				        for (int i = 0; i < res.length; i++) {
				        	for (int j = 0; j < models.length /*- 1*/; j++) {
				        		models[j] = new LinearFunction(inputs[i][0], j * inputs[i][1]);
				        	}
				        	//models[models.length - 1] = new ConstantFunction(Double.NaN);
				        	MockVisualizationChromosome c = new MockVisualizationChromosome(0.0, inputs[0][2], models);
				        	res[i][0] = fitness.getFitness(c);
//				        	System.out.println("Evaluating " + i + " of " + res.length + ": " + res[i][0] + ".");
				        	min = Math.min(min, res[i][0]);
				        	max = Math.max(max, res[i][0]);
				        }
//				        System.out.println("min " + min + " max " + max);
				        return res;
					}
				};
			}
			
			@Override
			public Metadata getMetadata() {
				return new MetadataBase() {
					
					@Override public void setTransformation(ReversibleTransformation transformation) {}
					
					@Override
					public List<AttributeInfo> getOutputAttributeInfo() {
						List<AttributeInfo> res = new ArrayList<AttributeInfo>(2);
						res.add(new AttributeInfoBase("fitness", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
						return res;
					}
					
					@Override
					public List<AttributeInfo> getInputAttributeInfo() {
						List<AttributeInfo> res = new ArrayList<AttributeInfo>(2);
						res.add(new AttributeInfoBase("sklon", AttributeInfo.AttributeRole.STANDARD_INPUT));
						res.add(new AttributeInfoBase("shoda", AttributeInfo.AttributeRole.STANDARD_INPUT));
						res.add(new AttributeInfoBase("velikost", AttributeInfo.AttributeRole.STANDARD_INPUT));
						return res;
					}
				};
			}
		});
		visualization.setProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER, 0.0);
		visualization.setProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER, 10.0);
		visualization.setProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER, 0.0);
		visualization.setProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_UPPER, 10.0);
		visualization.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, new GradientColorModel());
		visualization.setProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, new double[]{1.0, 0.1, 0.0});
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		final JLabel l = new JLabel("velikost " + Double.toString(0.0)); 
		f.add(l, BorderLayout.NORTH);
        f.setSize(600, 600);
        f.setVisible(true);
//        JButton b = new JButton(">>");
//        f.add(b, BorderLayout.SOUTH);
		f.add(visualization.getVisualizationAsComponent(), BorderLayout.CENTER);
        final ScatterplotVisualization v = visualization;
//        b.addActionListener(new ActionListener() {
//			double i = 0.0;
//        	
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				i++;
//	    		v.setProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, new double[]{1.0, 0.1, i});
//	    		l.setText(Double.toString(i));
//			}
//		});
        for (double i = 1.0; i < 10.0; i++) {
        	Thread.sleep(5000);
    		l.setText("velikost " + Double.toString(i) + " ... processing");
    		v.setProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, new double[]{1.0, 0.1, i});
    		l.setText("velikost " + Double.toString(i));
        }
        System.out.println("Finished");
	}
}
