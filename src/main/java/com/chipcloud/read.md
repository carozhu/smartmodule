### 说明
from com.github.fiskurgit:ChipCloud
modify:carozhu
结合com.google.android:flexbox使用

###使用参考

        FlexboxLayout flexbox = (FlexboxLayout) findViewById(R.id.flexbox);

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.single)//multi single
                .useInsetPadding(false)
                .setBooleanRaduis(false)
                .setBooleanCustomToggleChipLayout(false)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"));

        ChipCloud chipCloud = new ChipCloud(this, flexbox, config);
        chipCloud.addChip("HelloWorld!",R.layout.debug_toggle_chip);

        String[] demoArray = getResources().getStringArray(R.array.demo_array);
        chipCloud.addChips(demoArray,0);//R.layout.debug_toggle_chip

        //chipCloud.setChecked(2);

        String label = chipCloud.getLabel(2);
        Log.d(TAG, "Label at index 2: " + label);

        chipCloud.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int index, boolean checked, boolean userClick) {
                if (userClick) {
                    Log.d(TAG, String.format("chipCheckedChange Label at index: %d checked: %s", index, checked));
                }
            }
        });

        //Horizontal Scroll
        LinearLayout horizontalScroll = (LinearLayout) findViewById(R.id.horizontal_layout);
        config.useInsetPadding = true;
        ChipCloud horizontalChipCloud = new ChipCloud(this, horizontalScroll, config);
        horizontalChipCloud.addChips(demoArray,0);

###布局参考
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:paddingBottom="20dp"
        android:text="@string/standard_cloud"
        android:textSize="16sp" />

    <com.google.android.flexbox.FlexboxLayout
        android:id="@+id/flexbox"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:alignContent="space_around"
        app:alignItems="flex_start"
        app:dividerDrawable="@drawable/div"
        app:flexWrap="wrap"
        app:showDivider="middle" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:paddingTop="20dp"
        android:text="@string/horizontalscrollview_example"
        android:textSize="16sp" />

    <HorizontalScrollView
        android:id="@+id/horizontal_scroll"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:overScrollMode="never"
        android:paddingTop="20dp"
        android:scrollbars="none">

        <LinearLayout
            android:id="@+id/horizontal_layout"
            android:layout_width="match_parent"
            android:layout_gravity="center"
            android:gravity="center"
            android:layout_height="wrap_content"
            android:orientation="horizontal" />
    </HorizontalScrollView>