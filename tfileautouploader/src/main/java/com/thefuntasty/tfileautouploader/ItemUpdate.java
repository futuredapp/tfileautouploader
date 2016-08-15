package com.thefuntasty.tfileautouploader;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ItemUpdate {
	public static final int STATUS = 0;
	public static final int PROGRESS = 1;

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({STATUS, PROGRESS})
	public @interface UpdateType {}
}
