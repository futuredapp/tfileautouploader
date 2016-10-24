package com.thefuntasty.tfileautouploader.sample;

import com.thefuntasty.tfileautouploader.ItemUploadManager;

public class MyUploadManager {

	private static ItemUploadManager<Photo> manager;

	public static ItemUploadManager<Photo> get() {
		if (manager == null) {
			manager = new ItemUploadManager<>(App.context(), MyUploadService.class);
		}

		return manager;
	}
}
