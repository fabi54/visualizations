package org.modgen.rapidminer.modelling.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fabi.visualizations.rapidminer.tree.TreeIOObject;
import org.fabi.visualizations.rapidminer.tree.TreeNode;

import com.rapidminer.operator.ResultObjectAdapter;

import configuration.CfgTemplate;

public class ModgenTemplateContainer extends ResultObjectAdapter implements TreeIOObject {

	private static final long serialVersionUID = 6164996734695102103L;
	
	private CfgTemplate config;
	
	public ModgenTemplateContainer(CfgTemplate config) {
		this.config = config;
	}
	
	public CfgTemplate getConfig() {
		return config;
	}

	@Override
	public Collection<TreeNode> getRoots() {
		List<TreeNode> result = new ArrayList<TreeNode>(1);
		ModgenTemplateTreeNode node = ModelStringParser.getConfig(config.toString());
		node.standardize();
		result.add(node);
		return result;
	}

}
