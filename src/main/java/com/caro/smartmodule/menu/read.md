来源:https://github.com/uncleleonfan/FloatingActionMenu
因作者未提供compile.

自己定制优化了一些东西

usage:
<com.caro.smartmodule.menu.FloatingActionMenu
        android:id="@+id/fab_menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        app:layout_behavior="com.caro.smartmodule.menu.ScrollBehavior">

        <android.support.design.widget.FloatingActionButton
            android:id="@+id/fab_camera"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@android:drawable/ic_menu_camera"
            android:layout_gravity="center"
            app:backgroundTint="@android:color/white"
            app:fabSize="mini" />

        <android.support.design.widget.FloatingActionButton
            android:id="@+id/fab_gallery"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:src="@android:drawable/ic_menu_edit"
            app:backgroundTint="@android:color/white"
            app:fabSize="mini" />

        <android.support.design.widget.FloatingActionButton
            android:id="@+id/fab_add"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:layout_gravity="center"
            android:layout_margin="@dimen/fab_margin"
            android:src="@drawable/ic_add_white"
            app:backgroundTint="@color/colorPrimary" />
    </com.leon.floatingactionmenu.widget.FloatingActionMenu>