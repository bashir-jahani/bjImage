package bj.modules.bj_image;

import android.content.Context;
import android.print.PrintAttributes;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import bj.modules.bj_image_classes;

public class bj_imagesAlbum_RecyclerViewAdapter extends RecyclerView.Adapter<bj_imagesAlbum_RecyclerViewAdapter.SelectableViewHolder> {
	String TAG="bj_imagesAlbum_RecyclerViewAdapter";
	@bj_image_classes.FileNotice int fileNoticeForShow;
	private ArrayList<bj_imageNotice> _imagesPath;
	Context context;
	ViewGroup.LayoutParams layoutParams;
	int padding=0;
	bj_image_classes.OnImageSelectListener onImageSelectListener;
	bj_image_classes.OnImageClickListener onImageClickListener;
	boolean isHorizontal;

	public bj_imagesAlbum_RecyclerViewAdapter(Context context, ArrayList<bj_imageNotice> _imagesPath, boolean isHorizontal,@bj_image_classes.FileNotice int fileNoticeForShow, bj_image_classes.OnImageSelectListener onImageSelectListener) {
		this._imagesPath = _imagesPath;
		this.context = context;
		this.isHorizontal=isHorizontal;
		this.onImageSelectListener=onImageSelectListener;

		this.fileNoticeForShow=fileNoticeForShow;
	}

	public void setViewHolderLayoutParams(ViewGroup.LayoutParams layoutParams, int padding) {
		this.layoutParams = layoutParams;
		this.padding=padding;
	}

	@NonNull
	@Override
	public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// create a new view

		ConstraintLayout v=(ConstraintLayout) LayoutInflater.from(parent.getContext())
				.inflate(R.layout.bj_images_seletable_image, parent, false);
		if (layoutParams!=null)
			v.setLayoutParams(layoutParams);
		v.setPadding(padding,padding,padding,padding);
		SelectableViewHolder vh = new SelectableViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull final SelectableViewHolder holder, final int position) {
		String imageNotice=null;
		switch (fileNoticeForShow){
			case bj_image_classes.FileNotice
					.ALBUME_NAME:
				imageNotice= _imagesPath.get(position).ImageName;
			case bj_image_classes.FileNotice
					.NAME:
				imageNotice= _imagesPath.get(position).ImageAlbume;
			case bj_image_classes.FileNotice
					.ALBUME_NAME_FILE_NAME:
				imageNotice= _imagesPath.get(position).ImageAlbume+"/"+ _imagesPath.get(position).ImageName;;
			case bj_image_classes.FileNotice
					.DATE_TAKEN:
				imageNotice= _imagesPath.get(position).GetDateTakenString();
			case bj_image_classes.FileNotice
					.SIZE:
				long fSize= _imagesPath.get(position).ImageSize;
				String fSizeK=" Byte";


				if (fSize>1024){
					fSize=fSize/1024;
					fSizeK=" KByte";
				}
				if (fSize>1024){
					fSize=fSize/1024;
					fSizeK=" MByte";
				}
				if (fSize>1024){
					fSize=fSize/1024;
					fSizeK=" GByte";
				}
				if (fSize>1024){
					fSize=fSize/1024;
					fSizeK=" TByte";
				}
				imageNotice=fSize+fSizeK;
		}
		//Log.i(TAG, "onCheckedChanged:position "+position+"  _imagesPath.get(position).selected: "+_imagesPath.get(position).selected);
		Glide.with(context).load(_imagesPath.get(position).GetImagePath()).into(holder.imageView);
		holder.textView.setText(imageNotice);
		holder.checkBox.setChecked(_imagesPath.get(position).selected);
		holder.index=position;
		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (onImageSelectListener!=null && _imagesPath.get(position).selected!=isChecked){
					onImageSelectListener.onSelectionChanged(buttonView,isChecked,position,_imagesPath.get(position));
				}

			//	Log.i(TAG, "onCheckedChanged:position "+position+"  _imagesPath.get(position).selected change: "+_imagesPath.get(position).selected);
			}
		});
		if (onImageClickListener!=null){
			holder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onImageClickListener.onClick(v,position,_imagesPath.get(position));
				}
			});
		}
	}

	@Override
	public int getItemCount() {

		return _imagesPath.size();
	}

	public class SelectableViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView textView;
		CheckBox checkBox;
		int index;
		public SelectableViewHolder(@NonNull View itemView) {

			super(itemView);
			//Log.i(TAG, "SelectableViewHolder: new ViewHolder");
			imageView=(ImageView) itemView.findViewById(R.id.BJISIimage);
			textView=(TextView) itemView.findViewById(R.id.BJISItextView);
			checkBox=(CheckBox) itemView.findViewById(R.id.BJISIcheckBox);
		}
	}

	@Override
	public void onViewRecycled(@NonNull SelectableViewHolder holder) {
		super.onViewRecycled(holder);
		holder.checkBox.setOnCheckedChangeListener(null);
		//Log.i(TAG, "onViewRecycled: "+holder.index);
	}

	public void setOnImageClickListener(bj_image_classes.OnImageClickListener onImageClickListener) {
		this.onImageClickListener = onImageClickListener;
	}

	public void setOnImageSelectListener(bj_image_classes.OnImageSelectListener onImageSelectListener) {
		this.onImageSelectListener = onImageSelectListener;
	}
}
