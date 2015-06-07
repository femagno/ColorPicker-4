ColorPicker
===========

This is a simple color picker to select a color from a palette of colors.

## Download

gradle:

```groovy
compile 'ch.temparus.colorpicker:library:1.2.0'
```

Maven:
```xml
<dependency>
  <groupId>ch.temparus.colorpicker</groupId>
  <artifactId>library</artifactId>
  <version>1.2.0</version>
  <type>aar</type>
</dependency>
```

## Usage

In the root node, you have to put this:
```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

Use the ColorPickerPalette view as follows:

```xml
<ch.temparus.colorpicker.ColorPickerPalette
        android:id="@+id/colorpicker"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        app:colors="@array/sampleColors"
        app:size="Small" />
```

You have the following options to configure the view in the layout file:
```code
app:size             'Small' or 'Large' (default: 'Small')
app:gravity          'left', 'center' or 'right' (default: 'left')
app:colors           reference to integer-array containing color values
app:circleSize       diameter of circle (default: values provided by 'size' option)
app:spacing          space between circles (default: values provided by 'size' option)
app:circleCheckmark  drawable to visualize checked state (default: provided by this library)
```

## Change Log

See [here](https://github.com/sandrolutz/ColorPicker/blob/master/CHANGELOG.md).

## License

    Copyright 2015 Sandro Lutz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
