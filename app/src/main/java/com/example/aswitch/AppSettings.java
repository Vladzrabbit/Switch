package com.example.aswitch;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings extends SettingActivity
{
	public static AppSettings getAppSettings(Context context)
	{
		if (context instanceof Application) return ((Application) context).getSettings();

		Context appContext = context.getApplicationContext();
		if (appContext instanceof Application) return ((Application) appContext).getSettings();

		return null;
	}

	private final SharedPreferences mSettings;

	public AppSettings(SharedPreferences mSettings)
	{
		this.mSettings = mSettings;
	}

	public void load()
	{
		load(mSettings);
	}

	public void save()
	{
		save(mSettings);
	}

	public void saveDeferred()
	{
		saveDeferred(mSettings);
	}
}
