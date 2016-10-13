package com.orthopg.snaphy.orthopg.Fragment.HelpFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Ravi-Gupta on 10/3/2016.
 */
public class HelpFragmentTabLayoutAdapter extends FragmentStatePagerAdapter {

    public HelpFragmentTabLayoutAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : return new CaseHelpFragment();
            case 1 : return new BooksHelpFragment();
            case 2 : return new NewsHelpFragment();
            default: return new CaseHelpFragment();

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
