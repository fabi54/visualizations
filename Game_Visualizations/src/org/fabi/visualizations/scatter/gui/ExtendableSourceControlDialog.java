package org.fabi.visualizations.scatter.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.DerivedGroovyDataSource;
import org.fabi.visualizations.scatter.sources.ExtendableScatterplotSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

public class ExtendableSourceControlDialog extends JDialog implements Observer {

	private static final long serialVersionUID = -2910270312632113737L;
	
	protected ExtendableScatterplotSource source;
	
	protected JComponent[] cache;
	
	protected JComponent actComponent;
	
	protected JComboBox sourceChooser;
	
	protected NewDatasetPanel panel;
	
	public ExtendableSourceControlDialog(ExtendableScatterplotSource source) {
		this.source = source;
		panel = new NewDatasetPanel(this);
		source.addObserver(this);
		sourceChooser = new JComboBox();
		sourceChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (actComponent != null) {
					remove(actComponent); // TODO CardLayout?
				}
				actComponent = getComponentForIndex(sourceChooser.getSelectedIndex());
				add(actComponent, BorderLayout.CENTER);
				validate();
			}
		});
		update(null, null);
		add(sourceChooser, BorderLayout.NORTH);
		setSize(600, 500);
	}
	
	protected JComponent getComponentForIndex(int index) {
		int dataSourceCount = source.getEditableDataSourceCount();
		if (index < dataSourceCount) {
			int offset = source.getDataSourceCount() - dataSourceCount;
			JComponent c = getComponentForSource(source.getDataSource(offset + index));
			c.setEnabled(false);
			return c;
		}
		index -= dataSourceCount;
		int modelSourceCount = source.getEditableModelSourceCount();
		if (index < modelSourceCount) {
			int offset = source.getModelSourceCount() - modelSourceCount;
			JComponent c = getComponentForSource(source.getModelSource(offset + index));
			c.setEnabled(false);
			return c;
		}
		index -= modelSourceCount;
		switch (index) {
			case 0:
				return panel;
			case 1:
				return new JLabel("New model");
			default:
				throw new IllegalArgumentException("Index out of bounds.");
		}
	}
	
	protected JComponent getComponentForSource(DataSource source) {
		if (source instanceof DerivedGroovyDataSource) {
			return new DerivedGroovyDataSourceControlPanel((DerivedGroovyDataSource) source);
		} else {
			return new JLabel("GUI for editing not available.");
		}
	}
	
	protected JComponent getComponentForSource(ModelSource source) {
		return new JLabel(source.getName());
	}

	@Override
	public void update(Observable o, Object arg) {
		int dataSourceCnt = source.getDataSourceCount();
		int modelSourceCnt = source.getModelSourceCount();
		int coreDataSourceCnt = dataSourceCnt - source.getEditableDataSourceCount();
		int coreModelSourceCnt = modelSourceCnt - source.getEditableModelSourceCount();
		if (sourceChooser.getItemCount() > 0) {
			sourceChooser.removeAllItems();
		}
		for (int i = coreDataSourceCnt; i < dataSourceCnt; i++) {
			sourceChooser.addItem(source.getDataSource(i).getName());
		}
		for (int i = coreModelSourceCnt; i < modelSourceCnt; i++) {
			sourceChooser.addItem(source.getModelSource(i).getName());
		}
		sourceChooser.addItem("<new dataset>");
		sourceChooser.addItem("<new model>");
		sourceChooser.updateUI();
		cache = new JComponent[dataSourceCnt + modelSourceCnt];
	}
	
}
