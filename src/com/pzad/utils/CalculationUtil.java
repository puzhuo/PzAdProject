package com.pzad.utils;

import android.content.Context;

public class CalculationUtil {
	
	public static int dip2px(Context context, int dip){
		return (int) (dip * SystemMeasurementUtil.getScreenDensity(context) + 0.5F);
	}
}
