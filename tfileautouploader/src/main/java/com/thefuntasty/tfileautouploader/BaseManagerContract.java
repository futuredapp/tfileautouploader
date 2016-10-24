package com.thefuntasty.tfileautouploader;

import android.net.Uri;

import java.util.List;

interface BaseManagerContract<T> {

	List<FileHolder<T>> getItems();

	FileHolder<T> getItem(Uri path);
}
