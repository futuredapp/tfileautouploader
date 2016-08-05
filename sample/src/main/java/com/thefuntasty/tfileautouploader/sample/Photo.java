package com.thefuntasty.tfileautouploader.sample;

import android.graphics.Color;
import android.net.Uri;

public class Photo {
	public String uid;
	public Integer width;
	public Integer height;
	public String url;
	public String thumbnailUrl;
	public String dominantColor;

	public Uri getUri() {
		return Uri.parse(url);
	}

	public Uri getThumbnailUri() {
		return Uri.parse(thumbnailUrl);
	}

	public float getAspectRatio() {
		if (height != null && width != null) {
			return (float) width / (float) height;
		} else {
			return 1;
		}
	}

	public int getDominantColor() {
		if (dominantColor != null) {
			return Color.parseColor(dominantColor);
		} else {
			return 0;
		}
	}
}