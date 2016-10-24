package com.thefuntasty.tfileautouploader;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

public abstract class BaseItemUploadService<T> extends IntentService {

	private NotificationManager manager;
	private static final int NOTIFICATION_ID = 1111;

	private int itemCount = 0;
	private int currentItem = 0;
	private NotificationCompat.Builder notificationBuilder;

	public BaseItemUploadService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		manager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationBuilder = createNotification();
	}

	@Override
	public final int onStartCommand(Intent intent, int flags, int startId) {
		itemCount++;
		updateNotification(notificationBuilder, itemCount, currentItem);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override protected final void onHandleIntent(Intent intent) {
		Uri path = intent.getParcelableExtra("uri");
		Bundle config = intent.getBundleExtra("config");

		currentItem++;
		updateNotification(notificationBuilder, itemCount, currentItem);

		startForeground(NOTIFICATION_ID, notificationBuilder.build());

		notificationBuilder.setProgress(100, 0, false);
		manager.notify(NOTIFICATION_ID, notificationBuilder.build());

		ItemHolder<T> item = getUploadManager().getItem(path);

		if (item != null) { // item not removed
			item.status.statusType = Status.UPLOADING;
			uploadItemAndSave(item, config);
		} else {
			decreaseItemCount();
		}
	}

	protected abstract void uploadItemAndSave(@NonNull final ItemHolder<T> item, Bundle config);

	public abstract NotificationCompat.Builder createNotification();

	public abstract void updateNotification(NotificationCompat.Builder builder, int itemCount, int currentItem);

	public abstract ItemUploadManager<T> getUploadManager();

	public final void updateItemProgress(ItemHolder<T> item, int progress) {
		getUploadManager().updateItemProgress(item, progress);
	}

	public final void updateItemStatus(ItemHolder<T> item, @Status.UploadStatus int newStatus) {
		getUploadManager().updateItemStatus(item, newStatus);
	}

	public final void updateItemResult(ItemHolder<T> item, T result) {
		item.result = result;
	}

	public void showNotificationProgress(int percentage) {
		notificationBuilder.setProgress(100, percentage, false);
		manager.notify(NOTIFICATION_ID, notificationBuilder.build());
	}

	private void decreaseItemCount() {
		itemCount--;
		currentItem--;
	}

	public static Intent getStarterIntent(Context context, ItemHolder<?> image, Class<? extends BaseItemUploadService> serviceClass) {
		Intent intent = new Intent(context, serviceClass);
		intent.putExtra("uri", image.path);
		intent.putExtra("config", image.config);
		return intent;
	}
}
