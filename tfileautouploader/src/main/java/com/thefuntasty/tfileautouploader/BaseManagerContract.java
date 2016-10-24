package com.thefuntasty.tfileautouploader;

import android.net.Uri;

import java.util.List;

interface BaseManagerContract<T> {

	List<ItemHolder<T>> getItems();

	ItemHolder<T> getItem(Uri path);
}
