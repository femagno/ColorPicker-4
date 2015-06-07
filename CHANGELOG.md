Change Log
==========

## [1.2.0](https://github.com/sandrolutz/ColorPicker/tree/1.2.0) (2015-06-07)

**Implemented enhancements:**

- Add style option ```circleCheckmark``` to ColorPickerPalette.
- Add style option ```checkmark``` to ColorCircle.
- Replaced some deprecated method calls.

## [1.1.3](https://github.com/sandrolutz/ColorPicker/tree/1.1.3) (2015-06-03)

**Implemented enhancements:**

- Add style option ```maxColumns``` to ColorPickerPalette.
- Rename style options to ```circleSize``` and ```spacing```.
- Renamed class ColorPickerCircle to ColorCircle
- Add Constructors used by Android tools to ColorCircle.
- Add style options for ColorCircle

## [1.1.2](https://github.com/sandrolutz/ColorPicker/tree/1.1.2) (2015-06-02)

**Implemented enhancements:**

- Add method ```getSelectedColor()``` to ColorPickerPalette.


## [1.1.1](https://github.com/sandrolutz/ColorPicker/tree/1.1.1) (2015-06-02)

**Implemented enhancements:**

- Add method ```clearSelection()``` to ColorPickerPalette.

**Bugfixes:**

- Add null check to ```onSaveInstanceState()``` to cover situation when no color is selected.

## [1.1.0](https://github.com/sandrolutz/ColorPicker/tree/1.1.0) (2015-06-02)

**Implemented enhancements:**

- Remove option 'circle_margin'
- Add style option 'circle_spacing' replacing 'circle_margin'. It represents the total space between the circles now.
- Add style option 'gravity'. Possible values are 'left', 'center' and 'right'
- Implement SavedState to prevent losing selection when screen orientation changes

## [1.0.0](https://github.com/sandrolutz/ColorPicker/tree/1.0.0) (2015-06-01)