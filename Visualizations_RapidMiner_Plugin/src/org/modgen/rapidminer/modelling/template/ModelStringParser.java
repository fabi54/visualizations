package org.modgen.rapidminer.modelling.template;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ModelStringParser {
	
	protected final static char PARAMETER_OPEN = '(';
	protected final static char PARAMETER_CLOSE = ')';
	protected final static char CONNECTABLE_OPEN = '|';
	protected final static char CONNECTABLE_CLOSE = '|';
	protected final static char MODEL_OPEN = '[';
	protected final static char MODEL_CLOSE = ']';
	protected final static char CLASSIFIER_OPEN = '{';
	protected final static char CLASSIFIER_CLOSE = '}';
	protected final static char TIMES_PREDEFINED = 'X';
	protected final static char TIMES_RANDOM = 'x';
	protected final static char TIMES_MODEL = '*';
	protected final static char SEPARATOR = ',';
	protected final static String OUTPUT_DEPENDENT = "<outputs>";
	
	public static ModgenTemplateTreeNode getConfig(String str) {
		ModgenTemplateTreeNode cfg = new ModgenTemplateTreeNode();
		@SuppressWarnings("unused")
		int pos = getConfig(str, 0, cfg);
		// if (pos != str.length)
		return cfg;
	}
	
	public static int getConfig(String str, int pos, ModgenTemplateTreeNode res) {
		//System.out.println("GETTING CONFIG FOR: " + str.substring(pos));
		StringBuffer buf = new StringBuffer();
		for (; pos < str.length(); pos++) {
			char c = str.charAt(pos);
			switch (c) {
				case PARAMETER_OPEN:
					pos = readParams(str, pos + 1, res);
					res.name = buf.toString();
					buf = new StringBuffer();
					break;
				case MODEL_OPEN:
					if (res.name == null) {
						res.name = buf.toString();
					}
					buf = new StringBuffer();
					res.modelType = ModgenTemplateTreeNode.ModelType.MODEL;
					return getModels(str, pos + 1, c, res);
				case CLASSIFIER_OPEN:
					if (res.name == null) {
						res.name = buf.toString();
					}
					buf = new StringBuffer();
					res.modelType = ModgenTemplateTreeNode.ModelType.CLASSIFIER;
					return getModels(str, pos + 1, c, res);
				case CONNECTABLE_OPEN:
					if (res.name == null) {
						res.name = buf.toString();
					}
					buf = new StringBuffer();
					res.modelType = ModgenTemplateTreeNode.ModelType.CONNECTABLE;
					return getModels(str, pos + 1, c, res);
				default:
					if (!Character.isWhitespace(c)) {
						buf.append(c);
					}
					break;
			}
		}
		if (res.name == null) {
			res.name = buf.toString();
		}
		return pos;
	}
	
	protected static int getModels(String src, int pos, char type, ModgenTemplateTreeNode res) {
		//System.out.println("EXTRACTING MODELS FROM: " + src.substring(pos));
		Stack<Character> stack = new Stack<Character>();
		List<ModgenTemplateTreeNode> cfgs = new LinkedList<ModgenTemplateTreeNode>();
		stack.push(type);
		StringBuffer buf = new StringBuffer();
		int times = 1;
		boolean preamble = false;
		if (src.charAt(pos) == OUTPUT_DEPENDENT.charAt(0) || Character.isDigit(src.charAt(pos))) {
			preamble = true;
		}
		char p;
		for (; pos < src.length(); pos++) {
			char c = src.charAt(pos);
			switch (c) {
				case TIMES_PREDEFINED:
				case TIMES_RANDOM:
					if (preamble) {
						String str = buf.toString();
						if (str.equals(OUTPUT_DEPENDENT)) {
							res.modelCnt = ModgenTemplateTreeNode.OUTPUT_DEPENDENT_MODEL_CNT;
						} else {
							try {
								res.modelCnt = Integer.parseInt(str);
							} catch (NumberFormatException ex) {
								throw new ParsingException();
							}
						}
						if (c == TIMES_PREDEFINED) {
							res.ensembledModelsGeneration = ModgenTemplateTreeNode.EnsembledModelsGeneration.PREDEFINED;
						} else {
							res.ensembledModelsGeneration = ModgenTemplateTreeNode.EnsembledModelsGeneration.RANDOM;
						}
						buf = new StringBuffer();
						preamble = false;
					} else {
						buf.append(c);
					}
					break;
				case TIMES_MODEL:
					if (stack.size() == 1) {
						try {
							times = Integer.parseInt(buf.toString());
							buf = new StringBuffer();
						} catch (NumberFormatException ex) {
							throw new ParsingException("NumberFormat: " + buf.toString());
						}
						break;
					} else {
						buf.append(c);
						break;
					}
				case MODEL_OPEN:
				case CLASSIFIER_OPEN:
				case PARAMETER_OPEN:
					buf.append(c);
					stack.push(c);
					break;
				case MODEL_CLOSE:
					p = stack.pop();
					if (p != MODEL_OPEN) {
						throw new ParsingException("stack: " + Arrays.toString(stack.toArray(new Character[2])) + ", removed: " + p + "\n" + errorStr(src, pos));
					}
					if (!stack.isEmpty()) buf.append(c);
					break;
				case CLASSIFIER_CLOSE:
					p = stack.pop();
					if (p != CLASSIFIER_OPEN) {
						throw new ParsingException("stack: " + Arrays.toString(stack.toArray(new Character[2])) + ", removed: " + p + "\n" + errorStr(src, pos));
					}
					if (!stack.isEmpty()) buf.append(c);
					break;
				case PARAMETER_CLOSE:
					p = stack.pop();
					if (p != PARAMETER_OPEN) {
						throw new ParsingException("stack: " + Arrays.toString(stack.toArray(new Character[2])) + ", removed: " + p + "\n" + errorStr(src, pos));
					}
					buf.append(c);
					break;
				case CONNECTABLE_OPEN:
					if (stack.peek() == CONNECTABLE_OPEN) {
						stack.pop();
						//buf.append(c);
					} else {
						stack.push(c);
						if (!stack.isEmpty()) buf.append(c);
					}
					break;
				case SEPARATOR:
					if (stack.size() == 1) {
						ModgenTemplateTreeNode cfg = new ModgenTemplateTreeNode();
						getConfig(buf.toString(), 0, cfg);
						cfg.times = times;
						//for (int i = 0; i < times; i++) {
							cfgs.add(cfg);
						//}
						times = 1;
						buf = new StringBuffer();
					} else {
						buf.append(c);
					}
					break;
				default:
					if (!(Character.isWhitespace(c) && stack.size() == 1)) {
						buf.append(c);
					}
					break;
			}
			if (stack.size() == 0) {
				if (buf.length() > 0) {
					ModgenTemplateTreeNode cfg = new ModgenTemplateTreeNode();
					getConfig(buf.toString(), 0, cfg);
					cfg.times = times;
					cfgs.add(cfg);
					times = 1;
					buf = new StringBuffer();
				}
				res.ensembledModels = cfgs.toArray(new ModgenTemplateTreeNode[cfgs.size()]);
				return pos + 1;
			}
		}
		throw new ParsingException();
	}
	
	protected static int readParams(String str, int pos, ModgenTemplateTreeNode result) {
		StringBuffer buf = new StringBuffer();
		for (; pos < str.length(); pos++) {
			if (str.charAt(pos) == PARAMETER_CLOSE) {
				result.parameters = buf.toString();
				return pos;
			} else {
				buf.append(str.charAt(pos));
			}
		}
		throw new ParsingException();
	}
	
	protected static String errorStr(String s, int pos) {
		StringBuffer b = new StringBuffer();
		b.append("\n");
		for (int i = 0; i < pos; i++) {
			b.append(" ");
		}
		b.append("^");
		b.append("\n(at: " + pos + ")");
		return new String(s + b.toString());
	}
}
