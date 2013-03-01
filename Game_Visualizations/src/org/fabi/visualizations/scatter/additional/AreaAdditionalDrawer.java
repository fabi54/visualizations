package org.fabi.visualizations.scatter.additional;

import java.awt.Color;

import org.math.plot.render.AbstractDrawer;

	public class AreaAdditionalDrawer implements AdditionalDrawer {
		
		protected double xl; protected double xu; protected double yl; protected double yu;
		protected Color clr = Color.WHITE;
		
		public AreaAdditionalDrawer(double xl, double xu, double yl, double yu) {
			this.xl = xl; this.xu = xu; this.yl = yl; this.yu = yu;
		}
		
		public void setColor(Color clr) {
			this.clr = clr;
		}
		
		@Override
		public void draw(AbstractDrawer draw) {
			draw.setColor(clr);
			draw.drawPolygon(new double[]{xl, yl}, new double[]{xl, yu}, new double[]{xu, yu}, new double[]{xu, yl});
		}
	}