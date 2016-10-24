package com.thefuntasty.tfileautouploader;

import android.support.annotation.Nullable;

import java.util.List;

interface ManagerViewContract<T> extends BaseManagerContract<T> {

	void addAll(List<FileHolder<T>> files);

	void removeItem(FileHolder<T> file);

	void retryItem(FileHolder<T> file);

	boolean isEverythingUploaded();

	void setOnUploadFinishedListener(@Nullable OnUploadFinishedListener listener);

	void reset();

}