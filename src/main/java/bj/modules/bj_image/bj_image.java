package bj.modules.bj_image;
import bj.modules.bj_image. bj_loadable_image_listeners.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import bj.modules.bj_file;
import bj.modules.bj_file_classes.*;
import bj.modules.bj_messageBox.*;
import bj.modules.bj_image_classes.*;
import bj.modules.bj_permission;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static bj.modules.bj_messageBox.MessageBoxAsError;
import static bj.modules.bj_permission.CheckPermision;

/**
 * Created by bashir on 11/22/2017.
 */

public class bj_image {
    private static final String TAG="bj_image";
    public static class selectImage{
        private static File createCameraTemporaryFile(String part, String ext) throws Exception
        {
            File tempDir= Environment.getExternalStorageDirectory();
            tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
            if(!tempDir.exists())
            {
                tempDir.mkdirs();
            }
            return File.createTempFile(part, ext, tempDir);
        }
        public static Uri UriForCameraImage(){
            return mUriForCameraImage;
        }
        private static Uri mUriForCameraImage;
        public static Uri ImageUriFromGalleryAndContent(Intent data){
            return data.getData();
        }
        public static Uri ImageUriFromCamera(Intent data){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            if (destination.exists()){
                destination.delete();
            }
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
          return Uri.fromFile(destination);
        }
        public static Uri ImageUriFromCamera(Intent data,String ImagePath){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(ImagePath);
            if (destination.exists()){
                destination.delete();
            }
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Uri.fromFile(destination);
        }
        static BJAlertDialog GAD;
        private static void UserSelectAction(Context context, BJAlertDialog.BJAlertDialogItem SelectedItem){
            GAD.dismiss();
            if (SelectedItem.ID== -1){

                return;
            }

            if (SelectedItem.ID== BJRequestCodes.REQUEST_CODE_FILE_FROM_CAMERA){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (mUriForCameraImage!=null){


                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriForCameraImage);
                }

                startActivityForResult((Activity)context,intent, BJRequestCodes.REQUEST_CODE_FILE_FROM_CAMERA,null);
                //startActivityForResult(intent, BJRequestCodes.REQUEST_CODE_FILE_FROM_CAMERA);
                return;
            }
            if (SelectedItem.ID== BJRequestCodes.REQUEST_CODE_FILE_FROM_CONTENT){
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult((Activity)context, intent, BJRequestCodes.REQUEST_CODE_FILE_FROM_CONTENT,null);
                return;
            }
            if (SelectedItem.ID== BJRequestCodes.REQUEST_CODE_FILE_FROM_GALERY){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult((Activity)context,pickPhoto, BJRequestCodes.REQUEST_CODE_FILE_FROM_GALERY,null);
            }
        }
        public static void SelectImageMethodeDialog(String title, final Context context, boolean useCamera) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (useCamera) {
                File photo;
                try
                {
                    // place where to store camera taken picture
                    photo = createCameraTemporaryFile("picture", ".jpg");
                    photo.delete();

                    mUriForCameraImage = Uri.fromFile(photo);
                }
                catch(Exception e)
                {
                    Log.v(TAG, "Can't create file to take picture!");

                    useCamera= false;
                }

            }
            if (useCamera) {


                final BJAlertDialog.BJAlertDialogItem[] items = {
                        new BJAlertDialog.BJAlertDialogItem(BJRequestCodes.REQUEST_CODE_FILE_FROM_GALERY, context.getString(R.string.from_gallery), android.R.drawable.ic_menu_gallery),
                        new BJAlertDialog.BJAlertDialogItem(BJRequestCodes.REQUEST_CODE_FILE_FROM_CONTENT, context.getString(R.string.from_content), android.R.drawable.ic_menu_more),
                        new BJAlertDialog.BJAlertDialogItem(BJRequestCodes.REQUEST_CODE_FILE_FROM_CAMERA, context.getString(R.string.from_camera), android.R.drawable.ic_menu_camera),


                        new BJAlertDialog.BJAlertDialogItem(-1, context.getString(R.string.cancel)),//no icon for this one
                };
                GAD=new BJAlertDialog(context, title, R.drawable.ic_menu_gallery,

                        items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserSelectAction(context, items[position]);

                    }
                });


                GAD.show();
            }else {
                final BJAlertDialog.BJAlertDialogItem[] items = {
                        new BJAlertDialog.BJAlertDialogItem(BJRequestCodes.REQUEST_CODE_FILE_FROM_GALERY, context.getString(R.string.from_gallery), android.R.drawable.ic_menu_gallery),
                        new BJAlertDialog.BJAlertDialogItem(BJRequestCodes.REQUEST_CODE_FILE_FROM_CONTENT, context.getString(R.string.from_content), android.R.drawable.ic_menu_more),
                        //new BJAlertDialog.BJAlertDialogItem(BJRequestCodes.REQUEST_CODE_FILE_FROM_CAMERA, context.getString(R.string.from_camera), android.R.drawable.ic_menu_camera),


                        new BJAlertDialog.BJAlertDialogItem(-1, context.getString(R.string.cancel)),//no icon for this one
                };
                GAD=new BJAlertDialog(context, title, R.drawable.ic_menu_gallery,

                        items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserSelectAction(context, items[position]);

                    }
                });


                GAD.show();
            }


        }


        public static class Utility {
            public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public static boolean checkPermission(final Context context)
            {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if(currentAPIVersion>= Build.VERSION_CODES.M)
                {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                            alertBuilder.setCancelable(true);
                            alertBuilder.setTitle("Permission necessary");
                            alertBuilder.setMessage("External storage permission is necessary");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                        } else {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
    }
    public static Bitmap BitmapFromUri(Context context, Uri uri) throws FileNotFoundException {

    InputStream input = context.getContentResolver().openInputStream(uri);
    return BitmapFactory.decodeStream(input);
}
    public static void SaveImageToGallery(Context context, String ImagePath, OnGFileDialogResultListener listener){
        bj_file InFile=new bj_file(ImagePath,context);
        InFile.GFCopy(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+context.getApplicationInfo().processName, listener );
    }
    public static Boolean SaveImageToGallery(Context context,Bitmap ImageBitmap,String ImageName){
        Boolean RS=false;
        String OutputPath;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),context.getApplicationInfo().processName);

        OutputPath = storageDir +  File.separator + ImageName;
        File OutFile=new File(OutputPath);

        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(OutFile));
        } catch (FileNotFoundException e) {

        }
        ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(OutFile.exists() & OutFile.isFile()){
            RS=true;
            UpdateGallery(context, OutputPath);
        }

        return RS;
    }
    public static void SaveImageToContent(Context context,String ImagePath,String DirectoryPath, OnGFileDialogResultListener listener){
        bj_file InFile=new bj_file(ImagePath,context);
        InFile.GFCopy(DirectoryPath, listener);

    }
    public static void SaveImageToContent(Context context,String ImagePath,File Directory, OnGFileDialogResultListener listener){
        bj_file InFile=new bj_file(ImagePath,context);
        InFile.GFCopy(Directory, listener);

    }
    public static Boolean SaveImageToContent(Context context,Bitmap ImageBitmap,String ImageName,File DirectoryPath){
        Boolean RS=false;
        String OutputPath;


        OutputPath = DirectoryPath +  File.separator + ImageName;
        File OutFile=new File(OutputPath);

        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(OutFile));
        } catch (FileNotFoundException e) {

        }
        ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(OutFile.exists() & OutFile.isFile()){
            RS=true;
            UpdateGallery(context, OutputPath);
        }

        return RS;
    }
    public static Boolean SaveImageToContent(Context context,Bitmap ImageBitmap,String ImageName,String Directory){
        Boolean RS=false;
        String OutputPath;


        OutputPath = Directory +  File.separator + ImageName;
        File OutFile=new File(OutputPath);

        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(OutFile));
        } catch (FileNotFoundException e) {

        }
        ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(OutFile.exists() & OutFile.isFile()){
            RS=true;
            UpdateGallery(context, OutputPath);
        }

        return RS;
    }
    public static boolean Exist(Context context, String imagePath){
        bj_file bjFile=new bj_file(imagePath,context);

        if (bjFile.isDirectory()){
            return false;
        }
        return bjFile.exists();
    }
    public static void UpdateGallery(Context context, String ImagePath){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(ImagePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context. sendBroadcast (mediaScanIntent);
    }
    public static void UpdateGallery(Context context,File Image){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(Image);
        mediaScanIntent.setData(contentUri);
       context. sendBroadcast (mediaScanIntent);
    }
    public static Fragment bj_imagesAlbumOpen(Boolean AsFullScreen, Context context, String DirectoryPath, Boolean GotoLast, Integer position, @Nullable List<String> FILE_EXTN, Boolean CanAdd, Boolean CanDell) {
        if (FILE_EXTN == null  ) {
            FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");
        }
        Fragment fragment ;
        if(AsFullScreen) {
            fragment = new bj_imagesAlbumFullScreenFragment(position,GotoLast, DirectoryPath, FILE_EXTN,CanAdd,CanDell);
        }else {
            fragment=new bj_imagesAlbumGridViewFragment(position,GotoLast, DirectoryPath, FILE_EXTN,CanAdd,CanDell);
        }
        return fragment;
    }
    public static Fragment bj_imagesAlbumOpen(Context context, Boolean AsFullScreen, ArrayList<bj_imageNotice> ImagePaths, Boolean GotoLast, Integer position, Boolean CanAdd, Boolean CanDell) {

        Fragment fragment ;
        if(AsFullScreen) {
            fragment = new bj_imagesAlbumFullScreenFragment( position,GotoLast, ImagePaths,CanAdd,CanDell);
        }else {
            fragment=new bj_imagesAlbumGridViewFragment( position,GotoLast, ImagePaths,CanAdd,CanDell);
        }
        return fragment;
    }
    public static Fragment bj_imagesAlbumOpen(Context context, Boolean AsFullScreen, ArrayList<bj_imageNotice> ImagePaths, Boolean GotoLast, Integer position, Boolean CanAdd, Boolean CanDell, OnLoadOrginalRequestListener onLoadOrginalRequestListener) {

        Fragment fragment ;
        if(AsFullScreen) {
            fragment = new bj_imagesAlbumFullScreenFragment( position,GotoLast, ImagePaths,onLoadOrginalRequestListener,CanAdd,CanDell);
        }else {
            fragment=new bj_imagesAlbumGridViewFragment(position,GotoLast, ImagePaths,onLoadOrginalRequestListener,CanAdd,CanDell);
        }
        return fragment;
    }
    public static Fragment bj_imagesAlbumOpen( Boolean AsFullScreen, ArrayList<bj_imageNotice> ImagePaths, Boolean GotoLast, Integer position, Boolean CanAdd, Boolean CanDell, OnLoadOrginalRequestListener onLoadOrginalRequestListener, OnLoadThumbRequestListener onLoadThumbRequestListener) {

        Fragment fragment ;
        if(AsFullScreen) {
            fragment = new bj_imagesAlbumFullScreenFragment( position,GotoLast, ImagePaths,onLoadOrginalRequestListener, onLoadThumbRequestListener,CanAdd,CanDell);
        }else {
            fragment=new bj_imagesAlbumGridViewFragment(position,GotoLast, ImagePaths,onLoadOrginalRequestListener, onLoadThumbRequestListener,CanAdd,CanDell);
        }
        return fragment;
    }

    public static ArrayList<bj_imageNotice> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_index_name,column_index_date_taken,column_index_size;
        ArrayList<bj_imageNotice> listOfAllImages = new ArrayList<bj_imageNotice>();
        bj_imageNotice image = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATE_TAKEN,MediaStore.Images.Media.SIZE };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_index_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        column_index_date_taken = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        while (cursor.moveToNext()) {
            image = new bj_imageNotice();
            image.SetImagePath(cursor.getString(column_index_data),false,false);
            image.SetDateTaken(cursor.getLong(column_index_date_taken));
            image.ImageName=cursor.getString(column_index_name);
            image.ImageSize=cursor.getLong(column_index_size);
            image.ImageAlbume=cursor.getString(column_index_folder_name);

            listOfAllImages.add(image);
        }

        return listOfAllImages;
    }
    public static Bitmap drawOctagonShapedBitmap(Context context,Bitmap src, Integer pixels) {

        Bitmap output = Bitmap.createBitmap(src.getWidth(), src
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx;
        final float roundPy;
        if (pixels==-1) {
            roundPy = src.getHeight()/2;
        }else {
            roundPy = pixels;
        }
        if (pixels==-1) {
            roundPx = src.getWidth()/2;
        }else {
            roundPx = pixels;
        }


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        try {
            paint.setColor(context.getColor(color));
        }catch (Exception e){
            paint.setColor(color);
        }
        canvas.drawRoundRect(rectF, (float) (roundPx*1.05), (float) (roundPy*1.05), paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        return output;
    }
    public static Bitmap drawOctagonShapedBitmap(Context context,String ImagePath,Integer pixels) {
        Bitmap src=null;
        src= BitmapFactory.decodeFile(ImagePath);
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx;
        final float roundPy;
        if (pixels==-1) {
            roundPy = src.getHeight()/2;
        }else {
            roundPy = pixels;
        }
        if (pixels==-1) {
            roundPx = src.getWidth()/2;
        }else {
            roundPx = pixels;
        }


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        try {
            paint.setColor(context.getColor(color));
        }catch (Exception e){
            paint.setColor(color);
        }
        canvas.drawRoundRect(rectF, (float) (roundPx*1.05), (float) (roundPy*1.05), paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        return output;

    }
    public static Bitmap drawOctagonShapedBitmap_FromBase64(Context context,String ImageBase64String,Integer pixels) {
        Bitmap src=null;
        byte[] decodedString = Base64.decode(ImageBase64String, Base64.DEFAULT);
        src=BitmapFactory.decodeByteArray(decodedString  , 0, decodedString.length);
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx;
        final float roundPy;
        if (pixels==-1) {
            roundPy = src.getHeight()/2;
        }else {
            roundPy = pixels;
        }
        if (pixels==-1) {
            roundPx = src.getWidth()/2;
        }else {
            roundPx = pixels;
        }


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        try {
            paint.setColor(context.getColor(color));
        }catch (Exception e){
            paint.setColor(color);
        }
        canvas.drawRoundRect(rectF, (float) (roundPx*1.05), (float) (roundPy*1.05), paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        return output;
    }
    public static byte[] BitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static String BitmapToBase64String(Bitmap bitmap, Bitmap.CompressFormat format,Integer Quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, Quality, stream);
        byte[] byteArray = stream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }


    public static byte[] Base64StringConvertToByteArray(String Base64String){
        byte[] decodedString;
        decodedString = Base64.decode(Base64String, Base64.DEFAULT);
        return decodedString;
    }
    public static Bitmap Base64StringConvertToBitmap(Context context,String Base64String,Boolean Rounded){
        byte[] decodedString = Base64.decode(Base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString  , 0, decodedString.length);
        if (Rounded) {
            return drawOctagonShapedBitmap(context, bitmap,-1);
        }else {
            return bitmap;
        }
    }
    public static Bitmap Base64StringConvertToBitmap(String Base64String){
        byte[] decodedString = Base64.decode(Base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString  , 0, decodedString.length);
        return bitmap;
    }

    @NonNull
    public  static Boolean ImageSave(Context context, String ImagePath, String ImageBase64String)   {
        final boolean[] _HavePermission = {false};
        //Log.d("bj modules","ImageSave:" + "\n" + ImagePath + "\n" + ImageBase64String);
        CheckPermision(context, Manifest.permission.READ_EXTERNAL_STORAGE, R.string.permission_save_images, new bj_permission.OnGetPermissionListener() {
            @Override
            public void onPermissionProcesComplated(String PermissionNeeded, Boolean HavePermission) {
                _HavePermission[0] =HavePermission;
            }
        });
        boolean Saved=false;

        if (_HavePermission[0]) {
            Log.d("bj modules", "ImageSave : " + ImagePath);
            try {

                byte[] decodedString = Base64.decode(ImageBase64String, Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bm != null) {
                    File file = new File(ImagePath);
                    file.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Saved = true;
                }

            } catch (IOException e) {
                Log.d("bj modules", "ImageSave Error: " + e.getMessage());
            }

            File file1 = new File(ImagePath);
            Log.d("bj modules", "ImageSave result: " + (file1.exists() & file1.isFile()));



        }else {
            MessageBoxAsError(context.getResources().getString(R.string.permission_not_accept),context);
        }
        return Saved;

    }
    @NonNull
    public  static Boolean ImageSave(Context context,String ImagePath, InputStream inputStream)   {
        final boolean[] _HavePermission = {false};
        CheckPermision(context, Manifest.permission.READ_EXTERNAL_STORAGE, R.string.permission_save_images, new bj_permission.OnGetPermissionListener() {
            @Override
            public void onPermissionProcesComplated(String PermissionNeeded, Boolean HavePermission) {
                _HavePermission[0] =HavePermission;
            }
        });


        boolean Saved=false;


        if (_HavePermission[0]) {
            Log.d("bj modules","ImageSave : " +ImagePath);
            try{


                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                if (bm!=null){
                    File file = new File(ImagePath);
                    file.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Saved=true;
                }

            }catch (IOException e){
                Log.d("bj modules","ImageSave Error: " +e.getMessage());
            }

            File file1 = new File(ImagePath);
            Log.d("bj modules","ImageSave result: " +(file1.exists() & file1.isFile()));

        }else {
            MessageBoxAsError(context.getResources().getString(R.string.permission_not_accept),context);
        }
        return Saved;
    }
    public static Bitmap Load_PictureTo_ImageView(String ImageFilePath,   ImageView ImageViewForShow, Integer CournerRadius, @ColorRes int BorderColor, int BorderSize, @ColorRes int ShadowColor, Integer ShadowSize, Boolean CenterShadow, Boolean InversShadow, @ColorRes int SecondBorderColor, int SecondBorderSiz, int ResizeImageWidth, @DrawableRes int  errorImageDrawableResource,  @DrawableRes int  placeholderImageDrawableResource){
        Context context=ImageViewForShow.getContext();
        final boolean[] _HavePermission = {false};
        //Log.e("bj modules","Load_PictureTo_ImageView " );
        CheckPermision(context, Manifest.permission.READ_EXTERNAL_STORAGE, R.string.permission_save_images, new bj_permission.OnGetPermissionListener() {
            @Override
            public void onPermissionProcesComplated(String PermissionNeeded, Boolean HavePermission) {
                _HavePermission[0] =HavePermission;
            }
        });

        Bitmap Mybitmap = null;
        if (_HavePermission[0]){
            Mybitmap=CournerRadiusForImage(context, null,ImageFilePath,CournerRadius,BorderColor,BorderSize,ShadowColor,ShadowSize,CenterShadow,InversShadow,SecondBorderColor,SecondBorderSiz,ResizeImageWidth);
            if(Mybitmap!=null) {
                //Log.e("bj modules","Load_PictureTo_ImageView :Mybitmap <> null" );
                try {
                    if (placeholderImageDrawableResource==-1) {
                        Glide.with(ImageViewForShow.getContext()).load(BitmapToByte(Mybitmap)).placeholder(R.drawable.loading).into(ImageViewForShow);
                    }else {
                        Glide.with(ImageViewForShow.getContext()).load(BitmapToByte(Mybitmap)).placeholder(R.drawable.loading).into(ImageViewForShow);
                    }

                    //ImageViewForShow.setImageBitmap(Mybitmap);
                    //Log.e("bj modules","Load_PictureTo_ImageView :Mybitmap Loaded" );
                }catch (Exception e){
                    try{
                        //Log.e("bj modules","Load_PictureTo_ImageView : Load errorImageDrawableResource after cant Load MyBitmap"+ "\n"+ e.getMessage() );
                        Glide.with(ImageViewForShow.getContext()).load(errorImageDrawableResource).placeholder(R.drawable.loading).into(ImageViewForShow);
                        //ImageViewForShow.setImageResource(errorImageDrawableResource);
                    }catch (Exception e1){
                        Log.e("bj modules","Load_PictureTo_ImageView : Cant Load errorImageDrawableResource"+ "\n"+ e1.getMessage());
                    }

                }
            }else {
                try{
                    //Log.e("bj modules","Load_PictureTo_ImageView : Load errorImageDrawableResource" );
                    Glide.with(ImageViewForShow.getContext()).load(errorImageDrawableResource).placeholder(R.drawable.loading).into(ImageViewForShow);
                }catch (Exception e1){
                    Log.e("bj modules","error :Load_PictureTo_ImageView : cant Load errorImageDrawableResource" + "\n"+ e1.getMessage());
                }
            }

        }else {
            MessageBoxAsError(context.getResources().getString(R.string.permission_not_accept),context);
        }
        return Mybitmap;
    }
    public static Bitmap Load_PictureTo_ImageView(@DrawableRes int ImageDrawableResourceID, ImageView ImageViewForShow, Integer CournerRadius, @ColorRes int BorderColor, int BorderSize, @ColorInt @ColorRes int ShadowColor, Integer ShadowSize, Boolean CenterShadow, Boolean InversShadow, @ColorInt  @ColorRes int SecondBorderColor, int SecondBorderSiz, int ResizeImageWidth, @DrawableRes int  errorImageDrawableResource, @DrawableRes int  placeholderImageDrawableResource){

        Context context=ImageViewForShow.getContext();
        Bitmap Mybitmap =null;
        Mybitmap=CournerRadiusForImage(context, null,ImageDrawableResourceID,CournerRadius,BorderColor,BorderSize,ShadowColor,ShadowSize,CenterShadow,InversShadow,SecondBorderColor,SecondBorderSiz,ResizeImageWidth);
        if(Mybitmap!=null) {
            try {
                if (placeholderImageDrawableResource==-1) {
                    Glide.with(ImageViewForShow.getContext()).load(BitmapToByte(Mybitmap)).placeholder(R.drawable.loading).into(ImageViewForShow);
                }else {
                    Glide.with(ImageViewForShow.getContext()).load(BitmapToByte(Mybitmap)).placeholder(R.drawable.loading).into(ImageViewForShow);
                }
            }catch (Exception e){
                try{
                    Glide.with(ImageViewForShow.getContext()).load(errorImageDrawableResource).placeholder(R.drawable.loading).into(ImageViewForShow);
                    //ImageViewForShow.setImageResource(errorImageDrawableResource);
                }catch (Exception e1){

                }

            }
        }else {
            try{
                Glide.with(ImageViewForShow.getContext()).load(errorImageDrawableResource).placeholder(R.drawable.loading).into(ImageViewForShow);
            }catch (Exception e1){

            }
        }




        return Mybitmap;
    }

    public static Bitmap PictureBoxFrameForImage(Context context,@Nullable ImageView ImageViewForShow, @ColorRes int color, Integer BoxSiz, String ImagePath, int ResizeImageWidth){
        if (!(ImagePath==null)){
            //Log.e("bj modules", ImagePath);
            File file=new File(ImagePath);

            if (file.exists()) {
                Bitmap source = BitmapFactory.decodeFile(ImagePath);
                if (ResizeImageWidth>0 ){

                    source=Bitmap.createScaledBitmap(source,ResizeImageWidth,ResizeImageWidth*source.getHeight()/source.getWidth(),true);

                }else {
                    if (ImageViewForShow!=null){
                        source=Bitmap.createScaledBitmap(source,ImageViewForShow.getWidth(),ImageViewForShow.getWidth()*source.getHeight()/source.getWidth(),true);

                    }
                }
                Bitmap result=null;
                result=BorderForImage(context, source,0,Color.BLACK,1) ;
                result=ShadowForImage(context, result,5,Color.WHITE,BoxSiz,true,false);
                result=BorderForImage(context, result,0, color,3);
                if (ImageViewForShow!=null){
                    ImageViewForShow.setImageBitmap(result);
                }
                return result;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
    public static Bitmap CournerRadiusForImage(Context context,@Nullable ImageView ImageViewForShow,String ImagePath,Integer Radius, @ColorRes int BorderColor,int BorderSize, @ColorRes int ShadowColor,Integer ShadowSize,Boolean CenterShadow,Boolean InversShadow, @ColorRes int SecondBorderColor,int SecondBorderSize,int ResizeImageWidth){
        if (!(ImagePath==null)){
            //Log.e("bj modules", ImagePath);
            File file=new File(ImagePath);

            if (file.exists() & file.isFile()) {
                //Create and load images (immutable, typically)
                Bitmap source = BitmapFactory.decodeFile(ImagePath);
                if (source == null){
                    Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: source is null by BitmapFactory.decodeFile" +  "\n"+ImagePath);
                    return null;
                }
                if (ResizeImageWidth>0 ){
                    try {

                        source = Bitmap.createScaledBitmap(source, ResizeImageWidth, ResizeImageWidth * source.getHeight() / source.getWidth(), true);
                    }catch (Exception e){
                        Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: " + e.getMessage() + "\n"+ImagePath);

                    }

                }else {
                    if (ImageViewForShow!=null){
                        try {

                            source=Bitmap.createScaledBitmap(source,ImageViewForShow.getWidth(),ImageViewForShow.getWidth()*source.getHeight()/source.getWidth(),true);
                        }catch (Exception e){
                            Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: " + e.getMessage() + "\n"+ImagePath);

                        }


                    }
                }
                if (source == null){
                    Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: source is null after load" +  "\n"+ImagePath);
                    return null;
                }
                Bitmap result=null;
                float radiusx =0;
                float radiusy =0;
                if (Radius>50) {
                    // is Circle
                    int dim;
                    RectF rect;
                    int dx=0;
                    int dy=0;
                    if (source.getWidth()<source.getHeight()) {
                        dim = source.getWidth();
                        rect = new RectF(0, 0, dim, dim);
                        dx=0;
                        dy=(source.getWidth()-source.getHeight())/2;;

                    }else {
                            source=Bitmap.createScaledBitmap(source,ResizeImageWidth*source.getWidth()/source.getHeight(),ResizeImageWidth,true);

                        dim=source.getHeight();
                        dx=(source.getHeight()-source.getWidth())/2;
                        dy=0;
                        rect = new RectF(0, 0, dim, dim);

                    }
                    radiusx =(float) (((float) Radius)/100)*dim;
                    radiusy =(float) (((float) Radius)/100)*dim;
                    //dim=(source.getWidth()+source.getHeight())/2;
                    result = Bitmap.createBitmap(dim,dim, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);







                    paint.setColor(Color.WHITE);

                    canvas.drawRoundRect(rect, radiusx, radiusy, paint);

                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                    canvas.drawBitmap(source, dx, dy, paint);
                    paint.setXfermode(null);

                }else {
                    //Create a *mutable* location, and a canvas to draw into it
                    result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

                    RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());


                    radiusx =(float) (((float) Radius)/100)*source.getWidth();
                    radiusy =(float) (((float) Radius)/100)*source.getHeight();


                    paint.setColor(Color.BLACK);
                    canvas.drawRoundRect(rect, radiusx, radiusy, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                    canvas.drawBitmap(source, 0, 0, paint);
                    paint.setXfermode(null);


                }
                //Log.d("GGN1","Resize: " + source.getWidth()+"/"+ source.getHeight());
                if (BorderSize>0){  result=BorderForImage(context, result,Radius,BorderColor,BorderSize);}

                if(ShadowSize>0){result=ShadowForImage(context, result,Radius,ShadowColor,ShadowSize,CenterShadow,InversShadow);}
                if (SecondBorderSize>0){  result=BorderForImage(context, result,Radius,SecondBorderColor,SecondBorderSize);}

                if (!(ImageViewForShow == null)) {
                    ImageViewForShow.setImageBitmap(result);


                }
                return result;

            }else {
                Log.e(TAG, "CournerRadiusForImage: image exist: "+file.exists()+" is file: "+file.isFile()+System.lineSeparator()+file.getAbsolutePath() );
                return null;
            }
        }else {
            return null;
        }



    }
    public static Bitmap CournerRadiusForImage(Context context, @Nullable ImageView ImageViewForShow, @DrawableRes int ImageDrawableResourceID, Integer Radius, @ColorRes int BorderColor, int BorderSize, @ColorRes int ShadowColor, Integer ShadowSize, Boolean CenterShadow, Boolean InversShadow, @ColorInt  @ColorRes int SecondBorderColor, int SecondBorderSize, int ResizeImageWidth){

        //Create and load images (immutable, typically)
        Bitmap source = BitmapFactory.decodeResource(context.getResources(), ImageDrawableResourceID);

        if (ResizeImageWidth>0 ){

            source=Bitmap.createScaledBitmap(source,ResizeImageWidth,ResizeImageWidth*source.getHeight()/source.getWidth(),true);

        }else {
            if (ImageViewForShow!=null){
                source=Bitmap.createScaledBitmap(source,ImageViewForShow.getWidth(),ImageViewForShow.getWidth()*source.getHeight()/source.getWidth(),true);

            }
        }
        Bitmap result=null;
        float radiusx =0;
        float radiusy =0;
        if (Radius>50) {
            // is Circle
            int dim;
            RectF rect;
            int dx=0;
            int dy=0;
            if (source.getWidth()<source.getHeight()) {
                dim = source.getWidth();
                rect = new RectF(0, 0, dim, dim);
                dx=0;
                dy=(source.getWidth()-source.getHeight())/2;;

            }else {
                source=Bitmap.createScaledBitmap(source,ResizeImageWidth*source.getWidth()/source.getHeight(),ResizeImageWidth,true);

                dim=source.getHeight();
                dx=(source.getHeight()-source.getWidth())/2;
                dy=0;
                rect = new RectF(0, 0, dim, dim);

            }
            radiusx =(float) (((float) Radius)/100)*dim;
            radiusy =(float) (((float) Radius)/100)*dim;
            //dim=(source.getWidth()+source.getHeight())/2;
            result = Bitmap.createBitmap(dim,dim, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);







            paint.setColor(Color.WHITE);

            canvas.drawRoundRect(rect, radiusx, radiusy, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, dx, dy, paint);
            paint.setXfermode(null);

        }else {
            //Create a *mutable* location, and a canvas to draw into it
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());


            radiusx =(float) (((float) Radius)/100)*source.getWidth();
            radiusy =(float) (((float) Radius)/100)*source.getHeight();


            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rect, radiusx, radiusy, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, 0, 0, paint);
            paint.setXfermode(null);


        }
        //Log.d("GGN1","Resize: " + source.getWidth()+"/"+ source.getHeight());
        if (BorderSize>0){        result=BorderForImage(context, result,Radius,BorderColor,BorderSize);}
        if(ShadowSize>0){result=ShadowForImage(context, result,Radius,ShadowColor,ShadowSize,CenterShadow,InversShadow);}
        if (SecondBorderSize>0){  result=BorderForImage(context, result,Radius,SecondBorderColor,SecondBorderSize);}

        if (!(ImageViewForShow == null)) {
            ImageViewForShow.setImageBitmap(result);


        }

        return result;
    }
    public static Bitmap CournerRadiusForImage(@Nullable ImageView ImageViewForShow,String ImagePath,Integer Radius,int ResizeImageWidth){
        if (!(ImagePath==null)){
            //Log.e("bj modules", ImagePath);
            File file=new File(ImagePath);

            if (file.exists() & file.isFile()) {
                //Create and load images (immutable, typically)
                Bitmap source = BitmapFactory.decodeFile(ImagePath);
                if (source == null){
                    Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: source is null by BitmapFactory.decodeFile" +  "\n"+ImagePath);
                    return null;
                }
                if (ResizeImageWidth>0 ){
                    try {

                        source = Bitmap.createScaledBitmap(source, ResizeImageWidth, ResizeImageWidth * source.getHeight() / source.getWidth(), true);
                    }catch (Exception e){
                        Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: " + e.getMessage() + "\n"+ImagePath);

                    }

                }else {
                    if (ImageViewForShow!=null){
                        try {

                            source=Bitmap.createScaledBitmap(source,ImageViewForShow.getWidth(),ImageViewForShow.getWidth()*source.getHeight()/source.getWidth(),true);
                        }catch (Exception e){
                            Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: " + e.getMessage() + "\n"+ImagePath);

                        }


                    }
                }
                if (source == null){
                    Log.e("GGN-CournerRadiusForImage","Error CournerRadiusForImage: source is null after load" +  "\n"+ImagePath);
                    return null;
                }
                Bitmap result=null;
                float radiusx =0;
                float radiusy =0;
                if (Radius>50) {
                    // is Circle
                    int dim;
                    RectF rect;
                    int dx=0;
                    int dy=0;
                    if (source.getWidth()<source.getHeight()) {
                        dim = source.getWidth();
                        rect = new RectF(0, 0, dim, dim);
                        dx=0;
                        dy=(source.getWidth()-source.getHeight())/2;;

                    }else {
                        source=Bitmap.createScaledBitmap(source,ResizeImageWidth*source.getWidth()/source.getHeight(),ResizeImageWidth,true);

                        dim=source.getHeight();
                        dx=(source.getHeight()-source.getWidth())/2;
                        dy=0;
                        rect = new RectF(0, 0, dim, dim);

                    }
                    radiusx =(float) (((float) Radius)/100)*dim;
                    radiusy =(float) (((float) Radius)/100)*dim;
                    //dim=(source.getWidth()+source.getHeight())/2;
                    result = Bitmap.createBitmap(dim,dim, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);







                    paint.setColor(Color.WHITE);

                    canvas.drawRoundRect(rect, radiusx, radiusy, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                    canvas.drawBitmap(source, dx, dy, paint);
                    paint.setXfermode(null);

                }else {
                    //Create a *mutable* location, and a canvas to draw into it
                    result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

                    RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());


                    radiusx =(float) (((float) Radius)/100)*source.getWidth();
                    radiusy =(float) (((float) Radius)/100)*source.getHeight();


                    paint.setColor(Color.BLACK);
                    canvas.drawRoundRect(rect, radiusx, radiusy, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                    canvas.drawBitmap(source, 0, 0, paint);
                    paint.setXfermode(null);


                }
                //Log.d("GGN1","Resize: " + source.getWidth()+"/"+ source.getHeight());

                if (!(ImageViewForShow == null)) {
                    ImageViewForShow.setImageBitmap(result);


                }
                return result;

            }else {
                return null;
            }
        }else {
            return null;
        }



    }
    public static Bitmap CournerRadiusForImage(Context context,@Nullable ImageView ImageViewForShow,@DrawableRes int ImageDrawableResourceID,Integer Radius,int ResizeImageWidth){

        //Create and load images (immutable, typically)
        Bitmap source = BitmapFactory.decodeResource(context.getResources(), ImageDrawableResourceID);

        if (ResizeImageWidth>0 ){

            source=Bitmap.createScaledBitmap(source,ResizeImageWidth,ResizeImageWidth*source.getHeight()/source.getWidth(),true);

        }else {
            if (ImageViewForShow!=null){
                source=Bitmap.createScaledBitmap(source,ImageViewForShow.getWidth(),ImageViewForShow.getWidth()*source.getHeight()/source.getWidth(),true);

            }
        }
        Bitmap result=null;
        float radiusx =0;
        float radiusy =0;
        if (Radius>50) {
            // is Circle
            int dim;
            RectF rect;
            int dx=0;
            int dy=0;
            if (source.getWidth()<source.getHeight()) {
                dim = source.getWidth();
                rect = new RectF(0, 0, dim, dim);
                dx=0;
                dy=(source.getWidth()-source.getHeight())/2;;

            }else {
                source=Bitmap.createScaledBitmap(source,ResizeImageWidth*source.getWidth()/source.getHeight(),ResizeImageWidth,true);

                dim=source.getHeight();
                dx=(source.getHeight()-source.getWidth())/2;
                dy=0;
                rect = new RectF(0, 0, dim, dim);

            }
            radiusx =(float) (((float) Radius)/100)*dim;
            radiusy =(float) (((float) Radius)/100)*dim;
            //dim=(source.getWidth()+source.getHeight())/2;
            result = Bitmap.createBitmap(dim,dim, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);







            paint.setColor(Color.WHITE);

            canvas.drawRoundRect(rect, radiusx, radiusy, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, dx, dy, paint);
            paint.setXfermode(null);

        }else {
            //Create a *mutable* location, and a canvas to draw into it
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());


            radiusx =(float) (((float) Radius)/100)*source.getWidth();
            radiusy =(float) (((float) Radius)/100)*source.getHeight();


            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rect, radiusx, radiusy, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, 0, 0, paint);
            paint.setXfermode(null);


        }
        //Log.d("GGN1","Resize: " + source.getWidth()+"/"+ source.getHeight());

        if (!(ImageViewForShow == null)) {
            ImageViewForShow.setImageBitmap(result);


        }

        return result;
    }


    public static Bitmap BorderForImage(Context context,Bitmap source, Integer Radius, @ColorRes int color, int Size){
        byte errn=0;
        try{
            Bitmap result = Bitmap.createBitmap(source.getWidth()+(2*Size), source.getHeight()+(2*Size), source.getConfig());
            errn=1;
            Canvas canvas = new Canvas(result);
            //Draw Border
            float radiusx =0;
            float radiusy =0;
            int dim;
            errn=3;
            if (Radius>50) {
                errn=4;
                if (source.getWidth()<source.getHeight()) {
                    errn=5;
                    dim = source.getWidth();
                    errn=6;
                }else {
                    errn=7;
                    dim=source.getHeight();
                    errn=8;
                }
                errn=9;
                radiusx =(float) (((float) Radius)/100)*dim;
                errn=10;
                radiusy =(float) (((float) Radius)/100)*dim;
                errn=11;
            }else {
                errn=12;
                radiusx =(float) (((float) Radius)/100)*source.getWidth();
                errn=13;
                radiusy =(float) (((float) Radius)/100)*source.getHeight();
                errn=14;

            }
            errn=15;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            errn=16;
            try {
                paint.setColor(context.getResources().getColor(color));
            }catch (Exception e){
                paint.setColor(color);
            }

            errn=17;
            RectF rect = new RectF(0, 0, result.getWidth(), result.getHeight());
            errn=18;

            canvas.drawRoundRect(rect, radiusx, radiusy, paint);
            errn=19;
            //canvas.drawBitmap(firstImage, 0f, 0f, null);
            canvas.drawBitmap(source, Size, Size, null);
            errn=20;
            //Log.e("bj modules","new Border Proces");
            return result;
        }catch (Exception e){
            Log.e("bj modules","Error num " + errn + ": "+ e.getMessage());
            return source;
        }


    }
    public static Bitmap ShadowForImage(Context context,Bitmap source, Integer Radius, @ColorRes int color,int Size,Boolean Center,Boolean InversShadow){
        Bitmap result ;
        if (Center) {
            result = Bitmap.createBitmap(source.getWidth() + (2 * Size), source.getHeight() + (2 * Size), source.getConfig());
        }else {
            result = Bitmap.createBitmap(source.getWidth()+Size, source.getHeight()+Size, source.getConfig());
        }
        Canvas canvas = new Canvas(result);
        //Draw Border
        float radiusx =0;
        float radiusy =0;
        int dim;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        try {
            paint.setColor(context.getResources().getColor(color));
        }catch (Exception e){
            paint.setColor(color);
        }
        RectF rect;
        if (Radius>50) {
            if (source.getWidth()<source.getHeight()) {
                dim = source.getWidth();

            }else {
                dim=source.getHeight();

            }

            radiusx =(float) (((float) Radius)/100)*dim;
            radiusy =(float) (((float) Radius)/100)*dim;
        }else {
            radiusx =(float) (((float) Radius)/100)*source.getWidth();
            radiusy =(float) (((float) Radius)/100)*source.getHeight();

        }

        if (Center) {
            rect = new RectF(0, 0, result.getWidth(), result.getHeight());
            canvas.drawRoundRect(rect, radiusx, radiusy, paint);
            canvas.drawBitmap(source, Size, Size, null);
        }else {
            if (InversShadow) {
                rect = new RectF(Size, Size, result.getWidth(), result.getHeight());
                canvas.drawRoundRect(rect, radiusx, radiusy, paint);
                canvas.drawBitmap(source, 0,0, null);
            }else {
                rect = new RectF(0, Size, source.getWidth(), result.getHeight());
                canvas.drawRoundRect(rect, radiusx, radiusy, paint);
                canvas.drawBitmap(source, Size, 0, null);
            }
        }



        //canvas.drawBitmap(firstImage, 0f, 0f, null);

        //Log.e("bj modules","new Shadow Proces");
        return result;

    }
    public static Bitmap createSingleBitmapFromMultipleBitmap(Bitmap firstImage, Bitmap secondImage){

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 10, 10, null);
        return result;
    }
    public static int getpixels(Context context,int dp){

        //Resources r = boardContext.getResources();
        //float px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpis, r.getDisplayMetrics());

        final float scale =context. getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);



        return px;

    }

}
