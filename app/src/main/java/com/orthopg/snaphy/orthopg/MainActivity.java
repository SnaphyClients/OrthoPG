package com.orthopg.snaphy.orthopg;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BooksFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseFragment;
import com.orthopg.snaphy.orthopg.Fragment.LoginFragment.LoginFragment;
import com.orthopg.snaphy.orthopg.Fragment.MCIVerificationFragment.MCIVerificationFragment;
import com.orthopg.snaphy.orthopg.Fragment.MainFragment.MainFragment;
import com.orthopg.snaphy.orthopg.Fragment.NewsFragment.NewsFragment;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;
import com.orthopg.snaphy.orthopg.Interface.OnFragmentChange;

public class MainActivity extends AppCompatActivity implements OnFragmentChange, LoginFragment.OnFragmentInteractionListener,
        MCIVerificationFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        BooksFragment.OnFragmentInteractionListener, CaseFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void replaceFragment(int id, Object object) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
