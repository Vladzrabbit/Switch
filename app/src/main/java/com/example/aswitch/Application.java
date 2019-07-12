package com.example.aswitch;

import android.preference.PreferenceManager;


public class Application extends android.app.Application
{
	private AppSettings settings;

	public AppSettings getSettings()
	{
		return settings;
	}

	@Override
	public void onCreate()
	{
		settings.load();
		settings = new AppSettings(PreferenceManager.getDefaultSharedPreferences(this));

		super.onCreate();
	}
}
