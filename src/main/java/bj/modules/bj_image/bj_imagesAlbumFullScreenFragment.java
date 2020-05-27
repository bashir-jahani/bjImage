package bj.modules.bj_image;

import android.annotation.SuppressLint;
import android.app.ActionBar;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
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

import androidx.core.content.FileProvider;
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
import bj.modules.bj_image. bj_loadable_image_listeners.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class bj_imagesAlbumFullScreenFragment extends Fragment {
    // variables start
    private int newImageWirth=400;
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
    Integer mPosition=0;
    String mTitle="Gilas Images View";

    ArrayList<bj_imageNotice> mImagePaths;


    private ViewPager mPager;
    private bj_imagesAlbum_FullScreenImageAdapter mPagerAdapter;
    private bj_imagesAlbum_Utils utils;
    List<String> mFILE_EXTN;
    String mDirectoryPath;
    private OnLoadOrginalRequestListener mOnLoadOrginalRequestListener;
    private OnLoadThumbRequestListener mOnLoadThumbRequestListener;
    private OnUploadingImageListener mOnUploadingImageListener;

    private OnNewImageRequest mOnNewImageRequest;
    private OnDelleteImageRequest mOnDelleteImageRequest;
    private OnShareImageRequest mOnShareImageRequest;
    private String TAG="bj_imagesAlbumFullScreenFragment";
    // variables end
    //constractors start
    public bj_imagesAlbumFullScreenFragment(Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths, OnLoadOrginalRequestListener onLoadOrginalRequestListener , OnLoadThumbRequestListener onLoadThumbRequestListener, OnUploadingImageListener onUploadingImageListener, Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;

        mImagePaths=ImagePaths;

        mOnLoadOrginalRequestListener=onLoadOrginalRequestListener;
        mOnLoadThumbRequestListener=onLoadThumbRequestListener;
        mOnUploadingImageListener=onUploadingImageListener;
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
    public bj_imagesAlbumFullScreenFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths, OnLoadOrginalRequestListener onLoadOrginalRequestListener , OnLoadThumbRequestListener onLoadThumbRequestListener, Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;

        mImagePaths=ImagePaths;
        
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
    public bj_imagesAlbumFullScreenFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths, OnLoadOrginalRequestListener onLoadOrginalRequestListener , Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        mImagePaths=ImagePaths;
        
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
    public bj_imagesAlbumFullScreenFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths , Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        mImagePaths=ImagePaths;
        
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
	public bj_imagesAlbumFullScreenFragment( Integer FileIndex, Boolean GoToLast, String DirectoryPath, @Nullable List<String> FILE_EXTN, Boolean CanAdd, Boolean CanDell) {
        // Required empty public constructor
        mPosition=FileIndex;
        
        mFILE_EXTN=FILE_EXTN;
        mDirectoryPath=DirectoryPath;
        utils = new bj_imagesAlbum_Utils(getContext(),mFILE_EXTN);
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
	public bj_imagesAlbumFullScreenFragment() {
	}
	public bj_imagesAlbumFullScreenFragment(int contentLayoutId) {
		super(contentLayoutId);
	}
    //constractors end

    // set objects and variables start
	public void SetOptions(Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths , Boolean CanAdd, Boolean CanDell){
		mPosition=FileIndex;
		mImagePaths=ImagePaths;

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
		if (IMGBTNNew!=null){
            if(CanAdd) {
                IMGBTNNew.setVisibility(View.VISIBLE);
            }else {
                IMGBTNNew.setVisibility(View.GONE);
            }
        }
		if (IMGBTNDell!=null){
            if(CanDell) {
                IMGBTNDell.setVisibility(View.VISIBLE);
            }else {
                IMGBTNDell.setVisibility(View.GONE);
            }
        }

        if (ImagePaths!=null)
            loadImages();
	}
	public void SetOptions(Integer FileIndex, Boolean GoToLast, String DirectoryPath, @Nullable List<String> FILE_EXTN, Boolean CanAdd, Boolean CanDell){
		mPosition=FileIndex;

		mFILE_EXTN=FILE_EXTN;
		mDirectoryPath=DirectoryPath;
		utils = new bj_imagesAlbum_Utils(getContext(),mFILE_EXTN);
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
		loadImages();
	}
	public void SetImagePaths(@NonNull ArrayList<bj_imageNotice> ImagePaths){

		mImagePaths=ImagePaths;
        //mPagerAdapter.notifyDataSetChanged();
		loadImages();

	}
	public void SetImagePathsFromGallery(@NonNull Activity activity){
        mImagePaths =bj_image. getAllShownImagesPath(activity);
    }

	public void SetCanAdd(boolean canAdd){
    	this.CanAdd=canAdd;
	}
	public void SetCanDell(boolean canDell){
		this.CanDell=canDell;
	}
    public void SetOnLoadThumbRequestListener(OnLoadThumbRequestListener listener){
        mOnLoadThumbRequestListener=listener;
        if (mPagerAdapter!=null){
            mPagerAdapter.SetOnLoadThumbRequestListener(listener);
        }

    }
    public void SetOnUploadingImageListener(OnUploadingImageListener listener){
        mOnUploadingImageListener=listener;
        if (mPagerAdapter!=null){
            mPagerAdapter.SetOnUploadingImageListener(listener);
        }
    }
    public void SetOnLoadOrginalRequestListener(OnLoadOrginalRequestListener listener){
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
    public void SetOnImageChangedListener(OnImageChangedListener listener) {
        mOnImageChangedListener = listener;
    }
    public void SetTitle(String MyTitle){
        mTitle=MyTitle;
    }
    public void SetImageCurrent(int item){
        mPager.setCurrentItem(item);
    }
    // set objects and variables end

    //functions for set objects start
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

                                bj_image.UpdateGallery(getContext(), DestinationFile);
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
        File shareFile=new File(GetImageCurrentPath());
        Uri uri=bj_file.uriUtil.uriFromFile(getContext(),shareFile);
        if(mOnShareImageRequest!=null){
            mOnShareImageRequest.OnShareImage(shareFile,uri);
        }
    }
    public static String getRandomName() {
        String R;

        R =Long.toString((long) Math.floor((1 + Math.random() * 0x10000)),16);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        R =R +Long.toString((long) Math.floor((1 + Math.random() * 0x10000)),16);
        return R;

    }
    private void BTNProcesDellete(){
        if (mOnDelleteImageRequest!=null){
            mOnDelleteImageRequest.OnDelleteImage(mImagePaths.get(GetImageCurrentIndex()),GetImageCurrentIndex());

        }
    }
    private void loadImages() {

        if (mImagePaths==null || mImagePaths.size()==0){
            mImagePaths=new ArrayList<bj_imageNotice>();
            //mImagePaths.add(new bj_imageNotice())
        }
        mPagerAdapter = new bj_imagesAlbum_FullScreenImageAdapter(mImagePaths, mOnLoadOrginalRequestListener,  mOnLoadThumbRequestListener,mOnUploadingImageListener);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mPosition);
        TXVImageNumber.setText(GetFilesNumber()+"");
        TXVImageIndex.setText(GetImageCurrentNumber()+"");

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPager.setOnContextClickListener(new View.OnContextClickListener() {
                @Override
                public boolean onContextClick(View v) {
                    toggle();
                    return false;
                }
            });
        }else {

            mPager.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();

                }
            });
        }

    }
    //functions for set objects end

    // get objects and variables start
    public ArrayList<bj_imageNotice> GetImagePaths(){
        return   mImagePaths;

    }
	public boolean GetCanAdd(){
		return this.CanAdd;
	}
	public boolean GetCanDell(){
		return this.CanDell;
	}
    public  int GetFilesNumber(){
        try {
            return mImagePaths.size();
        }catch (Exception e){
            return 0;
        }
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
                return mImagePaths.get(mPager.getCurrentItem()).GetImagePath();
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
    // get objects and variables end

    // my functions start
    public void OnNewImageCreated(bj_imageNotice NewImageNotice, @Nullable String ProcessResult){
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
            File f=new File(mImagePaths.get(DeletedRowPosition).GetImagePath());
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
    public boolean AddImage(String newImagePathForAdd,Boolean isThumb,boolean addLast,boolean needUpload){
        Log.i(TAG, "AddImage: path: "+newImagePathForAdd);
        try {
            bj_imageNotice fp=new bj_imageNotice();
            fp.SetImagePath(newImagePathForAdd,isThumb,needUpload);


            if (addLast) {

                mImagePaths.add(mImagePaths.size(), fp);


            }else {

                mImagePaths.add(0, fp);
            }

            SetImagePaths(mImagePaths);
            //mImagePaths.get(indx).SetImagePath(newImagePathForAdd,isThumb,needUpload);
            return true;
        }catch (Exception e){
            Log.e(TAG, "AddImage: "+e.getMessage() );
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }
    public boolean DellImage(int ImageIndex,Boolean DeleteSorceFile_SO){
        Boolean RS;
        String FPTH;
        FPTH=mImagePaths.get(ImageIndex).GetImagePath();
        try {
            int p;
            if (mPager.getCurrentItem()==mImagePaths.size()) {
                p = mPager.getCurrentItem() - 1;
            }else {
                p = mPager.getCurrentItem();
            }
            mImagePaths.remove(ImageIndex);
            notifyDataChanged(ImageIndex);
            RS =true;
            Log.e(TAG, "DellImage: Remove from List.");
        }catch (Exception e){
            Toast.makeText(getContext(), "Error: Remove from List_" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "DellImage: Error: Remove from List_" + e.getMessage());
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
        if (RS){

            SetImagePaths(mImagePaths);
        }
        return RS;
    }
    public boolean DellImage(String ImagePath,Boolean DeleteSorceFile_SO){
        Boolean RS = false;
        int ImageIndex;
        try {
            for(int i=0;i<mImagePaths.size();i++){
                if (mImagePaths.get(i).GetImagePath()==ImagePath){
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
    public void notifyDataChanged(int position){
        mPagerAdapter.notifyDataSetChanged();

    }
    public bj_imagesAlbum_FullScreenImageAdapter GetPagerAdapter(){
        return mPagerAdapter;
    }

    // my functions end

    //override functions start
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bj_images_album_fullscreen, container, false);

    }
    @Override
    public void onStart() {
        super.onStart();
        mVisible = true;
        mControlsView = getActivity().findViewById(R.id.GIAF_LL_fullscreen_content_controls);
        mControlsViewUP = getActivity().findViewById(R.id.GIAF_LL_fullscreen_content_controls_Up);
        mContentView = getActivity().findViewById(R.id.GIAF_VP_fullscreen_content);
        mPager = (ViewPager) getActivity().findViewById(R.id.GIAF_VP_fullscreen_content);
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

        if(mPager==null) {
            //Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
        }else {

            loadImages();
        }

    }
    //override functions end

    // needed for fulscreen start
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


    // neede for fulscreen end

    // neede for fulscreen start

    // neede for fulscreen End
}
