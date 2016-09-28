package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.MenuFragment;
import com.orthopg.snaphy.orthopg.Fragment.PostedCasesFragment.PostedCasesFragment;
import com.orthopg.snaphy.orthopg.Fragment.SavedCasesFragment.SavedCasesFragment;

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
            case 0 : return new SavedCasesFragment();
            case 1 : return new PostedCasesFragment();
            case 2 : return new MenuFragment();
            default: return new SavedCasesFragment();

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
