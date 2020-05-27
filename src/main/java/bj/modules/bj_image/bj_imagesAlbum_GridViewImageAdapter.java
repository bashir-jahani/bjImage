package bj.modules.bj_image;
        import android.app.Activity;
        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.CompoundButton;
        import com.bumptech.glide.Glide;
        import java.util.ArrayList;

        import bj.modules.bj_image_classes;

public class bj_imagesAlbum_GridViewImageAdapter extends BaseAdapter {
    /** The context. */
    private ArrayList<bj_imageNotice> _imagesPath;
    private Context context;
    bj_image_classes.OnImageClickListener onImageClickListener;
    @bj_image_classes.FileNotice int fileNoticeForShow;
    private bj_image_classes.OnImageSelectListener onImageSelectListener;


    public bj_imagesAlbum_GridViewImageAdapter(Context context, ArrayList<bj_imageNotice> images, @bj_image_classes.FileNotice int fileNoticeForShow, bj_image_classes.OnImageClickListener onImageClickListener) {
        this.context = context;
        if (images!=null) {
            this._imagesPath = images;
        }else {
            this._imagesPath =bj_image. getAllShownImagesPath((Activity) context);

        }

        this.onImageClickListener=onImageClickListener;
        this.fileNoticeForShow=fileNoticeForShow;

    }
    public void SetOnImageSelectListener(bj_image_classes.OnImageSelectListener listener) {
       this. onImageSelectListener=listener;
    }


    public int getCount() {
        return _imagesPath.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        LoadableImageview MyView;

        String imageNotice=null;
        switch (fileNoticeForShow){
            case bj_image_classes.FileNotice
                    .ALBUME_NAME:
                imageNotice= _imagesPath.get(position).ImageName;
                break;
            case bj_image_classes.FileNotice
                    .NAME:
                imageNotice= _imagesPath.get(position).ImageAlbume;
                break;
            case bj_image_classes.FileNotice
                    .ALBUME_NAME_FILE_NAME:
                imageNotice= _imagesPath.get(position).ImageAlbume+"/"+ _imagesPath.get(position).ImageName;
                break;
            case bj_image_classes.FileNotice
                    .DATE_TAKEN:
                imageNotice= _imagesPath.get(position).GetDateTakenString();
                break;
            case bj_image_classes.FileNotice
                    .SIZE:
                long fSize= _imagesPath.get(position).ImageSize;
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
                break;
        }


        MyView = new LoadableImageview(context, imageNotice, _imagesPath.get(position).selected, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if ( onImageSelectListener!=null){
                    onImageSelectListener.onSelectionChanged(compoundButton,b,position, _imagesPath.get(position));
                }else {
                    _imagesPath.get(position).selected = b;
                }

            }
        });

        Glide.with(context).load(_imagesPath.get(position).GetImagePath()).placeholder(R.drawable.loading).into(MyView.imageView);
        if (onImageClickListener!=null){
            MyView.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onClick(v,position, _imagesPath.get(position));
                }
            });
        }


        return MyView;
    }
    private bj_loadable_image_listeners.OnLoadOrginalRequestListener mOnLoadOrginalRequestListener;
    private bj_loadable_image_listeners.OnLoadThumbRequestListener mOnLoadThumbRequestListener;
    private bj_loadable_image_listeners.OnUploadingImageListener mOnUploadingImageListener;
    public void SetOnLoadThumbRequestListener(bj_loadable_image_listeners.OnLoadThumbRequestListener listener){
        mOnLoadThumbRequestListener=listener;
    }
    public void SetOnLoadOrginalRequestListener(bj_loadable_image_listeners.OnLoadOrginalRequestListener listener){
        mOnLoadOrginalRequestListener=listener;
    }
    public void SetOnUploadingImageListener(bj_loadable_image_listeners.OnUploadingImageListener listener){
        mOnUploadingImageListener=listener;
    }

}
