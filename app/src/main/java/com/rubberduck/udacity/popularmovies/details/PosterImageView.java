package com.rubberduck.udacity.popularmovies.details;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class PosterImageView extends ImageView {

    public PosterImageView(Context context)
    {
        super(context);
    }

    public PosterImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PosterImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}