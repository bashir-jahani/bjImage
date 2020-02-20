package bj.modules.bj_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import bj.modules.bj_file;
import bj.modules.bj_image_objects.imagesView_TouchImageView;


/**
 * Created by Bashir jahani on 1/24/2018.
 */

public class bj_imagesAlbum_FullScreenImageAdapter extends PagerAdapter {
    //private AppCompatActivity _activity;
    private LayoutInflater inflater;
    private ArrayList<bj_image.GImageNotice> _imagesPath;
    private Boolean isNull=false;

    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_image.GImageNotice> imagesPath, OnLoadOrginalRequestListener onLoadOrginalRequestListener, OnLoadThumbRequestListener onLoadThumbRequestListener ){
        //_activity = activity;
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        mOnLoadThumbRequestListener=onLoadThumbRequestListener;
        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_image.GImageNotice>();
            _imagesPath.add(new bj_image.GImageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_image.GImageNotice>();
                _imagesPath.add(new bj_image.GImageNotice());
                isNull=true;
            }else {
                _imagesPath = imagesPath;

                isNull=false;
            }
        }
    }
    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_image.GImageNotice> imagesPath, OnLoadOrginalRequestListener onLoadOrginalRequestListener ){
        //_activity = activity;
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_image.GImageNotice>();
            _imagesPath.add(new bj_image.GImageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_image.GImageNotice>();
                _imagesPath.add(new bj_image.GImageNotice());
                isNull=true;
            }else {
                _imagesPath = imagesPath;

                isNull=false;
            }
        }
    }
    public bj_imagesAlbum_FullScreenImageAdapter(ArrayList<bj_image.GImageNotice> imagesPath){
        //_activity = activity;

        if (imagesPath==null) {
            _imagesPath= new ArrayList<bj_image.GImageNotice>();
            _imagesPath.add(new bj_image.GImageNotice());
            isNull=true;
        }else {

            if (imagesPath.size()==0) {
                _imagesPath= new ArrayList<bj_image.GImageNotice>();
                _imagesPath.add(new bj_image.GImageNotice());
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
    public void SetViewForDownloadThumb(LoadableImageview MyView,Integer Position){

    }
    public void SetViewForDownloadOrginal(LoadableImageview MyView,Integer Position){

    }
    public void SetViewForDownloadOrginalCancel(LoadableImageview MyView,Integer Position){

    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final LoadableImageview MyView=new LoadableImageview(container.getContext(),_imagesPath.get(position).IsThumb,"");


        if (!isNull ) {

            try{
                if ((new File(_imagesPath.get(position).ImagePath)).exists()){
                    if (bj_file.GetFileExtension(new File(_imagesPath.get(position).ImagePath)).toLowerCase().equals("gif")) {
                        Glide.with(container.getContext()).load(_imagesPath.get(position).ImagePath).into(MyView.imageView);
                    }else {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                        Bitmap bitmap = BitmapFactory.decodeFile(_imagesPath.get(position).ImagePath, options);
                        MyView.imageView.setImageBitmap(bitmap);
                    }

                }else {
                    if(_imagesPath.get(position).IsThumb) {
                        if (mOnLoadThumbRequestListener!=null){

                            mOnLoadThumbRequestListener.OnRequestThumb(MyView,_imagesPath.get(position));
                        }
                    }else {
                        MyView.imageView.setImageResource(R.drawable.no_images_available);
                    }
                }

            }catch (Exception e){
                MyView.imageView.setImageResource(R.drawable.no_images_available);
            }

            if(_imagesPath.get(position).IsThumb) {
                MyView.ButtonForDownloading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyView.GetIsLoadingOrginal()){
                            if (mOnLoadOrginalRequestListener!=null) {
                                mOnLoadOrginalRequestListener.OnRequestOrginalCancel(MyView, _imagesPath.get(position));

                            }
                            MyView.SetIsLoadingOrginal(false);

                        }else {
                            if (mOnLoadOrginalRequestListener!=null){
                                mOnLoadOrginalRequestListener.OnRequestOrginal(MyView, _imagesPath.get(position));
                            }

                            MyView.SetIsLoadingOrginal(true);
                        }

                    }
                });
            }

        }else {
            MyView.imageView.setImageResource(R.drawable.no_images_available);
        }

        MyView.imageView.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                container.performContextClick();

                return false;
            }
        });



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

    }
    private OnLoadOrginalRequestListener mOnLoadOrginalRequestListener;
    private OnLoadThumbRequestListener mOnLoadThumbRequestListener;

    public void SetOnLoadThumbRequestListener(OnLoadThumbRequestListener listener){
        mOnLoadThumbRequestListener=listener;
    }
    public void SetOnLoadOrginalRequestListener(OnLoadOrginalRequestListener listener){
        mOnLoadOrginalRequestListener=listener;
    }

    public interface OnLoadOrginalRequestListener{
        public void OnRequestOrginal(LoadableImageview RowViewGroup, bj_image.GImageNotice imageNotice);
        public void OnRequestOrginalCancel(LoadableImageview RowViewGroup, bj_image.GImageNotice imageNotice);
    }

    public interface OnLoadThumbRequestListener{
        public void OnRequestThumb(LoadableImageview RowViewGroup, bj_image.GImageNotice imageNotice);
    }

    public class LoadableImageview extends LinearLayout {
        Context vContext;
        Boolean vIsThumb=false;
        String vNotice="";
        Boolean mIsLoadingThumb=false;
        Boolean mIsLoadingOrginal=false;
        FrameLayout FL_Base;
        public ProgressBar ProgressbarForDownloading;
        public ImageView ButtonForDownloading;
        public imagesView_TouchImageView imageView;
        public TextView TextViewForImageNotice;
        public void SetIsThumb(Boolean IsThumb){
            vIsThumb=IsThumb;
            if(IsThumb) {
                ButtonForDownloading.setVisibility(VISIBLE);
            }else {
                ButtonForDownloading.setVisibility(GONE);
                ProgressbarForDownloading.setVisibility(GONE);
            }
        }
        public Boolean GetIsThumb(){
            return vIsThumb;
        }
        public Boolean GetIsLoadingThumb(){
            return mIsLoadingThumb;
        }
        public Boolean GetIsLoadingOrginal(){
            return mIsLoadingOrginal;
        }
        public void SetIsLoadingThumb(Boolean IsLoading){
            mIsLoadingThumb=IsLoading;
           if (IsLoading) {
               ButtonForDownloading.setVisibility(GONE);
               ProgressbarForDownloading.setVisibility(VISIBLE);
               ProgressbarForDownloading.setIndeterminate(true);
           }else {
               ButtonForDownloading.setVisibility(VISIBLE);
               ProgressbarForDownloading.setVisibility(GONE);
           }

        }
        public void SetIsLoadingOrginal(Boolean IsLoading){
            mIsLoadingOrginal=IsLoading;
            if (IsLoading) {
                ButtonForDownloading.setImageResource(R.drawable.icon_download_cancel);
                ProgressbarForDownloading.setVisibility(VISIBLE);
                ProgressbarForDownloading.setIndeterminate(false);
            }else {
                ButtonForDownloading.setImageResource(R.drawable.icon_download);
                ButtonForDownloading.setVisibility(VISIBLE);
                ProgressbarForDownloading.setVisibility(GONE);
                ProgressbarForDownloading.setProgress(0);
            }

        }

        public void SetLoadingOrginalCompllete(Context context, String Base64String,String SavePath){
            ButtonForDownloading.setVisibility(GONE);
            ProgressbarForDownloading.setVisibility(GONE);
            SetIsThumb(false);
            bj_image.ImageSave(context, SavePath,Base64String);

        }
        public void SetLoadingOrginalCompllete(){
            ButtonForDownloading.setVisibility(GONE);
            ProgressbarForDownloading.setVisibility(GONE);
            SetIsThumb(false);


        }
        public LoadableImageview(Context context,Boolean IsThumb,String ImageNotice) {
            super(context);
            vContext=context;
            vIsThumb=IsThumb;
            vNotice=ImageNotice;


            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            setPadding(0,0,0,0);
            setOrientation(LinearLayout.VERTICAL);

            FL_Base=new FrameLayout(context);
            FL_Base.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            FL_Base.setPadding(0,0,0,0);

            TextViewForImageNotice=new TextView(vContext);
            TextViewForImageNotice.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextViewForImageNotice.setTextAppearance(R.style.TextAppearance_Compat_Notification);
            TextViewForImageNotice.setBackgroundColor(vContext.getResources().getColor(R.color.black_overlay));
            TextViewForImageNotice.setTextColor(vContext.getResources().getColor(R.color.white_overlay));
            TextViewForImageNotice.setPadding(2,2,2,10);
            TextViewForImageNotice.setText(vNotice);
            TextViewForImageNotice.setGravity(Gravity.BOTTOM);

            if (vNotice.equals("")) {
                TextViewForImageNotice.setVisibility(GONE);
            }else {
                TextViewForImageNotice.setVisibility(VISIBLE);
            }

            imageView=new imagesView_TouchImageView(context);
            imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));




            ProgressbarForDownloading=new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
            ProgressbarForDownloading.setLayoutParams(new LayoutParams(bj_image.getpixels(context,70), bj_image.getpixels(context,70)));
            ProgressbarForDownloading.setMax(100);
            ProgressbarForDownloading.setIndeterminate(false);
            ProgressbarForDownloading.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_uncomplete));


            ProgressbarForDownloading.setVisibility(GONE);

            ButtonForDownloading =new ImageView(context,null,android.R.attr.progressBarStyle);
            ButtonForDownloading.setLayoutParams(new LayoutParams(bj_image.getpixels(context, 50), bj_image.getpixels(context, 50)));
            ButtonForDownloading.setClickable(true);
            ButtonForDownloading.setImageResource(R.drawable.icon_download);

            if ( IsThumb) {
                ButtonForDownloading.setVisibility(VISIBLE);
            }else {
                ButtonForDownloading.setVisibility(GONE);
            }

            FL_Base.addView(imageView);
            FL_Base.addView(ProgressbarForDownloading);
            FL_Base.addView(ButtonForDownloading);
            FL_Base.addView(TextViewForImageNotice);
            addView(FL_Base);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) TextViewForImageNotice.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
            TextViewForImageNotice.setLayoutParams(params);

            params = (FrameLayout.LayoutParams) ProgressbarForDownloading.getLayoutParams();
            params.gravity = Gravity.CENTER;
            ProgressbarForDownloading.setLayoutParams(params);

            params = (FrameLayout.LayoutParams) ButtonForDownloading.getLayoutParams();
            params.gravity = Gravity.CENTER;
            ButtonForDownloading.setLayoutParams(params);
        }


    }
}
