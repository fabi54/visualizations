package org.fabi.visualizations.tools.math;

import org.jfree.data.Range;

public class Arrays {

    private static double getMedian(double[] src) {
    	if (src.length < 1) {
    		return 0.5;
    	}
        double[] array = java.util.Arrays.copyOf(src, src.length);
        java.util.Arrays.sort(array);
        return array[array.length / 2];
    }

    /**
     * Returns a new data instance with each attribute value being a median value of that attribute
     * for all data instances.
     * @param data data
     * @param excludingIVectors indices of input vectors to be ignored
     * @return
     */
    public static double[] getMedians(double[][] inputs) {
    	if (inputs.length < 1 || inputs[0].length < 1) {
    		return new double[0];
    	}
    	double[] res = new double[inputs[0].length]; // supposes that inputs[i].length = const. for all i
    	for (int i = 0; i < res.length; i++) {
    		double[] array = new double[inputs.length];
    		for (int j = 0; j < inputs.length; j++) {
    			array[j] = inputs[j][i];
    		}
    		res[i] = getMedian(array);
    	}
    	return res;
    }
	
	public static int LOWER_BOUND = 0;
	public static int RANGE = 1;
	public static int UPPER_BOUND = 2;
    
    public static double[][] getBasicStats(double[][] inputs) {
    	if (inputs.length < 1) {
    		return null;
    	}
    	if (inputs[0] == null || inputs[0].length == 0) {
    		return null;
    	}
        double[][] result = new double[3][inputs[0].length]; // supposes that inputs[i].length = const. for all i
        for (int i = 0; i < inputs[0].length; i++) {
		    result[LOWER_BOUND][i] = Double.MAX_VALUE;
		    result[UPPER_BOUND][i] = Double.MIN_VALUE;
        }
        for (int i = 0; i < inputs.length; i++) {            
            for (int j = 0; j < inputs[i].length; j++) {
	            result[LOWER_BOUND][j] = Math.min(inputs[i][j], result[LOWER_BOUND][j]);
	            result[UPPER_BOUND][j] = Math.max(result[UPPER_BOUND][j], inputs[i][j]);
            }
        }
        for (int i = 0; i < result[RANGE].length; i++) {
        	result[RANGE][i] = result[UPPER_BOUND][i] - result[LOWER_BOUND][i];
        }
        return result;
    }

    public static double[] getLowerInputs(double[][] inputs) {
    	double[][] stats = getBasicStats(inputs);
    	if (stats != null) {
    		return stats[LOWER_BOUND];
    	}
    	return null;
    }

    public static double[] getInputRanges(double[][] inputs) {
    	double[][] stats = getBasicStats(inputs);
    	if (stats != null) {
    		return stats[RANGE];
    	}
    	return null;
    }

    public static double[] getUpperInputs(double[][] inputs) {
    	double[][] stats = getBasicStats(inputs);
    	if (stats != null) {
    		return stats[UPPER_BOUND];
    	}
    	return null;
    }
    
    public static Range getBounds(double[][] inputs, int attribute) {
    	if (inputs.length < 1) {
    		return null;
    	}
    	double lower = Double.POSITIVE_INFINITY, upper = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < inputs.length; i++) {
        	if (inputs[i][attribute] < lower) {
        		lower = inputs[i][attribute];
        	}
            if (inputs[i][attribute] > upper) {
            	upper = inputs[i][attribute];
            }
        }
        return new Range(lower, upper);
    }
    
    public static Range getBounds(double[][] inputs) {
    	if (inputs.length < 1) {
    		return null;
    	}
    	double lower = Double.POSITIVE_INFINITY, upper = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < inputs.length; i++) {
        	for (int attribute = 0; attribute < inputs[i].length; attribute++) {
	        	if (inputs[i][attribute] < lower) {
	        		lower = inputs[i][attribute];
	        	}
	            if (inputs[i][attribute] > upper) {
	            	upper = inputs[i][attribute];
	            }
        	}
        }
        return new Range(lower, upper);
    }
    
    public static double[] getColumn(double[][] inputs, int index) {
    	double[] column = new double[inputs.length];
    	for (int i = 0; i < inputs.length; i++) {
    		column[i] = inputs[i][index];
    	}
    	return column;
    }
    
    public static String matrixToCode(double[][] matrix) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("{");
    	for (int i = 0; i < matrix.length; i++) {
    		if (i > 0) {
    			sb.append(",");
    		}
    		sb.append("{");
    		for (int j = 0; j < matrix[i].length; j++) {
    			if (j > 0) {
    				sb.append(",");
    			}
    			sb.append(matrix[i][j]);
    		}
    		sb.append("}");
    	}
    	sb.append("}");
    	return sb.toString();
    }
    
    public static double[][] parseMatrix(String code) {
    	code = code.replace("},{", "s").replace("{", "").replace("}", "");
    	String[] s = code.split("s");
    	double[][] res = new double[s.length][];
    	for (int i = 0; i < s.length; i++) {
    		String[] s1 = s[i].split(",");
    		res[i] = new double[s1.length];
    		for (int j = 0; j < s1.length; j++) {
    			res[i][j] = Double.parseDouble(s1[j]);
    		}
    	}
    	return res;
    }
}
