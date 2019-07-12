package com.example.aswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootCompletedReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		/*AppSettings settings = AppSettings.getAppSettings(context);
		if (settings.isServiceEnabled())
		{
			context.startService(new Intent(context, StabilizationService.class));
		}
		*/
	}
}
