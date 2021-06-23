/**
 * @author seek 951882080@qq.com
 * @version V1.0
 */

package com.hkzr.wlwd.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.hkzr.wlwd.ui.utils.Log;

import java.util.List;


public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;


    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
            Log.d("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }
}
