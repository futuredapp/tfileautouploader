package com.thefuntasty.tfileautouploader;

import android.net.Uri;
import android.support.annotation.NonNull;

public class FileHolder<T> {
	@NonNull public Uri path;
	@NonNull public Status status;
	public T result;

	public FileHolder(@NonNull Uri path, @NonNull Status status) {
		this(path, status, null);
	}

	public FileHolder(@NonNull Uri path, @NonNull Status status, T result) {
		this.path = path;
		this.status = status;
		this.result = result;
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
