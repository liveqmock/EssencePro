package com.util;

import java.text.DecimalFormat;

public class DoubleUtil {
	/**
	 * 将double类型的数值保留小数点后两位输出
	 */
	public static double round(double d) {
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.parseDouble(df.format(d));
	}

	public static String NumberFormat(double c) {
		DecimalFormat format = new DecimalFormat("#0.00");
		String formatedNumber = format.format(c);

		return formatedNumber;
	}

}
