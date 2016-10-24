package com.thefuntasty.tfileautouploader.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.thefuntasty.tfileautouploader.BaseItemUploadService;
import com.thefuntasty.tfileautouploader.ItemHolder;
import com.thefuntasty.tfileautouploader.ItemUploadManager;
import com.thefuntasty.tfileautouploader.Status;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class MyUploadService extends BaseItemUploadService<Photo> {

	public MyUploadService() {
		super("MyUploadService");
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override public NotificationCompat.Builder createNotification() {
		return new NotificationCompat.Builder(this)
				.setContentTitle("Uploading")
				.setProgress(100, 0, false)
				.setSmallIcon(R.drawable.ic_dollar_1_normal);
	}

	@Override public void updateNotification(NotificationCompat.Builder builder, int itemCount, int currentItem) {
		builder.setContentText("Uploading photos: " + currentItem + "/" + itemCount);
	}

	@Override public ItemUploadManager<Photo> getUploadManager() {
		return MyUploadManager.get();
	}

	@Override protected void uploadItemAndSave(@NonNull final ItemHolder<Photo> item, Bundle config) {
		Observable.interval(50, TimeUnit.MILLISECONDS)
				.take(101)
				.filter(new Func1<Long, Boolean>() {
					@Override public Boolean call(Long aLong) {
						return aLong % 10 == 0;
					}
				})
				.toBlocking()
				.subscribe(new Subscriber<Long>() {
					@Override public void onCompleted() { }
					@Override public void onError(Throwable e) { }
					@Override public void onNext(Long aLong) {
						if (item.getStatus() != Status.REMOVED) {
							updateItemProgress(item, aLong.intValue());
							showNotificationProgress(aLong.intValue());
						} else {
							unsubscribe();
						}
					}
				});

		Observable.just("Fin")
				.toBlocking()
				.subscribe(new Subscriber<String>() {
					@Override public void onCompleted() { }
					@Override public void onError(Throwable e) { }
					@Override public void onNext(String s) {
						if (item.getStatus() != Status.REMOVED) {
							updateItemStatus(item, Status.UPLOADED);
						} else {
							unsubscribe();
						}
					}
				});
	}
}
