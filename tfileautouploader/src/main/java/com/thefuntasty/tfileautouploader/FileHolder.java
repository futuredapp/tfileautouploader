package com.thefuntasty.tfileautouploader;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FileHolder<T> {
	@NonNull public Uri path;
	@NonNull public Status status;
	@Nullable public T result;

	public FileHolder(@NonNull Uri path, @NonNull Status status) {
		this.path = path;
		this.status = status;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FileHolder<?> that = (FileHolder<?>) o;

		return path.equals(that.path);
	}

	@Override public int hashCode() {
		int result1 = path.hashCode();
		result1 = 31 * result1 + status.hashCode();
		result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
		return result1;
	}
}
