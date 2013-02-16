package org.modgen.rapidminer.modelling.template;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.fabi.visualizations.rapidminer.tree.TreeNodeBase;
import org.fabi.visualizations.tree.NodeContentRenderer;

public class ModgenTemplateTreeNode extends TreeNodeBase {
	
	public static class EnsembledModelsGeneration {
		public static final int PREDEFINED = 0;
		public static final int RANDOM = 1;
	}

	public static class ModelType {
		public static final int MODEL = 1;
		public static final int CLASSIFIER = 2;
		public static final int CONNECTABLE = 3;
	}

	static final int OUTPUT_DEPENDENT_MODEL_CNT = -1;
	
	protected String name;
	protected String parameters;
	protected int modelType = 0;
	protected int times = 1;
	
	protected int modelCnt = 0;
	protected int ensembledModelsGeneration;
	protected ModgenTemplateTreeNode[] ensembledModels = null;
	
	protected final static String CONNECTABLE_CLASSIFIER_NAME = "ConnectableClassifier";
	protected final static String CONNECTABLE_MODEL_NAME = "ConnectableModel";
	protected final static String CLASSIFIER_MODEL_NAME = "ClassifierModel";
	
	public void standardize() {
		if (ensembledModels == null) {
			return;
		}
		if (ensembledModelsGeneration == EnsembledModelsGeneration.RANDOM && ensembledModels.length == 1) {
			ensembledModelsGeneration = EnsembledModelsGeneration.PREDEFINED;
			ensembledModels[0].times *= modelCnt;
		}
		int childrenType = 0;
		if (name.equals(CONNECTABLE_CLASSIFIER_NAME) /* && modelType == ModelType.CONNECTABLE */) {
			childrenType = ModelType.CLASSIFIER;
		} else if (name.equals(CONNECTABLE_MODEL_NAME) /* && modelType == ModelType.CONNECTABLE */) {
			childrenType = ModelType.MODEL;
		} else if (name.equals(CLASSIFIER_MODEL_NAME)) {
			childrenType = ModelType.MODEL;
		} else if (modelType == ModelType.CLASSIFIER) {
			childrenType = ModelType.CLASSIFIER;
		} else if (modelType == ModelType.MODEL) {
			childrenType = ModelType.MODEL;
		}
		for (ModgenTemplateTreeNode cfg : ensembledModels) {
			if (cfg.modelType == 0) {
				cfg.modelType = childrenType;
			}
			cfg.standardize();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Collection<TreeNode> getChildren() {
		ModgenTemplateTreeNode[] children = ensembledModels;
		if (children != null) {
			List<TreeNode> childrenLst = new ArrayList<TreeNode>(children.length);
			for (ModgenTemplateTreeNode c : children) {
				childrenLst.add(c);
			}
			return childrenLst;
		} else {
			return null;
		}
	}

	@Override
	public Shape getShape(float x1, float y1, float x2, float y2) {
		if (modelType == ModelType.CLASSIFIER) {
			return new RoundRectangle2D.Float(x1, y1, x2, y2, 10.0f, 10.0f);
		} else {
			return new Rectangle2D.Float(x1, y1, x2, y2);
		}
	}

	@Override
	public String getParentEdgeLabel() {
		if (times == OUTPUT_DEPENDENT_MODEL_CNT) {
			return "n-×";
		} else if (times != 1) {
			return times + "×";
		} else {
			return null;
		}
	}
	
	@Override
	public String getChildrenEdgeLabel() {
		if (ensembledModelsGeneration == EnsembledModelsGeneration.RANDOM) {
			if (modelCnt == OUTPUT_DEPENDENT_MODEL_CNT) {
				return "n random";
			} else {
				return modelCnt + " random";
			}
		} else {
			return null;
		}
	}

	@Override
	public String getTreeTypeName() {
		return "Model Template";
	}
	
	@Override
	public List<NodeContentRenderer<TreeNode>> getNodeContentRenderers() {
		List<NodeContentRenderer<TreeNode>> result = new ArrayList<NodeContentRenderer<TreeNode>>(1);
		result.add(new ModgenTemplateTreeNodeParameterContentRenderer(this));
		return result;
	}
}
