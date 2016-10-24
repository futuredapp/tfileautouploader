package com.thefuntasty.tfileautouploader.request;

import android.net.Uri;
import android.os.Bundle;

public class AddItemToUploadRequest implements AddableItem {
	private Bundle config;
	private Uri uri;

	public AddItemToUploadRequest(Uri uri) {
		this.uri = uri;
	}

	public AddItemToUploadRequest(Uri uri, Bundle config) {
		this.uri = uri;
		this.config = config;
	}

	public Bundle getConfig() {
		return config;
	}

	@Override
	public Uri getUri() {
		return uri;
	}
}
