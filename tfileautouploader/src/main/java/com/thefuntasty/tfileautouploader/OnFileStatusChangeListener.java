package com.thefuntasty.tfileautouploader;

public interface OnFileStatusChangeListener<T> {
	void onImageStatusChanged(FileHolder<T> file);
}
