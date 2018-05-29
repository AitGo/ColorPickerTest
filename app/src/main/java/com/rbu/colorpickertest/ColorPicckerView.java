package com.rbu.colorpickertest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.icu.util.UniversalTimeScale;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

/**
 * @创建者 liu
 * @创建时间 2018/5/25 10:20
 * @描述 ${采色器view}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ColorPicckerView extends View {

    private Paint mPaint;//渐变色环画笔
    private Paint mClickPaint;//触控点画笔
    private int clickX = 0;
    private int clickY = 0;
    public OnColorChangedListener mListener;
    private ImageView smallCircle;//中间的小圆，手指触控滑动
    private final int[] mCircleColors = new int[] {0xFFFF0000,0xFFFF00FF,
        0xFF0000FF,0xFF00FFFF,0xFF00FF00,0xFFFFFF00,0xFFFF0000}; //颜色数组，用这个值来画圆

    private int mHeight;//view高
    private int mWidth;//view宽
    private float r;//色环半径
    private boolean downInCircle = true;//按在渐变环上
    private Context mContext;


    public ColorPicckerView(Context context) {
        super(context);
        init(context);
    }

    public ColorPicckerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorPicckerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mHeight = 500;
        mWidth = 500;
        setFocusable(true);
        setFocusableInTouchMode(true);
        //准备色圆的画笔
        Shader s = new SweepGradient(0,0,mCircleColors,null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.FILL);//设置实心

        r = mWidth / 2 * 0.7f;
        //控制点的画笔
        mClickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClickPaint.setStrokeWidth(1.5f);
        mClickPaint.setStyle(Paint.Style.STROKE);
        mClickPaint.setColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2,mHeight / 2);
        canvas.drawOval(new RectF(-r,-r,r,r),mPaint);
        canvas.drawCircle(clickX,clickY,16,mClickPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - mWidth/2;
        float y = event.getY() - mHeight/2 + 50;
        boolean inCircle = inColorCircle(x,y,r);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downInCircle = inCircle;
                if(downInCircle) {
                    clickX = (int) x;
                    clickY = (int) y;
                    invalidate();
                    mListener.onColorChanged(getColor());
                }
//                break;

            case MotionEvent.ACTION_MOVE:
                if(downInCircle && inCircle) {
                    clickX = (int) x;
                    clickY = (int) y;
                    invalidate();
                    mListener.onColorChanged(getColor());
                }
                break;

            case MotionEvent.ACTION_UP:
                if(downInCircle) {
                    downInCircle = false;
                    invalidate();
                }
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth,mHeight);
    }

    /**
     * 将触摸点转换成颜色值
     * @return
     */
    private int getColor() {
        float angle = (float) Math.atan2(clickY,clickX);
        float unit = (float) (angle / (2 * Math.PI));
        if(unit < 0) {
            unit += 1;
        }
        return interpCircleColor(mCircleColors,unit);
    }


    /**
     * 获取圆环上的颜色
     * @param colors
     * @param unit
     * @return
     */
    private int interpCircleColor(int colors[],float unit) {
        if(unit <= 0) {
            return colors[0];
        }
        if(unit >= 1) {
            return colors[colors.length - 1];
        }
        float p = unit * (colors.length - 1);
        int i = (int)p;
        p -= i;

        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0),Color.alpha(c1),p);
        int r = ave(Color.red(c0),Color.red(c1),p);
        int g = ave(Color.green(c0),Color.green(c1),p);
        int b = ave(Color.blue(c0),Color.blue(c1),p);

        return Color.argb(a,r,g,b);


    }

    private int ave(int s,int d, float p) {
        return s + Math.round(p * (d - s));
    }

    private boolean inColorCircle(float x,float y,float outRadius) {
        double outCircle = Math.PI * outRadius * outRadius;
        double fingerCircle = Math.PI * (x * x + y * y);
        if(fingerCircle < outCircle) {
            return true;
        }else {
            return false;
        }
    }



    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    public OnColorChangedListener getColorChangedListener() {
        return mListener;
    }

    public void setColorChangedListener(OnColorChangedListener mListener) {
        this.mListener = mListener;
    }

}
