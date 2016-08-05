package com.thefuntasty.tfileautouploader.sample;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

	static App instance;

	@Override public void onCreate() {
		super.onCreate();
		instance = this;

		Fresco.initialize(this);
	}

	public static Context context() {
		return instance.getApplicationContext();
	}
}
