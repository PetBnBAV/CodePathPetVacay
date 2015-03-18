package com.codepath.petbnbcodepath.viewpagers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by gangwal on 3/9/15.
 */
public class WrapContentHeightViewPager extends ViewPager
{
    public WrapContentHeightViewPager(Context context)
    {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//    {
//        super.onMeasure(400, 400);
//
////        int width = 0;
////        for(int i = 0; i < getChildCount(); i++)
////        {
////            View child = getChildAt(i);
////            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),heightMeasureSpec);
////
////            int w = child.getMeasuredWidth();
////            if(w > width) width = w;
////        }
////
////        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
////
//        super.onMeasure(400, 400);
//    }
}