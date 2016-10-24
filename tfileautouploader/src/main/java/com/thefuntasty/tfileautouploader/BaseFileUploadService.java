package com.thefuntasty.tfileautouploader;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

public abstract class BaseFileUploadService<T> extends IntentService {

	private NotificationManager manager;
	private static final int NOTIFICATION_ID = 1111;

	private int fileCount = 0;
	private int currentFile = 0;
	private NotificationCompat.Builder notificationBuilder;

	public BaseFileUploadService(String name) {
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
		fileCount++;
		updateNotification(notificationBuilder, fileCount, currentFile);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override protected final void onHandleIntent(Intent intent) {
		Uri path = intent.getParcelableExtra("uri");
		Bundle config = intent.getBundleExtra("config");

		currentFile++;
		updateNotification(notificationBuilder, fileCount, currentFile);

		startForeground(NOTIFICATION_ID, notificationBuilder.build());

		notificationBuilder.setProgress(100, 0, false);
		manager.notify(NOTIFICATION_ID, notificationBuilder.build());

		ItemHolder<T> file = getUploadManager().getItem(path);

		if (file != null) { // file not removed
			file.status.statusType = Status.UPLOADING;
			uploadFileAndSave(file, config);
		} else {
			decreaseFileCount();
		}
	}

	protected abstract void uploadFileAndSave(@NonNull final ItemHolder<T> file, Bundle config);

	public abstract NotificationCompat.Builder createNotification();

	public abstract void updateNotification(NotificationCompat.Builder builder, int fileCount, int currentFile);

	public abstract FileUploadManager<T> getUploadManager();

	public final void updateItemProgress(ItemHolder<T> file, int progress) {
		getUploadManager().updateItemProgress(file, progress);
	}

	public final void updateItemStatus(ItemHolder<T> file, @Status.UploadStatus int newStatus) {
		getUploadManager().updateItemStatus(file, newStatus);
	}

	public final void updateItemResult(ItemHolder<T> file, T result) {
		file.result = result;
	}

	public void showNotificationProgress(int percentage) {
		notificationBuilder.setProgress(100, percentage, false);
		manager.notify(NOTIFICATION_ID, notificationBuilder.build());
	}

	private void decreaseFileCount() {
		fileCount--;
		currentFile--;
	}

	public static Intent getStarterIntent(Context context, ItemHolder<?> image, Class<? extends BaseFileUploadService> serviceClass) {
		Intent intent = new Intent(context, serviceClass);
		intent.putExtra("uri", image.path);
		intent.putExtra("config", image.config);
		return intent;
	}
}
