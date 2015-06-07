package ch.temparus.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Creates a color circle of a specified color. Adds a checkmark if marked as checked.
 *
 * @author Sandro Lutz
 * @author Maurizio Pasquinelli
 */
public class ColorCircle extends FrameLayout {
    private int mColor;
    private boolean mChecked;
    private ImageView mCircleImage;
    private ImageView mSelectorImage;

    public ColorCircle(Context context) {
        this(context, null);
    }

    public ColorCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPalette);
        final int color = array.getColor(R.styleable.ColorCircle_circleColor, 0);
        final Drawable icon = array.getDrawable(R.styleable.ColorCircle_checkmark);
        final boolean checked = array.getBoolean(R.styleable.ColorCircle_checked, false);

        array.recycle();

        inflateLayout();
        setColor(color);
        setCheckmarkDrawable(icon);
        setChecked(checked);
    }

    public ColorCircle(Context context, int color, boolean checked) {
        super(context);

        inflateLayout();
        setColor(color);
        setCheckmarkDrawable(null);
        setChecked(checked);
    }

    public ColorCircle(Context context, int color, Drawable icon, boolean checked) {
        super(context);

        inflateLayout();
        setColor(color);
        setCheckmarkDrawable(icon);
        setChecked(checked);
    }

    /**
     * set color of this {@link ColorCircle}
     *
     * @param color background color of {@link ColorCircle}
     */
    public void setColor(int color) {
        mColor = color;
        Drawable[] colorDrawable = new Drawable[]{ResourcesCompat.getDrawable(getResources(), R.drawable.color_circle, null)};
        mCircleImage.setImageDrawable(new ColorStateDrawable(colorDrawable, color));
    }

    /**
     * Set icon for checkmark of this {@link ColorCircle}
     *
     * @param icon drawable is visible when {@link ColorCircle} is checked.
     */
    public void setCheckmarkDrawable(Drawable icon) {
        mSelectorImage.setImageDrawable((icon == null) ? ResourcesCompat.getDrawable(getResources(), R.drawable.ic_color_circle_selected, null) : icon);
    }

    /**
     * Set checked status.
     */
    public void setChecked(boolean checked) {
        mChecked = checked;
        if (checked) {
            mSelectorImage.setVisibility(View.VISIBLE);
        } else {
            mSelectorImage.setVisibility(View.GONE);
        }
    }

    /**
     * Get color of this {@link ColorCircle}
     *
     * @return Color as an integer value.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Check if this circle is checked.
     */
    public boolean isChecked() {
        return mChecked;
    }

    private void inflateLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.color_circle, this);
        mCircleImage = (ImageView) findViewById(R.id.color_circle);
        mSelectorImage = (ImageView) findViewById(R.id.color_selector);
    }
}
