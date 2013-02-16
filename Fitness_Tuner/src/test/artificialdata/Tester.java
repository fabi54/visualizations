package test.artificialdata;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.RegressionRainbowColorModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import configuration.CfgTemplate;
import configuration.models.single.ExpModelConfig;
import configuration.models.single.KNNModelConfig;
import configuration.models.single.NeuralNetModelConfig;
import configuration.models.single.PolynomialModelConfig;
import configuration.models.single.SVMModelConfig;

public class Tester {

	static CfgTemplate[] TEN_POLYNOMIALS;
	static CfgTemplate[] TEN_KNNS;
	static CfgTemplate[] TEN_NEURAL_NETS;
	static CfgTemplate[] TEN_EXPONENTIAL;
	static CfgTemplate[] TEN_SVM;
	
	static CfgTemplate[] COMBINATION_1_TEN;
	
	static CfgTemplate[][] getTemplates() {
		return new CfgTemplate[][]{
				TEN_POLYNOMIALS,
				TEN_KNNS,
				TEN_NEURAL_NETS,
				TEN_EXPONENTIAL,
				TEN_SVM,
				COMBINATION_1_TEN
		};
	}
	static String[] getNames() {
		return new String[]{
				"poly",
				"knn",
				"nn",
				"exp",
				"svm",
				"comb1"
		};
	}
	
	static {
		TEN_POLYNOMIALS = new CfgTemplate[10];
		for (int i = 0; i < TEN_POLYNOMIALS.length; i++) {
			PolynomialModelConfig cfg = new PolynomialModelConfig();
			cfg.setMaxDegree(i + 1);
			TEN_POLYNOMIALS[i] = cfg;
		}
		TEN_KNNS = new CfgTemplate[10];
		for (int i = 0; i < TEN_KNNS.length; i++) {
			KNNModelConfig cfg2 = new KNNModelConfig();
			cfg2.setNearestNeighbours(i + 1);
			TEN_KNNS[i] = cfg2;
		}
		TEN_NEURAL_NETS = new CfgTemplate[10];
		NeuralNetModelConfig cfg3 = new NeuralNetModelConfig();
		for (int i = 0; i < TEN_NEURAL_NETS.length; i++) {
			TEN_NEURAL_NETS[i] = cfg3;
		}
		TEN_EXPONENTIAL = new CfgTemplate[10];
		ExpModelConfig cfg4 = new ExpModelConfig();
		for (int i = 0; i < TEN_EXPONENTIAL.length; i++) {
			TEN_EXPONENTIAL[i] = cfg4;
		}
		TEN_SVM = new CfgTemplate[10];
		SVMModelConfig cfg5 = new SVMModelConfig();
		for (int i = 0; i < TEN_SVM.length; i++) {
			TEN_SVM[i] = cfg5;
		}
		COMBINATION_1_TEN = new CfgTemplate[]{TEN_POLYNOMIALS[0],TEN_POLYNOMIALS[1],
				TEN_KNNS[2], TEN_KNNS[3], TEN_NEURAL_NETS[4], TEN_NEURAL_NETS[5],
				TEN_EXPONENTIAL[6], TEN_EXPONENTIAL[7], TEN_SVM[8], TEN_SVM[9]};
	}
	
	public static void main(String[] args) {
		DataSource data = new Data1();
		ModelSource[] source = new ModelGroup(COMBINATION_1_TEN, data).getModels();
		ScatterplotVisualization visualization = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{data}, source));
		visualization.setOutputPrecision(50);
		visualization.setColorModel(new RegressionRainbowColorModel(0.0, 1.0));
		visualization.setDotSizeModel(new MinkowskiDistanceDotSizeModel());
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.add(visualization.getVisualizationAsComponent(), BorderLayout.CENTER);
		f.add(visualization.getControls(), BorderLayout.WEST);
		f.setSize(1024, 768);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
