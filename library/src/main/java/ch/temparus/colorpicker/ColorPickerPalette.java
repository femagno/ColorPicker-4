package ch.temparus.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * A color picker custom view which creates an grid of color circles.  The number of circles per
 * row is determined automatically.
 *
 * @author Sandro Lutz
 */
public class ColorPickerPalette extends ViewGroup {

    public OnColorSelectedListener mOnColorSelectedListener;
    private ColorPickerCircle mSelectedColorCircle;
    private int mCircleSize;
    private int mCircleMargin;

    /**
     * Interface for a callback when a color is selected.
     */
    public interface OnColorSelectedListener {
        /**
         * Called when a specific color has been selected.
         */
        void onColorSelected(int color);
    }

    public ColorPickerPalette(Context context) {
        this(context, null);
    }

    public ColorPickerPalette(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPalette);
        final int id = array.getResourceId(R.styleable.ColorPickerPalette_colors, 0);
        final int size = array.getInt(R.styleable.ColorPickerPalette_size, 0);

        Resources res = getResources();
        if(size == 0) {
            mCircleMargin = res.getDimensionPixelSize(R.dimen.color_circle_margins_small);
            mCircleSize = res.getDimensionPixelSize(R.dimen.color_circle_small);
        } else {
            mCircleMargin = res.getDimensionPixelSize(R.dimen.color_circle_margins_large);
            mCircleSize = res.getDimensionPixelSize(R.dimen.color_circle_large);
        }

        mCircleMargin = array.getDimensionPixelSize(R.styleable.ColorPickerPalette_circle_margin, mCircleMargin);
        mCircleSize = array.getDimensionPixelSize(R.styleable.ColorPickerPalette_circle_size, mCircleSize);

        array.recycle();

        if (id != 0) {
            setColorPalette(getResources().getIntArray(id), null);
        }
    }

    /**
     * Set OnColorSelectedListener. It gets called every time the user clicks on a color circle.
     *
     * @param listener
     */
    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        mOnColorSelectedListener = listener;
    }

    /**
     * Select a color in the current color palette.
     *
     * @param color color to be selected
     */
    public void selectColor(int color) {
        int childCount = getChildCount();
        ColorPickerCircle item = null;

        for(int i = 0; i < childCount; ++i) {
            ColorPickerCircle circle = (ColorPickerCircle) getChildAt(i);
            if(circle.getColor() == color) {
                item = circle;
                break;
            }
        }
        if(item == null) {
            throw new NullPointerException("Color does not exist in current palette.");
        }
        changeSelection(item, false);
    }

    /**
     * Set color palette for this {@link ColorPickerPalette}.
     *
     * @param colors
     * @param selectedColor Color which should be selected at the beginning. -1 to select nothing.
     */
    public void setColorPalette(int[] colors, Integer selectedColor) {
        removeAllViews();

        mSelectedColorCircle = null;

        MarginLayoutParams layoutParams = new MarginLayoutParams(mCircleSize, mCircleSize);
        layoutParams.setMargins(mCircleMargin, mCircleMargin, mCircleMargin, mCircleMargin);

        for (int color : colors) {
            boolean isSelected = selectedColor != null && color == selectedColor && mSelectedColorCircle == null;

            ColorPickerCircle circle = new ColorPickerCircle(getContext(), color, isSelected);
            circle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeSelection((ColorPickerCircle) view, true);
                }
            });
            if (isSelected) {
                mSelectedColorCircle = circle;
            }
            super.addView(circle, layoutParams);
        }
    }

    private void changeSelection(ColorPickerCircle newSelection, boolean dispatchEvent) {
        if(newSelection != mSelectedColorCircle) {
            if(mSelectedColorCircle != null) {
                mSelectedColorCircle.setChecked(false);
            }
            mSelectedColorCircle = newSelection;
            mSelectedColorCircle.setChecked(true);

            if(dispatchEvent && mOnColorSelectedListener != null) {
                mOnColorSelectedListener.onColorSelected(mSelectedColorCircle.getColor());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int circleDimension = (mCircleSize + 2 * mCircleMargin);
        int paddingX = 0;
        int numColumns;
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            paddingX = ((widthSize + 2 * mCircleMargin) % circleDimension) / 2;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = widthSize - ((widthSize + 2 * mCircleMargin) % circleDimension);
        } else {
            width = childCount * circleDimension - 2 * mCircleMargin;
        }

        numColumns = (width + 2 * mCircleMargin) / circleDimension;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = childCount / numColumns * circleDimension - 2 * mCircleMargin;
            if(childCount % numColumns > 0) {
                height += circleDimension;
            }
        }
        height += getPaddingTop() + getPaddingBottom();
        setPadding(paddingX, getPaddingTop(), paddingX, getPaddingBottom());

        for(int i = 0; i < childCount; ++i) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(mCircleSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mCircleSize, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();
        final int width = right - left;
        final int startLeft = getPaddingLeft();
        final int circleDimension = (mCircleSize + 2 * mCircleMargin);
        final int numColumns = (width + 2 * mCircleMargin) / circleDimension;
        int positionLeft = startLeft;
        int positionTop = getPaddingTop();

        for(int i = 0; i < childCount; ++i) {
            View child = getChildAt(i);
            child.setVisibility(VISIBLE);
            child.layout(positionLeft, positionTop, positionLeft + mCircleSize, positionTop + mCircleSize);

            if((i+1) % numColumns == 0) {
                positionLeft = startLeft;
                positionTop += circleDimension;
            } else {
                positionLeft += circleDimension;
            }
        }
    }
}