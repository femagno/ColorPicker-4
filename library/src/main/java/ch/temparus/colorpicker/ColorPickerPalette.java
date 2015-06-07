package ch.temparus.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
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

    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_RIGHT = 2;

    public OnColorSelectedListener mOnColorSelectedListener;
    private ColorCircle mSelectedColorCircle;
    private int mCircleSize;
    private int mCircleMargin;
    private int mCircleDimension;
    private int mGravity;
    private int mMaxColumns;
    private Drawable mSelectedIcon;

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
        mGravity = array.getInt(R.styleable.ColorPickerPalette_gravity, 0);
        mMaxColumns = array.getInt(R.styleable.ColorPickerPalette_maxColumns, 0);
        mSelectedIcon = array.getDrawable(R.styleable.ColorPickerPalette_selectedIcon);

        Resources res = getResources();
        if(size == 0) {
            mCircleMargin = res.getDimensionPixelSize(R.dimen.color_circle_margins_small);
            mCircleSize = res.getDimensionPixelSize(R.dimen.color_circle_small);
        } else {
            mCircleMargin = res.getDimensionPixelSize(R.dimen.color_circle_margins_large);
            mCircleSize = res.getDimensionPixelSize(R.dimen.color_circle_large);
        }

        mCircleMargin = array.getDimensionPixelSize(R.styleable.ColorPickerPalette_spacing, mCircleMargin*2) / 2;
        mCircleSize = array.getDimensionPixelSize(R.styleable.ColorPickerPalette_circleSize, mCircleSize);
        mCircleDimension = (mCircleSize + 2 * mCircleMargin);
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
        ColorCircle item = null;

        for(int i = 0; i < childCount; ++i) {
            ColorCircle circle = (ColorCircle) getChildAt(i);
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
     * Clear selection.
     */
    public void clearSelection() {
        if(mSelectedColorCircle != null) {
            mSelectedColorCircle.setChecked(false);
            mSelectedColorCircle = null;
        }
    }

    /**
     * Get selected color
     *
     * @return color as integer or null if no color is selected
     */
    public Integer getSelectedColor() {
        if(mSelectedColorCircle == null) {
            return null;
        }
        return mSelectedColorCircle.getColor();
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

            ColorCircle circle = new ColorCircle(getContext(), color, mSelectedIcon, isSelected);
            circle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeSelection((ColorCircle) view, true);
                }
            });
            if (isSelected) {
                mSelectedColorCircle = circle;
            }
            super.addView(circle, layoutParams);
        }
    }

    private void changeSelection(ColorCircle newSelection, boolean dispatchEvent) {
        if(newSelection != mSelectedColorCircle) {
            if(mSelectedColorCircle != null) {
                mSelectedColorCircle.setChecked(false);
            }
            mSelectedColorCircle = newSelection;
            mSelectedColorCircle.setChecked(true);

            if (dispatchEvent && mOnColorSelectedListener != null) {
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
        int paddingX = 0;
        int columnsCount;
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            paddingX = ((widthSize + 2 * mCircleMargin) % mCircleDimension) / 2;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = widthSize - ((widthSize + 2 * mCircleMargin) % mCircleDimension);
        } else {
            width = childCount * mCircleDimension - 2 * mCircleMargin;
        }

        columnsCount = (width + 2 * mCircleMargin) / mCircleDimension;

        if(mMaxColumns > 0 && columnsCount > mMaxColumns) {
            paddingX += (columnsCount - mMaxColumns) * mCircleDimension / 2;
            columnsCount = mMaxColumns;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = childCount / columnsCount * mCircleDimension - 2 * mCircleMargin;
            if(childCount % columnsCount > 0) {
                height += mCircleDimension;
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
        final int paddingLeft = getPaddingLeft();
        int columnsCount = (width + 2 * mCircleMargin) / mCircleDimension;
        int positionTop = getPaddingTop();
        int positionLeft = calculateLeftPosition(paddingLeft, childCount, columnsCount);

        if(mMaxColumns > 0 && columnsCount > mMaxColumns) {
            columnsCount = mMaxColumns;
        }

        for(int i = 0; i < childCount; ++i) {
            View child = getChildAt(i);
            child.setVisibility(VISIBLE);
            child.layout(positionLeft, positionTop, positionLeft + mCircleSize, positionTop + mCircleSize);

            if((i+1) % columnsCount == 0) {
                positionLeft = calculateLeftPosition(paddingLeft, childCount - i - 1, columnsCount);
                positionTop += mCircleDimension;
            } else {
                positionLeft += mCircleDimension;
            }
        }
    }

    private int calculateLeftPosition(int paddingLeft, int itemsCount, int columnsCount) {
        if(itemsCount >= columnsCount || mGravity == GRAVITY_LEFT) {
            return paddingLeft;
        }
        if(mGravity == GRAVITY_CENTER) {
            return paddingLeft + (columnsCount - itemsCount) * mCircleDimension / 2;
        }
        if(mGravity == GRAVITY_RIGHT) {
            return paddingLeft + (columnsCount - itemsCount) * mCircleDimension;
        }
        return paddingLeft;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.selectedColor = (mSelectedColorCircle == null) ? null : mSelectedColorCircle.getColor();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

        if(savedState.selectedColor != null) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; ++i) {
                ColorCircle circle = (ColorCircle) getChildAt(i);
                if(circle.getColor() == savedState.selectedColor) {
                    changeSelection(circle, false);
                    break;
                }
            }
        }
    }

    /**
     * SavedState of view holding selected color.
     */
    static class SavedState extends BaseSavedState {
        Integer selectedColor;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);

            selectedColor = in.readByte() == 0x00 ? null : in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (selectedColor == null ? (0x00) : (0x01)));
            if (selectedColor != null) out.writeInt(selectedColor);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }
                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
    }
}