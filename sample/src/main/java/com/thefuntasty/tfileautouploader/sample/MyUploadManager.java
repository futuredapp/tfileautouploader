package com.thefuntasty.tfileautouploader.sample;

import com.thefuntasty.tfileautouploader.FileUploadManager;

public class MyUploadManager {

	public static FileUploadManager<Photo> manager;


	public static FileUploadManager<Photo> get() {
		if (manager == null) {
			manager = new FileUploadManager<>(App.context(), MyUploadService.class);
		}

		return manager;
	}
}
