package ch.temparus.colorpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Creates a color circle of a specified color. Adds a checkmark if marked as checked.
 *
 * @author Sandro Lutz
 */
public class ColorPickerCircle extends FrameLayout {
    private int mColor;
    private boolean mChecked;
    private ImageView mCircleImage;
    private ImageView mSelectorImage;

    public ColorPickerCircle(Context context, int color, boolean checked) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.colorpicker_circle, this);
        mCircleImage = (ImageView) findViewById(R.id.colorpicker_circle);
        mSelectorImage = (ImageView) findViewById(R.id.colorpicker_selector);
        setColor(color);
        setChecked(checked);
    }

    protected void setColor(int color) {
        mColor = color;
        Drawable[] colorDrawable = new Drawable[]{getContext().getResources().getDrawable(R.drawable.colorpicker_circle)};
        mCircleImage.setImageDrawable(new ColorStateDrawable(colorDrawable, color));
    }
    protected void setChecked(boolean checked) {
        mChecked = checked;
        if (checked) {
            mSelectorImage.setVisibility(View.VISIBLE);
        } else {
            mSelectorImage.setVisibility(View.GONE);
        }
    }

    /**
     * Get color of this {@link ColorPickerCircle}
     *
     * @return
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Check if this circle is checked.
     *
     * @return
     */
    public boolean isChecked() {
        return mChecked;
    }
}
