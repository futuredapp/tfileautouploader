package com.thefuntasty.tfileautouploader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.thefuntasty.tfileautouploader.request.AddItemToUploadRequest;
import com.thefuntasty.tfileautouploader.request.AddItemsRequest;
import com.thefuntasty.tfileautouploader.request.AddUploadedItemRequest;
import com.thefuntasty.tfileautouploader.request.AddableItem;

import java.util.ArrayList;

public class ItemUploadManager<T> implements ManagerViewContract<T> {
	private static final String TAG = ItemUploadManager.class.getSimpleName();

	private ArrayList<ItemHolder<T>> images;
	private OnUploadFinishedListener onUploadFinishedListener;
	private Context context;
	private AdapterContract<T> adapterContract;
	private Handler handler;
	private Class<? extends BaseItemUploadService> serviceClass;

	/**
	 * Create singleton instance of this with preferred type
	 */
	public ItemUploadManager(@NonNull Context context, Class<? extends BaseItemUploadService> serviceClass) {
		this.context = context.getApplicationContext();
		this.handler = new Handler(context.getMainLooper());
		this.serviceClass = serviceClass;

		images = new ArrayList<>();
	}

	public void setAdapterContract(AdapterContract<T> adapterContract) {
		this.adapterContract = adapterContract;
	}

	@Override public void addItem(AddUploadedItemRequest request) {
		if (itemAlreadyAdded(request.getUri())) return;

		ItemHolder<T> holder = new ItemHolder<>(request.getUri(), Status.create(Status.UPLOADED));
		this.images.add(holder);
		this.adapterContract.itemAdded(holder);
	}

	@Override public void addItem(AddItemToUploadRequest request) {
		if (itemAlreadyAdded(request.getUri())) return;

		ItemHolder<T> holder = new ItemHolder<>(request.getUri(), Status.create(Status.WAITING), null, request.getConfig());
		this.images.add(holder);
		this.adapterContract.itemAdded(holder);

		Intent intent = getServiceIntent(holder);
		context.startService(intent);
	}

	@Override public void addItems(AddItemsRequest request) {
		for (AddableItem item : request.getItems()) {
			if (item instanceof AddItemToUploadRequest) {
				addItem((AddItemToUploadRequest) item);
			} else if (item instanceof AddUploadedItemRequest) {
				addItem((AddUploadedItemRequest) item);
			}
		}
	}

	@Override public void removeItem(final ItemHolder<T> image) {
		for (ItemHolder<T> img : images) {
			if (img.equals(image)) {
				img.status.statusType = Status.REMOVED;

				handler.post(new Runnable() {
					@Override public void run() {
						adapterContract.itemStatusUpdate(image);
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

	@Override public void retryItem(final ItemHolder<T> image) {
		for (ItemHolder<T> img : images) {
			if (img.equals(image)) {
				img.status.statusType = Status.WAITING;

				handler.post(new Runnable() {
					@Override public void run() {
						adapterContract.itemStatusUpdate(image);
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
		for (ItemHolder<T> img : images) {
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

	void updateItemProgress(final ItemHolder<T> item, int value) {
		int indexOf = images.indexOf(item);

		if (indexOf != -1) {
			ItemHolder<T> itemHolder = images.get(indexOf);
			itemHolder.status = item.status;
			itemHolder.status.progress = value;
			itemHolder.path = item.path;
			itemHolder.result = item.result;

			handler.post(new Runnable() {
				@Override public void run() {
					adapterContract.itemUploadProgressUpdate(item);
				}
			});
		} else {
			Log.w(TAG, "Attempts to update non-existing item!");
		}
	}

	void updateItemStatus(final ItemHolder<T> item, @Status.UploadStatus int newStatus) {
		int indexOf = images.indexOf(item);

		if (indexOf != -1) {
			ItemHolder<T> itemHolder = images.get(indexOf);
			itemHolder.status.statusType = newStatus;
			itemHolder.path = item.path;
			itemHolder.result = item.result;

			handler.post(new Runnable() {
				@Override public void run() {
					adapterContract.itemStatusUpdate(item);
				}
			});

			if (onUploadFinishedListener != null && isEverythingUploaded() && itemHolder.status.statusType == Status.UPLOADED) {
				onUploadFinishedListener.onUploadFinished();
			}

		} else {
			Log.w(TAG, "Attempts to update non-existing item!");
		}
	}

	@Override public ArrayList<ItemHolder<T>> getItems() {
		return images;
	}

	@Override public ItemHolder<T> getItem(Uri path) {
		for (ItemHolder<T> image : images) {
			if (image.path.equals(path)) {
				return image;
			}
		}
		Log.w(TAG, "Try to getItem() with invalid uri: " + path.toString() + ", content size: " + images.size());
		return null;
	}


	public ItemHolder<T> getItem(int position) {
		if (position < images.size()) {
			return images.get(position);
		} else {
			Log.w(TAG, "Try to getItem() at invalid position: " + position + ", content size: " + images.size());
			return null;
		}
	}

	private boolean itemAlreadyAdded(Uri uri) {
		for (ItemHolder<T> image : images) {
			if (image.getPath().equals(uri)) {
				Log.w(TAG, "Does not support duplicate images!");
				return true;
			}
		}
		return false;
	}

	private Intent getServiceIntent(ItemHolder<T> image) {
		return BaseItemUploadService.getStarterIntent(context, image, serviceClass);
	}
}
