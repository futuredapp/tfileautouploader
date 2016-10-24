package com.thefuntasty.tfileautouploader;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Status {
	public static final int WAITING = 0;
	public static final int UPLOADING = 1;
	public static final int UPLOADED = 2;
	public static final int FAILED = 3;
	public static final int REMOVED = 4;

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({WAITING, UPLOADING, UPLOADED, FAILED, REMOVED})
	public @interface UploadStatus {}

	// TODO decrease visibility
	public int progress = 0;
	public @UploadStatus int statusType = WAITING;

	private Status(int statusType) {
		this.statusType = statusType;
	}

	private Status(int statusType, int progress) {
		this.statusType = statusType;
		this.progress = progress;
	}

	public static Status create(@UploadStatus int statusType) {
		return new Status(statusType);
	}

	public static Status createProgress(int progress) {
		return new Status(UPLOADING, progress);
	}
}
