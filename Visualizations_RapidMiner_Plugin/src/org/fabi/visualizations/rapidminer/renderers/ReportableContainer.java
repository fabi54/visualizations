package org.fabi.visualizations.rapidminer.renderers;

import java.util.Collection;

import com.rapidminer.operator.IOObject;

public interface ReportableContainer {
	public Collection<IOObject> getOtherReportables();
}
