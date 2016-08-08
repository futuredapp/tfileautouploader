package com.thefuntasty.tfileautouploader.sample;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import com.thefuntasty.tfileautouploader.FileHolder;
import com.thefuntasty.tfileautouploader.FileUploadManager;
import com.thefuntasty.tfileautouploader.OnUploadFinishedListener;
import com.thefuntasty.tfileautouploader.Status;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

	private final int PICK_IMAGE_MULTIPLE = 1;
	private FileUploadManager<Photo> uploadManager;
	private RecyclerView recycler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		recycler = (RecyclerView) findViewById(R.id.recycler);
		recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

		uploadManager = MyUploadManager.get();
		final ImageAdapter adapter = new ImageAdapter(this, new View.OnClickListener() {
			@Override public void onClick(View view) {
				int pos = recycler.getChildLayoutPosition(view);
				FileHolder<Photo> item = uploadManager.getItem(pos);
					uploadManager.removeItem(item);
			}
		});

		uploadManager.setAdapterContract(adapter);
		uploadManager.setOnUploadFinishedListener(new OnUploadFinishedListener() {
			@Override public void onUploadFinished() {
				Log.d("MainActivity", "finished");
			}
		});

		recycler.setAdapter(adapter);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		uploadManager.reset();
	}

	@OnClick(R.id.add)
	void requestImages() {
		RxPermissions.getInstance(MainActivity.this)
				.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.subscribe(new Action1<Boolean>() {
					@Override
					public void call(Boolean granted) {
						if (granted) {
							startActivityForResult(Intent.createChooser(TIntent.createLibraryIntent(true), "Select picture"), PICK_IMAGE_MULTIPLE);
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
				&& null != data) {
			List<Uri> imagesEncodedList = TIntent.getUrisFromLibrary(this, data);

			ArrayList<FileHolder<Photo>> fileHolders = new ArrayList<>();
			if (imagesEncodedList != null && !imagesEncodedList.isEmpty()) {
				for (Uri uri : imagesEncodedList) {
					fileHolders.add(new FileHolder<Photo>(uri, new Status()));
				}

				if (fileHolders.size() != 0) {
					MyUploadManager.get().addAll(fileHolders);
				}
			} else {
				Toast.makeText(this, "No images recognized", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
