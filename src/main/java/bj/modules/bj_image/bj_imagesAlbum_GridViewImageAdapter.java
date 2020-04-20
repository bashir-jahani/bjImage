package bj.modules.bj_image;


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;

        import androidx.appcompat.app.AppCompatActivity;

        import com.bumptech.glide.Glide;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.util.ArrayList;

        import static bj.modules.bj_image_classes.fragmentOpen;


/**
 * Created by Bashir jahani on 1/24/2018.
 */

class bj_imagesAlbum_GridViewImageAdapter extends BaseAdapter {
    private AppCompatActivity _activity;

    private ArrayList<bj_imageNotice> _filePaths ;
    private int imageWidth;
    Boolean CanAdd,CanDel;

    public bj_imagesAlbum_GridViewImageAdapter(AppCompatActivity activity, ArrayList<bj_imageNotice> filePaths,
                                              int imageWidth, Boolean CanAdd, Boolean CanDel) {
        this._activity = activity;
        this._filePaths = filePaths;

        this.imageWidth = imageWidth;
        this.CanAdd=CanAdd;
        this.CanDel=CanDel;
    }


    @Override
    public int getCount() {

        return this._filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView=(ImageView) parent.findViewById(R.id.GIAPI_imageView);
        /* get screen dimensions
        Bitmap image = decodeFile(_filePaths.get(position), imageWidth,
                imageWidth);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        //imageView.setImageBitmap(image);
        */
        imageView.setScaleType(ImageView.ScaleType.FIT_END);
        Glide.with(_activity).load(_filePaths.get(position)).thumbnail((float) .05).into(imageView);

        // image view click listener
        imageView.setOnClickListener(new OnImageClickListener(position,parent));

        return imageView;
    }

    class OnImageClickListener implements OnClickListener {

        int _postion;
        ViewGroup _parent;
        // constructor
        public OnImageClickListener(int position, ViewGroup parent) {
            this._postion = position;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            fragmentOpen(  _activity.getSupportFragmentManager(),new bj_imagesAlbumFullScreenFragment( _postion,false,_filePaths,CanAdd,CanDel),true, bj_imagesAlbum_AppConstant.FragmentContainerID);
            //Intent i = new Intent(_activity, FullScreenViewActivity.class);
            //i.putExtra("position", _postion);
            //_activity.startActivity(i);
            //Toast.makeText(_activity,_postion+ "", Toast.LENGTH_SHORT).show();
        }

    }
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
