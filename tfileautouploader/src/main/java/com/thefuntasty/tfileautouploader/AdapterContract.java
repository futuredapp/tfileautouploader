package com.thefuntasty.tfileautouploader;

import android.support.annotation.UiThread;

import java.util.List;

public interface AdapterContract<T> {
	@UiThread void itemUploadProgressUpdate(ItemHolder<T> file);

	@UiThread void itemStatusUpdate(ItemHolder<T> file);

	@UiThread void itemsAdded(List<ItemHolder<T>> files);

	@UiThread void itemAdded(ItemHolder<T> file);
}
