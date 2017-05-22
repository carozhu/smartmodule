package com.chipcloud;

import android.graphics.PorterDuff;

class ConfigHelper {

  static void initialise(ToggleChip toggleChip, ChipCloudConfig config){
    if(config != null){
        toggleChip.getBackground().setColorFilter(config.uncheckedChipColor, PorterDuff.Mode.SRC);
        toggleChip.setTextColor(config.uncheckedTextColor);
      if(config.typeface != null){
        toggleChip.setTypeface(config.typeface);
      }
    }
  }

  static void update(ToggleChip toggleChip, ChipCloudConfig config){
    if(config != null) {
      if (toggleChip.isChecked()) {
          toggleChip.getBackground().setColorFilter(config.checkedChipColor, PorterDuff.Mode.SRC);
          toggleChip.setTextColor(config.checkedTextColor);
      } else {
          toggleChip.getBackground().setColorFilter(config.uncheckedChipColor, PorterDuff.Mode.SRC);
          toggleChip.setTextColor(config.uncheckedTextColor);
      }
    }
  }
}
