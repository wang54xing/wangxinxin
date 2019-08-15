package com.fulaan.utils;

import java.math.BigDecimal;

public class DoubleUtil {
	public static double add(double a, double b) {
		return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).doubleValue();
	}

	public static double subtract(double a, double b) {
		return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue();
	}

	public static double multiply(double a, double b) {
		return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue();
	}

	public static double divide(double a, double b, int scale) {
		return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
