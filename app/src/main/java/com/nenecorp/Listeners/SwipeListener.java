package com.nenecorp.Listeners;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SwipeListener implements View.OnTouchListener {

    private int getDistance() {
        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return 200;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return 150;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return 50;
            default:
                return 100;
        }

    }

    static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    private float downX, downY, upX, upY;

    public SwipeListener(Activity mainActivity) {
        activity = mainActivity;
    }

    public void onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!");
        Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
        Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onTopToBottomSwipe() {
        Log.i(logTag, "onTopToBottomSwipe!");
        Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
        Log.i(logTag, "onBottomToTopSwipe!");
        Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > getDistance()) {
                    // left or right
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe();
                        return true;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + getDistance());
                    // return false; // We don't consume the event
                }

                // swipe vertical?
                if (Math.abs(deltaY) > getDistance()) {
                    // top or down
                    if (deltaY < 0) {
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    if (deltaY > 0) {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + getDistance());
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}