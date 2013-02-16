package org.fabi.visualizations.rapidminer.operators;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.rapidminer.VisualizationContainer;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypeInt;

public class ExportVisualizationOperator extends Operator {
	
	protected InputPort visualizationInput = getInputPorts().createPort("visualization");
	
	public ExportVisualizationOperator(OperatorDescription description) {
		super(description);
		visualizationInput.addPrecondition(new SimplePrecondition(visualizationInput,
				new MetaData(VisualizationContainer.class), true));
	}
	
    @Override
    public List<ParameterType> getParameterTypes() {
	    List<ParameterType> types = super.getParameterTypes();
	      types.add(new ParameterTypeFile("file", "", ".png", false));
	      types.add(new ParameterTypeBoolean("overwrite", "", true, true));
	      types.add(new ParameterTypeInt("width", "", 1, 4000, 800));
	      types.add(new ParameterTypeInt("height", "", 1, 4000, 600));
	      return types;
    }
	
	@Override
	public void doWork() {
		try {
			Visualization<?> visualization = visualizationInput.getData(VisualizationContainer.class).getVisualization();
			BufferedImage image = visualization.getVisualizationAsImage(getParameterAsInt("width"), getParameterAsInt("height"));
			File f = getParameterAsFile("file");
			String n = f.getAbsolutePath();
			if (!getParameterAsBoolean("overwrite")) {
				for (int i = 1; f.exists(); i++) {
					String[] split = n.split(".");
					if (split.length == 1) {
						f = new File(split[0] + "(" + i + ")");
					} else {
						for (int j = 0; i < split.length - 2; i++) {
							split[j + 1] = split[j] + split[j + 1];
						}
						f = new File(split[split.length - 2] + "(" + i + ")" + split[split.length - 1]);
						
					}
				}
			}
			ImageIO.write(image, "png", f);
		} catch (Exception ex) {
			return;
		}
		
	}
	
}