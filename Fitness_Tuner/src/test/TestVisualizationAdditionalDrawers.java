package test;

import java.awt.Color;

import javax.swing.JFrame;

import org.fabi.visualizations.evolution.fitness.mockchromosomes.SineFunction;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.additional.HistogramAdditionalDrawer;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

public class TestVisualizationAdditionalDrawers {

	public static void main(String[] args) {
		ScatterplotVisualization vis = new ScatterplotVisualization(new ScatterplotSourceBase(new ModelSource[]{new SineFunction(1, 1, 0)}));
		vis.setAdditionalDrawers(new AdditionalDrawer[]{new HistogramAdditionalDrawer(vis, "H", Color.RED, new double[][]
				{{0.15,0.5}, {0.25,0.2},
					{0.35,0.1}, {0.45,0.5}, {0.55,0.2},
					{0.65,-0.1}, {0.75,0.5}, {0.85,0.2}, {0.95,-0.2}}, 0.1, 0.2)});
		vis.setxAxisRangeLower(0.0);
		vis.setxAxisRangeUpper(1.0);
		JFrame f = new JFrame();
		f.add(vis.getVisualizationAsComponent());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 600);
		f.setVisible(true);
	}
}
