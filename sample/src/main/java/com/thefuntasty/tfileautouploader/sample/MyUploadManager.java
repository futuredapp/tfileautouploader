package com.thefuntasty.tfileautouploader.sample;

import android.content.Intent;

import com.thefuntasty.tfileautouploader.FileHolder;
import com.thefuntasty.tfileautouploader.FileUploadManager;

public class MyUploadManager {

	public static FileUploadManager<Photo> manager;


	public static FileUploadManager<Photo> get() {
		if (manager == null) {
			manager = new FileUploadManager<Photo>(App.context()) {

				@Override public Intent getServiceIntent(FileHolder<Photo> image) {
					return MyUploadService.getStarterIntent(image);
				}
			};
		}

		return manager;
	}
}
