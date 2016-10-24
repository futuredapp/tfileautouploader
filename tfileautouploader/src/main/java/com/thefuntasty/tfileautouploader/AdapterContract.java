package com.thefuntasty.tfileautouploader;

import android.support.annotation.UiThread;

import java.util.List;

public interface AdapterContract<T> {
	@UiThread void itemUploadProgressUpdate(ItemHolder<T> item);

	@UiThread void itemStatusUpdate(ItemHolder<T> item);

	@UiThread void itemsAdded(List<ItemHolder<T>> items);

	@UiThread void itemAdded(ItemHolder<T> item);
}
