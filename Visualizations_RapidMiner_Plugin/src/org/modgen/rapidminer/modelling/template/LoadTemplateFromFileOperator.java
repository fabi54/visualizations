package org.modgen.rapidminer.modelling.template;

import java.io.File;
import java.util.List;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.UndefinedParameterError;

import configuration.CfgTemplate;
import configuration.ConfigurationFactory;

public class LoadTemplateFromFileOperator extends Operator {
	
    protected final OutputPort output = getOutputPorts().createPort("template");

	public LoadTemplateFromFileOperator(OperatorDescription description) {
		super(description);
        getTransformer().addGenerationRule(output,ModgenTemplateContainer.class);
	}

    @Override
    public List<ParameterType> getParameterTypes() {
      List<ParameterType> types=  super.getParameterTypes();
        types.add( new ParameterTypeFile("file", "", null, ""));
        return types;
    }

    @Override
    public void doWork()  {
        try {
            File f=getParameterAsFile("file");
            CfgTemplate cfg = ConfigurationFactory.getConfiguration(f.getAbsolutePath());
            output.deliver(new ModgenTemplateContainer(cfg));
        } catch (UndefinedParameterError undefinedParameterError) {
            undefinedParameterError.printStackTrace();
        }
    }

}
