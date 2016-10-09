package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.MenuFragment;

/**
 * Created by Ravi-Gupta on 9/26/2016.
 */
public class ProfileFragmentTabLayoutAdapter extends FragmentStatePagerAdapter {

    public ProfileFragmentTabLayoutAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0 : return new MenuFragment();
            default: return new MenuFragment();

        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
