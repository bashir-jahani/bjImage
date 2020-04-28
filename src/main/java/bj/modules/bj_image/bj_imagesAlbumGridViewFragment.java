package bj.modules.bj_image;



import android.app.Activity;

import android.os.Bundle;
import bj.modules.bj_image. bj_loadable_image_listeners.*;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bj.modules.bj_file;
import bj.modules.bj_file_classes;
import bj.modules.bj_file_objcets.file_object;
import bj.modules.bj_image_classes;
import bj.modules.bj_messageBox;
import bj.modules.bj_messageBox_objcets.messageBox;

import static bj.modules.bj_messageBox.MessageBox;


public class bj_imagesAlbumGridViewFragment extends Fragment {
    // variables start
    public 	ArrayList<bj_imageNotice> selectedImagePaths;
	@bj_image_classes.FileNotice int fileNoticeForShow;
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
    private bj_image_classes.OnImageSelectListener onCheckedChangeListener;
	Integer mPosition=0;
    String mTitle="Gilas Images View";

    ArrayList<bj_imageNotice> mImagePaths;


    private GridView gridView;
    private RecyclerView recyclerView;
    private bj_imagesAlbum_GridViewImageAdapter mAdapter;

    private bj_imagesAlbum_Utils utils;
    List<String> mFILE_EXTN;
    String mDirectoryPath;
    private OnLoadOrginalRequestListener mOnLoadOrginalRequestListener;
    private OnLoadThumbRequestListener mOnLoadThumbRequestListener;
    private OnUploadingImageListener mOnUploadingImageListener;

    private OnNewImageRequest mOnNewImageRequest;
    private OnDelleteImageRequest mOnDelleteImageRequest;
    private OnShareImageRequest mOnShareImageRequest;
    private bj_image_classes. OnImageClickListener onImageClickListener;
    private String TAG="bj_imagesAlbumGridViewFragment";
    // variables end
    //constractors start
    public bj_imagesAlbumGridViewFragment(Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths, OnLoadOrginalRequestListener onLoadOrginalRequestListener , OnLoadThumbRequestListener onLoadThumbRequestListener, OnUploadingImageListener onUploadingImageListener, Boolean CanAdd, Boolean CanDell) {
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
    public bj_imagesAlbumGridViewFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths, OnLoadOrginalRequestListener onLoadOrginalRequestListener , OnLoadThumbRequestListener onLoadThumbRequestListener, Boolean CanAdd, Boolean CanDell) {
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
    public bj_imagesAlbumGridViewFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths, OnLoadOrginalRequestListener onLoadOrginalRequestListener , Boolean CanAdd, Boolean CanDell) {
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
    public bj_imagesAlbumGridViewFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths , Boolean CanAdd, Boolean CanDell) {
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
    public bj_imagesAlbumGridViewFragment( Integer FileIndex, Boolean GoToLast, String DirectoryPath, @Nullable List<String> FILE_EXTN, Boolean CanAdd, Boolean CanDell) {
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
    public bj_imagesAlbumGridViewFragment() {
    }
    public bj_imagesAlbumGridViewFragment(int contentLayoutId) {
        super(contentLayoutId);
    }
    //constractors end
	public GridView GetGridView(){
    	return gridView;
	}
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
            loadImages("SetOptions 1");
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
        loadImages("SetOptions 2");
    }
    public void SetImagePaths(@NonNull ArrayList<bj_imageNotice> ImagePaths){

        mImagePaths=ImagePaths;
        //mPagerAdapter.notifyDataSetChanged();
        loadImages("SetImagePaths");

    }
    public void LoadImagePathsFromGallery(@NonNull Activity activity, @bj_image_classes.FileNotice int fileNoticeForShow){
        mImagePaths =bj_image. getAllShownImagesPath(activity);
        this.fileNoticeForShow=fileNoticeForShow;

        sortByDateTaken();
        //loadImages("LoadImagePathsFromGallery");

    }
    public void sortByName(){
        Collections.sort(this.mImagePaths, new Comparator<bj_imageNotice>() {
            @Override
            public int compare(bj_imageNotice o1, bj_imageNotice o2) {
                String image1 = o1.ImageName;
                String image2 = o2.ImageName;
                return image2.compareTo(image1);
            }
        });
    }
    public void sortByDateTaken(){
        Collections.sort(this.mImagePaths, new Comparator<bj_imageNotice>() {
            @Override
            public int compare(bj_imageNotice o1, bj_imageNotice o2) {
                Long image1 = o1.GetDateTaken();
                Long image2 = o2.GetDateTaken();


                return image2.compareTo(image1);
            }
        });
    }
    public void sortByNameDes(){
        Collections.sort(this.mImagePaths, new Comparator<bj_imageNotice>() {
            @Override
            public int compare(bj_imageNotice o1, bj_imageNotice o2) {
                String image1 = o1.ImageName;
                String image2 = o2.ImageName;
                return image1.compareTo(image2);
            }
        });
    }
    public void sortByDateTakenDes(){
        Collections.sort(this.mImagePaths, new Comparator<bj_imageNotice>() {
            @Override
            public int compare(bj_imageNotice o1, bj_imageNotice o2) {
                Long image1 = o1.GetDateTaken();
                Long image2 = o2.GetDateTaken();


                return image1.compareTo(image2);
            }
        });
    }

	public void SetOnItemClickListener(bj_image_classes. OnImageClickListener onImageClickListener){
    	this.onImageClickListener=onImageClickListener;
	}

    public void SetCanAdd(boolean canAdd){
        this.CanAdd=canAdd;
    }
    public void SetCanDell(boolean canDell){
        this.CanDell=canDell;
    }
    public void SetOnLoadThumbRequestListener(OnLoadThumbRequestListener listener){
        mOnLoadThumbRequestListener=listener;
        if (mAdapter !=null){
            mAdapter.SetOnLoadThumbRequestListener(listener);
        }

    }
    public void SetOnUploadingImageListener(OnUploadingImageListener listener){
        mOnUploadingImageListener=listener;
        if (mAdapter !=null){
            mAdapter.SetOnUploadingImageListener(listener);
        }
    }
    public void SetOnLoadOrginalRequestListener(OnLoadOrginalRequestListener listener){
        mOnLoadOrginalRequestListener=listener;
        if (mAdapter !=null){
            mAdapter.SetOnLoadOrginalRequestListener(listener);
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

    // set objects and variables end

    //functions for set objects start
    private void ProcesSetButtonsClickEvents(){
        if (IMGBTNBack==null){
            return;
        }
        IMGBTNBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        MessageBox(R.string.question_title,R.string.question_Save_in_Gallery, bj_messageBox.BJMessagesButtonKind.Yes_No,getContext(),new messageBox.OnDialogResultListener(){
            @Override
            public boolean OnResult(Boolean dialogResult) {
                if (dialogResult) {


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
    }
    private void BTNProcesShare(){

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

        }
    }

    private void loadImages(String tag) {

        Log.i(TAG, "loadImages: "+tag+": vertically");
        gridView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        if (mImagePaths==null || mImagePaths.size()==0){
            mImagePaths=new ArrayList<bj_imageNotice>();
            //mImagePaths.add(new bj_imageNotice())
        }
        mAdapter = new bj_imagesAlbum_GridViewImageAdapter(getContext(), mImagePaths,fileNoticeForShow,onImageClickListener);
		mAdapter.SetOnImageSelectListener(new bj_image_classes.OnImageSelectListener() {
			@Override
			public void onSelectionChanged(CompoundButton buttonView, boolean isSelect, int index, bj_imageNotice imageNotice) {
				if (onCheckedChangeListener!=null){
					onCheckedChangeListener.onSelectionChanged(buttonView,isSelect,index,imageNotice);

				}
                if (isSelect){
                    if (selectedImagePaths==null){
                        selectedImagePaths=new ArrayList<bj_imageNotice>();
                    }
                    selectedImagePaths.add(mImagePaths.get(index));
                }else {
                    selectedImagePaths.remove(mImagePaths.get(index));
                }
				mImagePaths.get(index).selected=isSelect;
			}
		});
        gridView.setAdapter(mAdapter);
        if (mOnImageChangedListener!=null){
            mOnImageChangedListener.OnChanged(mPosition,mPosition+1);
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

    // get objects and variables end

    // my functions start
    public void OnNewImageCreated(bj_imageNotice NewImageNotice, @Nullable String ProcessResult){
        if (NewImageNotice!=null) {
            mImagePaths.add(NewImageNotice);
            mAdapter.notifyDataSetChanged();

            TXVImageNumber.setText(GetFilesNumber()+"");

        }
        if (ProcessResult!=null){
            Toast.makeText(getContext(), ProcessResult, Toast.LENGTH_SHORT).show();
        }
    }
    public void OnDelletedImage(Integer DeletedRowPosition,@Nullable String ProcessResult){
        if (DeletedRowPosition>-1){
            File f=new File(mImagePaths.get(DeletedRowPosition).GetImagePath());
            f.delete();
            File TFile=new File(f.getParent() +File.separator + "thumbs" + File.separator + f.getName());

            TFile.delete();
            mImagePaths.remove(mImagePaths.get(DeletedRowPosition));

            mAdapter.notifyDataSetChanged();
            gridView.setAdapter(gridView.getAdapter());

            TXVImageNumber.setText(GetFilesNumber()+"");

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

                    mImagePaths.remove(ImageIndex);

                    gridView.notifyAll();

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
        mAdapter.notifyDataSetChanged();

    }
    public bj_imagesAlbum_GridViewImageAdapter GetAdapter(){
        return mAdapter;
    }


    // my functions end

    //override functions start
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.bj_images_album_gridview, container, false);
        gridView = (GridView)view. findViewById(R.id.GIAGVgridView);
		recyclerView = (RecyclerView) view. findViewById(R.id.GIAGVrecyclerView);
        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        mVisible = true;




        if(CanAdd) {
           // IMGBTNNew.setVisibility(View.VISIBLE);
        }else {
           // IMGBTNNew.setVisibility(View.GONE);
        }
        if(CanDell) {
            //IMGBTNDell.setVisibility(View.VISIBLE);
        }else {
           //IMGBTNDell.setVisibility(View.GONE);
        }

        ProcesSetButtonsClickEvents();

        ///////////**************************************************************

       loadImages("onStart");

    }

	public void SetOnImageSelectListener(bj_image_classes.OnImageSelectListener listener) {
		onCheckedChangeListener=listener;
	}
}
