package org.fabi.visualizations.scatter.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.fabi.visualizations.scatter.gui.ColorModelComboBox;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Series dependent color model")
public class SeriesDependentColorModel extends ColorModelBase {

	@Property(name="Data color models")
	private ColorModel[] dataModels;
	@Property(name="Model color models")
	private ColorModel[] modelModels;
	
	protected String[] dataNames;
	protected String[] modelNames;
	
	protected JComponent actControls;
	protected JComboBox seriesComboBox;
	protected ColorModelComboBox modelComboBox;
	
	protected ScatterplotSource source;
	
	@Override
	public void init(ScatterplotSource source) {
		this.source = source;
		if (dataModels == null || dataModels.length != source.getDataSourceCount()) {
			dataModels = new ColorModel[source.getDataSourceCount()];
			for (int i = 0; i < dataModels.length; i++) {
				dataModels[i] = new ConstantColorModel();
			}
		}
		if (modelModels == null || modelModels.length != source.getModelSourceCount()) {
			modelModels = new ColorModel[source.getModelSourceCount()];
			for (int i = 0; i < modelModels.length; i++) {
				modelModels[i] = new ConstantColorModel();
			}
		}
		dataNames = new String[dataModels.length];
		for (int i = 0; i < dataNames.length; i++) {
			dataNames[i] = source.getDataSource(i).getName();
		}
		modelNames = new String[modelModels.length];
		for (int i = 0; i < modelNames.length; i++) {
			modelNames[i] = source.getModelSource(i).getName();
		}
		updateSeriesComboBox();
	}
	
	protected void updateSeriesComboBox() {
		if (seriesComboBox != null) {
			if (seriesComboBox.getItemCount() > 0) {
				seriesComboBox.removeAllItems();
			}
			for (int i = 0; i < dataModels.length; i++) {
				seriesComboBox.addItem(dataNames[i]);
			}
			for (int i = 0; i < modelModels.length; i++) {
				seriesComboBox.addItem(modelNames[i]);
			}
		}
	}

	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data,
			int index, int[] inputsIndices, int[] outputsIndices) {
		if (data) {
			return dataModels[index].getColor(inputs, outputs, data, index, inputsIndices, outputsIndices);
		} else {
			return modelModels[index].getColor(inputs, outputs, data, index, inputsIndices, outputsIndices);
		}
	}

	public ColorModel[] getDataModels() {
		return dataModels;
	}

	public void setDataModels(ColorModel[] dataModels) {
		this.dataModels = dataModels;
	}

	public ColorModel[] getModelModels() {
		return modelModels;
	}

	public void setModelModels(ColorModel[] modelModels) {
		this.modelModels = modelModels;
	}

	@Override
	public String getName() {
		return "Different model for each dataset and model";
	}

	@Override
	public JComponent getControls() {
		final JPanel res = new JPanel(new BorderLayout());
		final JPanel top = new JPanel(new GridBagLayout());
		seriesComboBox = new JComboBox();
		updateSeriesComboBox();
		modelComboBox = new ColorModelComboBox(source);
		seriesComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = seriesComboBox.getSelectedIndex(); 
				if (i < 0) {
					return;
				}
				if (i < dataModels.length) {
					modelComboBox.init(source, dataModels[i]);
				} else {
					i -= dataModels.length;
					modelComboBox.init(source, modelModels[i]);
				}
				for (int j = 0; j < modelComboBox.getItemCount(); j++) {
					Object item = modelComboBox.getItemAt(j);
					if (item instanceof SeriesDependentColorModel) {
						modelComboBox.removeItem(item);
					}
				}
			}
		});
		modelComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateModelControls(res);
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		top.add(seriesComboBox, c);
		c.gridx = 1;
		top.add(modelComboBox, c);
		res.add(top, BorderLayout.NORTH);
		updateModelControls(res);
		return res;
	}
	
	protected void updateModelControls(JPanel res) {
		int i = seriesComboBox.getSelectedIndex();
		ColorModel model = (ColorModel) modelComboBox.getSelectedItem();
		if (i < dataModels.length) {
			dataModels[i] = model;
		} else {
			i -= dataModels.length;
			modelModels[i] = model;
		}
		if (model != null) {
			if (actControls != null) res.remove(actControls);
			actControls = model.getControls();
			res.add(actControls, BorderLayout.CENTER);
			res.validate();
		}
	}

}
