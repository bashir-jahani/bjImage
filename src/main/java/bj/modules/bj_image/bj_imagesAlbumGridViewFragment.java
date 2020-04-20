package bj.modules.bj_image;


import android.content.res.Resources;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;



public class bj_imagesAlbumGridViewFragment extends Fragment {
    public bj_imagesAlbumGridViewFragment(ArrayList<bj_imageNotice> ImagePaths, Boolean CanAdd, Boolean CanDel){
        imagePaths=ImagePaths;
        this.CanAdd=CanAdd;
        this.CanDel=CanDel;
    }
    public bj_imagesAlbumGridViewFragment(String DirectoryPath, @Nullable List<String> FILE_EXTN, Boolean CanAdd, Boolean CanDel){
        utils = new bj_imagesAlbum_Utils(getContext(),FILE_EXTN);
        imagePaths = utils.getFilePaths(DirectoryPath);
        this.CanAdd=CanAdd;
        this.CanDel=CanDel;
    }
    private bj_imagesAlbum_Utils utils;
    private ArrayList<bj_imageNotice> imagePaths ;
    private bj_imagesAlbum_GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
Boolean CanAdd,CanDel;
    @Override
    public void onStart() {
        super.onStart();
        gridView = (GridView) getActivity().findViewById(R.id.GIA_Grid_view);


        if(gridView==null) {
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
        }else {
            // Initilizing Grid View
            InitilizeGridLayout();
            // loading all image paths from SD card


            // Gridview adapter
            adapter = new bj_imagesAlbum_GridViewImageAdapter((AppCompatActivity)getActivity(), imagePaths,
                    columnWidth,CanAdd,CanDel);

            // setting grid view adapter
            gridView.setAdapter(adapter);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bj_images_album_gridview, container, false);

    }
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                bj_imagesAlbum_AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((bj_imagesAlbum_AppConstant.NUM_OF_COLUMNS + 1) * padding)) / bj_imagesAlbum_AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(bj_imagesAlbum_AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

    }

}
