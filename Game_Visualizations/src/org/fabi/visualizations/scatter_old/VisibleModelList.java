package org.fabi.visualizations.scatter_old;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@Deprecated
public class VisibleModelList extends JList implements Refreshable {

	private static final long serialVersionUID = -5244899146949363145L;

	protected ScatterplotVisualization visualization;
	
	protected ListSelectionListener listener;
	
	public VisibleModelList(final ScatterplotVisualization visualization) {
		super(visualization.getModelNames().toArray());
		getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.visualization = visualization;
		listener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int size = getModel().getSize();
				List<Boolean> res = new ArrayList<Boolean>(size);
				for (int i = 0; i < size; i++) {
					res.add(getSelectionModel().isSelectedIndex(i));
				}
				visualization.setProperty(ScatterplotVisualization.PROPERTY_MODELS_VISIBLE, res);
			} 
		};
		refresh();
	}

	@Override
	public void refresh() {
		int size = getModel().getSize();
		removeListSelectionListener(listener);
		this.setEnabled(visualization.isDisplayMultiple());
		if (!isEnabled()) {
			setSelectedIndices(new int[]{});
		} else {
			List<Boolean> selected = visualization.getModelVisible();
			List<Integer> selectedIndices = new ArrayList<Integer>(selected.size());
			int cnt = 0;
			for (int i = 0; i < size; i++) {
				if (selected.get(i)) {
					selectedIndices.add(i);
					cnt++;
				}
			}
			Integer[] indices = new Integer[cnt];
			indices = selectedIndices.toArray(indices);
			int[] indices2 = new int[indices.length];
			for (int i = 0; i < indices.length; i++) {
				indices2[i] = indices[i];
			}
			this.setSelectedIndices(indices2);
		}
		addListSelectionListener(listener);
	}
}
