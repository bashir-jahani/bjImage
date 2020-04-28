package bj.modules.bj_image;

import bj.modules.bj_image. bj_loadable_image_listeners.*;
import bj.modules.bj_image_classes;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Bashir jahani on 1/24/2018.
 */

public class bj_imagesAlbum_FullScreenImageAdapter extends PagerAdapter {
    final String TAG ="bj_imagesAlbum_FullScreenImageAdapter";
    @bj_image_classes.FileNotice int fileNoticeForShow;
    //private AppCompatActivity _activity;
    Handler handler;
    private ArrayList<bj_imageNotice> _imagesPath;
    private Boolean isNull=false;
    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_imageNotice> imagesPath, OnLoadOrginalRequestListener onLoadOrginalRequestListener, OnLoadThumbRequestListener onLoadThumbRequestListener, OnUploadingImageListener onUploadingImageListener){
        //_activity = activity;
        handler=new Handler();
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        mOnLoadThumbRequestListener=onLoadThumbRequestListener;
        mOnUploadingImageListener=onUploadingImageListener;
        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_imageNotice>();
            _imagesPath.add(new bj_imageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_imageNotice>();
                _imagesPath.add(new bj_imageNotice());
                isNull=true;
            }else {
                _imagesPath = imagesPath;

                isNull=false;
            }
        }
    }
    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_imageNotice> imagesPath, OnLoadOrginalRequestListener onLoadOrginalRequestListener, OnLoadThumbRequestListener onLoadThumbRequestListener ){
        handler=new Handler();
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        mOnLoadThumbRequestListener=onLoadThumbRequestListener;
        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_imageNotice>();
            _imagesPath.add(new bj_imageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_imageNotice>();
                _imagesPath.add(new bj_imageNotice());
                isNull=true;
            }else {
                _imagesPath = imagesPath;

                isNull=false;
            }
        }
    }
    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_imageNotice> imagesPath, OnLoadOrginalRequestListener onLoadOrginalRequestListener ){
        handler=new Handler();
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_imageNotice>();
            _imagesPath.add(new bj_imageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_imageNotice>();
                _imagesPath.add(new bj_imageNotice());
                isNull=true;
            }else {
                _imagesPath = imagesPath;

                isNull=false;
            }
        }
    }
    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_imageNotice> imagesPath,@bj_image_classes.FileNotice int fileNoticeForShow){
        handler=new Handler();
        this.fileNoticeForShow=fileNoticeForShow;
        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_imageNotice>();
            _imagesPath.add(new bj_imageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_imageNotice>();
                _imagesPath.add(new bj_imageNotice());
                isNull=true;
            }else {
                _imagesPath = imagesPath;

                isNull=false;
            }
        }
    }
    @Override
    public int getCount() {
        return this._imagesPath.size();
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {


        String imageNotice=null;
        switch (fileNoticeForShow){
            case bj_image_classes.FileNotice
                    .ALBUME_NAME:
                imageNotice=_imagesPath.get(position).ImageName;
            case bj_image_classes.FileNotice
                    .NAME:
                imageNotice=_imagesPath.get(position).ImageAlbume;
            case bj_image_classes.FileNotice
                    .ALBUME_NAME_FILE_NAME:
                imageNotice=_imagesPath.get(position).ImageAlbume+"/"+_imagesPath.get(position).ImageName;;
            case bj_image_classes.FileNotice
                    .DATE_TAKEN:
                imageNotice=_imagesPath.get(position).GetDateTakenString();
            case bj_image_classes.FileNotice
                    .SIZE:
                long fSize=_imagesPath.get(position).ImageSize;
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
        final LoadableImageview MyView=new LoadableImageview(container.getContext(),imageNotice);
        if (!isNull ) {
            final File currentImageFile=new File(_imagesPath.get(position).GetImagePath());

            setProgress(MyView,position);

            setImage(MyView,position);
            _imagesPath.get(position).SetOnProgressListener(new bj_imageNotice.OnProgressListener() {
                @Override
                public void OnProgressOn(boolean indeterminate, int percent, boolean cancelable) {
                    setProgress(MyView,position);
                }

                @Override
                public void OnProgressOff(int progressKind, boolean completed) {
                    setProgress(MyView,position);

                }

                @Override
                public void OnImagePathSet(String newImagePath, boolean isThumb, boolean needUpload) {
                    setImage(MyView,position);
                }
            });
        }else {
            // image list is empty so show message
            MyView.touchImageView.setImageResource(R.drawable.no_images_available);
        }

        // perform click event
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MyView.touchImageView.setOnContextClickListener(new View.OnContextClickListener() {
                @Override
                public boolean onContextClick(View v) {
                    container.performContextClick();

                    return false;
                }
            });
        }else {
            MyView.touchImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.performClick();

                }
            });
        }
        container.addView(MyView);
        return MyView;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
        if (position<_imagesPath.size()){
            _imagesPath.get(position).SetOnProgressListener(null);
        }

    }
    public void setImage(LoadableImageview MyView, int position) {
        File imageFile=new File(_imagesPath.get(position).GetImagePath());
        boolean isThumbNail=_imagesPath.get(position).GetIsThumb();
        boolean needUpload=_imagesPath.get(position).GetNeedUpload();
        try{

            if (imageFile.exists()){

                if (!isThumbNail || _imagesPath.get(position).GetNeedUpload()){
                    Glide.with(MyView.getContext()).load(imageFile).placeholder(R.drawable.loading).into(MyView.touchImageView);
                }else {
                    MyView.touchImageView.SetImage(imageFile.getAbsolutePath());
                }

            }else {
                //image not exists

                if(isThumbNail) {

                    Log.i(TAG, "setImage: thumbnail image exist: "+_imagesPath.get(position).ImageExists()+System.lineSeparator()+_imagesPath.get(position).GetImagePath());
                    if (_imagesPath.get(position).IsDownloadingThumbnails == false && mOnLoadThumbRequestListener!=null){
                        _imagesPath.get(position).SetProgresOn(bj_imageNotice.progressKinds.downloadThumnails,0);
                        MyView.SetProgresOn(true,0,false);
                        mOnLoadThumbRequestListener.OnRequestThumb(MyView,_imagesPath.get(position),position);
                    }

                }else {
                    // image not found and not thumbnails so show error image
                    MyView.touchImageView.setImageResource(R.drawable.no_images_available);
                }
            }
        }catch (Exception e){
            MyView.touchImageView.setImageResource(R.drawable.no_images_available);
        }
    }
    public void setProgress(final LoadableImageview MyView, final int position) {

        //Log.i(TAG, "setProgress: "+_imagesPath.get(position).ImageName+" kind is "+_imagesPath.get(position).GetProgressKind()+" NeedUploading: "+_imagesPath.get(position).GetNeedUpload()+" IsThumb: "+_imagesPath.get(position).GetIsThumb());
        switch (_imagesPath.get(position).GetProgressKind()){
            case 1:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyView.ProgressbarButton.setVisibility(View.GONE);
                        MyView.Progressbar.setVisibility(View.VISIBLE);
                        MyView.Progressbar.setIndeterminate(true);
                    }
                });


                break;
            case 2:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyView.ProgressbarButton.setImageResource(R.drawable.icon_download_cancel);
                        MyView.ProgressbarButton.setVisibility(View.VISIBLE);
                        MyView.Progressbar.setVisibility(View.VISIBLE);
                        MyView.Progressbar.setIndeterminate(false);
                        MyView.Progressbar.setProgress(_imagesPath.get(position).GetProgress());
                    }
                });
                break;
            case 3:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyView.ProgressbarButton.setImageResource(R.drawable.icon_download_cancel);
                        MyView.ProgressbarButton.setVisibility(View.VISIBLE);
                        MyView.Progressbar.setVisibility(View.VISIBLE);
                        MyView.Progressbar.setIndeterminate(false);
                        MyView.Progressbar.setProgress(_imagesPath.get(position).GetProgress());
                    }
                });
                break;
            default:
                if (_imagesPath.get(position).GetNeedUpload()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyView.ProgressbarButton.setImageResource(R.drawable.icon_upload);
                            MyView.ProgressbarButton.setVisibility(View.VISIBLE);
                            MyView.Progressbar.setVisibility(View.GONE);
                        }
                    });
                }else if (_imagesPath.get(position).GetIsThumb()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyView.ProgressbarButton.setImageResource(R.drawable.icon_download);
                            MyView.ProgressbarButton.setVisibility(View.VISIBLE);
                            MyView.Progressbar.setVisibility(View.GONE);
                        }
                    });

                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyView.ProgressbarButton.setVisibility(View.GONE);
                            MyView.Progressbar.setVisibility(View.GONE);
                        }
                    });
                }


        }
        setProgressButtonClickEvent(MyView,position);

    }
    private void setProgressButtonClickEvent(final LoadableImageview MyView, final int position) {


        MyView.ProgressbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_imagesPath.get(position).GetNeedUpload()){
                    if (_imagesPath.get(position).IsUploading){
                        if (mOnUploadingImageListener!=null) {
                            mOnUploadingImageListener.OnUploadingCancel(MyView, _imagesPath.get(position), position);
                        }
                        //MyView.SetProgresOff(bj_imageNotice.progressKinds.upload,false);
                        _imagesPath.get(position).SetProgresOff(false);
                        Log.i(TAG, "onClick: //////////////////////////////////////// perform OnUploadingCancel");

                    }else {
                        if (mOnUploadingImageListener!=null){
                            mOnUploadingImageListener.OnUploading(MyView, _imagesPath.get(position),position);
                        }
                        //MyView.SetProgresOn(false,0,true);
                        _imagesPath.get(position).SetProgresOn(bj_imageNotice.progressKinds.upload,0);
                        Log.i(TAG, "onClick: perform OnUploading: "+position+ " / "+_imagesPath.get(position).ImageName);

                    }
                }else if(_imagesPath.get(position).GetIsThumb()){
                    Log.i(TAG, "************* image exist: "+_imagesPath.get(position).ImageExists()+System.lineSeparator()+_imagesPath.get(position).GetImagePath());
                    if (_imagesPath.get(position).IsDownloadingOriginal){
                        if (mOnLoadOrginalRequestListener!=null) {
                            mOnLoadOrginalRequestListener.OnRequestOrginalCancel(MyView, _imagesPath.get(position),position);
                        }
                        //MyView.SetProgresOff(bj_imageNotice.progressKinds.downloadOrginalImage,false);
                        _imagesPath.get(position).SetProgresOff(false);

                    }else {
                        if (mOnLoadOrginalRequestListener!=null){
                            mOnLoadOrginalRequestListener.OnRequestOrginal(MyView, _imagesPath.get(position),position);
                        }
                        //MyView.SetProgresOn(false,0,true);
                        _imagesPath.get(position).SetProgresOn(bj_imageNotice.progressKinds.downloadOrginalImage,0);
                        Log.i(TAG, "onClick: perform OnRequestOrginal: "+position+ " / "+_imagesPath.get(position).ImageName);

                    }
                }

            }
        });

    }

    private OnLoadOrginalRequestListener mOnLoadOrginalRequestListener;
    private OnLoadThumbRequestListener mOnLoadThumbRequestListener;
    private OnUploadingImageListener mOnUploadingImageListener;


    public void SetOnLoadThumbRequestListener(OnLoadThumbRequestListener listener){
        mOnLoadThumbRequestListener=listener;
    }
    public void SetOnLoadOrginalRequestListener(OnLoadOrginalRequestListener listener){
        mOnLoadOrginalRequestListener=listener;
    }
    public void SetOnUploadingImageListener(OnUploadingImageListener listener){
        mOnUploadingImageListener=listener;
    }





}
