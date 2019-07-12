package com.example.aswitch;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Display;

public class Utils
{
	@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static boolean isScreenOn(Context context)
	{
		DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
		for (Display display : dm.getDisplays())
		{
			if (display.getState() != Display.STATE_OFF)
				return true;
		}
		return false;
	}

	public static void lowPassFilter(float[] input, float[] output, float alpha)
	{
		for (int i = 0; i < input.length; i++)
			output[i] = output[i] + alpha * (input[i] - output[i]);
	}

	public static float rangeValue(float value, float min, float max)
	{
		if (value > max) return max;
		if (value < min) return min;
		return value;
	}

	public static float fixNanOrInfinite(float value)
	{
		if (Float.isNaN(value) || Float.isInfinite(value)) return 0;
		return value;
	}
}
