package test;

import java.awt.*;
import java.awt.image.WritableRaster;

import javax.swing.*;
 
import org.fabi.visualizations.tools.math.Arrays;
import org.math.plot.*;
import org.math.plot.canvas.Plot2DCanvas;
import org.math.plot.plotObjects.*;
import org.math.plot.plots.ScatterPlot;
import org.math.plot.render.AbstractDrawer;
 
import static java.lang.Math.*;
 
import static org.math.array.StatisticSample.*;


public class JMathPlot {
    public static void main(String[] args) {
    	 
        // define your data
        double[] x = randomNormal(1000, 0, 1);
        double[] y = randomNormal(1000, 0, 1);
        final double[] z = randomNormal(1000, 0, 1);
        final double[][] data = new double[1000][2];
        for (int i = 0; i < 1000; i++) {
        	data[i][0] = x[i];
        	data[i][1] = y[i];
        }
//        double[][] data2 = {y, z};

        // create your PlotPanel (you can use it as a JPanel) with a legend at SOUTH
        Plot2DPanel plot;
        
        Plot2DCanvas canvas = new Plot2DCanvas();
        ScatterPlot scatter = new ScatterPlot("A", Color.BLACK, data) {
        	@Override
            public void plot(AbstractDrawer draw, Color c) {
                if (!visible) {
                    return;
                }
//                int[] l = draw.project(-4, -4);
//                int[] t = draw.project(3, 4);
//                if (t[0] != l[0] && t[1] != l[1]) {
	                double xstep = Math.abs(7.0 / 100.0) /*(double) (2.0 * (t[0] - l[0])))*/;
	                double ystep = Math.abs(8.0 / 100.0) /*(double) (2.0 * (t[1] - l[1])))*/;
	                System.out.println(xstep + " " + ystep);
	                float alpha = 0.01f;
	                draw.setColor(Color.GRAY);
	                for (double i = -4; i <= 3; i += xstep) {
	                	for (double j = -4; j < 4; j += ystep) {
	                        draw.fillPolygon(alpha, new double[]{i, j}, new double[]{i + xstep, j}, new double[]{i + xstep, j + ystep}, new double[]{i, j + ystep});
	                        alpha += 0.01;
	                        if (alpha >= 1.0f) {
	                        	alpha = 0.0f;
	                        }
	                	}
//	                }
                }
                double[] sorted = new double[z.length];
                System.arraycopy(z, 0, sorted, 0, z.length);
                double min = sorted[sorted.length - 1];
                double max = sorted[0];
                draw.setDotType(AbstractDrawer.PATTERN_DOT);
                draw.setDotType(AbstractDrawer.ROUND_DOT);
                for (int i = 0; i < data.length; i++) {
                    draw.setColor(getPointColor((z[i] - min) / (max - min), 255));
                    draw.setDotRadius((int) (5.0 * (z[i] - min) / (max - min)));
                    draw.drawDot(data[i]);
                }
            }
        };
        canvas.addPlotable(new Plotable() {

			@Override
			public void plot(AbstractDrawer draw) {
			}

			@Override
			public void setVisible(boolean v) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean getVisible() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void setColor(Color c) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Color getColor() {
				// TODO Auto-generated method stub
				return Color.RED;
			}
        	
        });
        canvas.addPlot(scatter);
//        
        plot = new Plot2DPanel(canvas);
//        plot.setAutoBounds();
//        for (int i = 0; i < x.length; i++) {
//        	plot.addScatterPlot(Integer.toString(i), new Color(i), new double[][]{new double[]{x[i]}, new double[]{y[i]}});
//        }
//        plot.addScatterPlot("A", Color.RED, data);
//        plot.addScatterPlot("B", Color.BLUE, data2);
        
        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);

}
    
    public static Color getPointColor(double value, int alpha) {
    if (Double.isNaN(value))
        return Color.LIGHT_GRAY;
    Color MIN_LEGEND_COLOR = Color.BLUE;
    Color MAX_LEGEND_COLOR = Color.RED;
    float[] minCol = Color.RGBtoHSB(MIN_LEGEND_COLOR.getRed(), MIN_LEGEND_COLOR.getGreen(), MIN_LEGEND_COLOR.getBlue(), null);
    float[] maxCol = Color.RGBtoHSB(MAX_LEGEND_COLOR.getRed(), MAX_LEGEND_COLOR.getGreen(), MAX_LEGEND_COLOR.getBlue(), null);
    //double hColorDiff = 1.0f - 0.68f;
    double hColorDiff = maxCol[0] - minCol[0];
    double sColorDiff = maxCol[1] - minCol[1];
    double bColorDiff = maxCol[2] - minCol[2];

    Color color = new Color(Color.HSBtoRGB((float)(minCol[0] + hColorDiff * value), (float)(minCol[1] + value * sColorDiff), (float)(minCol[2] + value * bColorDiff)));

    if (alpha < 255)
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    return color;
}

}
