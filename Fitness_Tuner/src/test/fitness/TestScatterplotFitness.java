package test.fitness;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosome;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeBoundsHolder;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.color.GradientColorModel;
import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.MetadataBase;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;

import test.artificialdata.onedimensional.*;

import configuration.CfgTemplate;
import configuration.models.single.PolynomialModelConfig;

public class TestScatterplotFitness {

	protected static DataSource data = new ConstantLinearConstantData();
	
	protected static ModelSource[] getModels(DataSource data) {
		CfgTemplate[] templates = new CfgTemplate[10];
		for (int i = 0; i < templates.length; i++) {
			PolynomialModelConfig cfg = new PolynomialModelConfig();
			cfg.setMaxDegree(i + 3);
			templates[i] = cfg;
		}
		ModelSource[] ms = new ModelSource[10];
		for (int i = 0; i < ms.length; i++) {
			ms[i] = ModGenTools.learnRegressionModel(templates[i], data);
		}
		return ms;
	}
	
	public static void main(String[] args) {
		ModelSource[] models = getModels(data);
		final ScatterplotChromosomeFitnessFunction f = new ScatterplotChromosomeFitnessFunction(models, data);
		final ScatterplotChromosomeBoundsHolder bounds = new ScatterplotChromosomeBoundsHolder(data.getInputDataVectors());
		ModelSource ms = new ModelSource() {
			
			@Override
			public int outputsNumber() {
				return 1;
			}
			
			@Override
			public int inputsNumber() {
				return 2;
			}
			
			@Override
			public String getName() {
				return "Visualization fitness";
			}
			
			@Override
			public double[][] getModelResponses(double[][] inputs) {
				double[][] responses = new double[inputs.length][1];
				for (int i = 0; i < responses.length; i++) {
					double lower = (inputs[i][0] - bounds.bounds[org.fabi.visualizations.tools.math.Arrays.LOWER_BOUND][0])
							/ bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0];
					double length = (inputs[i][1] - inputs[i][0]) / (bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0] * Math.abs(1 - lower));
					if (length < 0) {
						responses[i][0] = Double.NEGATIVE_INFINITY;
					} else {
						ScatterplotChromosome c = new ScatterplotChromosome(f, new int[]{0}, new double[]{lower}, new double[]{length}, new double[]{0.0}, true, bounds);
						responses[i][0] = c.getFitness();
					}
				}
				return responses;
			}
		};
		Metadata md = new MetadataBase() {
			
			@Override
			public void setTransformation(ReversibleTransformation transformation) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public List<AttributeInfo> getOutputAttributeInfo() {
				List<AttributeInfo> res = new ArrayList<AttributeInfo>(1);
				res.add(new AttributeInfoBase("fitness", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
				return res;
			}
			
			@Override
			public List<AttributeInfo> getInputAttributeInfo() {
				List<AttributeInfo> res = new ArrayList<AttributeInfo>(2);
				res.add(new AttributeInfoBase("begin", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
				res.add(new AttributeInfoBase("end", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
				return res;
			}
		};
		ScatterplotVisualization vis = new ScatterplotVisualization(new ScatterplotSourceBase(new ModelSource[]{ms}, md));
		vis.setxAxisRangeLower(-3);
		vis.setxAxisRangeUpper(3);
		vis.setyAxisRangeLower(-3);
		vis.setyAxisRangeUpper(3);
		vis.setxAxisAttributeIndex(0);
		vis.setyAxisAttributeIndex(1);
		vis.setOutputPrecision(500);
		
		double[][][] outputs = LocalFitness.getResponses(vis, new ModelSource[]{ms});
		double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < outputs[0].length; i++) {
			if (!Double.isInfinite(outputs[0][i][0])) {
				min = Math.min(min, outputs[0][i][0]);
				max = Math.max(max, outputs[0][i][0]);
			}
		}
		GradientColorModel cm = new GradientColorModel();
		cm.setMin(new double[]{min});
		cm.setRange(new double[]{max - min});
		cm.setComponents(new int[][][]{{{255, 255, 255, 255},{0, 0, 0, 255}}});
		vis.setColorModel(cm);
		
		ScatterplotVisualization vis2 = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{data}));
		ConstantColorModel cm2 = new ConstantColorModel();
		cm2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));
		vis2.setColorModel(cm2);
		
		JFrame fr = new JFrame();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setSize(new Dimension(600, 600));
		fr.add(vis.getVisualizationAsComponent());
		fr.setVisible(true);
		
		JFrame fr2 = new JFrame();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr2.setSize(new Dimension(600, 600));
		fr2.add(vis2.getVisualizationAsComponent());
		fr2.setVisible(true);
	}
}
