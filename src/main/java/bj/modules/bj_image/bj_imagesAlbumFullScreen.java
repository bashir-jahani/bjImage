package bj.modules.bj_image;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import bj.modules.bj_file;
import bj.modules.bj_file_classes;
import bj.modules.bj_file_objcets.file_object;
import bj.modules.bj_messageBox_objcets.messageBox;

import static bj.modules.bj_messageBox.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class bj_imagesAlbumFullScreen extends Fragment {
    public bj_imagesAlbumFullScreen(Context context, Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_image.GImageNotice> ImagePaths, bj_imagesAlbum_FullScreenImageAdapter.OnLoadOrginalRequestListener onLoadOrginalRequestListener , bj_imagesAlbum_FullScreenImageAdapter.OnLoadThumbRequestListener onLoadThumbRequestListener, Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        mImagePaths=ImagePaths;
        this.context=context;
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        mOnLoadThumbRequestListener=onLoadThumbRequestListener;
        if (GoToLast){
            if (ImagePaths!=null) {
                mPosition = mImagePaths.size() - 1;
            }else {
                mPosition=0;
            }

        }else {
            mPosition=FileIndex;
        }
        this.CanAdd=CanAdd;
        this.CanDell=CanDell;

    }
    public bj_imagesAlbumFullScreen(Context context, Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_image.GImageNotice> ImagePaths, bj_imagesAlbum_FullScreenImageAdapter.OnLoadOrginalRequestListener onLoadOrginalRequestListener , Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        mImagePaths=ImagePaths;
        this.context=context;
        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;

        if (GoToLast){
            if (ImagePaths!=null) {
                mPosition = mImagePaths.size() - 1;
            }else {
                mPosition=0;
            }

        }else {
            mPosition=FileIndex;
        }
        this.CanAdd=CanAdd;
        this.CanDell=CanDell;

    }
    public bj_imagesAlbumFullScreen(Context context, Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_image.GImageNotice> ImagePaths , Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        mImagePaths=ImagePaths;
        this.context=context;
        if (GoToLast){
            if (ImagePaths!=null) {
                mPosition = mImagePaths.size() - 1;
            }else {
                mPosition=0;
            }

        }else {
            mPosition=FileIndex;
        }
        this.CanAdd=CanAdd;
        this.CanDell=CanDell;

    }

    public bj_imagesAlbumFullScreen(Context context, Integer FileIndex, Boolean GoToLast, String DirectoryPath, @Nullable List<String> FILE_EXTN, Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        this.context=context;
        mFILE_EXTN=FILE_EXTN;
        mDirectoryPath=DirectoryPath;
        utils = new bj_imagesAlbum_Utils(context,mFILE_EXTN);
        mImagePaths = utils.getFilePaths(mDirectoryPath);
        if (GoToLast) {
            if (mImagePaths!=null) {
                mPosition = mImagePaths.size() - 1;
            }else {
                mPosition=0;
            }
        }else {
            mPosition=FileIndex;
        }
        this.CanAdd=CanAdd;
        this.CanDell=CanDell;
    }

    public void SetOnLoadThumbRequestListener(bj_imagesAlbum_FullScreenImageAdapter.OnLoadThumbRequestListener listener){
        mOnLoadThumbRequestListener=listener;
        if (mPagerAdapter!=null){
            mPagerAdapter.SetOnLoadThumbRequestListener(listener);
        }

    }
    public void SetOnLoadOrginalRequestListener(bj_imagesAlbum_FullScreenImageAdapter.OnLoadOrginalRequestListener listener){
        mOnLoadOrginalRequestListener=listener;
        if (mPagerAdapter!=null){
            mPagerAdapter.SetOnLoadOrginalRequestListener(listener);
        }

    }

    public void SetOnNewImageRequest(OnNewImageRequest onNewImageRequest){
        mOnNewImageRequest=onNewImageRequest;
    }
    public void SetOnDelleteImageRequest(OnDelleteImageRequest onDelleteImageRequest){
        mOnDelleteImageRequest=onDelleteImageRequest;
    }
    public void SetOnShareImageRequest(OnShareImageRequest onShareImageRequest){
        mOnShareImageRequest=onShareImageRequest;
    }
    private bj_imagesAlbum_FullScreenImageAdapter.OnLoadOrginalRequestListener mOnLoadOrginalRequestListener;
    private bj_imagesAlbum_FullScreenImageAdapter.OnLoadThumbRequestListener mOnLoadThumbRequestListener;

    private OnNewImageRequest mOnNewImageRequest;
    private OnDelleteImageRequest mOnDelleteImageRequest;
    private OnShareImageRequest mOnShareImageRequest;
    Context context;

    public interface OnNewImageRequest{
        public void OnNewImage();
    }
    public interface OnDelleteImageRequest{
        public void OnDelleteImage(bj_image.GImageNotice ImageNotice, Integer position);
    }
    public interface OnShareImageRequest{
        public void OnShareImage(bj_image.GImageNotice ImageNotice);
    }
    public void OnNewImageCreated(bj_image.GImageNotice NewImageNotice, @Nullable String ProcessResult){
        if (NewImageNotice!=null) {
            mImagePaths.add(NewImageNotice);
            mPagerAdapter.notifyDataSetChanged();
            SetImageCurrent(mPagerAdapter.getCount() - 1);
            TXVImageNumber.setText(GetFilesNumber()+"");
            TXVImageIndex.setText(GetImageCurrentNumber()+"");
        }
        if (ProcessResult!=null){
            Toast.makeText(getContext(), ProcessResult, Toast.LENGTH_SHORT).show();
        }
    }
    public void OnDelletedImage(Integer DeletedRowPosition,@Nullable String ProcessResult){
        if (DeletedRowPosition>-1){
            //Toast.makeText(getContext(), mImagePaths.size()+"/"+DeletedRowPosition, Toast.LENGTH_LONG).show();
            File f=new File(mImagePaths.get(DeletedRowPosition).ImagePath);
            f.delete();
            File TFile=new File(f.getParent() +File.separator + "thumbs" + File.separator + f.getName());

            TFile.delete();
            mImagePaths.remove(mImagePaths.get(DeletedRowPosition));

            mPagerAdapter.notifyDataSetChanged();
            mPager.setAdapter(mPager.getAdapter());
            //Toast.makeText(getContext(), mImagePaths.size()+"", Toast.LENGTH_SHORT).show();
            SetImageCurrent(DeletedRowPosition-1);
            TXVImageNumber.setText(GetFilesNumber()+"");
            TXVImageIndex.setText(GetImageCurrentNumber()+"");
        }
        if (ProcessResult!=null){
            Toast.makeText(getContext(), ProcessResult, Toast.LENGTH_SHORT).show();
        }
    }
    ImageView IMGBTNBack,IMGBTNNew,IMGBTNDell,IMGBTNShare,IMGBTNSave;
    TextView TXVTitle,TXVImageIndex,TXVImageNumber,TXVFileName;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private View mControlsView;
    private View mControlsViewUP;
    private boolean mVisible;
    Boolean CanAdd=false;
    Boolean CanDell=false;
    private OnImageChangedListener mOnImageChangedListener;
    Integer mPosition;
    String mTitle="Gilas Images View";

    ArrayList<bj_image.GImageNotice> mImagePaths;


    private ViewPager mPager;
    private bj_imagesAlbum_FullScreenImageAdapter mPagerAdapter;
    private bj_imagesAlbum_Utils utils;
    List<String> mFILE_EXTN;
    String mDirectoryPath;

    public interface OnImageChangedListener {
        public long OnChanged(Integer FileIndex, int FileCurrentNumber);
    }
    public void SetOnImageChangedListener(OnImageChangedListener listener) {
        mOnImageChangedListener = listener;
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = null;
            androidx.appcompat.app.ActionBar actionBarAPP=null ;
            try {
                actionBar = getActivity().getActionBar();
            }catch (Exception e){
                try{
                    actionBarAPP =((AppCompatActivity)getActivity()). getSupportActionBar();
                }catch (Exception e1){

                }
            }
            if (actionBar != null) {
                actionBar.show();
            }
            if (actionBarAPP != null) {
                actionBarAPP.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
            mControlsViewUP.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private void hide() {
        // Hide UI first
        ActionBar actionBar=null ;
        androidx.appcompat.app.ActionBar actionBarAPP=null ;
        try {
            actionBar = getActivity().getActionBar();
        }catch (Exception e){
            try{
                actionBarAPP =((AppCompatActivity)getActivity()). getSupportActionBar();
            }catch (Exception e1){

            }
        }
        if (actionBar != null) {
            actionBar.hide();
        }
        if (actionBarAPP != null) {
            actionBarAPP.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mControlsViewUP.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private void ProcesSetButtonsClickEvents(){
        IMGBTNBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), (getFragmentManager().popBackStackImmediate())+"", Toast.LENGTH_SHORT).show();
                if (getFragmentManager().popBackStackImmediate()) {
                    getFragmentManager().popBackStack();
                }else {
                    getActivity().finish();
                }

            }
        });
        IMGBTNSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTNProcesSave();
            }
        });
        IMGBTNNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTNProcesNew();
            }
        });
        IMGBTNShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTNProcesShare();
            }
        });
        IMGBTNDell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTNProcesDellete();
            }
        });

    }



    private void BTNProcesSave(){
        MessageBox(R.string.question_title,R.string.question_Save_in_Gallery, BJMessagesButtonKind.Yes_No,getContext(),new messageBox.OnDialogResultListener(){
            @Override
            public boolean OnResult(Boolean dialogResult) {
                if (dialogResult) {
                    bj_image.SaveImageToGallery(getContext(), GetImageCurrentPath(), new bj_file_classes.OnGFileDialogResultListener() {
                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn) {

                        }

                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn, String DestinationFilePath) {

                        }

                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn, @Nullable File DestinationFile) {
                            //
                            // Toast.makeText(getContext(), DestinationFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            if (MyResult== bj_file_classes.GFileDialogsResults.Completed) {
                                bj_image.UpdateGallery(context, DestinationFile);
                                Toast.makeText(getContext(), "Image Saved Succecfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "Image Dont Saved"+ "\n" + ResultDescriptiopn, Toast.LENGTH_SHORT).show();
                            }
                        }


						@Override
                        public void OnResult(int MyResult, String ResultDescriptiopn, @Nullable file_object DestinationFile) {

                        }

                        @Override
                        public void OnSelect(Boolean IsSelected, @Nullable file_object SelectedFile) {

                        }
                    });

                }else {
                    // Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    bj_file.OpenChoicerDirectory(getContext(), false, new bj_file_classes.OnGFileDialogResultListener() {
                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn) {

                        }

                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn, String DestinationFilePath) {

                        }

                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn, @Nullable File DestinationFile) {

                        }

                        @Override
                        public void OnResult(int MyResult, String ResultDescriptiopn, @Nullable file_object DestinationFile) {

                        }

                        @Override
                        public void OnSelect(Boolean IsSelected, @Nullable file_object SelectedFile) {
                            if (IsSelected) {
                                bj_image.SaveImageToContent(getContext(), GetImageCurrentPath(), SelectedFile.getAbsolutePath(), new bj_file_classes.OnGFileDialogResultListener() {
                                    @Override
                                    public void OnResult(int MyResult, String ResultDescriptiopn) {

                                    }

                                    @Override
                                    public void OnResult(int MyResult, String ResultDescriptiopn, String DestinationFilePath) {

                                    }

                                    @Override
                                    public void OnResult(int MyResult, String ResultDescriptiopn, @Nullable File DestinationFile) {
                                        //Toast.makeText(getContext(), DestinationFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                        if (MyResult== bj_file_classes.GFileDialogsResults.Completed) {

                                            Toast.makeText(getContext(), "Image Saved Succecfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(), "Image Dont Saved"+ "\n" + ResultDescriptiopn, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void OnResult(int MyResult, String ResultDescriptiopn, @Nullable file_object DestinationFile) {

                                    }

                                    @Override
                                    public void OnSelect(Boolean IsSelected, @Nullable file_object SelectedFile) {

                                    }
                                });


                            }
                        }
                    });

                }
                return super.OnResult(dialogResult);
            }
        });


    }
    private void BTNProcesNew(){
        if (mOnNewImageRequest!=null){
           mOnNewImageRequest.OnNewImage();
        }
       // Toast.makeText(getContext(),(mOnNewImageRequest!=null)+ "", Toast.LENGTH_SHORT).show();
    }
    private void BTNProcesShare(){
        if(mOnShareImageRequest!=null){
            mOnShareImageRequest.OnShareImage(mImagePaths.get(GetImageCurrentIndex()));
        }
    }
    private void BTNProcesDellete(){
        if (mOnDelleteImageRequest!=null){
            mOnDelleteImageRequest.OnDelleteImage(mImagePaths.get(GetImageCurrentIndex()),GetImageCurrentIndex());

        }
    }
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };


    ////////**************************************************************************






    public  int GetFilesNumber(){
        try {
            return mImagePaths.size();
        }catch (Exception e){
            return 0;
        }
    }

    public  void SetTitle(String MyTitle){
        mTitle=MyTitle;
    }
    public  String GetTitle(){
       return mTitle;
    }
    public  int GetImageCurrentNumber(){
        if (mImagePaths==null) {
            return 0;
        }else {
            return   mPager.getCurrentItem()+1;
        }

    }
    public  int GetImageCurrentIndex(){
        return   mPager.getCurrentItem();
    }
    public  String GetImageCurrentPath(){
        if (mImagePaths!=null) {
            try{
                return mImagePaths.get(mPager.getCurrentItem()).ImagePath;
            }catch (Exception e){
                return "";
            }
        }else {
            return   "";
        }

    }
    public  String GetImageCurrentName(){
        String path=GetImageCurrentPath();
        return   path.substring(path.lastIndexOf("/")+1);
    }
    public boolean AddImage(String ImagePath,Boolean isThumb){
        try {
            bj_image.GImageNotice fp=new bj_image.GImageNotice();
            fp.ImagePath=ImagePath;
            fp.IsThumb=isThumb;
            mImagePaths.add(mImagePaths.size(), fp);
            int p = mPager.getCurrentItem();
            mPager.notifyAll();
            mPager.setCurrentItem(p);
            return true;
        }catch (Exception e){
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean DellImage(int ImageIndex,Boolean DeleteSorceFile_SO){
        Boolean RS;
        String FPTH;
        FPTH=mImagePaths.get(ImageIndex).ImagePath;
        try {
            int p;
            if (mPager.getCurrentItem()==mImagePaths.size()) {
                p = mPager.getCurrentItem() - 1;
            }else {
                p = mPager.getCurrentItem();
            }
            mImagePaths.remove(ImageIndex);


            mPager.notifyAll();
            mPager.setCurrentItem(p);
            RS =true;
        }catch (Exception e){
            Toast.makeText(getContext(), "Error: Remove from List_" + e.getMessage(), Toast.LENGTH_SHORT).show();
            RS= false;
        }
        if(DeleteSorceFile_SO){
            File file=new File(FPTH);
            try {

                if (file !=null){
                    if(file.exists() & file.isFile()){
                        file.delete();
                    }
                }

            }catch (Exception e){
                Toast.makeText(getContext(), "Error: Remove Source File_" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            file=null;

        }
        return RS;
    }
    public boolean DellImage(String ImagePath,Boolean DeleteSorceFile_SO){
        Boolean RS = false;
        int ImageIndex;
        try {
            for(int i=0;i<mImagePaths.size();i++){
                if (mImagePaths.get(i).ImagePath==ImagePath){
                    ImageIndex=i;

                    int p;
                    if (ImageIndex==(mImagePaths.size()-1)) {
                        p = mPager.getCurrentItem() - 1;
                    }else {
                        p = mPager.getCurrentItem();
                    }
                    mImagePaths.remove(ImageIndex);

                    mPager.notifyAll();
                    mPager.setCurrentItem(p);
                    RS =true;
                    break;

                }
            }

        }catch (Exception e){
            Toast.makeText(getContext(), "Error: Remove from List_" + e.getMessage(), Toast.LENGTH_SHORT).show();
            RS= false;
        }
        if(DeleteSorceFile_SO){
            File file=new File(ImagePath);
            try {

                if (file !=null){
                    if(file.exists() & file.isFile()){
                        file.delete();
                    }
                }

            }catch (Exception e){
                Toast.makeText(getContext(), "Error: Remove Source File_" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            file=null;

        }
        return RS;
    }

    public  void SetImageCurrent(int item){
           mPager.setCurrentItem(item);
    }



    @Override
    public void onStart() {
        super.onStart();
        mVisible = true;
        mControlsView = getActivity().findViewById(R.id.GIAF_LL_fullscreen_content_controls);
        mControlsViewUP = getActivity().findViewById(R.id.GIAF_LL_fullscreen_content_controls_Up);
        mContentView = getActivity().findViewById(R.id.GIAF_VP_fullscreen_content);
        IMGBTNBack=(ImageView)getActivity().findViewById(R.id.GIAF_IMG_Back);
        IMGBTNDell=(ImageView)getActivity().findViewById(R.id.GIAF_IMG_Delete);
        IMGBTNNew=(ImageView)getActivity().findViewById(R.id.GIAF_IMG_New);
        IMGBTNShare=(ImageView)getActivity().findViewById(R.id.GIAF_IMG_Share);
        IMGBTNSave=(ImageView)getActivity().findViewById(R.id.GIAF_IMG_Save);
        TXVImageIndex=(TextView)getActivity().findViewById(R.id.GIAF_TXV_ImageIndex);
        TXVImageNumber=(TextView)getActivity().findViewById(R.id.GIAF_TXV_ImageNumber);
        TXVTitle=(TextView)getActivity().findViewById(R.id.GIAF_TXV_Title);
        TXVFileName=(TextView)getActivity().findViewById(R.id.GIAF_TXV_Filename);
        TXVTitle.setText(GetTitle());

        if(CanAdd) {
            IMGBTNNew.setVisibility(View.VISIBLE);
        }else {
            IMGBTNNew.setVisibility(View.GONE);
        }
        if(CanDell) {
            IMGBTNDell.setVisibility(View.VISIBLE);
        }else {
            IMGBTNDell.setVisibility(View.GONE);
        }
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        ProcesSetButtonsClickEvents();
        delayedHide(3000);
        ///////////**************************************************************
        mPager = (ViewPager) getActivity().findViewById(R.id.GIAF_VP_fullscreen_content);
        if(mPager==null) {
            //Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
        }else {

            mPagerAdapter = new bj_imagesAlbum_FullScreenImageAdapter(mImagePaths, mOnLoadOrginalRequestListener,  mOnLoadThumbRequestListener);
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(mPosition);
            TXVImageNumber.setText(GetFilesNumber()+"");
            TXVImageIndex.setText(GetImageCurrentNumber()+"");
            Log.d("BJFile","Count="+mPagerAdapter.getCount());
            TXVFileName.setText(GetImageCurrentName());
            //Toast.makeText(getContext(), mPosition + "/"+ GetFilesIndex(), Toast.LENGTH_SHORT).show();
            if (mOnImageChangedListener!=null){
                mOnImageChangedListener.OnChanged(mPosition,mPosition+1);
            }

            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    TXVImageIndex.setText(GetImageCurrentNumber()+"");
                    TXVFileName.setText(GetImageCurrentName());
                    if (mOnImageChangedListener!=null){

                        mOnImageChangedListener.OnChanged(GetImageCurrentIndex(),GetImageCurrentNumber());

                    }
                    //Toast.makeText(getContext(),position+ "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mPager.setOnContextClickListener(new View.OnContextClickListener() {
                @Override
                public boolean onContextClick(View v) {
                    toggle();
                    return false;
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.g_images_album_fullscreen, container, false);

    }

}
