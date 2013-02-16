package org.fabi.visualizations.scatter.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

public class VisibilityControlDialog extends JDialog {

	private static final long serialVersionUID = 6547403030716570627L;

	protected ScatterplotVisualization visualization;
	
	protected JCheckBox[][] dataOutputs;
	protected JCheckBox[][] modelOutputs;
	
	protected JCheckBox[] outputs;
	protected JCheckBox[] data;
	protected JCheckBox[] models;
	
	public VisibilityControlDialog(ScatterplotVisualization visualization, JComponent parent) {
		super(SwingUtilities.windowForComponent(parent), "Visibility Settings");
		this.visualization = visualization;
		setSize(600, 500);
		ScatterplotSource source = visualization.getSource();
		int dataCnt = source.getDataSourceCount();
		int modelCnt = source.getModelSourceCount();
		int outputCnt = source.getOutputsNumber();
		dataOutputs = new JCheckBox[dataCnt][outputCnt];
		modelOutputs = new JCheckBox[modelCnt][outputCnt];
		outputs = new JCheckBox[outputCnt];
		data = new JCheckBox[dataCnt];
		models = new JCheckBox[modelCnt];
		initGui(source);
		updateChecked(IGNORE_NOTHING);
		setListeners();
	}
	
	// TODO refactor
	protected void initGui(ScatterplotSource source) {
		setLayout(new GridBagLayout());
		Metadata metadata = source.getMetadata();
		String[] outputLabels = new String[source.getOutputsNumber()];
		if (metadata != null) {
			List<AttributeInfo> outputAttrs = metadata.getOutputAttributeInfo();
			Iterator<AttributeInfo> iterator = outputAttrs.iterator();
			for (int i = 0; i < outputLabels.length; i++) {
				outputLabels[i] = iterator.next().getName();
			}
		} else {
			for (int i = 0; i < outputLabels.length; i++) {
				outputLabels[i] = "out" + i;
			}
		}
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		for (int i = 0; i < outputs.length; i++) {
			c.gridx++;
			add(new JLabel(outputLabels[i]), c); // if (!iterator.hasNext()) ... exception
		}
		for (int i = 0; i < data.length; i++) {
			c.gridy++;
			c.gridx = 0;
			add(new JLabel(source.getDataSource(i).getName()), c);
			for (int j = 0; j < outputs.length; j++) {
				c.gridx++;
				dataOutputs[i][j] = new JCheckBox();
				add(dataOutputs[i][j], c);
			}
			c.gridx++;
			data[i] = new JCheckBox();
			add(data[i], c);
		}
		for (int i = 0; i < models.length; i++) {
			c.gridy++;
			c.gridx = 0;
			add(new JLabel(source.getModelSource(i).getName()), c);
			for (int j = 0; j < outputs.length; j++) {
				c.gridx++;
				modelOutputs[i][j] = new JCheckBox();
				add(modelOutputs[i][j], c);
			}
			c.gridx++;
			models[i] = new JCheckBox();
			add(models[i], c);
		}
		c.gridx = 0;
		c.gridy++;
		for (int i = 0; i < outputs.length; i++) {
			c.gridx++;
			outputs[i] = new JCheckBox();
			add(outputs[i], c);
		}
	}
	
	protected void setListeners() {
		DataListener dataLstnr = new DataListener();
		ModelListener modelLstnr = new ModelListener();
		for (int i = 0; i < dataOutputs.length; i++) {
			for (int j = 0; j < dataOutputs[i].length; j++) {
				dataOutputs[i][j].addItemListener(new UpdateListener(IGNORE_DATAOUTPUTS));
				dataOutputs[i][j].addItemListener(dataLstnr);
			}
		}
		for (int i = 0; i < modelOutputs.length; i++) {
			for (int j = 0; j < modelOutputs[i].length; j++) {
				modelOutputs[i][j].addItemListener(new UpdateListener(IGNORE_MODELOUTPUTS));
				modelOutputs[i][j].addItemListener(modelLstnr);
			}
		}
		for (int i = 0; i < outputs.length; i++) {
			final int index = i;
			outputs[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					for (int j = 0; j < dataOutputs[index].length; j++) {
						dataOutputs[j][index].setSelected(outputs[index].isSelected());
					}
					for (int j = 0; j < modelOutputs[index].length; j++) {
						modelOutputs[j][index].setSelected(outputs[index].isSelected());
					}
				}
			});
		}
		for (int i = 0; i < data.length; i++) {
			final int index = i;
			data[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					for (int j = 0; j < dataOutputs.length; j++) {
						dataOutputs[index][j].setSelected(data[index].isSelected());
					}
				}
			});
		}
		for (int i = 0; i < models.length; i++) {
			final int index = i;
			models[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					for (int j = 0; j < modelOutputs.length; j++) {
						modelOutputs[index][j].setSelected(models[index].isSelected());
					}
				}
			});
		}
	}
	
	protected static int IGNORE_NOTHING = 0;
	protected static int IGNORE_DATAOUTPUTS = 1;
	protected static int IGNORE_MODELOUTPUTS = 2;
	protected static int IGNORE_DATA = 3;
	protected static int IGNORE_MODELS = 4;
	protected static int IGNORE_OUTPUTS = 5;
	
	protected void updateChecked(int ignore) {
		boolean[][] dataVisible = visualization.getDataVisible();
		boolean[][] modelsVisible = visualization.getModelsVisible();
		boolean b;
		if (ignore != IGNORE_DATAOUTPUTS) {
			for (int i = 0; i < dataOutputs.length; i++) {
				for (int j = 0; j < dataOutputs[i].length; j++) {
					dataOutputs[i][j].setSelected(dataVisible[i][j]);
				}
			}
		}
		if (ignore != IGNORE_DATA) {
			for (int i = 0; i < dataOutputs.length; i++) {
				b = true;
				for (int j = 0; j < dataOutputs[i].length; j++) {
					b &= dataVisible[i][j];
				}
				data[i].setSelected(b);
			}
		}
		if (ignore != IGNORE_MODELOUTPUTS) {
			for (int i = 0; i < modelOutputs.length; i++) {
				for (int j = 0; j < modelOutputs[i].length; j++) {
					modelOutputs[i][j].setSelected(modelsVisible[i][j]);
				}
			}
		}
		if (ignore != IGNORE_MODELS) {
			for (int i = 0; i < modelOutputs.length; i++) {
				b = true;
				for (int j = 0; j < modelOutputs[i].length; j++) {
					b &= modelsVisible[i][j];
				}
				models[i].setSelected(b);
			}
		}
		if (ignore != IGNORE_OUTPUTS) {
			for (int i = 0; i < outputs.length; i++) {
				b = true;
				for (int j = 0; j < data.length; j++) {
					b &= dataVisible[j][i];
				}
				for (int j = 0; j < models.length; j++) {
					b &= modelsVisible[j][i];
				}
				outputs[i].setSelected(b);
			}
		}
	}
	
	protected class UpdateListener implements ItemListener {

		protected int ignore;
		
		public UpdateListener(int ignore) {
			this.ignore = ignore;
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			updateChecked(ignore);
		}
	}
	
	protected class DataListener implements ItemListener { // TODO? ChangeListener

		@Override
		public void itemStateChanged(ItemEvent e) {
			boolean[][] dataVisible = new boolean[dataOutputs.length][outputs.length];
			for (int i = 0; i < dataVisible.length; i++) {
				for (int j = 0; j < dataVisible[i].length; j++) {
					dataVisible[i][j] = dataOutputs[i][j].isSelected();
				}
			}
			visualization.setDataVisible(dataVisible);
		}
	}
	
	protected class ModelListener implements ItemListener { // TODO? ChangeListener

		@Override
		public void itemStateChanged(ItemEvent e) {
			boolean[][] modelsVisible = new boolean[modelOutputs.length][outputs.length];
			for (int i = 0; i < modelsVisible.length; i++) {
				for (int j = 0; j < modelsVisible[i].length; j++) {
					modelsVisible[i][j] = modelOutputs[i][j].isSelected();
				}
			}
			visualization.setModelsVisible(modelsVisible);
		}
	}
	
}
