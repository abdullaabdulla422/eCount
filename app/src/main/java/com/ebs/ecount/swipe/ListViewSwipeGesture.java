package com.ebs.ecount.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebs.ecount.R;
import com.ebs.ecount.objects.PartObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cbe-teclwsp-008 on 15/12/16.
 */
@SuppressWarnings("ConstantConditions")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ListViewSwipeGesture implements View.OnTouchListener {
    private static String TAG="ListViewSwipeGesture";

    public static int Single = 1;
    public static int Double = 2;
    public static int Dismiss = 3;
    static TouchCallbacks tcallbacks;
    public boolean moptionsDisplay = false;
    //Functional  Usages
    public String HalfColor;          //Green
    public String FullColor;         //Orange
    public String HalfText;
    public String FullText;
    public String HalfTextFinal;
    public String FullTextFinal;
    public Drawable HalfDrawable;
    public Drawable FullDrawable;
    //Swipe Types
    public int SwipeType;
    Activity activity;
    //Intermediate Usages`
    String TextColor = "#FFFFFF";      //#FF4444
    String RangeOneColor = "#FFD060";   //"#FFD060"
    String singleColor = "#FF4444";
    int cust_width, cust_height;
    // Cached ViewConfiguration and system-wide constant values
    private int mSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private long mAnimationTime;
    // Fixed properties
    private ListView mListView;
    //private DismissCallbacks mCallbacks;
    private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero
    private int smallWidth = 1;
    private int largewidth = 1;
    private int textwidth = 1;
    private int textwidth2 = 1;
    private int textheight = 1;
    // Transient properties
    private List<PendingDismissData> mPendingDismisses = new ArrayList<PendingDismissData>();
    private int mDismissAnimationRefCount = 0;
    private float mDownX;
    private boolean mSwiping;
    private VelocityTracker mVelocityTracker;
    private int temp_position, opened_position, stagged_position;
    private ViewGroup mDownView, old_mDownView;
    private ViewGroup mDownView_parent;
    private TextView mDownView_parent_txt1, mDownView_parent_txt2;
    private boolean mPaused;
    private ArrayList<PartObject> partObjects;

    public ListViewSwipeGesture(ListView listView, TouchCallbacks Callbacks, Activity context, final ArrayList<PartObject> partObjects, int cust_width, int cust_height) {
        ViewConfiguration vc = ViewConfiguration.get(listView.getContext());
        mSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 16;
        Log.d(TAG, "ListViewSwipeGesture: "+mMinFlingVelocity);

        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        Log.d(TAG, "ListViewSwipeGesture: "+mMinFlingVelocity);
        mListView = listView;
        activity = context;
        tcallbacks = Callbacks;
        SwipeType = Double;
        this.partObjects = partObjects;
        this.cust_width = cust_width;
        this.cust_height = cust_height;

        GetResourcesValues();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //Invokes OnClick Functionality
                if (!moptionsDisplay) {

                    tcallbacks.OnClickListView((Integer) mListView.getItemAtPosition(arg2));
                }

            }
        });
    }

    private void GetResourcesValues() {
        mAnimationTime = mListView.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        HalfColor = RangeOneColor;          //Green
        FullColor = activity.getResources().getString(R.string.str_orange);         //Orange
        HalfText = activity.getResources().getString(R.string.basic_action_3);
        HalfTextFinal = activity.getResources().getString(R.string.basic_action_1);
        FullText = activity.getResources().getString(R.string.basic_action_2);
        FullTextFinal = activity.getResources().getString(R.string.basic_action_2);
        HalfDrawable = activity.getResources().getDrawable(R.drawable.content_discard);
        FullDrawable = activity.getResources().getDrawable(R.drawable.content_discard);
    }

    public void setEnabled(boolean enabled) {
        mPaused = !enabled;
    }

    public GestureScroll makeScrollListener() {
        return new GestureScroll();
    }

    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public boolean onTouch(final View view, MotionEvent event) {

        if (mViewWidth < 2) {
            mViewWidth = mListView.getWidth();
            smallWidth = mViewWidth / 7;
            textwidth2 = mViewWidth / 3;
            textwidth = textwidth2;
            largewidth = textwidth + textwidth2;
        }

        int tempwidth = 0;
        if (SwipeType == 1)
            tempwidth = smallWidth;
        else
            tempwidth = textwidth2;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int mDownPosition;
            {
                if (mPaused) {
                    return false;
                }
                Rect rect = new Rect();
                int childCount = mListView.getChildCount();
                int[] listViewCoords = new int[2];
                mListView.getLocationOnScreen(listViewCoords);
                int x = (int) event.getRawX() - listViewCoords[0];
                int y = (int) event.getRawY() - listViewCoords[1];
                ViewGroup child;
                for (int i = 0; i < childCount; i++) {
                    child = (ViewGroup) mListView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mDownView_parent = child;
                        mDownView = (ViewGroup) child.findViewById(R.id.list_display_view_container);

                        if (mDownView_parent.getChildCount() == 1) {
                            textheight = mDownView_parent.getHeight();
                            if (SwipeType == Dismiss) {
                                HalfColor = singleColor;
                                HalfDrawable = activity.getResources().getDrawable(R.drawable.cancel);
                            }

                            int pos = mListView.pointToPosition((int) event.getX(), (int) event.getY());

                            if (pos == -1) {
                                pos = 0;
                            } else {
                                pos = pos;
                            }

                            Log.d("Click ", "temp_position " + pos);
                            if (partObjects.get(pos).getStatus().contains("R") && partObjects.get(pos).getReplacedPart()) {
                                SwipeType = Single;
                            } else if (partObjects.get(pos).getStatus().contains("C")
                                    && partObjects.get(pos).getFlag() == false) {
                                SwipeType = Single;
                            } else if (partObjects.get(pos).getStatus().contains("C")) {
                                FullText = "InComplete";
                                SwipeType = Double;
                            } else if (partObjects.get(pos).getStatus().contains("I") &&
                                    (!partObjects.get(pos).getReplacedPart())) {
                                FullText = "Complete";
                                SwipeType = Double;
                            } else if (partObjects.get(pos).getStatus().contains("I") &&
                                    partObjects.get(pos).getReplacedPart()) {
                                SwipeType = Single;
                            }
//                            else if(partObjects.get(pos).getStatus().toString().contains("I")){
//                                FullText = "Complete";
//                                SwipeType = Double;
//                            }

                            SetBackGroundforList(pos);

                        }


                        if (old_mDownView != null && mDownView != old_mDownView) {
                            ResetListItem(old_mDownView);
                            old_mDownView = null;
                            return false;
                        }
                        break;
                    }
                }

                if (mDownView != null) {
                    mDownX = event.getRawX();
                    mDownPosition = mListView.getPositionForView(mDownView);
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                } else {
                    mDownView = null;
                }

                //mSwipeDetected              =   false;
                temp_position = mListView.pointToPosition((int) event.getX(), (int) event.getY());

                if (temp_position == -1) {
                    temp_position = 0;
                }
                view.onTouchEvent(event);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (mVelocityTracker == null) {
                    break;
                }
                float deltaX = event.getRawX() - mDownX;
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000); // 1000 by defaut but
                float velocityX = mVelocityTracker.getXVelocity();                                            // it was too much
                float absVelocityX = Math.abs(velocityX);
                float absVelocityY = Math.abs(mVelocityTracker.getYVelocity());
                boolean swipe = false;
                boolean swipeRight = false;


                if (Math.abs(deltaX) > tempwidth) {
                    swipe = true;
                    swipeRight = deltaX > 0;

                } else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity && absVelocityY < absVelocityX) {
                    // dismiss only if flinging in the same direction as dragging
                    swipe = (velocityX < 0) == (deltaX < 0);
                    swipeRight = mVelocityTracker.getXVelocity() > 0;
                }

                if (deltaX < 0 && swipe) {
                    mListView.setDrawSelectorOnTop(false);

                    if (swipe && !swipeRight && deltaX <= -tempwidth) {
                        FullSwipeTrigger();
                    } else if (deltaX >= -textwidth && SwipeType == Double) {
                        ResetListItem(mDownView);
                    } else {
                        ResetListItem(mDownView);
                    }
                } else {
                    ResetListItem(mDownView);
                }

                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mDownX = 0;
                mDownView = null;
                mDownPosition = ListView.INVALID_POSITION;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                float deltaX = event.getRawX() - mDownX;
                if (mVelocityTracker == null || mPaused || deltaX > 0) {
                    break;
                }

                mVelocityTracker.addMovement(event);

                if (Math.abs(deltaX) > mSlop) {
                    mSwiping = true;
                    mListView.requestDisallowInterceptTouchEvent(true);

                    // Cancel ListView's touch (un-highlighting the item)
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (event.getActionIndex()
                                    << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    mListView.onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                }
                if (mSwiping && deltaX < 0) {

                    int width;
                    if (SwipeType == 1) {
                        width = textwidth;
                    } else {
                        width = largewidth;
                    }

                    if (-deltaX < width) {
                        mDownView.setTranslationX(deltaX);
                        return false;
                    }
                    return false;
                } else if (mSwiping) {
                    ResetListItem(mDownView);
                }
                break;
            }
        }
        return false;
    }

    private void SetBackGroundforList(int pos) {
        // TODO Auto-generated method stub
        int button1_width = cust_width / 5;

        Log.d("Button Width1", "" + button1_width / 6);
        Log.d("Button Width2", "" + button1_width / 6);

//        int button_width = (cust_width/3)/2;

        mDownView_parent_txt1 = new TextView(activity.getApplicationContext());
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mDownView_parent_txt1.setId(111111);

        mDownView_parent_txt1.setLayoutParams(lp1);
        mDownView_parent_txt1.setGravity(Gravity.CENTER);

        if (partObjects.get(pos).getReplacedPart() && partObjects.get(pos).getStatus().contains("I")) {
            mDownView_parent_txt1.setText(HalfTextFinal);
        } else if ((!partObjects.get(pos).getReplacedPart()) && partObjects.get(pos).getStatus().contains("I")) {
            mDownView_parent_txt1.setText(HalfText);
        } else if (partObjects.get(pos).getReplacedPart() && partObjects.get(pos).getStatus().contains("R")) {
            mDownView_parent_txt1.setText(HalfText);
        }

//        mDownView_parent_txt1.setTextSize(12);
//        mDownView_parent_txt1.setText(HalfText);
        mDownView_parent_txt1.setWidth(button1_width);
        mDownView_parent_txt1.setHeight(textheight);
//        mDownView_parent_txt1.setWidth((int) (button_width));
//        mDownView_parent_txt1.setWidth((int) (button1_width/3));
        mDownView_parent_txt1.setBackgroundColor(Color.parseColor("#FF4444"));
        mDownView_parent_txt1.setTextColor(Color.parseColor(TextColor));
        mDownView_parent_txt1.setTypeface(mDownView_parent_txt1.getTypeface(), Typeface.BOLD);
        mDownView_parent_txt1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        if ((!partObjects.get(pos).getReplacedPart()) && partObjects.get(pos).getStatus().equals("R")) {

        } else {

            mDownView_parent.addView(mDownView_parent_txt1, 0);

        }

        if (SwipeType == Double) {
            mDownView_parent_txt2 = new TextView(activity.getApplicationContext());
            mDownView_parent_txt2.setId(222222);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.LEFT_OF, mDownView_parent_txt1.getId());
            mDownView_parent_txt2.setLayoutParams(lp2);
            mDownView_parent_txt2.setGravity(Gravity.CENTER);

            mDownView_parent_txt2.setText(FullText);
            mDownView_parent_txt2.setWidth(button1_width);
//            mDownView_parent_txt1.setWidth((int) (button_width));
//            mDownView_parent_txt1.setWidth((int) (button1_width/6));
            mDownView_parent_txt2.setHeight(textheight);
            mDownView_parent_txt2.setBackgroundColor(Color.parseColor("#007AE0"));
            mDownView_parent_txt2.setTextColor(Color.parseColor(TextColor));
            mDownView_parent_txt2.setTypeface(mDownView_parent_txt2.getTypeface(), Typeface.BOLD);
            mDownView_parent_txt2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            if ((!partObjects.get(pos).getReplacedPart()) && partObjects.get(pos).getStatus().equals("R")) {

            } else {
                mDownView_parent.addView(mDownView_parent_txt2, 1);
            }

        }
    }

    private void ResetListItem(View tempView) {

        tempView.animate().translationX(0).alpha(1f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(
                    Animator animation) {
                super.onAnimationEnd(animation);
                int count = mDownView_parent.getChildCount() - 1;
                for (int i = 0; i < count; i++) {
                    View V = mDownView_parent.getChildAt(i);
                    Log.d("removing child class", "" + V.getClass());
                    mDownView_parent.removeViewAt(0);
                }
                moptionsDisplay = false;

            }
        });

        stagged_position = -1;
        opened_position = -1;

    }

    private void FullSwipeTrigger() {

        Log.d("FUll Swipe trigger call", "Works**********************" + mDismissAnimationRefCount);
        old_mDownView = mDownView;
        int width;
        if (SwipeType == Single || SwipeType == Dismiss) {
            //width	=	200;
            width = cust_width / 5;
            if (SwipeType == Dismiss) {
                if (mDismissAnimationRefCount == 1) {
                    mDismissAnimationRefCount = 0;
                    ++mDismissAnimationRefCount;
                } else {
                    ++mDismissAnimationRefCount;
                }

            }
        } else {

            width = (int) ((cust_width / 5) + (cust_width / 5));
        }
        mDownView.animate().translationX(-width).setDuration(50)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(
                            Animator animation) {
                        super.onAnimationEnd(animation);
                        moptionsDisplay = true;
                        stagged_position = temp_position;
                        mDownView_parent_txt1.setOnTouchListener(new touchClass());
                        if (SwipeType == Double)
                            mDownView_parent_txt2.setOnTouchListener(new touchClass());

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }
                });
    }

    private void performDismiss(final View dismissView, final int dismissPosition) {

        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
        final int originalHeight = dismissView.getHeight();

        ((ViewGroup) dismissView).getChildAt(1).animate().translationX(0).alpha(1f).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((ViewGroup) dismissView).removeViewAt(0);
                Log.d("Selected view", dismissView.getClass() + "..." + dismissView.getId() + mDismissAnimationRefCount);
                ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0).setDuration(mAnimationTime);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        --mDismissAnimationRefCount;
                        if (mDismissAnimationRefCount == 0) {

                            Collections.sort(mPendingDismisses);

                            int[] dismissPositions = new int[mPendingDismisses.size()];
                            for (int i = mPendingDismisses.size() - 1; i >= 0; i--) {
                                dismissPositions[i] = mPendingDismisses.get(i).position;
                                Log.d("Dismiss positions....", dismissPositions[i] + "");
                            }
                            tcallbacks.onDismiss(dismissPositions);
                            mPendingDismisses.clear();
                        }
                    }
                });

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        lp.height = (Integer) valueAnimator.getAnimatedValue();
                        dismissView.setLayoutParams(lp);
                    }
                });

                mPendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
                animator.start();
            }
        });


    }

    public interface TouchCallbacks {                                           //Callback functions
        void FullSwipeListView(int position, String status);

        void HalfSwipeListView(int position, String text);

        void OnClickListView(int position);

        void LoadDataForScroll();

        void onDismiss(int[] reverseSortedPositions);
    }

    class GestureScroll implements AbsListView.OnScrollListener {
        //Scroll Usages
        private int visibleThreshold = 4;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;
        private int previousFirstVisibleItem = 0;
        private long previousEventTime = 0, currTime, timeToScrollOneElement;
        private double speed = 0;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            setEnabled(scrollState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                tcallbacks.LoadDataForScroll();
                loading = true;
            }

            if (previousFirstVisibleItem != firstVisibleItem) {
                currTime = System.currentTimeMillis();
                timeToScrollOneElement = currTime - previousEventTime;
                speed = ((double) 1 / timeToScrollOneElement) * 1000;

                previousFirstVisibleItem = firstVisibleItem;
                previousEventTime = currTime;

            }

        }

        public double getSpeed() {
            return speed;
        }
    }

    class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;

        public PendingDismissData(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public int compareTo(PendingDismissData other) {
            // Sort by descending position
            return other.position - position;
        }
    }

    class touchClass implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            opened_position = mListView.getPositionForView((View) v.getParent());

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    if (opened_position == stagged_position && moptionsDisplay) {
                        switch (v.getId()) {
                            case 111111:
                                if (SwipeType == Dismiss) {
                                    moptionsDisplay = false;
                                    performDismiss(mDownView_parent, temp_position);
                                } else {

                                    if (partObjects.get(temp_position).getReplacedPart() &&
                                        partObjects.get(temp_position).getStatus().contains("I")) {
                                        tcallbacks.HalfSwipeListView(temp_position, "REMOVE");
                                    } else if ((!partObjects.get(temp_position).getReplacedPart()) &&

                                        partObjects.get(temp_position).getStatus().contains("I")) {
                                        tcallbacks.HalfSwipeListView(temp_position, "REPLACE");
                                    } else if (partObjects.get(temp_position).getStatus().equals("R") && partObjects.get(temp_position).getReplacedPart()) {
                                        tcallbacks.HalfSwipeListView(temp_position, "REPLACE");
                                    }

                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDownView = (ViewGroup) mDownView_parent.findViewById(R.id.list_display_view_container);
                                        ResetListItem(mDownView);
                                    }
                                }, 250);

                                return true;
                            case 222222:

                                Log.d("FULL_TEXT", FullText);
                                tcallbacks.FullSwipeListView(temp_position, FullText);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDownView = (ViewGroup) mDownView_parent.findViewById(R.id.list_display_view_container);
                                        ResetListItem(mDownView);


                                    }
                                }, 250);
                                return true;
                        }
                    }
                }
                return false;
            }

            return false;
        }

    }
}