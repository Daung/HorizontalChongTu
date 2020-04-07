package com.wzy.horizontalchongtu1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MyHorizontalScrollView extends ViewGroup {

    private static final String TAG = "MyHorizontalScrollView";
    private final Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;


    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mScreenWidth = getScreenWidth();
        mScreenHeight = getScreenHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int paddingLeft = getPaddingLeft();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int left = paddingLeft;
            int top = getPaddingTop();
            int right = childView.getMeasuredWidth() + left;
            int bottom = childView.getMeasuredHeight() + top;

            childView.layout(left, top, right, bottom);
            paddingLeft = getPaddingLeft() + right;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int selfWidht = 0;
        int selfHeight = 0;
        for (int i = 0; i < count; i++) {

            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            selfHeight = Math.max(selfHeight, childView.getMeasuredHeight());
            selfWidht += childView.getMeasuredWidth();

        }

        setMeasuredDimension(selfWidht, selfHeight);
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();

    }

    private int getScreenHeight() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    int mLastX, mLastY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltax = x - mLastX;
                int deltay = y - mLastY;
                if (Math.abs(deltax) > Math.abs(deltay)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;

        }
        mLastX = x;
        mLastY = y;
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltax = x - mLastX;
                int deltay = y - mLastY;
                if (Math.abs(deltax) > Math.abs(deltay)) {
                    scrollBy(-deltax, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                float halfWidth = mScreenWidth * 0.5f;
                int position = Math.abs(scrollX) / mScreenWidth;
                int offset = Math.abs(scrollX) % mScreenWidth;
                if (scrollX > 0) {
                    //向左滑动
                    if (offset >= halfWidth) {
                        position++;
                    }

                    position = Math.min(position, getChildCount() - 1);
                } else {
                    if (offset >= halfWidth) {
                        position--;
                    }

                    position = Math.max(0, position);
                }

                scrollTo(position * mScreenWidth, 0);

                break;

        }
        mLastX = x;
        mLastY = y;
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addView(new MyRecyclerView(mContext), new ViewGroup.LayoutParams(mScreenWidth, mScreenHeight));
        addView(new MyRecyclerView(mContext), new ViewGroup.LayoutParams(mScreenWidth, mScreenHeight));
        addView(new MyRecyclerView(mContext), new ViewGroup.LayoutParams(mScreenWidth, mScreenHeight));

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (i == 0) {
                childView.setBackgroundColor(Color.RED);
            } else if (i == 1) {
                childView.setBackgroundColor(Color.GREEN);
            } else {
                childView.setBackgroundColor(Color.BLUE);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
