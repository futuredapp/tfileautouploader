package com.thefuntasty.tfileautouploader.sample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import com.thefuntasty.tfileautouploader.AdapterContract;
import com.thefuntasty.tfileautouploader.FileHolder;
import com.thefuntasty.tfileautouploader.Status;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements AdapterContract<Photo> {

	private ArrayList<FileHolder<Photo>> images;
	private Context context;
	private final View.OnClickListener listener;
	private Object payload;
	private int imageSize;
	private RecyclerView attachedRecycler;

	public ImageAdapter(Context context, View.OnClickListener listener) {
		this.context = context;
		this.listener = listener;
		this.payload = new Object();
		this.images = new ArrayList<>();
		this.imageSize = Math.round(convertDpToPixel(150, context));

		notifyDataSetChanged();
	}

	@Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		attachedRecycler = recyclerView;
	}

	@Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
	}

	@Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override public void onBindViewHolder(ViewHolder holder, int position) {
		FileHolder<Photo> fileHolder = images.get(position);
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(fileHolder.getPath())
				.setResizeOptions(new ResizeOptions(imageSize, imageSize))
				.build();
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setOldController(holder.image.getController())
				.setImageRequest(request)
				.build();
		holder.image.setController(controller);
		holder.uploadIndicator.setBackgroundColor(getColorForStatus(fileHolder.getStatus()));
		holder.progressBar.setProgress(fileHolder.getProgress());

		holder.itemView.setOnClickListener(listener);
	}

	@Override public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
		if (!payloads.contains(payload)) {
			onBindViewHolder(holder, position);
		}

		// prevents from laggy animation experience when item is removed from recycler and progress is updated frequently
		if (attachedRecycler != null && attachedRecycler.isAnimating()) {
			return;
		}

		FileHolder<Photo> fileHolder = images.get(position);
		holder.uploadIndicator.setBackgroundColor(getColorForStatus(fileHolder.getStatus()));
		holder.progressBar.setProgress(fileHolder.getProgress());
	}

	@Override public int getItemCount() {
		return images.size();
	}

	private void refreshItem(FileHolder<Photo> image) {
		int index = images.indexOf(image);
		images.set(index, image);
		notifyItemChanged(index, payload);
	}

	private void removeItem(FileHolder<Photo> image) {
		int index = images.indexOf(image);
		images.remove(index);
		notifyItemRemoved(index);
	}

	@Override public void itemUploadProgressUpdate(FileHolder<Photo> file) {
		refreshItem(file);
	}

	@Override public void itemStatusUpdate(FileHolder<Photo> file) {
		if (file.getStatus() == Status.REMOVED) {
			removeItem(file);
		} else {
			refreshItem(file);
		}
	}

	@Override
	@UiThread
	public void itemsAdded(List<FileHolder<Photo>> images) {
		int startPosition = this.images.size();
		this.images.addAll(images);
		notifyItemRangeInserted(startPosition, images.size());
	}

	@Override
	@UiThread
	public void itemAdded(FileHolder<Photo> file) {
		this.images.add(file);
		notifyItemInserted(images.size() - 1);
	}


	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.image) SimpleDraweeView image;
		@BindView(R.id.progress_bar) ProgressBar progressBar;
		@BindView(R.id.upload_indicator) View uploadIndicator;

		ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	private int getColorForStatus(@Status.UploadStatus int statusType) {
		switch (statusType) {
			case Status.FAILED:
				return Color.RED;
			default:
			case Status.WAITING:
				return Color.YELLOW;
			case Status.REMOVED:
				return Color.BLACK;
			case Status.UPLOADED:
				return Color.GREEN;
			case Status.UPLOADING:
				return Color.BLUE;
		}
	}

	private static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}
}
