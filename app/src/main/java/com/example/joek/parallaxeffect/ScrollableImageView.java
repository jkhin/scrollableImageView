package com.example.joek.parallaxeffect;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.OverScroller;

import java.util.ArrayList;


public class ScrollableImageView extends android.support.v7.widget.AppCompatImageView {

    private GestureDetectorCompat gestureDetector;
    private OverScroller overScroller;
    private ArrayList<ScrollableImageView> children;

    private final int screenW;
    private final int screenH;

    private int positionX = 0;
    private int positionY = 0;

    private String percentValue;
    public int percent;


    public ScrollableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        this.percent = Integer.parseInt(percentValue);

        // We will need screen dimensions to make sure we don't overscroll the
        // image
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;

        children = new ArrayList<>();
        gestureDetector = new GestureDetectorCompat(context, gestureListener);
        overScroller = new OverScroller(context);
    }

    private void init(final AttributeSet attrs) {
        if (attrs != null) {
            String packageName = "http://schemas.android.com/apk/res-auto";
            percentValue = attrs.getAttributeValue(packageName, "setPercentVelocity");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("CurrentY1", String.format(Locale.US, "%f", event.getY()));
//        event.getY();
        gestureDetector.onTouchEvent(event);
        if(children !=null){
            for(ScrollableImageView child: this.children){
                child.gestureDetector.onTouchEvent(event);
            }
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // computeScrollOffset() returns true only when the scrolling isn't
        // already finished
        if (overScroller.computeScrollOffset()) {
            positionY = overScroller.getCurrY();
            scrollTo(positionX, positionY);
        } else {
            // when scrolling is over, we will want to "spring back" if the
            // image is overscrolled
            overScroller.springBack(positionX, positionY, 0, getMaxHorizontal(), 0, getMaxVertical());
        }
    }

    private int getMaxHorizontal() {
        return (Math.abs(getDrawable().getBounds().width() - screenW));
    }

    private int getMaxVertical() {
        return (Math.abs(getDrawable().getBounds().height() - screenH));
    }

    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            overScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(ScrollableImageView.this);
            return true;
        }

        private int transformUnitScrollVel(float value, int percent){
            float unit = ((value * percent) / 100);
            return (int) unit;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            overScroller.forceFinished(true);
            overScroller.fling(positionX, positionY, (int) -velocityX, -transformUnitScrollVel(velocityY, percent), 0, getMaxHorizontal(), 0,
                    getMaxVertical());
            ViewCompat.postInvalidateOnAnimation(ScrollableImageView.this);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            overScroller.forceFinished(true);
            // normalize scrolling distances to not overscroll the image
            int dy = transformUnitScrollVel(distanceY, percent);
            int newPositionY = positionY + dy;

            if (newPositionY < 0) {
                dy -= newPositionY;
            } else if (newPositionY > getMaxVertical()) {
                dy -= (newPositionY - getMaxVertical());
            }

            overScroller.startScroll(positionX, positionY, 0, dy, 0);
            ViewCompat.postInvalidateOnAnimation(ScrollableImageView.this);
            return true;
        }
    };

    public void setChildren(ScrollableImageView children) {
        if(!this.children.contains(children)){
            this.children.add(children);

        }
    }
}
