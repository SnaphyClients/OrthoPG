package com.orthopg.snaphy.orthopg.Fragment.MainFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BooksFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseFragment;
import com.orthopg.snaphy.orthopg.Fragment.NewsFragment.NewsFragment;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;

/**
 * Created by Ravi-Gupta on 9/16/2016.
 */
public class MainFragmentTabLayoutAdapter extends FragmentStatePagerAdapter {

    public MainFragmentTabLayoutAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : return new CaseFragment();
            case 1 : return new BooksFragment();
            case 2 : return new NewsFragment();
            case 3 : return new ProfileFragment();
            default: return new CaseFragment();

        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
