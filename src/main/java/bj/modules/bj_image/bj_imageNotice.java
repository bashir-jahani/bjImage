package bj.modules.bj_image;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import bj.modules.bj_file;

public class bj_imageNotice   {
	private String TAG="bj_imageNotice";
	private OnProgressListener mOnProgressListener;
	public bj_imageNotice() {

	}
	public bj_imageNotice(String imagePath, Boolean isThumb, Boolean needUpload, String imageName, long imageSize) {
		ImagePath = imagePath;
		IsThumb = isThumb;
		NeedUpload = needUpload;
		ImageName = imageName;
		ImageSize = imageSize;
	}
	private String date_taken_string;
	private long date_taken;
	private String ImagePath;
	private Boolean IsThumb;
	private Boolean NeedUpload;
	private int progres=0;
	private int progresKind=0;
	public String ImageName;
	public String ImageAlbume;
	public long ImageSize;
	public boolean IsUploading;
	public boolean IsDownloadingOriginal;
	public boolean IsDownloadingThumbnails;
	public boolean selected=false;
	public void SetDateTakenString(String date){
		date_taken=Date.parse(date);
		date_taken_string=date;
	}
	public String GetDateTakenString(){
		return date_taken_string;
	}
	public void SetDateTaken(long date){

		Date mdate=new Date(date);
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		date_taken_string= df2.format(mdate);
		date_taken=date;
	}
	public long GetDateTaken(){
		return date_taken;
	}
	public String GetImagePath(){
		return ImagePath;
	}
	public boolean GetNeedUpload(){
		return NeedUpload;
	}
	public boolean GetIsThumb(){
		return IsThumb;
	}
	public void SetImagePath(String newImagePath,boolean isThumb,boolean needUpload){
		String lastPath=ImagePath;
		ImagePath=newImagePath;
		NeedUpload =needUpload;
		IsThumb=isThumb;
		if (mOnProgressListener!=null)
			mOnProgressListener.OnImagePathSet(newImagePath,isThumb,needUpload);
		//Log.i(TAG, "SetImagePath: exists "+ImageExists()+System.lineSeparator()+ImagePath);
	}
	public void SetProgresOn(@progressKinds int progressKind, int percent){
		boolean  indtmnt=false,cnclbl=true;
		this.progresKind=progressKind;
		this.progres=percent;
		//Log.i(TAG, "SetProgresOn: "+ImageName+" " +percent + " %");
		if (progressKind==progressKinds.downloadThumnails){
			IsThumb=true;
			IsDownloadingOriginal =true;
			IsDownloadingThumbnails=true;
			IsUploading=false;

		}if (progressKind==progressKinds.downloadOrginalImage){
			IsThumb=true;
			IsDownloadingOriginal =true;
			IsDownloadingThumbnails=false;
			IsUploading=false;

		}if (progressKind==progressKinds.upload){
			IsDownloadingOriginal =false;
			IsDownloadingThumbnails=false;
			IsUploading=true;

		}
		if (mOnProgressListener!=null)
			mOnProgressListener.OnProgressOn(indtmnt,progres,!indtmnt);

	}
	public void SetProgresOff(boolean complete){
		IsDownloadingOriginal =false;
		IsDownloadingThumbnails=false;
		IsUploading=false;
		if (complete){
			this.NeedUpload =false;
			if (this.progresKind!=progressKinds.downloadThumnails){
				this.IsThumb=false;
			}else {
				this.IsThumb=true;
			}
		}
		this.progres=0;
		this.progresKind=0;
		//Log.i(TAG, "SetProgresOff: "+ImageName+" complete: " +complete);
		if (mOnProgressListener!=null)
			mOnProgressListener.OnProgressOff(progresKind,complete);
	}
	public int GetProgressKind(){
		return this.progresKind;
	}
	public int GetProgress(){
		return this.progres;
	}

	public void SetOnProgressListener(OnProgressListener onProgressListener){
		this.mOnProgressListener=onProgressListener;
	}
	public File imageFile(){
		return new File(GetImagePath());
	}

	public Uri imageUri(Context context){
		Uri uri;
		uri=bj_file.uriUtil.uriFromFile(context,imageFile());
		return uri;
	}



	public interface OnProgressListener{
		void OnProgressOn(boolean indeterminate, int percent,boolean cancelable);
		void OnProgressOff(@bj_imageNotice.progressKinds int progressKind,boolean completed);
		void OnImagePathSet(String newImagePath,boolean isThumb,boolean needUpload);
	}
	public @interface progressKinds{
		public int upload=3;
		int downloadOrginalImage =2;
		int downloadThumnails=1;
	}

	public boolean ImageExists(){
		return (new File(ImagePath)).exists();
	}
}
