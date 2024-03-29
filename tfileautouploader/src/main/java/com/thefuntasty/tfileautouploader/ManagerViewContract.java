package com.thefuntasty.tfileautouploader;

import android.support.annotation.Nullable;

import com.thefuntasty.tfileautouploader.request.AddItemToUploadRequest;
import com.thefuntasty.tfileautouploader.request.AddItemsRequest;
import com.thefuntasty.tfileautouploader.request.AddUploadedItemRequest;

interface ManagerViewContract<T> extends BaseManagerContract<T> {

	void addItem(AddUploadedItemRequest request);

	void addItem(AddItemToUploadRequest request);

	void addItems(AddItemsRequest requests);

	void removeItem(ItemHolder<T> item);

	void retryItem(ItemHolder<T> item);

	boolean isEverythingUploaded();

	void setOnUploadFinishedListener(@Nullable OnUploadFinishedListener listener);

	void reset();

}