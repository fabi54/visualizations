package org.fabi.visualizations.evolution.scatterplot.modelling;

import java.util.LinkedList;
import java.util.List;

import configuration.CfgTemplate;
import configuration.models.single.DecisionTreeModelConfig;
import configuration.models.single.ExpModelConfig;
import configuration.models.single.GaussianModelConfig;
import configuration.models.single.KNNModelConfig;
import configuration.models.single.LinearModelConfig;
import configuration.models.single.LocalPolynomialModelConfig;
import configuration.models.single.NeuralNetModelConfig;
import configuration.models.single.PolynomialModelConfig;
import configuration.models.single.RBFModelConfig;
import configuration.models.single.SVMModelConfig;
import configuration.models.single.SigmoidModelConfig;
import configuration.models.single.SineModelConfig;

public class ModelPool {
	
	public static List<CfgTemplate> getTemplates() {
		List<CfgTemplate> res = new LinkedList<CfgTemplate>();
		for (Class<CfgTemplate> cls : templates) {
			try {
				res.add(cls.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// higher degree polynomials
		for (int i : new int[]{2, 5, 10}) {
			PolynomialModelConfig cfg = new PolynomialModelConfig();
			cfg.setMaxDegree(i);
			res.add(cfg);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	static Class<CfgTemplate>[] templates = new Class[] {
		DecisionTreeModelConfig.class,
		ExpModelConfig.class,
		GaussianModelConfig.class,
//		GaussianMultiModelConfig.class,
//		GaussianNormModelConfig.Class,
		KNNModelConfig.class,
		LinearModelConfig.class,
		LocalPolynomialModelConfig.class,
		NeuralNetModelConfig.class,
		PolynomialModelConfig.class,
		RBFModelConfig.class,
		SigmoidModelConfig.class,
//		SigmoidNormModelConfig.class,
		SineModelConfig.class,
//		SineNormModelConfig.class,
		SVMModelConfig.class
	};
}
