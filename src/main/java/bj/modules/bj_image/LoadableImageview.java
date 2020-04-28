package bj.modules.bj_image;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import bj.modules.bj_image_objects.imagesView_TouchImageView;

public class LoadableImageview extends LinearLayout {

	FrameLayout FL_Base;
	public ProgressBar Progressbar;
	public ImageView ProgressbarButton;
	public imagesView_TouchImageView touchImageView=null;
	public ImageView imageView=null;
	public TextView TextViewForImageNotice;
	public String vNotice;
	public CheckBox checkBox;


	public LoadableImageview(Context context, String notice) {
		super(context);
		vNotice=notice;
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		setPadding(0,0,0,0);
		setOrientation(LinearLayout.VERTICAL);

		FL_Base=new FrameLayout(context);
		FL_Base.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		FL_Base.setPadding(0,0,0,0);

		TextViewForImageNotice=new TextView( getContext());
		TextViewForImageNotice.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			TextViewForImageNotice.setTextAppearance(R.style.TextAppearance_Compat_Notification);
		}else {
			TextViewForImageNotice.setTextAppearance(getContext(), R.style.TextAppearance_Compat_Notification);
		}
		TextViewForImageNotice.setBackgroundColor(getContext().getResources().getColor(R.color.black_overlay));
		TextViewForImageNotice.setTextColor(getContext().getResources().getColor(R.color.white_overlay));
		TextViewForImageNotice.setPadding(2,2,2,10);
		TextViewForImageNotice.setText(vNotice);
		TextViewForImageNotice.setGravity(Gravity.BOTTOM);

		if (vNotice==null || vNotice.equals("")) {
			TextViewForImageNotice.setVisibility(GONE);
		}else {
			TextViewForImageNotice.setVisibility(VISIBLE);
		}

		touchImageView =new imagesView_TouchImageView(context);
		touchImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));



		//set progress downloading
		Progressbar =new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
		Progressbar.setLayoutParams(new LayoutParams(bj_image.getpixels(context,70), bj_image.getpixels(context,70)));
		Progressbar.setMax(100);
		Progressbar.setIndeterminate(false);
		Progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_uncomplete));


		Progressbar.setVisibility(GONE);


		//set ButtonForDownloading
		ProgressbarButton =new ImageView(context,null,android.R.attr.progressBarStyle);
		ProgressbarButton.setLayoutParams(new LayoutParams(bj_image.getpixels(context, 50), bj_image.getpixels(context, 50)));
		ProgressbarButton.setClickable(true);
		ProgressbarButton.setImageResource(R.drawable.icon_download);

		ProgressbarButton.setVisibility(GONE);
		ProgressbarButton.setVisibility(GONE);

		FL_Base.addView(touchImageView);
		FL_Base.addView(Progressbar);

		FL_Base.addView(ProgressbarButton);

		FL_Base.addView(TextViewForImageNotice);
		addView(FL_Base);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) TextViewForImageNotice.getLayoutParams();
		params.gravity = Gravity.BOTTOM;
		TextViewForImageNotice.setLayoutParams(params);

		params = (FrameLayout.LayoutParams) Progressbar.getLayoutParams();
		params.gravity = Gravity.CENTER;
		Progressbar.setLayoutParams(params);


		params = (FrameLayout.LayoutParams) ProgressbarButton.getLayoutParams();
		params.gravity = Gravity.CENTER;
		ProgressbarButton.setLayoutParams(params);

	}
	public LoadableImageview(Context context, String notice,boolean selected,CompoundButton.OnCheckedChangeListener listener) {
		super(context);
		vNotice=notice;
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		setPadding(0,0,0,0);
		setOrientation(LinearLayout.VERTICAL);

		FL_Base=new FrameLayout(context);
		FL_Base.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		FL_Base.setPadding(0,0,0,0);

		TextViewForImageNotice=new TextView( context);
		checkBox=new CheckBox(context);
		checkBox.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		checkBox.setChecked(selected);
		checkBox.setVisibility(VISIBLE);

		checkBox.setOnCheckedChangeListener(listener);
		checkBox.setGravity(Gravity.TOP);
		checkBox.setPadding(10,10,10,0);
		TextViewForImageNotice.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			TextViewForImageNotice.setTextAppearance(R.style.TextAppearance_Compat_Notification);
		}else {
			TextViewForImageNotice.setTextAppearance(getContext(), R.style.TextAppearance_Compat_Notification);
		}
		TextViewForImageNotice.setBackgroundColor(getContext().getResources().getColor(R.color.black_overlay));
		TextViewForImageNotice.setTextColor(getContext().getResources().getColor(R.color.white_overlay));
		TextViewForImageNotice.setPadding(2,2,2,10);
		TextViewForImageNotice.setText(vNotice);
		TextViewForImageNotice.setGravity(Gravity.BOTTOM);

		if (vNotice==null || vNotice.equals("")) {
			TextViewForImageNotice.setVisibility(GONE);
		}else {
			TextViewForImageNotice.setVisibility(VISIBLE);
		}

		imageView =new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));



		//set progress downloading
		Progressbar =new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
		Progressbar.setLayoutParams(new LayoutParams(bj_image.getpixels(context,70), bj_image.getpixels(context,70)));
		Progressbar.setMax(100);
		Progressbar.setIndeterminate(false);
		Progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_uncomplete));


		Progressbar.setVisibility(GONE);


		//set ButtonForDownloading
		ProgressbarButton =new ImageView(context,null,android.R.attr.progressBarStyle);
		ProgressbarButton.setLayoutParams(new LayoutParams(bj_image.getpixels(context, 50), bj_image.getpixels(context, 50)));
		ProgressbarButton.setClickable(true);
		ProgressbarButton.setImageResource(R.drawable.icon_download);

		ProgressbarButton.setVisibility(GONE);
		ProgressbarButton.setVisibility(GONE);

		FL_Base.addView(imageView);
		FL_Base.addView(Progressbar);

		FL_Base.addView(ProgressbarButton);

		FL_Base.addView(TextViewForImageNotice);
		FL_Base.addView(checkBox);
		addView(FL_Base);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) TextViewForImageNotice.getLayoutParams();
		params.gravity = Gravity.BOTTOM;
		TextViewForImageNotice.setLayoutParams(params);

		params = (FrameLayout.LayoutParams) Progressbar.getLayoutParams();
		params.gravity = Gravity.CENTER;
		Progressbar.setLayoutParams(params);


		params = (FrameLayout.LayoutParams) ProgressbarButton.getLayoutParams();
		params.gravity = Gravity.CENTER;
		ProgressbarButton.setLayoutParams(params);

	}
	public void SetProgresOn(boolean indeterminate, int percent,boolean cancelable){
		//Log.i("LoadableImageview", "SetProgresOn: "+vNotice+" indeterminate: "+indeterminate+" cancelable: "+cancelable+" " +percent + " %");
		Progressbar.setIndeterminate(indeterminate);
		Progressbar.setProgress(percent);
		Progressbar.setVisibility(VISIBLE);
		if (cancelable) {
			ProgressbarButton.setVisibility(VISIBLE);
		}else {
			ProgressbarButton.setVisibility(GONE);
		}


	}
	public void SetProgresOff(@bj_imageNotice.progressKinds int progressKind,boolean completed){
		//Log.i("LoadableImageview", "SetProgresOff: "+vNotice+" progressKind: "+progressKind+" completed: "  + completed);
		if (completed  ){
			if (progressKind==bj_imageNotice.progressKinds.downloadThumnails){
				Progressbar.setVisibility(GONE);
				ProgressbarButton.setVisibility(VISIBLE);
				ProgressbarButton.setImageResource(R.drawable.icon_download);

			}else {
				Progressbar.setVisibility(GONE);
				ProgressbarButton.setVisibility(GONE);
			}

		}else {
			if (progressKind== bj_imageNotice.progressKinds.downloadThumnails){
				ProgressbarButton.setImageResource(R.drawable.icon_download);
			}if (progressKind== bj_imageNotice.progressKinds.downloadOrginalImage){
				ProgressbarButton.setImageResource(R.drawable.icon_download);
			}if (progressKind== bj_imageNotice.progressKinds.upload){
				ProgressbarButton.setImageResource(R.drawable.icon_upload);
			}
			Progressbar.setVisibility(GONE);
			ProgressbarButton.setVisibility(VISIBLE);
		}


	}
	public interface OnSelectListener{
		void onSelectionChange(boolean selected);
	}
}
