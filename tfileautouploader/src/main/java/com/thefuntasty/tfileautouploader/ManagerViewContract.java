package com.thefuntasty.tfileautouploader;

import android.support.annotation.Nullable;

import com.thefuntasty.tfileautouploader.request.AddItemToUploadRequest;
import com.thefuntasty.tfileautouploader.request.AddItemsRequest;
import com.thefuntasty.tfileautouploader.request.AddUploadedItemRequest;

interface ManagerViewContract<T> extends BaseManagerContract<T> {

	void addItem(AddUploadedItemRequest request);

	void addItem(AddItemToUploadRequest request);

	void addItems(AddItemsRequest requests);

	void removeItem(ItemHolder<T> file);

	void retryItem(ItemHolder<T> file);

	boolean isEverythingUploaded();

	void setOnUploadFinishedListener(@Nullable OnUploadFinishedListener listener);

	void reset();

}