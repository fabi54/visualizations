package org.modgen.rapidminer.modelling;

import game.classifiers.Classifier;
import game.classifiers.ensemble.ClassifierEnsemble;
import game.classifiers.single.ClassifierModel;
import game.models.Model;
import game.models.ensemble.ModelEnsemble;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.fabi.visualizations.tree.NodeContentRenderer;
import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.fabi.visualizations.rapidminer.tree.TreeNodeBase;

public class ModgenTreeNode extends TreeNodeBase {
	
	protected static class RenameProvider extends Properties {
		
		private static final long serialVersionUID = -6318376846899580362L;
		
		protected static RenameProvider instance = null;
		
		public static RenameProvider getInstance() {
			if (instance == null) {
				instance = new RenameProvider();
				InputStream is = null;
				try {
					is = ModgenTreeNode.class.getResourceAsStream("/org/modgen/renames.txt");
					instance.load(is);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (Exception e) { }
				}
			}
			return instance;
		}
	}
	
	Object item;
	
	public ModgenTreeNode(Object item) {
		this.item = item;
	}

	@Override
	public Collection<TreeNode> getChildren() {
		List<TreeNode> result = null;
		if (item instanceof ModelEnsemble) {
			ModelEnsemble ensemble = (ModelEnsemble) item;
			result = new ArrayList<TreeNode>(ensemble.getModelsNumber());
			for (int i = 0; i < ensemble.getModelsNumber(); i++) {
				result.add(new ModgenTreeNode(ensemble.getModel(i)));
			}
			return result;
		} else if (item instanceof ClassifierEnsemble) {
			ClassifierEnsemble ensemble = (ClassifierEnsemble) item;
			result = new ArrayList<TreeNode>(ensemble.getClasifiersNumber());
			for (int i = 0; i < ensemble.getClasifiersNumber(); i++) {
				result.add(new ModgenTreeNode(ensemble.getClassifier(i)));
			}
			return result;
		} else if (item instanceof ClassifierModel) {
			Model[] models = ((ClassifierModel) item).getAllModels();
			result = new ArrayList<TreeNode>(models.length);
			for (Model model : models) {
				result.add(new ModgenTreeNode(model));
			}
		}
		return result;
	}

	@Override
	public String getName() {
		String name = "";
		if (item instanceof Classifier) {
			name = ((Classifier) item).getConfig().getClassRef().getSimpleName();
		} else if (item instanceof Model) {
			name = ((Model) item).getConfig().getClassRef().getSimpleName();
		}
//		if (!name.isEmpty()) {
//			String s = RenameProvider.getInstance().getProperty(name);
//			if (s != null) return s;
//		}
//		return "";
		return name;
	}

	@Override
	public Shape getShape(float x1, float y1, float x2, float y2) {
		if (item instanceof Classifier) {
			return new RoundRectangle2D.Float(x1, y1, x2, y2, 10.0f, 10.0f);
		} else {
			return new Rectangle2D.Float(x1, y1, x2, y2);
		}
	}

	@Override
	public String getTreeTypeName() {
		return "Ensemble Structure";
	}

	@Override
	public List<NodeContentRenderer<TreeNode>> getNodeContentRenderers() {
		List<NodeContentRenderer<TreeNode>> result = new ArrayList<NodeContentRenderer<TreeNode>>(1);
		result.add(new ModgenTreeNodeScatterplotContentRenderer());
		return result;
	}
}
