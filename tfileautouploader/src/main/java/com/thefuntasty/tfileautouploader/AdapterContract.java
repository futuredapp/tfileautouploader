package com.thefuntasty.tfileautouploader;

import android.support.annotation.UiThread;

import java.util.List;

public interface AdapterContract<T> {
	@UiThread void itemUploadProgressUpdate(FileHolder<T> file);

	@UiThread void itemStatusUpdate(FileHolder<T> file);

	@UiThread void itemsAdded(List<FileHolder<T>> files);
}
