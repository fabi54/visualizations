
package org.fabi.visualizations.rapidminer;

import org.fabi.visualizations.Global;

import com.rapidminer.gui.MainFrame;

public class VisualizationsPluginInit {
	
	/**
	 * This method will be called directly after the extension is initialized.
	 * This is the first hook during start up. No initialization of the operators
	 * or renderers has taken place when this is called.
	 */
	public static void initPlugin() {
		Global.getInstance().init();
	}
	
	/**
	 * This method is called during start up as the second hook. It is 
	 * called before the gui of the mainframe is created. The Mainframe is 
	 * given to adapt the gui.
	 * The operators and renderers have been registered in the meanwhile.
	 */
	public static void initGui(MainFrame mainframe) {}
	
	/**
	 * The last hook before the splash screen is closed. Third in the row.
	 */
	public static void initFinalChecks() {}

	/**
	 * Will be called as fourth method, directly before the UpdateManager is used
	 * for checking updates. Location for exchanging the UpdateManager. 
	 * The name of this method unfortunately is a result of a historical typo, so it's
	 * a little bit misleading.
	 */
	public static void initPluginManager() {}
}