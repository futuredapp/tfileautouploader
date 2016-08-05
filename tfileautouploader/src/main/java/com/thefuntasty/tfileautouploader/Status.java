package com.thefuntasty.tfileautouploader;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Status {
	public static final int IDLE = 0;
	public static final int UPLOADING = 1;
	public static final int UPLOADED = 2;
	public static final int FAILED = 3;
	public static final int REMOVED = 4;

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({IDLE, UPLOADING, UPLOADED, FAILED, REMOVED})
	public @interface UploadStatus {}

	public int progress = 0;
	public @UploadStatus int statusType = IDLE;
}
