package ch.temparus.colorpicker.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.temparus.colorpicker.ColorPickerPalette;


public class MainActivity extends ActionBarActivity {

    ColorPickerPalette mColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorPicker = (ColorPickerPalette) findViewById(R.id.colorpicker);
        mColorPicker.setOnColorSelectedListener(new ColorPickerPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                Toast.makeText(MainActivity.this, R.string.color_selected, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_select_red) {
            mColorPicker.selectColor(getResources().getColor(R.color.red));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
