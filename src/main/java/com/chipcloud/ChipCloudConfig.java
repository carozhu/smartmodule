package com.chipcloud;

import android.graphics.Typeface;

public class ChipCloudConfig {

  public Typeface typeface = null;
  public int checkedChipColor = -1;
  public int uncheckedChipColor = -1;
  public int checkedTextColor = -1;
  public int uncheckedTextColor = -1;
  public ChipCloud.SelectMode selectMode = ChipCloud.SelectMode.multi;
  public boolean useInsetPadding = false;

  public boolean isCustomToggleChipLayout() {
    return customToggleChipLayout;
  }

  public ChipCloudConfig setBooleanCustomToggleChipLayout(boolean customToggleChipLayout) {
    this.customToggleChipLayout = customToggleChipLayout;
    return this;
  }

  /*if you custom a ToggleChip layout,dont set update to check status*/
  public boolean customToggleChipLayout = false;

  public boolean isBooleanRaduis() {
    return booleanRaduis;
  }

  public ChipCloudConfig setBooleanRaduis(boolean booleanRaduis) {
    this.booleanRaduis = booleanRaduis;
    return this;
  }

  public boolean booleanRaduis = false;

  public ChipCloudConfig(){
  }

  public ChipCloudConfig typeface(Typeface typeface){
    this.typeface = typeface;
    return this;
  }

  public ChipCloudConfig checkedChipColor(int checkedChipColor){
    this.checkedChipColor = checkedChipColor;
    return this;
  }

  public ChipCloudConfig uncheckedChipColor(int uncheckedChipColor){
    this.uncheckedChipColor = uncheckedChipColor;
    return this;
  }

  public ChipCloudConfig checkedTextColor(int checkedTextColor){
    this.checkedTextColor = checkedTextColor;
    return this;
  }

  public ChipCloudConfig uncheckedTextColor(int uncheckedTextColor){
    this.uncheckedTextColor = uncheckedTextColor;
    return this;
  }

  public ChipCloudConfig selectMode(ChipCloud.SelectMode selectMode){
    this.selectMode = selectMode;
    return this;
  }

  public ChipCloudConfig useInsetPadding(boolean useInsetPadding){
    this.useInsetPadding = useInsetPadding;
    return this;
  }
}
