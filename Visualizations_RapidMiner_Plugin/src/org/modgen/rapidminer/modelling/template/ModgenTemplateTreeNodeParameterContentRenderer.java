package org.modgen.rapidminer.modelling.template;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Collection;
//import java.util.SortedMap;
//import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.fabi.visualizations.tree.NodeContentRenderer;

public class ModgenTemplateTreeNodeParameterContentRenderer implements NodeContentRenderer<TreeNode> {

	protected static final Font FONT = new Font("SansSerif", Font.PLAIN, 10);
	protected static final String NO_PARAMETERS_MESSAGE = "(no parameters)";
	
	protected Dimension nodeContentSize;
	
	public ModgenTemplateTreeNodeParameterContentRenderer(TreeNode root) {
		nodeContentSize = getMaxContentSize(root);
	}
	
	protected Dimension getMaxContentSize(TreeNode node) {
		if (!(node instanceof ModgenTemplateTreeNode)) {
			return new Dimension(0, 0);
		}
		String parameterString = ((ModgenTemplateTreeNode) node).parameters;
		Dimension mySize = new Dimension(0, 0);
		String[] parameters;
		if (parameterString != null) {
			parameters = parameterString.split(",");
		} else {
			parameters = new String[]{NO_PARAMETERS_MESSAGE};
		}
    	JComponent c = new JLabel();
    	c.setFont(FONT);
    	FontMetrics fm = c.getFontMetrics(FONT);
		for (String p : parameters) {
			mySize.width = Math.max(mySize.width, fm.stringWidth(p.replace("=", ": ")));
		}
		mySize.height = (fm.getHeight()) * parameters.length;
		
		Collection<TreeNode> children = node.getChildren();
		if (children == null) {
			return mySize;
		}
		
		for (TreeNode n : children) {
			Dimension d = getMaxContentSize(n);
			mySize.width = Math.max(mySize.width, d.width);
			mySize.height = Math.max(mySize.height, d.height);
		}
		return mySize;
	}
	
	@Override
	public Component getVertexContentComponent(TreeNode node) {
		if (!(node instanceof ModgenTemplateTreeNode)) {
			return null;
		}
		ModgenTemplateTreeNode item = ((ModgenTemplateTreeNode) node);
		String parameterString = item.parameters;
		StringBuffer buf = new StringBuffer();
		buf.append("<html>");
		if (parameterString != null) {
		String[] parameters = parameterString.split(",");
			for (String s : parameters) {
				String[] keyValue = s.split("=");
				if(keyValue.length == 2) {
					buf.append(keyValue[0]);
					buf.append(": <font color=\"gray\">").append(keyValue[1]).append("</font><br/>");
				} else {
					buf.append(s).append("<br/>");
				}
				//parameterMap.put(keyValue[0], keyValue[1]);
			}
		} else {
			buf.append("<font color=\"gray\">").append(NO_PARAMETERS_MESSAGE).append("</font>");
		}
		buf.append("</html>");
		JLabel label = new JLabel(buf.toString());
		label.setFont(FONT);
		return label;
	}

	@Override
	public Dimension getPreferredNodeContentSize() {
		return nodeContentSize;
	}

	@Override
	public String getName() {
		return "Parameters";
	}

}
