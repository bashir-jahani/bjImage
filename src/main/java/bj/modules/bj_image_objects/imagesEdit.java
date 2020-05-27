package bj.modules.bj_image_objects;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.net.Uri;


import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;

import android.util.Log;
import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import bj.modules.bj_file;
import bj.modules.bj_image.R;

import static bj.modules.bj_file_path.*;


@SuppressWarnings("ALL")
public class imagesEdit extends AppCompatActivity {
    private String TAG="imagesEdit";
    boolean minimizeImage=false;
    Uri ImageUri;
    String PathForExportImage;
    ImageView FinalIMGV,BTNBack,BTNCrop,BTNFrame,BTNCaption,BTNOK,BTNSettingCancel,BTNFontColor;
    imagesView_TouchImageView MyIMGV;
    RelativeLayout CropRL;
    FrameLayout FRCrope;
    Bitmap MyBitMap;

    TextView MyCaption,CropCaption,SeekBarCaption,SeekBarCaptionSpace;
    EditText MyCaptionEDT;
    LinearLayout LL_SeekBar,LL_Settings,LL_SeekBar_CaptionSpaceSize,LL_CaptionTXB;
    HorizontalScrollView framesView;
    SeekBar seekbar,seekbar_CaptionSpaceSize;
    int SizeOFfFrame=0,SizeOfCaptionSpace=100,SizeOfCaption=16;
    // Array of frame images
    int[] framesListBTN = new int[]{
            R.drawable.frame0_btn, R.drawable.frame1_btn,
            R.drawable.frame2_btn, R.drawable.frame3_btn,
            R.drawable.frame4_btn,R.drawable.frame5_btn,
            R.drawable.frame6_btn,
    };
    int[] framesList = new int[]{
            R.drawable.frame0, R.drawable.frame1,
            R.drawable.frame2, R.drawable.frame3,
            R.drawable.frame4,R.drawable.frame5,
            R.drawable.frame6,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bj_images_edit);
        //Set Objects
        ImageUri= (Uri) this.getIntent().getData();
        if (ImageUri==null){
            if (this.getIntent().getExtras()==null){
                getIntent().setData(null);
                finish();
                return;
            }
            MyBitMap=this.getIntent().getExtras().getParcelable("data");
            if (MyBitMap==null){
                getIntent().setData(null);
                finish();
                return;
            }
        }
        PathForExportImage= Paths_Temp()+File.separator+getRandomName()+".jpg";

        if (this.getIntent().getExtras()!=null) {
            minimizeImage = this.getIntent().getExtras().getBoolean("minimizeImage", false);
            PathForExportImage = getIntent().getExtras().getString("PathForExportImage", PathForExportImage);
        }






        MyIMGV=(imagesView_TouchImageView) findViewById(R.id.GIE_IMG_MyImage);
        FinalIMGV=(ImageView) findViewById(R.id.GIE_IMG_FinalImage);

        BTNOK=(ImageView) findViewById(R.id.GIE_IMG_BTN_OK);
        BTNBack=(ImageView) findViewById(R.id.GIE_IMG_BTN_Back);
        BTNCaption=(ImageView) findViewById(R.id.GIE_IMG_BTN_Caption);
        BTNFontColor=(ImageView) findViewById(R.id.GIE_IMG_Font_Color);
        BTNCrop=(ImageView) findViewById(R.id.GIE_IMG_BTN_Crop);
        BTNFrame=(ImageView) findViewById(R.id.GIE_IMG_BTN_Frame);
        BTNSettingCancel=(ImageView) findViewById(R.id.GIE_IMG_BTN_Setting_Cancel);
        FRCrope=findViewById(R.id.GIE_FL_Crop);
        FRCrope.setVisibility(View.GONE);
        CropRL=(RelativeLayout) findViewById(R.id.GIE_RL_CropView);
        LL_SeekBar=(LinearLayout) findViewById(R.id.GIE_LL_SeekBar);
        LL_CaptionTXB=(LinearLayout) findViewById(R.id.GIE_LL_EDT_Caption);

        LL_SeekBar_CaptionSpaceSize=(LinearLayout) findViewById(R.id.GIE_LL_SeekBar_CaptionSpaceSize);
        LL_Settings=(LinearLayout) findViewById(R.id.GIE_LL_Settings);
        MyCaption=(TextView) findViewById(R.id.GIE_TXV_CaptionTxt);
        CropCaption=(TextView) findViewById(R.id.GIE_TXV_Crop);
        SeekBarCaption=(TextView) findViewById(R.id.GIE_TXV_SeekBar_Caption);
        SeekBarCaptionSpace=(TextView) findViewById(R.id.GIE_TXV_SeekBar_CaptionSpaceSize);
        MyCaptionEDT=(EditText) findViewById(R.id.GIE_EDT_Caption);
        framesView = (HorizontalScrollView) findViewById(R.id.GIE_HS_Frames);

        seekbar=(SeekBar) findViewById(R.id.GIE_SeekBar);
        seekbar_CaptionSpaceSize=(SeekBar) findViewById(R.id.GIE_SeekBarCaptionSpaceSize);

        //Load Image
        if (MyBitMap!=null){
            Glide.with(imagesEdit.this).load(MyBitMap).placeholder(R.drawable.loading).into(MyIMGV);
        }else if(ImageUri!=null){
            Glide.with(imagesEdit.this).load(ImageUri).placeholder(R.drawable.loading).into(MyIMGV);
            //Generate BitMap
            InputStream in = null;
            try {
                in = this.getContentResolver().openInputStream(ImageUri);
            } catch (FileNotFoundException|NullPointerException e) {
                e.printStackTrace();
                Log.e(TAG, "onCreate:  in = this.getContentResolver().openInputStream(ImageUri): "+e.getMessage() );
                Toast.makeText(this, "Error#1: "+e.getMessage() +".\n"+"Retry.", Toast.LENGTH_LONG).show();
                finish();
            }
            MyBitMap = BitmapFactory.decodeStream(in);
            try {
                in.close();
            } catch (IOException|NullPointerException e) {
                Log.e(TAG, "onCreate: in.close(): "+e.getMessage() );
                Toast.makeText(this, "Error#2: Cant Generate Bitmap" +".\n"+"Retry.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            if (MyBitMap==null){
                Toast.makeText(this, "Error#3: Cant Generate Bitmap" +".\n"+"Retry.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }else {

            getIntent().setData(null);
            finish();
            return;
        }




        //Set Buttons Action
        BTNBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntent().setData(null);
                finish();

            }
        });
        BTNFontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker cp=new colorPicker(imagesEdit.this,"Text Color",MyCaption.getCurrentTextColor(),Color.BLACK, new colorPicker.OnColorChangedListener() {
                    @Override
                    public void colorChanged(String key, int color) {
                        MyCaption.setTextColor(color);
                    }
                });
                cp.show();
            }
        });
        BTNOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureImage();
            }
        });
        BTNCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingShowCrop();
            }
        });
        BTNCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SettingShowCaption();

            }
        });

        BTNFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingShowFrame();
            }
        });
        BTNSettingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyCaptionEDT.getVisibility()==View.VISIBLE) {
                    ClearCaption();
                    SettingHide();
                    SetFrame();
                }else {
                    ClearFrame();
                    SettingHide();
                    SetFrame();
                }
            }
        });




        seekbar_CaptionSpaceSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SizeOfCaptionSpace=progress;
                SeekBarCaptionSpace.setText("Space Size: "+progress);
                SetFrame();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SettingHide();
        ResetImage();
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
    private void ResetImage() {
        MyIMGV.SetZoomScale(1);
        ClearFrame();
        ClearCaption();

    }

    private void ClearFrame(){
        FinalIMGV.setBackground(null);

        MyIMGV.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        SizeOFfFrame=0;
    }
    private void ClearCaption(){
        MyCaptionEDT.setText("Caption");
        MyCaption.setText("Caption");
        MyCaption.setVisibility(View.GONE);




    }
    private void SettingHide(){
        LL_Settings.setVisibility(View.GONE);
        CropCaption.setVisibility(View.GONE);
        FRCrope.setVisibility(View.GONE);
        LL_CaptionTXB.setVisibility(View.GONE);
        framesView.setVisibility(View.GONE);
        seekbar.setOnSeekBarChangeListener(null);
        LL_SeekBar_CaptionSpaceSize.setVisibility(View.GONE);
    }
    private void SettingShowCrop(){
        if (CropCaption.getVisibility()==View.VISIBLE) {
            SettingHide();

        }else {
            SettingHide();
            CropCaption.setVisibility(View.VISIBLE);
            FRCrope.setVisibility(View.VISIBLE);

        }


    }
    private void SettingShowCaption(){
        SettingHide();

        LL_Settings.setVisibility(View.VISIBLE);
        LL_SeekBar.setVisibility(View.VISIBLE);
        LL_SeekBar_CaptionSpaceSize.setVisibility(View.VISIBLE);
        seekbar_CaptionSpaceSize.setProgress(SizeOfCaptionSpace);
        LL_CaptionTXB.setVisibility(View.VISIBLE);
        MyCaption.setVisibility(View.VISIBLE);
        seekbar.setMax(50);
        //seekbar.setProgress((int) MyCaption.getTextSize());
        seekbar.setProgress(SizeOfCaption);
        MyCaption.setTextSize(seekbar.getProgress());
        SeekBarCaption.setText("Font  Size: " + seekbar.getProgress());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MyCaption.setTextSize(progress);
                SeekBarCaption.setText("Font  Size: " + progress);
                SizeOfCaption=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        MyCaption.setText(MyCaptionEDT.getText());
        MyCaptionEDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MyCaption.setText(MyCaptionEDT.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SetFrame();


    }
    private void SettingShowFrame(){
        for (int i = 0; i< framesListBTN.length; i++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.framesLayout);

            // Setup the Buttons
            Button btnTag = new Button( imagesEdit.this);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            layoutParams.setMargins(0, 0, 0, 0);
            btnTag.setLayoutParams(layoutParams);
            btnTag.setId(i);

            btnTag.setBackgroundResource(framesListBTN[i]);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinalIMGV.setBackgroundResource(framesList[v.getId()]);
                    if (SizeOFfFrame==0){
                        SizeOFfFrame=20;
                        seekbar.setProgress(20);
                        SeekBarCaption.setText("Font  Size: " + 20);
                        SetFrame();
                    }

                }
            });

            //add button to the layout
            layout.addView(btnTag);
        }
        SettingHide();
        LL_Settings.setVisibility(View.VISIBLE);
        LL_SeekBar.setVisibility(View.VISIBLE);
        seekbar.setMax(100);
        SeekBarCaption.setText("Frame Size: " + SizeOFfFrame);
        seekbar.setProgress(SizeOFfFrame);
        SetFrame();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SizeOFfFrame=progress;
                SeekBarCaption.setText("Frame Size: " + progress);
                SetFrame();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        framesView.setVisibility(View.VISIBLE);

        if (MyIMGV.GetZoomScale()==1){
            MyIMGV.SetWidth();
        }
    }
    private void SetFrame(){
        RelativeLayout.LayoutParams pr= (RelativeLayout.LayoutParams) MyIMGV.getLayoutParams();
        pr.alignWithParent=true;
        pr.width=FinalIMGV.getWidth()-(2*SizeOFfFrame);
        //Toast.makeText(this, MyCaption.getVisibility()+"=="+View.VISIBLE, Toast.LENGTH_SHORT).show();
        if (MyCaption.getVisibility()==View.VISIBLE) {
            if(SizeOfCaptionSpace<SizeOFfFrame) {
                pr.height=FinalIMGV.getHeight()-(SizeOFfFrame+SizeOFfFrame);
            }else {
                pr.height=FinalIMGV.getHeight()-(SizeOFfFrame+SizeOfCaptionSpace);
            }

        }else {
            pr.height=FinalIMGV.getHeight()-(2*SizeOFfFrame);
        }

        pr.topMargin=SizeOFfFrame;
        pr.leftMargin=SizeOFfFrame;

        MyIMGV.setLayoutParams(pr);

    }
    public void CaptureImage(){
        SettingHide();
        if (ImageUri==null){
            if (MyCaption.getVisibility()!=View.VISIBLE && SizeOFfFrame==0 && MyIMGV.GetZoomScale()==1 && !minimizeImage ){
                takeScreenshotOfCropView(MyBitMap);
            }else {
                takeScreenshotOfCropView(null);
            }
        }else if (MyCaption.getVisibility()!=View.VISIBLE && SizeOFfFrame==0 && MyIMGV.GetZoomScale()==1 && !minimizeImage ) {
            //Orginal Image
            Toast.makeText(this, "Orginal Image", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setData(ImageUri);

            setResult(RESULT_OK,intent);
        }else {
            //Procesed Image
            //Toast.makeText(this, "Procesed Image", Toast.LENGTH_SHORT).show();
            takeScreenshotOfCropView(null);

        }

        finish();
    }
    public void takeScreenshotOfCropView(Bitmap bitmap) {

        Bitmap bmp ;
        if (bitmap==null){
            View v = CropRL;
            v.setDrawingCacheEnabled(true);
            try{
                bmp = Bitmap.createBitmap(v.getDrawingCache());
                v.setDrawingCacheEnabled(false);
                //Log.i(TAG, "takeScreenshotOfCropView: bmp size #1 "+bmp.getByteCount());
                if (bmp.getByteCount()>MyBitMap.getByteCount() && MyCaption.getVisibility()!=View.VISIBLE && SizeOFfFrame==0 && MyIMGV.GetZoomScale()==1){
                    bmp=MyBitMap;
                }
            }catch (OutOfMemoryError e){
                bmp=MyBitMap;
            }

            //Log.i(TAG, "takeScreenshotOfCropView: bmp size #2 "+bmp.getByteCount());
        }else {
            bmp=bitmap;
            //Log.i(TAG, "takeScreenshotOfCropView: bmp size #3 "+bmp.getByteCount());
        }

        Log.i(TAG, "takeScreenshotOfCropView: bmp size "+bmp.getByteCount());
        File exportFile = new File(PathForExportImage);
        if (exportFile.exists()){
            exportFile.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(exportFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Call Load image
            Uri uri;
            uri= bj_file.uriUtil.uriFromFile(getBaseContext(),exportFile);

            Log.i(TAG, "takeScreenshotOfCropView: "+exportFile.getAbsolutePath() + " exist: "+exportFile.exists()+ " size: "+exportFile.length());
            Intent intent = new Intent();
            intent.setData(uri);
            setResult(RESULT_OK,intent);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "takeScreenshotOfCropView#1: "+e.getMessage() );
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "takeScreenshotOfCropView#2: "+e.getMessage() );
            e.printStackTrace();
        }
    }

}
