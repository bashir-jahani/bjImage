package bj.modules.bj_image_objects;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Bashir jahani on 01/07/2018.
 */

public class colorPicker extends Dialog {
    public interface OnColorChangedListener {
        void colorChanged(String key, int color);
    }
    private OnColorChangedListener mListener;
    private int mInitialColor, mDefaultColor;

    private String mKey;

    private static class ColorPickerView extends View {
        private Paint mPaint;
        private float mCurrentHue = 0;
        private float mCurrentSatuaratin = 0;
        private float mCurrentValue = 0;
        private int mCurrentX = 0, mCurrentY = 0;
        private int mCurrentColor, mDefaultColor;
        private final int[] mHueBarColors = new int[360];
        private int[] mMainColors = new int[65536];
        private OnColorChangedListener mListener;
        public final int MyWidth,MyHeigth,MyLineWidth;
        private final Point P1F,P1L,P2F,P2L,P3F,P3L,P4F,P4L;
        private  final  int Pers;

        ColorPickerView(Context c, OnColorChangedListener l, int color,int defaultColor,int DWidth,int DHeigth) {
            super(c);
            mListener = l;
            mDefaultColor = defaultColor;

            // Get the current hue from the current color and update the main
            // color field
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            mCurrentHue = hsv[0];
            mCurrentSatuaratin=hsv[1];
            mCurrentValue=hsv[2];
            updateMainColors();
            MyHeigth=DHeigth;
            MyWidth=DWidth;
            MyLineWidth=(int) (MyWidth-20)/360;

            Pers=(MyHeigth-60)/10;

            P1F=new Point(10,10);
            P1L=new Point(MyWidth-20,Pers);

            P2F=new Point(10,P1L.y+30);
            P2L=new Point(MyWidth-20,P2F.y+Pers*8);

            P3F=new Point(10,P2L.y+20);
            P3L=new Point(MyWidth/2,P3F.y+Pers);

            P4F=new Point(P3L.x,P3F.y);
            P4L=new Point(MyWidth-10,P3L.y);


            mCurrentColor = color;

            // Initialize the colors of the hue slider bar

            int index = 0;
            float[] HList_hsv = new float[3];
            HList_hsv[1]=1;
            HList_hsv[2]=1;
            for (index=0;index<360;index++){

                HList_hsv[0]=index;

                mHueBarColors[index]=Color.HSVToColor(HList_hsv);
            }


            // Initializes the Paint that will draw the View
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(20);

        }

        // Get the current selected color from the hue bar
        private int getCurrentMainColor() {
            float[] HList_hsv = new float[3];
            HList_hsv[1]=mCurrentSatuaratin;
            HList_hsv[2]=mCurrentValue;
            HList_hsv[0]=mCurrentHue;
            mCurrentColor=Color.HSVToColor(HList_hsv);
            return Color.HSVToColor(HList_hsv);
        }

        // Update the main field colors depending on the current selected hue
        private void updateMainColors() {
            int mainColor = getCurrentMainColor();
            int index = 0;
            int[] topColors = new int[256];
            for (int y = 0; y < 256; y++) {
                for (int x = 0; x < 256; x++) {
                    if (y == 0) {
                        mMainColors[index] = Color.rgb(
                                255 - (255 - Color.red(mainColor)) * x / 255,
                                255 - (255 - Color.green(mainColor)) * x / 255,
                                255 - (255 - Color.blue(mainColor)) * x / 255);
                        topColors[x] = mMainColors[index];
                    } else
                        mMainColors[index] = Color.rgb(
                                (255 - y) * Color.red(topColors[x]) / 255,
                                (255 - y) * Color.green(topColors[x]) / 255,
                                (255 - y) * Color.blue(topColors[x]) / 255);
                    index++;
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {


            // Display all the colors of the hue bar with lines
            for (int x = 0; x < 360; x++) {
                // If this is not the current selected hue, display the actual
                // color

                if ((int)mCurrentHue != x) {
                    mPaint.setColor(mHueBarColors[x]);
                    mPaint.setStrokeWidth(MyLineWidth);
                } else // else display a slightly larger black line
                {
                    mPaint.setColor(Color.BLACK);
                    mPaint.setStrokeWidth(MyLineWidth);
                }
                canvas.drawLine(x + P1F.x +((MyLineWidth-1)*x), P1F.y, x + P1F.x+((MyLineWidth-1)*x), P1L.y, mPaint);
                // canvas.drawLine(0, x+10, 40, x+10, mPaint);
            }

            // Display the main field colors using LinearGradient
            for (int x = 0; x < 360; x++) {
                int[] colors = new int[2];
                float[] HList_hsv = new float[3];
                HList_hsv[1]= (float) x/360;
                HList_hsv[2]= (float) 1;

                HList_hsv[0]=mCurrentHue;
                colors[0] = Color.HSVToColor(HList_hsv);
                colors[1] = Color.BLACK;

                Shader shader = new LinearGradient(0, 0, 0,P2L.y-P2F.y, colors,null, Shader.TileMode.CLAMP /*or REPEAT*/);

                mPaint.setShader(shader);
                //mPaint.setColor(colors[0]);
                mPaint.setStrokeWidth(MyLineWidth);
                canvas.drawLine(x + P2F.x+((MyLineWidth-1)*x), P2F.y, x + P2F.x+((MyLineWidth-1)*x), P2L.y, mPaint);
            }
            mPaint.setShader(null);

            mCurrentX= (int) (((float)mCurrentSatuaratin*(P2L.x-P2F.x))+P2F.x);
            mCurrentY= (int) (((float)(1-mCurrentValue)*(P2L.y-P2F.y))+P2F.y);

            // Display the circle around the currently selected color in the
            // main field
            if (mCurrentX != 0 && mCurrentY != 0) {
                mPaint.setStyle(Paint.Style.STROKE);
                if (Color.red(mCurrentColor) + Color.green(mCurrentColor)
                        + Color.blue(mCurrentColor) < 384)
                    mPaint.setColor(Color.WHITE);
                else
                    mPaint.setColor(Color.BLACK);
                canvas.drawCircle(mCurrentX, mCurrentY, 10, mPaint);
            }

            // Draw a 'button' with the currently selected color
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(getCurrentMainColor());
            canvas.drawRect(P3F.x, P3F.y, P3L.x, P3L.y, mPaint);

            // Set the text color according to the brightness of the color
            if (Color.red(mCurrentColor) + Color.green(mCurrentColor)
                    + Color.blue(mCurrentColor) < 384)
                mPaint.setColor(Color.WHITE);
            else
                mPaint.setColor(Color.BLACK);
            canvas.drawText(
                    "Selected Color", P3F.x+100,
                    P3F.y+Pers/2, mPaint);

            // Draw a 'button' with the default color
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mDefaultColor);
            canvas.drawRect(P4F.x, P4F.y, P4L.x, P4L.y, mPaint);

            // Set the text color according to the brightness of the color
            if (Color.red(mDefaultColor) + Color.green(mDefaultColor)
                    + Color.blue(mDefaultColor) < 384)
                mPaint.setColor(Color.WHITE);
            else
                mPaint.setColor(Color.BLACK);
            canvas.drawText(
                    "First Color", P4F.x+70, P4F.y+Pers/2,
                    mPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(MyWidth, MyHeigth);

        }



        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return true;
            float x = event.getX();
            float y = event.getY();

            // If the touch event is located in the hue bar
            if (x > P1F.x && x < P1L.x && y > P1F.y && y < P1L.y) {
                // Update the main field colors
                mCurrentHue = (x-P1F.x)*((float)360/(MyWidth-20));


                // Force the redraw of the dialog
                invalidate();
            }

            // If the touch event is located in the main field
            if (x > P2F.x && x < P2L.x && y > P2F.y && y < P2L.y) {
                mCurrentX = (int) (x);
                mCurrentY = (int) (y);
                mCurrentSatuaratin = (float) ((x-P2F.x)/(P2L.x-P2F.x)) ;
                mCurrentValue = (float) (1-((y-P2F.y)/(P2L.y-P2F.y))) ;
                //Log.e("asas",mCurrentSatuaratin + "/" + mCurrentValue);
                invalidate();
            }

            // If the touch event is located in the left button, notify the
            // listener with the current color
            if (x > P3F.x && x < P3L.x && y > P3F.y && y < P3L.y)
                mListener.colorChanged("", mCurrentColor);

            // If the touch event is located in the right button, notify the
            // listener with the default color
            if (x > P4F.x && x < P4L.x && y > P4F.y && y < P4L.y)
                mListener.colorChanged("", mDefaultColor);

            return true;
        }
    }

    public colorPicker(Context context,
                         String key, int initialColor, int defaultColor, OnColorChangedListener listener) {
        super(context);

        mListener = listener;
        mKey = key;
        mInitialColor = initialColor;
        mDefaultColor = defaultColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(String key, int color) {
                mListener.colorChanged(mKey, color);
                dismiss();
            }
        };
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        width=(width-20)/360;
        width=width*360;


        View v=new ColorPickerView(getContext(), l, mInitialColor,
        mDefaultColor,(int) (width+20),(int) (height*0.6));

        setContentView(v);
        setTitle("Select Your Favorite Color");


    }
}
