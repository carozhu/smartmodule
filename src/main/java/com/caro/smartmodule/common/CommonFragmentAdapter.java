package com.caro.smartmodule.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by caro on 16/1/27.
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArray;
    public CommonFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArray) {
        this(fm);
        this.fragmentArray = fragmentArray;
    }
    public CommonFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int arg0) {
        return this.fragmentArray.get(arg0);
    }

    @Override
    public int getCount() {
        return this.fragmentArray.size();
    }
}
