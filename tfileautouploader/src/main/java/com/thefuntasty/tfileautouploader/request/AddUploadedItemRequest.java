package com.thefuntasty.tfileautouploader.request;

import android.net.Uri;

public class AddUploadedItemRequest implements AddableItem {

	private Uri uri;

	public AddUploadedItemRequest(Uri uri) {
		this.uri = uri;
	}

	@Override
	public Uri getUri() {
		return uri;
	}
}