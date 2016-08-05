package com.thefuntasty.tfileautouploader;

import android.net.Uri;

import java.util.List;

public interface BaseManagerContract<T> {

	List<FileHolder<T>> getImages();
	FileHolder<T> getImage(Uri path);
}
