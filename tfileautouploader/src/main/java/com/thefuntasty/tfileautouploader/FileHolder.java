package com.thefuntasty.tfileautouploader;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FileHolder<T> {
	@NonNull public Uri path;
	@NonNull public Status status;
	public T result;
	@Nullable public Bundle config;

	public FileHolder(@NonNull Uri path, @NonNull Status status) {
		this(path, status, null);
	}

	public FileHolder(@NonNull Uri path, @NonNull Status status, T result) {
		this(path, status, result, null);
	}

	public FileHolder(@NonNull Uri path, @NonNull Status status, T result, @Nullable Bundle config) {
		this.path = path;
		this.status = status;
		this.result = result;
		this.config = config;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FileHolder<?> that = (FileHolder<?>) o;

		return path.toString().equals(that.path.toString());

	}

	@Override public int hashCode() {
		return path.hashCode();
	}
}
