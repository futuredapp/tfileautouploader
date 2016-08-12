package com.thefuntasty.tfileautouploader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class FileUploadManager<T> implements ManagerServiceContract<T>, ManagerViewContract<T> {
	private static final String TAG = FileUploadManager.class.getSimpleName();

	private ArrayList<FileHolder<T>> images;
	private OnUploadFinishedListener onUploadFinishedListener;
	private Context context;
	private AdapterContract<T> adapterContract;
	private Handler handler;

	/**
	 * Create singleton instance of this with preferred type
	 */
	public FileUploadManager(@NonNull Context context) {
		this.context = context.getApplicationContext();
		this.handler = new Handler(context.getMainLooper());

		images = new ArrayList<>();
	}

	@Override public void addAll(List<FileHolder<T>> images) {
		// Remove duplicate values
		ArrayList<FileHolder<T>> distinctImages = new ArrayList<>();
		for (FileHolder<T> image : images) {
			if (!distinctImages.contains(image)) {
				distinctImages.add(image);
			} else {
				Log.w(TAG, "Does not support duplicate images!");
			}
		}

		this.images.addAll(distinctImages);
		this.adapterContract.addAll(distinctImages);

		for (FileHolder<T> image : distinctImages) {
			if (image.status.statusType == Status.UPLOADED) {
				continue;
			}

			Intent intent = getServiceIntent(image);
			context.startService(intent);
		}
	}

	public abstract Intent getServiceIntent(FileHolder<T> image);

	public void setAdapterContract(AdapterContract<T> adapterContract) {
		this.adapterContract = adapterContract;
	}

	@Override public void removeItem(final FileHolder<T> image) {
		for (FileHolder<T> img : images) {
			if (img.equals(image)) {
				img.status.statusType = Status.REMOVED;

				handler.post(new Runnable() {
					@Override public void run() {
						adapterContract.removeItem(image);
					}
				});
				images.remove(img);

				if (onUploadFinishedListener != null && isEverythingUploaded()) {
					onUploadFinishedListener.onUploadFinished();
				}

				return;
			}
		}

		Log.w(TAG, "Try to call removeItem() with unavailable item!");
	}

	@Override public void retryItem(final FileHolder<T> image) {
		for (FileHolder<T> img : images) {
			if (img.equals(image)) {
				img.status.statusType = Status.IDLE;

				handler.post(new Runnable() {
					@Override public void run() {
						adapterContract.updateItem(image, ItemUpdate.STATUS);
					}
				});

				Intent intent = getServiceIntent(image);
				context.startService(intent);

				return;
			}
		}

		Log.w(TAG, "Try to call retryItem() with unavailable item!");
	}

	@Override public boolean isEverythingUploaded() {
		for (FileHolder<T> img : images) {
			if (img.status.statusType != Status.UPLOADED) {
				return false;
			}
		}
		return true;
	}

	@Override public void setOnUploadFinishedListener(@Nullable OnUploadFinishedListener listener) {
		onUploadFinishedListener = listener;
	}

	@Override public void reset() {
		onUploadFinishedListener = null;
		adapterContract = null;

		images.clear();
	}

	@Override public void updateItem(final FileHolder<T> image, @ItemUpdate.UpdateType final int updateType) {
		int indexOf = images.indexOf(image);

		if (indexOf != -1) {
			FileHolder<T> fileHolder = images.get(indexOf);
			fileHolder.status = image.status;
			fileHolder.path = image.path;
			fileHolder.result = image.result;

			handler.post(new Runnable() {
				@Override public void run() {
					adapterContract.updateItem(image, updateType);
				}
			});

			if (onUploadFinishedListener != null && isEverythingUploaded() && fileHolder.status.statusType == Status.UPLOADED) {
				onUploadFinishedListener.onUploadFinished();
			}

		} else {
			Log.w(TAG, "Attempts to update non-existing item!");
		}
	}

	@Override public ArrayList<FileHolder<T>> getImages() {
		return images;
	}

	@Override public FileHolder<T> getImage(Uri path) {
		for (FileHolder<T> image : images) {
			if (image.path.equals(path)) {
				return image;
			}
		}
		Log.w(TAG, "Try to getItem() with invalid uri: " + path.toString() + ", content size: " + images.size());
		return null;
	}


	public FileHolder<T> getItem(int position) {
		if (position < images.size()) {
			return images.get(position);
		} else {
			Log.w(TAG, "Try to getItem() at invalid position: " + position + ", content size: " + images.size());
			return null;
		}
	}
}
