package com.thefuntasty.tfileautouploader;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ItemHolder<T> {
	@NonNull Uri path;
	@NonNull Status status;
	@Nullable T result;
	@Nullable Bundle config;

	public ItemHolder(@NonNull Uri path, @NonNull Status status) {
		this(path, status, null, null);
	}

	public ItemHolder(@NonNull Uri path, @NonNull Status status, @Nullable T result) {
		this(path, status, result, null);
	}

	public ItemHolder(@NonNull Uri path, @NonNull Status status, @Nullable T result, @Nullable Bundle config) {
		this.path = path;
		this.status = status;
		this.result = result;
		this.config = config;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemHolder<?> that = (ItemHolder<?>) o;

		return path.toString().equals(that.path.toString());
	}

	public @Status.UploadStatus int getStatus() {
		return status.statusType;
	}

	public int getProgress() {
		return status.progress;
	}

	@NonNull public Uri getPath() {
		return path;
	}

	@Nullable private T getResult() {
		return result;
	}

	@Override public int hashCode() {
		return path.hashCode();
	}
}
