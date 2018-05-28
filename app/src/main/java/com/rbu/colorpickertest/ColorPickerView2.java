package com.rbu.colorpickertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @创建者 huangyan
 * @创建时间 2018/5/28 9:47
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ColorPickerView2 extends View {
    private String TAG="colorPicker";
    private boolean DBG=false;
    private Context mContext;
    private Paint mRightPaint;            //画笔
    private int mHeight;                  //view高
    private int mWidth;                   //view宽
    private int[] mRightColors;
    private int LEFT_WIDTH;
    private Bitmap mLeftBitmap;
    private Bitmap mLeftBitmap2;
    private Paint mBitmapPaint;
    private PointF mLeftSelectPoint;
    private OnColorChangedListenerD mChangedListenerD;
    private boolean mLeftMove = false;
    private float mLeftBitmapRadius;
    private Bitmap mGradualChangeBitmap;
    private Bitmap bitmapTemp;
    private int mCallBackColor = Integer.MAX_VALUE;
    int newWidgth;
    int newHeigh;
    public static String hexColor="FFFFFF";
    public static int ColorText=0;

    private  Canvas mCan=null;

    public ColorPickerView2(Context context) {
        this(context, null);
    }

    public ColorPickerView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }
    public void setOnColorChangedListennerD(OnColorChangedListenerD listener) {
        mChangedListenerD = listener;
    }


    private void init() {

        bitmapTemp = BitmapFactory.decodeResource(getResources(), R.mipmap.piccolor);

        mRightPaint = new Paint();
        mRightPaint.setStyle(Paint.Style.FILL);
        mRightPaint.setStrokeWidth(1);

        mRightColors = new int[3];
        mRightColors[0] = Color.WHITE;
        mRightColors[2] = Color.BLACK;

        mBitmapPaint = new Paint();

        mLeftBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.reading__color_view__button);
        mLeftBitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.reading__color_view__button_press);
        mLeftBitmapRadius = mLeftBitmap.getWidth() / 2;
        mLeftSelectPoint = new PointF(0, 0);
        newWidgth=BitmapFactory.decodeResource(getResources(), R.mipmap.piccolor).getWidth();
        newHeigh=BitmapFactory.decodeResource(getResources(), R.mipmap.piccolor).getHeight();

    }

    //important patient please!!!
    @Override
    protected void onDraw(Canvas canvas) {
        //mCan=canvas;
        canvas.drawBitmap(getGradual() , null , new
                Rect(0, 0, LEFT_WIDTH , mHeight ), mBitmapPaint);
        if(!hexColor.equals("ffffff"))
        {

            System.out.println(TAG+"draw2");
            if (mLeftMove) {

                canvas.drawBitmap(mLeftBitmap, mLeftSelectPoint.x - mLeftBitmapRadius,
                        mLeftSelectPoint.y - mLeftBitmapRadius, mBitmapPaint);

            } else {
                try {

                    canvas.drawBitmap(mLeftBitmap2, mLeftSelectPoint.x - mLeftBitmapRadius,
                            mLeftSelectPoint.y - mLeftBitmapRadius, mBitmapPaint);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }



        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        } else {
            mWidth = newHeigh;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        } else {
            mHeight = newHeigh;
        }
        LEFT_WIDTH = mWidth;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ColorText=getLeftColor(x, y);
                //  System.out.println("color num="+getLeftColor(x, y));
                if(getLeftColor(x, y)!=-1)
                    invalidate();
            case MotionEvent.ACTION_MOVE:

            {
                try {
                    //                  mLeftMove = true;
                    if(getLeftColor(x, y)!=-1)
                    {
                        ColorText=getLeftColor(x, y);
                        proofLeft(x, y);

                        int rmove = Color.red(ColorText);
                        int gmove = Color.green(ColorText);
                        int bmove = Color.blue(ColorText);
                        //System.out.println("color rgb");
                        String r11=Integer.toHexString(rmove);
                        String g11=Integer.toHexString(gmove);
                        String b11=Integer.toHexString(bmove);
                        String colorStr1=r11+g11+b11;    //十六进制的颜色字符串。
                        //System.out.println("color="+colorStr1);
                        hexColor=colorStr1;
                        mChangedListenerD.onColorChanged(ColorText, colorStr1);
                        //changeBGLIS.onColorChanged(ColorText);
                        invalidate();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    //invalidate();
                }


            }
            break;
            case MotionEvent.ACTION_UP:
                try {
                    if(getLeftColor(x, y)!=-1)
                    {
                        ColorText=getLeftColor(x, y);
                        //System.out.println("color="+ColorText);
                        mLeftMove = false;
                        int rup = Color.red(ColorText);
                        int gup= Color.green(ColorText);
                        int bup = Color.blue(ColorText);
                        //System.out.println("color rgb");
                        String rupStr=Integer.toHexString(rup);
                        String gupStr=Integer.toHexString(gup);
                        String bupStr=Integer.toHexString(bup);
                        String colorUpStr=rupStr+gupStr+bupStr;    //十六进制的颜色字符串。
                        System.out.println("color="+colorUpStr);
                        Log.e("color:" ,rup + " " + gup + " " + bup);
                        hexColor=colorUpStr;
                        mChangedListenerD.onColorChanged(ColorText, colorUpStr);
                        invalidate();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    // invalidate();
                }




        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mGradualChangeBitmap != null && mGradualChangeBitmap.isRecycled() == false) {
            mGradualChangeBitmap.recycle();
        }
        if (mLeftBitmap != null && mLeftBitmap.isRecycled() == false) {
            mLeftBitmap.recycle();
        }
        if (mLeftBitmap2 != null && mLeftBitmap2.isRecycled() == false) {
            mLeftBitmap2.recycle();
        }
        super.onDetachedFromWindow();
    }

    private Bitmap getGradual() {
        if (mGradualChangeBitmap == null) {
            Paint leftPaint = new Paint();
            leftPaint.setStrokeWidth(1);
            mGradualChangeBitmap = Bitmap.createBitmap(LEFT_WIDTH, mHeight, Config.RGB_565);
            mGradualChangeBitmap.eraseColor(Color.WHITE);
            Canvas canvas = new Canvas(mGradualChangeBitmap);
            canvas.drawBitmap( bitmapTemp, null , new Rect(0, 0, LEFT_WIDTH , mHeight ), mBitmapPaint);
        }
        return mGradualChangeBitmap;
    }
    // 校正xy
    private void proofLeft(float x, float y) {
        if (x < 0) {
            mLeftSelectPoint.x = 0;
        } else if (x > (LEFT_WIDTH)) {
            mLeftSelectPoint.x = LEFT_WIDTH;
        } else {
            mLeftSelectPoint.x = x;
        }
        if (y < 0) {
            mLeftSelectPoint.y = 0;
        } else if (y > (mHeight - 0)) {
            mLeftSelectPoint.y = mHeight - 0;
        } else {
            mLeftSelectPoint.y = y;
        }
    }

    private int getLeftColor(float x, float y) {
        Bitmap temp = getGradual();
        // 为了防止越界
        int intX = (int) x;
        int intY = (int) y;
        if(intX<0)intX=0;
        if(intY<0)intY=0;
        if (intX >= temp.getWidth()) {
            intX = temp.getWidth() - 1;
        }
        if (intY >= temp.getHeight()) {
            intY = temp.getHeight() - 1;
        }

        System.out.println("leftColor"+temp.getPixel(intX, intY));
        return temp.getPixel(intX, intY);
    }



    // ### 内部类 ###
    public interface OnColorChangedListenerD {
        void onColorChanged(int color, String hexStrColor);
    }
}

