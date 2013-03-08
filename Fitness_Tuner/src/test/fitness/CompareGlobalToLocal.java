package test.fitness;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosome;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeBoundsHolder;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.additional.AreaAdditionalDrawer;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.color.GradientColorModel;
import org.fabi.visualizations.scatter.color.SeriesDependentColorModel;
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

public class CompareGlobalToLocal {

	protected static String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Global_Vs_Local_Fitness\\";
	
	protected static DataSource data = new ConstantLinearConstantData();
	protected static double[][] optimum = new double[0][0]/*{{0.0,1.0}}*/;
	
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
	
	public static void main(String[] args) throws IOException {
		String s = "Run_";
		File dir;
		int i = 0;
		do {
			dir = new File(PATH + s + (i++));
		} while (dir.exists());
		dir.mkdir();
		String path = dir.getPath() + "\\";
		
		ModelSource[] models = getModels(data);
		final ScatterplotChromosomeFitnessFunction f = new ScatterplotChromosomeFitnessFunction(models, data);
//		final OldFitness fg = new OldFitness(models, data);
		final ScatterplotChromosomeFitnessFunctionSubtrSim fg = new ScatterplotChromosomeFitnessFunctionSubtrSim(models, data);
		final ScatterplotChromosomeBoundsHolder bounds = new ScatterplotChromosomeBoundsHolder(data.getInputDataVectors());
		ModelSource ms = new ModelSource() {
			
			@Override
			public int outputsNumber() {
				return 2;
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
				double[][] responses = new double[inputs.length][2];
				for (int i = 0; i < responses.length; i++) {
					double lower = (inputs[i][0] - bounds.bounds[org.fabi.visualizations.tools.math.Arrays.LOWER_BOUND][0])
							/ bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0];
					double length = (inputs[i][1] - inputs[i][0]) / (bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0] * Math.abs(1 - lower));
					if (length < 0) {
						responses[i][0] = Double.NEGATIVE_INFINITY;
					} else {
						ScatterplotChromosome c = new ScatterplotChromosome(f, new int[]{0}, new double[]{lower}, new double[]{length}, new double[]{0.0}, true, bounds);
						responses[i][0] = c.getFitness();
						ScatterplotChromosome c2 = new ScatterplotChromosome(fg, new int[]{0}, new double[]{lower}, new double[]{length}, new double[]{0.0}, true, bounds);
						responses[i][1] = c2.getFitness();
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
				res.add(new AttributeInfoBase("local", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
				res.add(new AttributeInfoBase("global", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
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
		DataSource ods = new DataSource() {
			
			@Override
			public int outputsNumber() {
				return 2;
			}
			
			@Override
			public int inputsNumber() {
				return 2;
			}
			
			@Override
			public double[][] getOutputDataVectors() {
				double[][] outputs = new double[optimum.length][2];
				for (int i = 0; i < outputs.length; i++) {
					outputs[i][0] = 1;
					outputs[i][1] = 1;
				}
				return outputs;
			}
			
			@Override
			public String getName() {
				return "optimum";
			}
			
			@Override
			public double[][] getInputDataVectors() {
				return optimum;
			}
		};
		
		ScatterplotVisualization vis = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{ods}, new ModelSource[]{ms}, md));
		vis.setxAxisRangeLower(-3);
		vis.setxAxisRangeUpper(3);
		vis.setyAxisRangeLower(-3);
		vis.setyAxisRangeUpper(3);
		vis.setxAxisAttributeIndex(0);
		vis.setyAxisAttributeIndex(1);
		vis.setOutputPrecision(500);
		vis.setModelsVisible(new boolean[][]{{true, false}});
		
		double[][][] outputs = LocalFitness.getResponses(vis, new ModelSource[]{ms});
		double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
		double min2 = Double.POSITIVE_INFINITY, max2 = Double.NEGATIVE_INFINITY;
		for (int i2 = 0; i2 < outputs[0].length; i2++) {
			if (!Double.isInfinite(outputs[0][i2][0])) {
				min = Math.min(min, outputs[0][i2][0]);
				max = Math.max(max, outputs[0][i2][0]);
				min2 = Math.min(min2, outputs[0][i2][1]);
				max2 = Math.max(max2, outputs[0][i2][1]);
			}
		}
		SeriesDependentColorModel cmod = new SeriesDependentColorModel();
		ConstantColorModel ccm = new ConstantColorModel();
		GradientColorModel cm = new GradientColorModel();
		cm.setMin(new double[]{min, min2});
		cm.setRange(new double[]{max - min, max2 - min2});
		cm.setComponents(new int[][][]{{{255, 255, 255, 255},{0, 0, 0, 255}}, {{255, 255, 255, 255},{0, 0, 0, 255}}});
		ccm.setColor(Color.RED);
		cmod.setDataModels(new ColorModel[]{ccm});
		cmod.setModelModels(new ColorModel[]{cm});
		vis.setColorModel(cmod);
		 
		double[][][] responses = LocalFitness.getResponses(vis, new ModelSource[]{ms});
		int maxR = 0;
		for (int i2 = 1; i2 < responses[0].length; i2++) {
			if (responses[0][i2][0] > responses[0][maxR][0]) {
				maxR = i2;
			}
		}
		double[] visBounds = LocalFitness.getModelInputs(vis)[maxR];
		
		ScatterplotVisualization vis2 = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{data}, models));
		ConstantColorModel cm2 = new ConstantColorModel();
		cm2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.25f));
		vis2.setColorModel(cm2);
		vis2.getVisualizationAsComponent();
		double[][] abounds = vis2.getActualAxesBounds();
		AreaAdditionalDrawer ad = new AreaAdditionalDrawer(visBounds[0], visBounds[1], abounds[1][0], abounds[1][1]);
		ad.setColor(Color.BLACK);
		vis2.setAdditionalDrawers(new AdditionalDrawer[]{ad});
		
		BufferedImage img = vis.getVisualizationAsImage(800, 600);
		ImageIO.write(img, "png",new File(path + "subtrsim_5000.png"));
		img = vis2.getVisualizationAsImage(800, 600);
		ImageIO.write(img, "png",new File(path + "subtrsim_5000_area.png"));
		
		maxR = 0;
		for (int i2 = 1; i2 < responses[0].length; i2++) {
			if (responses[0][i2][1] > responses[0][maxR][1]) {
				maxR = i2;
			}
		}
		visBounds = LocalFitness.getModelInputs(vis)[maxR];
		ad = new AreaAdditionalDrawer(visBounds[0], visBounds[1], abounds[1][0], abounds[1][1]);
		ad.setColor(Color.BLACK);
		vis2.setAdditionalDrawers(new AdditionalDrawer[]{ad});
		img = vis2.getVisualizationAsImage(800, 600);
		ImageIO.write(img, "png",new File(path + "def_area.png"));
		
		vis.setModelsVisible(new boolean[][]{{false, true}});
		img = vis.getVisualizationAsImage(800, 600);
		ImageIO.write(img, "png",new File(path + "def.png"));
	}
}
