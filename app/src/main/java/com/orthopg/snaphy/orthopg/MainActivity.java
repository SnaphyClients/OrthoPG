package com.orthopg.snaphy.orthopg;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BooksFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment.CaseDetailFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseFragment;
import com.orthopg.snaphy.orthopg.Fragment.LoginFragment.LoginFragment;
import com.orthopg.snaphy.orthopg.Fragment.MCIVerificationFragment.MCIVerificationFragment;
import com.orthopg.snaphy.orthopg.Fragment.MainFragment.MainFragment;
import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.AboutUsFragment;
import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.ContactUsFragment;
import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.FAQsFragment;
import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.MenuFragment;
import com.orthopg.snaphy.orthopg.Fragment.MenuFragment.TermsAndConditionsFragment;
import com.orthopg.snaphy.orthopg.Fragment.NewCase.CaseDescriptionFragment;
import com.orthopg.snaphy.orthopg.Fragment.NewCase.CaseHeadingFragment;
import com.orthopg.snaphy.orthopg.Fragment.NewCase.CaseUploadImageFragment;
import com.orthopg.snaphy.orthopg.Fragment.NewsFragment.NewsFragment;
import com.orthopg.snaphy.orthopg.Fragment.PostedCasesFragment.PostedCasesFragment;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;
import com.orthopg.snaphy.orthopg.Fragment.SavedCasesFragment.SavedCasesFragment;
import com.orthopg.snaphy.orthopg.Interface.OnFragmentChange;

public class MainActivity extends AppCompatActivity implements OnFragmentChange, LoginFragment.OnFragmentInteractionListener,
        MCIVerificationFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        BooksFragment.OnFragmentInteractionListener, CaseFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        SavedCasesFragment.OnFragmentInteractionListener, PostedCasesFragment.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener, AboutUsFragment.OnFragmentInteractionListener,
        FAQsFragment.OnFragmentInteractionListener, TermsAndConditionsFragment.OnFragmentInteractionListener,
        ContactUsFragment.OnFragmentInteractionListener, CaseHeadingFragment.OnFragmentInteractionListener,
        CaseDescriptionFragment.OnFragmentInteractionListener, CaseUploadImageFragment.OnFragmentInteractionListener,
        CaseDetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(R.layout.fragment_main, null);
    }

    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.layout.fragment_main:
                loadMainFragment(fragmentTransaction);
                break;

            case R.id.fragment_menu_button1:
                openAboutUsFragment(fragmentTransaction);
                break;

            case R.id.fragment_menu_button2:
                openContactUsFragment(fragmentTransaction);
                break;

            case R.id.fragment_menu_button3:
                openFAQsFragment(fragmentTransaction);
                break;

            case R.id.fragment_menu_button4:
                openTermsAndConditionsFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_button4:
                openCaseHeadingFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_heading_button1:
                openImageUploadFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_upload_image_button1:
                openCaseDescriptionFragment(fragmentTransaction);
                break;

            case R.id.layout_case_list_textview4:
                openCaseDetailFragment(fragmentTransaction);
                break;
        }
    }

    /**
     *  Main Fragment is open from here
     * @param fragmentTransaction {FragmentTransaction}
     */
    private void loadMainFragment(FragmentTransaction fragmentTransaction) {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().
                findFragmentByTag(MainFragment.TAG);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, mainFragment, MainFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openAboutUsFragment(FragmentTransaction fragmentTransaction){
        AboutUsFragment aboutUsFragment = (AboutUsFragment) getSupportFragmentManager().
                findFragmentByTag(AboutUsFragment.TAG);
        if (aboutUsFragment == null) {
            aboutUsFragment = AboutUsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, aboutUsFragment, AboutUsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openContactUsFragment(FragmentTransaction fragmentTransaction){
        ContactUsFragment contactUsFragment = (ContactUsFragment) getSupportFragmentManager().
                findFragmentByTag(ContactUsFragment.TAG);
        if (contactUsFragment == null) {
            contactUsFragment = ContactUsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, contactUsFragment, ContactUsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openFAQsFragment(FragmentTransaction fragmentTransaction){
        FAQsFragment faQsFragment = (FAQsFragment) getSupportFragmentManager().
                findFragmentByTag(FAQsFragment.TAG);
        if (faQsFragment == null) {
            faQsFragment = FAQsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, faQsFragment, FAQsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openTermsAndConditionsFragment(FragmentTransaction fragmentTransaction){
        TermsAndConditionsFragment faQsFragment = (TermsAndConditionsFragment) getSupportFragmentManager().
                findFragmentByTag(TermsAndConditionsFragment.TAG);
        if (faQsFragment == null) {
            faQsFragment = TermsAndConditionsFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, faQsFragment, TermsAndConditionsFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openCaseHeadingFragment(FragmentTransaction fragmentTransaction) {
        CaseHeadingFragment caseHeadingFragment = (CaseHeadingFragment) getSupportFragmentManager().
                findFragmentByTag(TermsAndConditionsFragment.TAG);
        if (caseHeadingFragment == null) {
            caseHeadingFragment = CaseHeadingFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, caseHeadingFragment, CaseHeadingFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openImageUploadFragment(FragmentTransaction fragmentTransaction) {
        CaseUploadImageFragment caseUploadImageFragment = (CaseUploadImageFragment) getSupportFragmentManager().
                findFragmentByTag(CaseUploadImageFragment.TAG);
        if (caseUploadImageFragment == null) {
            caseUploadImageFragment = CaseUploadImageFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, caseUploadImageFragment, CaseUploadImageFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openCaseDescriptionFragment(FragmentTransaction fragmentTransaction) {
        CaseDescriptionFragment caseDescriptionFragment = (CaseDescriptionFragment) getSupportFragmentManager().
                findFragmentByTag(CaseDescriptionFragment.TAG);
        if (caseDescriptionFragment == null) {
            caseDescriptionFragment = CaseDescriptionFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, caseDescriptionFragment, CaseDescriptionFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openCaseDetailFragment(FragmentTransaction fragmentTransaction) {
        CaseDetailFragment caseDetailFragment = (CaseDetailFragment) getSupportFragmentManager().
                findFragmentByTag(CaseDetailFragment.TAG);
        if (caseDetailFragment == null) {
            caseDetailFragment = CaseDetailFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, caseDetailFragment, CaseDetailFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }

    }

}
