package bj.modules.bj_image_objects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;


import com.bumptech.glide.Glide;


import bj.modules.bj_image.R;
import bj.modules.bj_image.bj_image;

import static bj.modules.bj_file.GetFileExtension;


/**
 * Created by Bashir jahani on 1/25/2018.
 */

public class imagesView_TouchImageView extends ImageView {
    Matrix matrix;
    Context mContext;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = .25f;
    float maxScale = 3f;
    float[] m;



    int viewWidth, viewHeight;
    static final int CLICK = 3;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

    ScaleGestureDetector mScaleDetector;

    Context context;

    public imagesView_TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }
    public void SetImage(String ImagePath){
        if (GetFileExtension(ImagePath).toLowerCase().equals("gif")) {
            Glide.with(getContext()).load(ImagePath).placeholder(R.drawable.loading).into(imagesView_TouchImageView.this);
        }else {
            Bitmap src=null;
            src= BitmapFactory.decodeFile(ImagePath);
            setImageBitmap(src);
        }
    }
    public void SetImage(@DrawableRes int resourceID){
        Glide.with(getContext()).load(context.getResources().getDrawable(resourceID)).placeholder(R.drawable.loading).into(imagesView_TouchImageView.this);

    }
    public void SetImageAsCircle(@DrawableRes int resourceID, Integer CournerRadius, @ColorRes int BorderColor, int BorderSize, @ColorRes int ShadowColor, Integer ShadowSize, Boolean CenterShadow, Boolean InversShadow, @ColorRes int SecondBorderColor, int SecondBorderSiz, int ResizeImageWidth, @DrawableRes int  errorImageDrawableResource, @DrawableRes int  placeholderImageDrawableResource){
        bj_image bj_image1=new bj_image();
        bj_image1.Load_PictureTo_ImageView(resourceID,imagesView_TouchImageView.this,CournerRadius,BorderColor,BorderSize,ShadowColor,ShadowSize,CenterShadow,InversShadow,SecondBorderColor,SecondBorderSiz,ResizeImageWidth,errorImageDrawableResource,placeholderImageDrawableResource);
    }
    public void SetImageAsCircle(String ImagePath, Integer CournerRadius, @ColorRes int BorderColor, int BorderSize, @ColorRes int ShadowColor, Integer ShadowSize, Boolean CenterShadow, Boolean InversShadow, @ColorRes int SecondBorderColor, int SecondBorderSiz, int ResizeImageWidth, @DrawableRes int  errorImageDrawableResource, @DrawableRes int  placeholderImageDrawableResource){
        bj_image bj_image1=new bj_image();
        bj_image1.Load_PictureTo_ImageView(ImagePath,imagesView_TouchImageView.this,CournerRadius,BorderColor,BorderSize,ShadowColor,ShadowSize,CenterShadow,InversShadow,SecondBorderColor,SecondBorderSiz,ResizeImageWidth,errorImageDrawableResource,placeholderImageDrawableResource);
    }
    public imagesView_TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
        TypedArray a=getContext().obtainStyledAttributes(attrs, R.styleable.imagesView_TouchImageView);;
        minScale=a.getFloat(R.styleable.imagesView_TouchImageView_minScale,1);
        maxScale=a.getFloat(R.styleable.imagesView_TouchImageView_maxScale,3);
    }
    public void SetminScale(float MinScale){
        minScale=MinScale;
    }
    public void SetmaxScale(float MaxScale){
        maxScale=MaxScale;
    }
    public float GetminScale(){
        return minScale;
    }
    public float GetmaxScale(){
        return maxScale;
    }

    private void sharedConstructing(final Context context) {
        super.setClickable(true);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setOnClickListener(new OnClickListener() {
            private long firstClick;
            private long lastClick;
            private int count; // to count click times
            @Override
            public void onClick(View v) {

                if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                    count = 0;
                }
                count++;
                if (count == 1) {
                    firstClick = System.currentTimeMillis();
                    //Toast.makeText(context, getRootView().getId()+"/"+ getRootView().toString(), Toast.LENGTH_SHORT).show();
                } else if (count == 2) {
                    lastClick = System.currentTimeMillis();
                    // if these two clicks is closer than 300 millis second
                    if (lastClick - firstClick < 300) {

                        if (saveScale!=1 ) {
                           // Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
                            matrix.postScale(1/saveScale, 1/saveScale, viewWidth/2 ,
                                    viewHeight/2 );
                            saveScale=1;
                            fixTrans();
                        }else {
                           //1 Toast.makeText(context, "2222", Toast.LENGTH_SHORT).show();
                            matrix.postScale(maxScale/saveScale, maxScale/saveScale, viewWidth/2 ,
                                    viewHeight/2 );
                            saveScale=maxScale;
                            fixTrans();

                        }
                    }
                }

            }
        });


        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mScaleDetector.onTouchEvent(event);
                PointF curr = new PointF(event.getX(), event.getY());





                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Toast.makeText(context, "ACTION_DOWN", Toast.LENGTH_SHORT).show();


                        last.set(curr);
                        start.set(last);
                        mode = DRAG;

                        break;

                    case MotionEvent.ACTION_MOVE:
                        //Toast.makeText(context, "ACTION_MOVE", Toast.LENGTH_SHORT).show();
                        if (mode == DRAG ) {

                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth,
                                    origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight,
                                    origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);



                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        //Toast.makeText(context, "ACTION_UP", Toast.LENGTH_SHORT).show();

                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                performContextClick();
                            }
                            performClick();

                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        //Toast.makeText(context, "ACTION_POINTER_UP", Toast.LENGTH_SHORT).show();
                        mode = NONE;

                        break;

                }

                setImageMatrix(matrix);
                invalidate();
                return true; // indicate event was handled
            }

        });

    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
                //Log.d("GGGG", "1");
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
                //Log.d("GGGG", "2");
            }

            if (origWidth * saveScale <= viewWidth
                    || origHeight * saveScale <= viewHeight) {
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
                        viewHeight / 2);
                //Log.d("GGGG","3");

            }else {

                matrix.postScale(mScaleFactor, mScaleFactor,
                        detector.getFocusX(), detector.getFocusY());
            }
            fixTrans();
            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight
                * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            // Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0
                    || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight
                    - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth
                    - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }
    public float GetZoomScale(){
        return saveScale;
    }
    public void SetWidth(){
        float MyZoom=1;
        float scale;

        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0
                || drawable.getIntrinsicHeight() == 0)
            return;
        int bmWidth = drawable.getIntrinsicWidth();
        int bmHeight = drawable.getIntrinsicHeight();

        Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

        float scaleX = (float) viewWidth / (float) bmWidth;
        float scaleY = (float) viewHeight / (float) bmHeight;

        if (scaleX>scaleY) {

            scale = Math.max(scaleX, scaleY);
            scale=scale*MyZoom;
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight
                    - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth
                    - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            redundantYSpace =0;
            redundantXSpace =0;
            matrix.postTranslate(redundantXSpace, redundantYSpace);


            setImageMatrix(matrix);


            //Toast.makeText(context,saveScale+ "", Toast.LENGTH_SHORT).show();
            fixTrans();
            saveScale = scaleX / scaleY;
        }else {

            SetZoomScale(1);
        }

    }
    public void SetZoomScale(float ZoomScale){


        float MyZoom=ZoomScale;
        if (ZoomScale>GetmaxScale()){
            MyZoom=GetmaxScale();
        }

        float scale;

        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0
                || drawable.getIntrinsicHeight() == 0)
            return;
        int bmWidth = drawable.getIntrinsicWidth();
        int bmHeight = drawable.getIntrinsicHeight();

        Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

        float scaleX = (float) viewWidth / (float) bmWidth;
        float scaleY = (float) viewHeight / (float) bmHeight;
        scale = Math.min(scaleX, scaleY);
        scale=scale*MyZoom;
        matrix.setScale(scale, scale);

        // Center the image
        float redundantYSpace = (float) viewHeight
                - (scale * (float) bmHeight);
        float redundantXSpace = (float) viewWidth
                - (scale * (float) bmWidth);
        redundantYSpace /= (float) 2;
        redundantXSpace /= (float) 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);


        setImageMatrix(matrix);
        saveScale=MyZoom;
    fixTrans();
    }
}
