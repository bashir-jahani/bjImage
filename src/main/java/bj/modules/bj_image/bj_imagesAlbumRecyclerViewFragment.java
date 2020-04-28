package bj.modules.bj_image;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bj.modules.bj_image_classes;

public class bj_imagesAlbumRecyclerViewFragment extends Fragment {
	public 	ArrayList<bj_imageNotice> selectedImagePaths;
	String TAG="bj_imagesAlbumRecyclerViewFragment";
	private Integer mPosition;
	private bj_imagesAlbum_Utils utils;
	List<String> mFILE_EXTN;
	String mDirectoryPath;
	ArrayList<bj_imageNotice> mImagePaths;
	private boolean isHorizontally;
	@bj_image_classes.FileNotice int fileNoticeForShow;
	private RecyclerView recyclerView;
	bj_imagesAlbum_RecyclerViewAdapter adapter;
	private int padding=0;
	private ViewGroup.LayoutParams layoutParams=null;
	bj_image_classes.OnImageSelectListener onImageSelectListener;
	bj_image_classes.OnImageClickListener onImageClickListener;

	public bj_imagesAlbumRecyclerViewFragment( Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths ) {
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


	}
	public bj_imagesAlbumRecyclerViewFragment(Integer FileIndex, Boolean GoToLast, String DirectoryPath, @Nullable List<String> FILE_EXTN) {
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

	}
	public bj_imagesAlbumRecyclerViewFragment() {
	}
	public bj_imagesAlbumRecyclerViewFragment(int contentLayoutId) {
		super(contentLayoutId);
	}

	public void SetOptions(Integer FileIndex, Boolean GoToLast, @NonNull ArrayList<bj_imageNotice> ImagePaths ){
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



		loadImages("SetOptions 2");
	}
	public void SetImagePaths(@NonNull ArrayList<bj_imageNotice> ImagePaths){

		mImagePaths=ImagePaths;
		//mPagerAdapter.notifyDataSetChanged();
		loadImages("SetImagePaths");

	}
	public void LoadImagePathsFromGallery(@NonNull AppCompatActivity activity, @bj_image_classes.FileNotice int fileNoticeForShow, boolean isHorizontally){
		mImagePaths =bj_image. getAllShownImagesPath(activity);
		this.fileNoticeForShow=fileNoticeForShow;
		this.isHorizontally =isHorizontally;
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
	public void setViewHolderLayoutParams(ViewGroup.LayoutParams layoutParams, int padding) {
		this.layoutParams = layoutParams;
		this.padding=padding;
	}
	private void loadImages(String tag) {
		if (mImagePaths==null) {
			return;

		}



		adapter=new bj_imagesAlbum_RecyclerViewAdapter(getContext(), mImagePaths, true, bj_image_classes.FileNotice.SIZE, new bj_image_classes.OnImageSelectListener() {
			@Override
			public void onSelectionChanged(CompoundButton buttonView, boolean isSelect, int index, bj_imageNotice imageNotice) {
				if (mImagePaths.get(index).selected!=isSelect){
					mImagePaths.get(index).selected=isSelect;
					if (isSelect){
						if (selectedImagePaths==null){
							selectedImagePaths=new ArrayList<bj_imageNotice>();
						}
						selectedImagePaths.add(mImagePaths.get(index));
					}else {
						selectedImagePaths.remove(mImagePaths.get(index));
					}
				}

				if (bj_imagesAlbumRecyclerViewFragment.this.onImageSelectListener!=null){
					bj_imagesAlbumRecyclerViewFragment.this.onImageSelectListener.onSelectionChanged(buttonView,isSelect,index,imageNotice);
				}

			}
		});
		adapter.setOnImageClickListener(onImageClickListener);

		adapter.setViewHolderLayoutParams(layoutParams,padding);
		if (recyclerView==null){
			Toast.makeText(getContext(), "recyclerView is null", Toast.LENGTH_SHORT).show();
		}else {

			// use this setting to improve performance if you know that changes
			// in content do not change the layout size of the RecyclerView
			recyclerView.setHasFixedSize(true);


			// use a linear layout manager
			LinearLayoutManager layoutManager ;
			if (isHorizontally){
				layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
			}else {
				layoutManager = new LinearLayoutManager(getContext());
			}
			recyclerView.setLayoutManager(layoutManager);

			recyclerView.setAdapter(adapter);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.bj_images_album_recyclerview, container, false);

		recyclerView = (RecyclerView) view. findViewById(R.id.GIAGVrecyclerView);

		return view;

	}
	@Override
	public void onStart() {
		super.onStart();
		loadImages("onStart");

	}
	public void setOnImageClickListener(bj_image_classes.OnImageClickListener onImageClickListener) {
		this.onImageClickListener = onImageClickListener;
	}

	public void setOnImageSelectListener(bj_image_classes.OnImageSelectListener onImageSelectListener) {
		this.onImageSelectListener = onImageSelectListener;
	}
}
