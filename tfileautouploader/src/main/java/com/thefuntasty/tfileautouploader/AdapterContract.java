package com.thefuntasty.tfileautouploader;

import android.support.annotation.UiThread;

import java.util.List;

public interface AdapterContract<T> {
	@UiThread void updateItem(FileHolder<T> file, @ItemUpdate.UpdateType int updateType);

	@UiThread void removeItem(FileHolder<T> file);

	@UiThread void addAll(List<FileHolder<T>> files);
}
