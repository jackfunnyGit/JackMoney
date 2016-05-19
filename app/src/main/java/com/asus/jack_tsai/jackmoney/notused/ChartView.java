package com.asus.jack_tsai.jackmoney.notused;

/**
 * Created by Jack_Tsai on 2016/4/29.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.asus.jack_tsai.jackmoney.R;

public class ChartView extends View{

    private List mPointList = new ArrayList();
    private int mPointY = 0;
    private Paint mPoint = new Paint();   //画笔

    public ChartView(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        //初始化画笔
        Log.e("jackfunny","ChartView(Context context, AttributeSet attrs, int defStyleAttr)");
        mPoint.setColor(getContext().getResources().getColor(
                R.color.colorAccent));
        mPoint.setStrokeWidth(5.0f);
        mPoint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        Log.e("jackfunny", "ChartView OnDraw!!;\n");
        Log.e("jackfunny", "getWidth() ="+getWidth()+" getHeight()= "+getHeight() );
        int width = getWidth()-dp2px(50);
        int height = getHeight() - dp2px(50);
        int boundx=dp2px(25),boundy=dp2px(25);
        int xperwidth=width/7;
        int yperheight=height/10;
        mPoint.setStrokeWidth(2.0f);
        paramCanvas.drawLine(boundx, boundy , boundx, boundy+height, mPoint);//draw left line
        paramCanvas.drawLine(boundx, boundy+height , boundx+width, boundy+height, mPoint);//draw bottom line
        for (int i=1;i<8;i++){

            Point point= new Point(boundx+xperwidth*i,(int) (Math.random() * height) );
            mPointList.add(point);
        }

        if (mPointList.size() >= 2) {
            for (int k = 0; k < -1 + mPointList.size(); k++) {
                paramCanvas.drawLine(((Point) mPointList.get(k)).x,
                        ((Point) mPointList.get(k)).y,
                        ((Point) mPointList.get(k + 1)).x,
                        ((Point) mPointList.get(k + 1)).y, mPoint);
            }
        }






    }

    public final void ClearList() {
        mPointList.clear();
    }

    public final void AddPointToList(int paramInt) {
        mPointY = paramInt;
        invalidate();//重绘
    }

    public void stop(){
        mPointList.clear();
        invalidate();
    }
    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }
}
