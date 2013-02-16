package test.artificialdata;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.color.RegressionRainbowColorModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

public class Data11D implements DataSource {

	double[][] inputs;
	double[][] outputs;
	
	@Override
	public double[][] getInputDataVectors() {
		if (inputs == null) {
			inputs = new double[10000][1];
			double d = 0.0;
			for (int i = 0; i < 10000; i++) {
				inputs[i][0] = d++ / 1000.0;
			}
		}
		return inputs;
	}

	@Override
	public double[][] getOutputDataVectors() {
		if (outputs == null) {
			getInputDataVectors();
			outputs = new double[10000][1];
			for (int i = 0; i < 10000; i++) {
				outputs[i][0] = inputs[i][0] /* * inputs[i][2] * inputs[i][2]*/;
			}
		}
		return outputs;
	}

	@Override
	public int inputsNumber() {
		return 1;
	}

	@Override
	public int outputsNumber() {
		return 1;
	}

	@Override
	public String getName() {
		return "Simple artificial data";
	}

	public static void main(String[] args) {
		ScatterplotVisualization visualization = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{new Data11D()}));
		visualization.setColorModel(new RegressionRainbowColorModel(0.0, 1.0));
		visualization.setDotSizeModel(new MinkowskiDistanceDotSizeModel());
		visualization.setBackground(Color.BLACK);
		visualization.setyAxisAttributeIndex(ScatterplotVisualization.OUTPUT_AXIS);
		//visualization.setDotSizeModel(TestEvolution2.getDotSizeModel(visualization));
		ConstantColorModel model = new ConstantColorModel();
		model.setColor(new Color(1.0f, 1.0f, 1.0f, 0.25f));
		visualization.setColorModel(model);
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.add(visualization.getVisualizationAsComponent(), BorderLayout.CENTER);
		f.add(visualization.getControls(), BorderLayout.WEST);
		f.setSize(1024, 768);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
