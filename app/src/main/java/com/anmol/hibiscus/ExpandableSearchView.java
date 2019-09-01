package com.anmol.hibiscus;

import android.graphics.Paint;
import androidx.annotation.NonNull;
import android.widget.FrameLayout;
import android.animation.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Paint.*;

import androidx.annotation.Nullable;
import androidx.core.content.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.view.inputmethod.*;
import android.widget.*;


import java.lang.reflect.*;

public class ExpandableSearchView extends FrameLayout {
    public static final long ANIMATION_DURATION = 200L;
    private static final int DEFAULT_PADDING = 3; //dp
    private static final int SEARCH_HANDLE_PADDING = 10; //dp
    private static final long EXPAND_ANIMATION_DURATION = 300L;

    @NonNull
    private final Paint clearViewPathPaint1 = new Paint();
    @NonNull
    private final Path arcPath = new Path();
    private final int searchHandlePadding;
    private final int padding;
    private final int defaultHeight;
    private OnSearchActionListener onSearchActionListener;
    private EditText searchEditText;
    private Paint clearViewPathPaint2;
    private Paint arcPathPaint;
    private Paint searchHandlePathPaint;
    private int clearViewWidth;
    private int textColor;
    @Nullable
    private Path searchHandlePath;
    private ObjectAnimator searchHandlePathAnimator;
    private float searchHandlePathLength;
    private ObjectAnimator clearViewAnimator1;
    private ObjectAnimator clearViewAnimator2;
    @Nullable
    private Path clearViewPath1;
    @Nullable
    private Path clearViewPath2;
    private float clearViewPath1Length;
    private float clearViewPath2Length;
    @Nullable
    private ValueAnimator expandCollapseAnimator;
    private boolean isClearViewClicked;
    private boolean isExpanded;
    @Nullable
    private String text;
    @Nullable
    private RectF clearViewBounds;
    private boolean isDrawn;
    private int maxWidth;

    {
        defaultHeight = dpToPx(50);
        padding = dpToPx(DEFAULT_PADDING);
        searchHandlePadding = dpToPx(SEARCH_HANDLE_PADDING);
    }

    public ExpandableSearchView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        int transparentColor = ContextCompat.getColor(context, android.R.color.transparent);
        textColor = ContextCompat.getColor(context, android.R.color.holo_blue_light);
        maxWidth = getResources().getDisplayMetrics().widthPixels / 2;

        float textSize = 12;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableSearchView, 0, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.ExpandableSearchView_textColor:
                    textColor = a.getColor(attr, textColor);
                    break;
                case R.styleable.ExpandableSearchView_maxWidth:
                    float width = a.getDimension(attr, maxWidth);
                    if (width >= 0) {
                        maxWidth = (int) width;
                    }
                    break;
                case R.styleable.ExpandableSearchView_textSize:
                    textSize = a.getDimension(attr, textSize);
                    break;
            }
        }
        a.recycle();

        setBackgroundColor(transparentColor);

        setPadding(padding, padding, padding, padding);

        searchEditText = new SearchEditText(context);
        searchEditText.setBackgroundColor(transparentColor);
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        searchEditText.setEnabled(false);
        addView(searchEditText, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL));

        clearViewPathPaint1.setStyle(Style.STROKE);
        clearViewPathPaint1.setColor(textColor);
        clearViewPathPaint1.setStrokeCap(Cap.ROUND);
        clearViewPathPaint1.setAntiAlias(true);
        clearViewPathPaint1.setStrokeWidth(10);

        clearViewPathPaint2 = new Paint(clearViewPathPaint1);
        searchHandlePathPaint = new Paint(clearViewPathPaint1);
        arcPathPaint = new Paint(clearViewPathPaint1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(defaultHeight, heightSize);
        } else {
            height = defaultHeight;
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isClearViewClicked = isWithinBounds(clearViewBounds, event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if (isExpanded && isClearViewClicked && isWithinBounds(clearViewBounds, event.getX(), event.getY()) || !isExpanded) {
                    searchEditText.getText().clear();
                    startExpandContractAnimation();
                }
                break;
        }

        searchEditText.onTouchEvent(event);

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (!isDrawn) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = h;

            w = h;
            if (maxWidth < w) {
                maxWidth = getResources().getDisplayMetrics().widthPixels / 2;
            }
            clearViewWidth = h / 2;
            searchEditText.setPadding(h / 4, 0, clearViewWidth + clearViewWidth / 4 + searchHandlePadding, searchHandlePadding);

            requestLayout();
        }

        updateBorder(w, h);
        initHandlePath(w, h, false);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        isDrawn = true;

        if (clearViewPath1 != null) {
            canvas.drawPath(clearViewPath1, clearViewPathPaint1);
        }
        if (clearViewPath2 != null) {
            canvas.drawPath(clearViewPath2, clearViewPathPaint2);
        }
        if (searchHandlePath != null) {
            canvas.drawPath(searchHandlePath, searchHandlePathPaint);
        }
        canvas.drawPath(arcPath, arcPathPaint);
    }

    private void updateBorder(int w, int h) {
        arcPath.reset();
        RectF leftArc = new RectF(padding, padding, h - searchHandlePadding - padding, h - padding - searchHandlePadding);
        arcPath.arcTo(leftArc, 90, 180);
        RectF rightArc = new RectF(w - h + padding, padding, w - padding - searchHandlePadding, h - padding - searchHandlePadding);
        arcPath.arcTo(rightArc, -90, 180);
        arcPath.close();

        invalidate();
    }

    protected void setClearViewPath1Progress(float value) {
        clearViewPathPaint1.setPathEffect(createPathEffect(clearViewPath1Length, value, 0f));
        invalidate();
    }

    protected void setClearViewPath2Progress(float value) {
        clearViewPathPaint2.setPathEffect(createPathEffect(clearViewPath2Length, value, 0f));
        invalidate();
    }

    protected void setSearchHandlePathProgress(float value) {
        searchHandlePathPaint.setPathEffect(createPathEffect(searchHandlePathLength, value, 0f));
        invalidate();
    }

    @NonNull
    private PathEffect createPathEffect(float pathLength, float value, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength}, Math.max(value * pathLength, offset));
    }

    public void setOnSearchActionListener(OnSearchActionListener onSearchActionListener) {
        this.onSearchActionListener = onSearchActionListener;
    }

    private void startExpandContractAnimation() {
        startExpandContractAnimation(true);
    }

    private void startExpandContractAnimation(boolean waitForOtherAnimations) {
        if (expandCollapseAnimator != null && expandCollapseAnimator.isRunning()) {
            return;
        }

        int startWidth;
        int endWidth;
        final boolean isExpanding = getMeasuredWidth() != maxWidth;
        if (isExpanding) {
            if (waitForOtherAnimations) {
                hideHandlePath();
                return;
            }
        } else {
            clearViewBounds = null;
            if (waitForOtherAnimations) {
                hideClearView(true);
                return;
            }
        }

        if (isExpanding) {
            startWidth = getMeasuredWidth();
            endWidth = maxWidth;

            isExpanded = true;
            searchEditText.setEnabled(true);
            if (!TextUtils.isEmpty(text)) {
                searchEditText.setText(text);
                searchEditText.setSelection(text.length());
            }
        } else {
            startWidth = maxWidth;
            endWidth = getMeasuredHeight();

            isExpanded = false;
            text = searchEditText.getText().toString();
            searchEditText.getText().clear();
            searchEditText.setEnabled(false);
        }

        expandCollapseAnimator = ValueAnimator.ofInt(startWidth, endWidth);
        expandCollapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                int width = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = width;
                requestLayout();
            }
        });
        expandCollapseAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isExpanding) {
                    clearViewBounds = new RectF(getMeasuredWidth() - clearViewWidth - searchHandlePadding - padding, padding, getMeasuredWidth() - padding - searchHandlePadding, getMeasuredHeight()
                            - padding - searchHandlePadding);
                    showClearView();
                    showKeyboard();
                } else {
                    showHandlePath();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        expandCollapseAnimator.setDuration(EXPAND_ANIMATION_DURATION);
        expandCollapseAnimator.start();
    }

    private void hideHandlePath() {
        searchHandlePath = null;
        initHandlePath(getMeasuredWidth(), getMeasuredHeight(), true);
        searchHandlePathAnimator.start();
    }

    private void showHandlePath() {
        searchHandlePath = null;
        initHandlePath(getMeasuredWidth(), getMeasuredHeight(), false);
        searchHandlePathAnimator.start();
    }

    private void initHandlePath(int w, int h, boolean reversed) {
        if (searchHandlePath != null) {
            return;
        }

        double degrees = 315 * (Math.PI / 180d);
        int radius = h / 2 - padding - searchHandlePadding;
        int x = h / 2 + (int) (radius * Math.cos(degrees));
        int y = h / 2 - (int) (radius * Math.sin(degrees));

        searchHandlePath = new Path();
        searchHandlePath.moveTo(x, y);
        searchHandlePath.lineTo(w - padding, h - padding);

        PathMeasure handleMeasure = new PathMeasure(searchHandlePath, false);
        searchHandlePathLength = handleMeasure.getLength();

        if (reversed) {
            searchHandlePathAnimator = ObjectAnimator.ofFloat(this, "searchHandlePathProgress", 0f, 1f);
        } else {
            searchHandlePathAnimator = ObjectAnimator.ofFloat(this, "searchHandlePathProgress", 1f, 0f);
        }
        searchHandlePathAnimator.setDuration(ANIMATION_DURATION);
        searchHandlePathAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (reversed) {
            searchHandlePathAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startExpandContractAnimation(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private void showClearView() {
        clearViewPath1 = null;
        clearViewPath2 = null;
        initClearView(getMeasuredWidth(), getMeasuredHeight(), false);

        clearViewAnimator1.start();
        clearViewPathPaint2.setPathEffect(createPathEffect(clearViewPath2Length, 1f, 0f));
        clearViewAnimator2.start();
    }

    private void hideClearView(boolean animated) {
        if (animated) {
            clearViewPath1 = null;
            clearViewPath2 = null;
            initClearView(getMeasuredWidth(), getMeasuredHeight(), true);

            clearViewAnimator1.start();
            clearViewAnimator2.start();
        } else {
            clearViewPathPaint1.setPathEffect(createPathEffect(clearViewPath1Length, 1f, 0f));
            clearViewPathPaint2.setPathEffect(createPathEffect(clearViewPath2Length, 1f, 0f));
            invalidate();
        }
    }

    private void initClearView(int w, int h, boolean reversed) {
        if (clearViewPath1 != null && clearViewPath2 != null) {
            return;
        }

        int availableHeight = h - 2 * padding - searchHandlePadding;

        clearViewPath1 = new Path();
        clearViewPath1.moveTo(w - searchHandlePadding - clearViewWidth, availableHeight / 3 + padding);
        clearViewPath1.lineTo(w - padding - searchHandlePadding - availableHeight / 3, 2 * availableHeight / 3 + padding);

        clearViewPath2 = new Path();
        clearViewPath2.moveTo(w - padding - searchHandlePadding - availableHeight / 3, availableHeight / 3 + padding);
        clearViewPath2.lineTo(w - clearViewWidth - searchHandlePadding, 2 * availableHeight / 3 + padding);

        PathMeasure measure1 = new PathMeasure(clearViewPath1, false);
        clearViewPath1Length = measure1.getLength();

        PathMeasure measure2 = new PathMeasure(clearViewPath2, false);
        clearViewPath2Length = measure2.getLength();

        if (reversed) {
            clearViewAnimator1 = ObjectAnimator.ofFloat(this, "clearViewPath1Progress", 0f, 1f);
        } else {
            clearViewAnimator1 = ObjectAnimator.ofFloat(this, "clearViewPath1Progress", 1f, 0f);
        }
        clearViewAnimator1.setDuration(reversed ? ANIMATION_DURATION : ANIMATION_DURATION);
        clearViewAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        if (!reversed) {
            clearViewPathPaint1.setPathEffect(createPathEffect(clearViewPath1Length, 1f, 0f));
        }

        if (reversed) {
            clearViewAnimator2 = ObjectAnimator.ofFloat(this, "clearViewPath2Progress", 0f, 1f);
        } else {
            clearViewAnimator2 = ObjectAnimator.ofFloat(this, "clearViewPath2Progress", 1f, 0f);
        }
        clearViewAnimator2.setDuration(reversed ? ANIMATION_DURATION : ANIMATION_DURATION);
        clearViewAnimator2.setStartDelay(100);
        clearViewAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        if (reversed) {
            clearViewAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startExpandContractAnimation(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            clearViewPathPaint2.setPathEffect(createPathEffect(clearViewPath2Length, 1f, 0f));
        }
    }

    private void search() {
        if (!isExpanded || TextUtils.isEmpty(searchEditText.getText().toString())) {
            return;
        }

        if (onSearchActionListener != null) {
            onSearchActionListener.onSearchAction(searchEditText.getText().toString());
        }

        startExpandContractAnimation();
    }

    private int dpToPx(float dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private boolean isWithinBounds(@Nullable RectF bounds, float xPoint, float yPoint) {
        return bounds != null && xPoint > bounds.left && xPoint < bounds.right && yPoint > bounds.top && yPoint < bounds.bottom;
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    public interface OnSearchActionListener {
        void onSearchAction(String text);
    }

    private final class SearchEditText extends androidx.appcompat.widget.AppCompatEditText {

        public SearchEditText(@NonNull Context context) {
            super(context);

            setTextColor(textColor);
            setSingleLine(true);
            setImeOptions(EditorInfo.IME_ACTION_SEARCH);

            try {
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                field.set(this, 0);
            } catch (Exception ignored) {
            }

            setOnEditorActionListener(new OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        search();
                        return true;
                    }

                    return false;
                }
            });
        }
    }
}
