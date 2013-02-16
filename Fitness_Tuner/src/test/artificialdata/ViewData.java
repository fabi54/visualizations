package test.artificialdata;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import test.artificialdata.onedimensional.*;
import test.evolution.TestEvolution;

public class ViewData {

	public static void main(String[] args) {
		ScatterplotVisualization visualization = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{new RandomArtificialData()}));
		visualization.setBackground(Color.BLACK);
		visualization.setDotSizeModel(TestEvolution.getDotSizeModel(visualization));
		visualization.setGridVisible(false);
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
