package com.orthopg.snaphy.orthopg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.News;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PaymentRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostRepository;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.CustomModel.NewCase;
import com.orthopg.snaphy.orthopg.CustomModel.TrackImage;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BookDescriptionFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BookTestFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BooksFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.CheckoutFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.FailureFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.SuccessFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.ViewAllBooksFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment.CaseDetailFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment.PostAnswerFragment;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseFragment;
import com.orthopg.snaphy.orthopg.Fragment.HelpFragment.BooksHelpFragment;
import com.orthopg.snaphy.orthopg.Fragment.HelpFragment.CaseHelpFragment;
import com.orthopg.snaphy.orthopg.Fragment.HelpFragment.HelpFragment;
import com.orthopg.snaphy.orthopg.Fragment.HelpFragment.NewsHelpFragment;
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
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.EditProfileFragment;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.OtherProfileFragment;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;

import com.orthopg.snaphy.orthopg.Interface.OnFragmentChange;
import com.orthopg.snaphy.orthopg.OrderHistoryFragment.OrderHistoryFragment;
import com.orthopg.snaphy.orthopg.PushNotification.RegistrationIntentService;
import com.orthopg.snaphy.orthopg.QualificationFragment.QualificationFragment;
import com.orthopg.snaphy.orthopg.SpecialityFragment.SpecialityFragment;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.AccessTokenRepository;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.remoting.JsonUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.fabric.sdk.android.Fabric;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class MainActivity extends AppCompatActivity implements OnFragmentChange, LoginFragment.OnFragmentInteractionListener,
        MCIVerificationFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        BookTestFragment.OnFragmentInteractionListener, CaseFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener, AboutUsFragment.OnFragmentInteractionListener,
        FAQsFragment.OnFragmentInteractionListener, TermsAndConditionsFragment.OnFragmentInteractionListener,
        ContactUsFragment.OnFragmentInteractionListener, CaseHeadingFragment.OnFragmentInteractionListener,
        CaseDescriptionFragment.OnFragmentInteractionListener, CaseUploadImageFragment.OnFragmentInteractionListener,
        CaseDetailFragment.OnFragmentInteractionListener, PostAnswerFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener, CaseHelpFragment.OnFragmentInteractionListener,
        BooksHelpFragment.OnFragmentInteractionListener, NewsHelpFragment.OnFragmentInteractionListener,
        ViewAllBooksFragment.OnFragmentInteractionListener, BookDescriptionFragment.OnFragmentInteractionListener,
        OtherProfileFragment.OnFragmentInteractionListener, SpecialityFragment.OnFragmentInteractionListener,
        QualificationFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        OrderHistoryFragment.OnFragmentInteractionListener {

    RestAdapter restAdapter;
    Context context;
    GoogleCloudMessaging gcm;
    public static LocalInstallation installation;
    public SnaphyHelper snaphyHelper;
    SharedPreferences sharedPreferences;
    public CircleProgressBar progressBar;
    TextView notConnectedText;
    Button retryButton;
    MainActivity mainActivity;
    String paymentIdNumber;
    boolean isBranchIO = false;
    private static final int RC_SIGN_IN = 0;
    public Uri globalUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        ButterKnife.bind(this);
        progressBar = (CircleProgressBar) findViewById(R.id.activity_main_progressBar);
        notConnectedText = (TextView) findViewById(R.id.activity_main_textview1);
        retryButton = (Button) findViewById(R.id.activity_main_button1);
        context = getApplicationContext();
        mainActivity = this;
        Presenter.getInstance().addModel(Constants.MAINACTIVITY_INSTANCE, mainActivity);
        snaphyHelper = new SnaphyHelper(this);
        resetVariables();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check device for Play Services APK.
                snaphyHelper.checkPlayServices();
            }
        }, 100);


        //Initialize book and news..
        initializeBookAndNews();
        sharedPreferences = this.getApplicationContext().getSharedPreferences("HelpScreen", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(snaphyHelper.isNetworkAvailable()) {
            onAppStart();
            // Internet is connected
            boolean displayHelpScreen = sharedPreferences.getBoolean("showHelpScreen", false);
            if(!displayHelpScreen) {
                editor.putBoolean("showHelpScreen", true);
                editor.commit();
                replaceFragment(R.layout.fragment_help, null);
            } else {
                //Check Login
                checkLogin();
                stopProgressBar(progressBar);



            }
        } else {
            // No Internet is connected
            //Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
            checkLogin();
            /*TastyToast.makeText(getApplicationContext(), "Connection Error! Check your network", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            hideRetryButton(false);*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void onAppStart() {
        Uri data = getIntent().getData();
        if(data != null){
            try {
                String link_click_id = (String)data.getQueryParameter("link_click_id");
                isBranchIO = true;
                // params are the deep linked params associated with the link that the user clicked before showing up
                if(link_click_id != null){
                    if(!link_click_id.isEmpty()){
                        //Start branch..
                        //SHOW LOADING BAR..
                        startProgressBar(progressBar);
                        Branch branch = Branch.getInstance();
                        branch.initSession(new Branch.BranchReferralInitListener(){

                            @Override
                            public void onInitFinished(JSONObject referringParams, BranchError error) {
                                //CLOSE LOADING BAR..
                                stopProgressBar(progressBar);
                                if (error == null) {
                                    try {

                                        /*String url = referringParams.getString(Constants.BRANCH_IO_URL_PROPERTY);*/
                                        String params = referringParams.toString();
                                        /*String id = referringParams.get("id").toString();*/
                                        // params are the deep linked params associated with the link that the user clicked before showing up
                                        if(params != null){
                                            Presenter.getInstance().addModel(Constants.BRANCH_IO_INSTANCE, referringParams);
                                        }
                                        isBranchIO = false;
                                        startApp();
                                    } catch (Exception e) {
                                        Log.e(Constants.TAG, e.toString());
                                        Log.e(Constants.TAG, "Error occured parsing json from branch.io data in smartShoppr");
                                        startApp();
                                    }

                                }else{
                                    Log.e(Constants.TAG, "Error occured in branch.io" + error.toString());
                                    startApp();
                                }
                            }
                        }, this.getIntent().getData(), this);
                    }else{
                        startApp();
                    }
                }else{
                    startApp();
                }

            } catch (Exception e) {
                Log.e(Constants.TAG, e.toString());
                Log.e(Constants.TAG, "Error occured parsing json from branch.io data in smartShoppr");
                startApp();
            }
        }else{
            startApp();
        }
    }

    public void startApp() {
        checkLogin();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri data = getIntent().getData();
        String link_click_id = null;
        if(data != null) {
            link_click_id = (String) data.getQueryParameter("link_click_id");
        }
        if(intent.getStringExtra("event") != null){
            //Show push message data..
            parsePushMessage(intent);
        } else {
            replaceFragment(R.layout.fragment_main, null);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(Constants.TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }


    private void resetVariables(){
        Presenter.getInstance().removeModelFromList(Constants.GOOGLE_ACCESS_TOKEN);
        Presenter.getInstance().removeModelFromList(Constants.MCI_NUMBER);
    }

    public void parsePushMessage(){
        String event = getIntent().getStringExtra("event");
        String id = getIntent().getStringExtra("id");
        if(event != null) {
            if (event.equals("newsRelease")) {
                Presenter.getInstance().addModel(Constants.VIEW_PAGER_ID, 2);
                replaceFragment(R.layout.fragment_main, null);
            } else if(event.equals("bookRelease")) {
                Presenter.getInstance().addModel(Constants.VIEW_PAGER_ID, 1);
                replaceFragment(R.layout.fragment_main, null);
            } else if(event.equals("comment")) {
                replaceFragment(R.layout.fragment_main, null);
                fetchCaseFromId(id);
            } else if(event.equals("like")) {
                replaceFragment(R.layout.fragment_main, null);
                fetchCaseFromId(id);
            } else if(event.equals("save")) {
                replaceFragment(R.layout.fragment_main, null);
                fetchCaseFromId(id);
            } else {
                //MOVE TO HOME..
                showHomeFragment();
            }
        } else {
            //MOVE TO HOME
            showHomeFragment();
        }
    }

    public void parsePushMessage(Intent intent){
        String event = intent.getStringExtra("event");
        String id = intent.getStringExtra("id");
        if(event != null) {
            if (event.equals("newsRelease")) {
                Presenter.getInstance().addModel(Constants.VIEW_PAGER_ID, 2);
                replaceFragment(R.layout.fragment_main, null);
            } else if(event.equals("bookRelease")) {
                Presenter.getInstance().addModel(Constants.VIEW_PAGER_ID, 1);
                replaceFragment(R.layout.fragment_main, null);
            } else if(event.equals("comment")) {
                replaceFragment(R.layout.fragment_main, null);
                fetchCaseFromId(id);
            } else if(event.equals("like")) {
                replaceFragment(R.layout.fragment_main, null);
                fetchCaseFromId(id);
            } else if(event.equals("save")) {
                replaceFragment(R.layout.fragment_main, null);
                fetchCaseFromId(id);
            } else {
                //MOVE TO HOME..
                showHomeFragment();
            }
        } else {
            //MOVE TO HOME
            showHomeFragment();
        }
    }


    public void fetchBookFromId(String id){
        BookRepository bookRepository = snaphyHelper.getLoopBackAdapter().createRepository(BookRepository.class);
        HashMap<String, Object> filter = new HashMap<>();
        bookRepository.findById(id, filter, new ObjectCallback<Book>() {
            @Override
            public void onBefore() {
                super.onBefore();
                startProgressBar(progressBar);
            }

            @Override
            public void onSuccess(Book object) {
                super.onSuccess(object);
                if(object != null) {
                    Presenter.getInstance().addModel(Constants.BOOK_DESCRIPTION_ID,object);
                    mainActivity.replaceFragment(R.layout.fragment_book_description, null);
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
                TastyToast.makeText(getApplicationContext(), Constants.NETWORK_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFinally() {
                super.onFinally();
                stopProgressBar(progressBar);
            }
        });
    }


    public void fetchCaseFromId(String id) {
        PostRepository postRepository = snaphyHelper.getLoopBackAdapter().createRepository(PostRepository.class);
        postRepository.fetchPostById(id, new ObjectCallback<Post>() {
            @Override
            public void onBefore() {
                super.onBefore();
                startProgressBar(progressBar);
            }

            @Override
            public void onSuccess(Post object) {
                super.onSuccess(object);
                if(object != null) {
                    if(object.getPostDetails() != null) {
                        object.getPostDetails().addRelation(object);
                    }

                    Presenter.getInstance().addModel(Constants.POST_PUSH_EVENT_DATA, object);
                    replaceFragment(R.layout.fragment_case_detail, null);
                }else{
                    //MOVE TO HOME..
                }

            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
                TastyToast.makeText(getApplicationContext(), Constants.NETWORK_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFinally() {
                super.onFinally();
                stopProgressBar(progressBar);
            }
        });

    }


    public void initializeBookAndNews(){
        DataList<News> newsDataList = new DataList<>();
        Presenter.getInstance().addList(Constants.NEWS_LIST_NEWS_FRAGMENT, newsDataList);

        DataList<News> bookDataList = new DataList<>();
        Presenter.getInstance().addList(Constants.BOOK_LIST_BOOKS_FRAGMENT, bookDataList);
    }


    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.layout.fragment_main:
                loadMainFragment(fragmentTransaction);
                break;

            case R.layout.fragment_help:
                loadHelpFragment(fragmentTransaction);
                break;

            case R.layout.fragment_login:
                openLoginFragment(fragmentTransaction);
                break;

            case R.layout.fragment_mciverification:
                openMCIVerifcationFragment(fragmentTransaction);
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

            case R.id.fragment_menu_button6:
                openTermsAndConditionsFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_button4:
                openImageUploadFragment(fragmentTransaction);
                //openCaseHeadingFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_heading_button1:
                openCaseDescriptionFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_upload_image_button1:
                openCaseHeadingFragment(fragmentTransaction);
                //openCaseDescriptionFragment(fragmentTransaction);
                break;

            case R.id.layout_case_list_textview4:
                openCaseDetailFragment(fragmentTransaction, object);
                break;


            case R.id.fragment_case_detail_button4:
                openPostAnswerFragment(fragmentTransaction, object);
                break;

            case R.id.layout_case_list_linear_layout_my_answer:
                openPostAnswerFragmentForHome(fragmentTransaction, object);
                break;

            case R.layout.fragment_case_detail:
                openCaseDetailFragmentAsParent(fragmentTransaction);
                break;

            case R.layout.fragment_view_all_books:
                openViewAllBooksFragment(fragmentTransaction);
                break;

            case R.layout.fragment_book_description:
                openFragmentBookDescription(fragmentTransaction);
                break;

            case R.layout.fragment_doctor_profile:
                openOtherProfileFragment(fragmentTransaction, object);
                break;

            case R.layout.fragment_speciality:
                openSpecialityFragment(fragmentTransaction);
                break;

            case R.layout.fragment_qualification:
                openQualificationFragment(fragmentTransaction);
                break;

            case R.layout.fragment_edit_profile:
                openEditProfileFragment(fragmentTransaction, object);
                break;

            case R.layout.fragment_order_history:
                openOrderHistory(fragmentTransaction);
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

    private void openCaseDetailFragmentAsParent(FragmentTransaction fragmentTransaction) {
        CaseDetailFragment caseDetailFragment = (CaseDetailFragment) getSupportFragmentManager().
                findFragmentByTag(CaseDetailFragment.TAG);

        if (caseDetailFragment == null) {
            caseDetailFragment = CaseDetailFragment.newInstance();
        }

        Bundle bundle = new Bundle();
        int pos = -1;
        bundle.putInt("position", pos);
        caseDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, caseDetailFragment, CaseDetailFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void openLoginFragment(FragmentTransaction fragmentTransaction) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().
                findFragmentByTag(LoginFragment.TAG);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, loginFragment, LoginFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void loadHelpFragment(FragmentTransaction fragmentTransaction) {
        HelpFragment helpFragment = (HelpFragment) getSupportFragmentManager().
                findFragmentByTag(HelpFragment.TAG);
        if (helpFragment == null) {
            helpFragment = HelpFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, helpFragment, HelpFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openMCIVerifcationFragment(FragmentTransaction fragmentTransaction) {
        MCIVerificationFragment mciVerificationFragment = (MCIVerificationFragment) getSupportFragmentManager().
                findFragmentByTag(MCIVerificationFragment.TAG);
        if (mciVerificationFragment == null) {
            mciVerificationFragment = MCIVerificationFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.container, mciVerificationFragment, MCIVerificationFragment.TAG);
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

    private void openCaseDetailFragment(FragmentTransaction fragmentTransaction, Object position) {
        CaseDetailFragment caseDetailFragment = (CaseDetailFragment) getSupportFragmentManager().
                findFragmentByTag(CaseDetailFragment.TAG);
        if (caseDetailFragment == null) {
            caseDetailFragment = CaseDetailFragment.newInstance();
        }

        Bundle bundle = new Bundle();
        int pos = (int) position;
        bundle.putInt("position", pos);
        caseDetailFragment.setArguments(bundle);


        fragmentTransaction.replace(R.id.main_container, caseDetailFragment, CaseDetailFragment.TAG).addToBackStack(CaseDetailFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openPostAnswerFragmentForHome(FragmentTransaction fragmentTransaction, Object position){

        PostAnswerFragment postAnswerFragment = (PostAnswerFragment) getSupportFragmentManager().
                findFragmentByTag(PostAnswerFragment.TAG);
        String TAG = String.valueOf(position);
        if (postAnswerFragment == null) {
            postAnswerFragment = PostAnswerFragment.newInstance(TAG);
        }
        Bundle bundle = new Bundle();
        int pos = (int) position;
        bundle.putInt("position", pos);
        postAnswerFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_container, postAnswerFragment, PostAnswerFragment.TAG).addToBackStack(CaseDetailFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openPostAnswerFragment(FragmentTransaction fragmentTransaction, Object object) {
        PostAnswerFragment postAnswerFragment = (PostAnswerFragment) getSupportFragmentManager().
                findFragmentByTag(PostAnswerFragment.TAG);
        String TAG = (String)object;
        if (postAnswerFragment == null) {
            postAnswerFragment = PostAnswerFragment.newInstance(TAG);
        }
        fragmentTransaction.replace(R.id.main_container, postAnswerFragment, PostAnswerFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openViewAllBooksFragment(FragmentTransaction fragmentTransaction){
        ViewAllBooksFragment viewAllBooksFragment = (ViewAllBooksFragment) getSupportFragmentManager().
                findFragmentByTag(ViewAllBooksFragment.TAG);
        if(viewAllBooksFragment == null){
            viewAllBooksFragment = ViewAllBooksFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container,viewAllBooksFragment, ViewAllBooksFragment.TAG).addToBackStack(ViewAllBooksFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openFragmentBookDescription(FragmentTransaction fragmentTransaction){
        BookDescriptionFragment bookDescriptionFragment = (BookDescriptionFragment) getSupportFragmentManager().
                findFragmentByTag(BookDescriptionFragment.TAG);
        if(bookDescriptionFragment == null){
            bookDescriptionFragment = BookDescriptionFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container,bookDescriptionFragment,BookDescriptionFragment.TAG).addToBackStack(BookDescriptionFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openOtherProfileFragment(FragmentTransaction fragmentTransaction, Object object){
        OtherProfileFragment otherProfileFragment = (OtherProfileFragment)getSupportFragmentManager().findFragmentByTag(OtherProfileFragment.TAG);
        String TAG = (String)object;
        if(otherProfileFragment==null){
            otherProfileFragment = OtherProfileFragment.newInstance(TAG);
        }
        fragmentTransaction.replace(R.id.main_container,otherProfileFragment, OtherProfileFragment.TAG).addToBackStack(OtherProfileFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openSpecialityFragment(FragmentTransaction fragmentTransaction){
        SpecialityFragment specialityFragment = (SpecialityFragment)getSupportFragmentManager().findFragmentByTag(SpecialityFragment.TAG);
        if(specialityFragment==null){
            specialityFragment = SpecialityFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, specialityFragment, SpecialityFragment.TAG).addToBackStack(SpecialityFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openQualificationFragment(FragmentTransaction fragmentTransaction){
        QualificationFragment qualificationFragment = (QualificationFragment)getSupportFragmentManager().findFragmentByTag(QualificationFragment.TAG);
        if(qualificationFragment == null){
            qualificationFragment = QualificationFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, qualificationFragment, QualificationFragment.TAG).addToBackStack(QualificationFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openEditProfileFragment(FragmentTransaction fragmentTransaction, Object object){
        EditProfileFragment editProfileFragment = (EditProfileFragment)getSupportFragmentManager().findFragmentByTag(EditProfileFragment.TAG);
        String TAG = (String)object;
        if(editProfileFragment == null){
            editProfileFragment = EditProfileFragment.newInstance(TAG);
        }
        fragmentTransaction.replace(R.id.main_container, editProfileFragment, EditProfileFragment.TAG).addToBackStack(EditProfileFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openOrderHistory(FragmentTransaction fragmentTransaction){
        OrderHistoryFragment orderHistoryFragment = (OrderHistoryFragment)getSupportFragmentManager().findFragmentByTag(OrderHistoryFragment.TAG);
        if(orderHistoryFragment == null){
            orderHistoryFragment = OrderHistoryFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, orderHistoryFragment, OrderHistoryFragment.TAG).addToBackStack(OrderHistoryFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }





    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void startProgressBar(com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void stopProgressBar(com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }



    public void showSoftKeyboard(View view1) {
        // Show soft keyboard for the user to enter the value.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public String parseDate(String postedDate) {

        DateTime parsePostedDate;
        try {
            parsePostedDate = ISODateTimeFormat.dateTime().withZone(DateTimeZone.forID("Asia/Kolkata")).parseDateTime(postedDate);
        } catch(Exception e) {
            parsePostedDate = new DateTime(DateTimeZone.forID("Asia/Kolkata"));
        }
        Log.v(Constants.TAG, parsePostedDate.toString());


        DateTime currentDate = new DateTime(DateTimeZone.forID("Asia/Kolkata"));
        Log.v(Constants.TAG, currentDate.toString());
        long elapsedTime = currentDate.getMillis() - parsePostedDate.getMillis();
        String time = "";

        int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        int minutes = (int) (seconds/60);
        int hours = (int) (minutes/60);
        int days = (int) (hours/24);
        int months = (int) (days/30);
        int years = (int) (months/365);


        if(years == 0) {
            if(months == 0) {
                if(days == 0) {
                    if(hours == 0){
                        if(minutes == 0) {
                            if(seconds == 0) {
                                // DO NOTHING
                                time = 1 + " "+ Constants.SECOND;
                            } else {
                                time = seconds + " "+ Constants.SECONDS;
                            }
                        } else {
                            time = minutes + " "+ Constants.MINUTE;
                        }
                    } else {
                        time = hours + " "+ Constants.HOUR;
                    }
                } else {
                    time = days + " "+ Constants.DAY;
                }
            } else {
                time = months + " "+ Constants.MONTH;
            }
        } else {
            time = years + " "+ Constants.YEAR;
        }

        return time;
    }


    /*public String parseDate(String postedDate) {

        DateTime parsePostedDate = ISODateTimeFormat.dateTime().withZone(DateTimeZone.forID("Asia/Kolkata")).parseDateTime(postedDate);
        Log.v(Constants.TAG, parsePostedDate.toString());


        DateTime currentDate = new DateTime(DateTimeZone.forID("Asia/Kolkata"));
        Log.v(Constants.TAG, currentDate.toString());

        String time = applyRegexOnDate(parsePostedDate.toString(), currentDate.toString());
        return time;

    }*/

    public String applyRegexOnDate(String postedDate, String currentDate) {
        //Fri Oct 07 16:01:58 GMT+05:30 2016 JAVA

        /*******************************POSTED DATE*************************************/


        Log.v(Constants.TAG, postedDate);
        Log.v(Constants.TAG, currentDate);
        int postedDayOfMonth = Integer.parseInt(postedDate.toString().substring(8, 10));
        int postedMonth = Integer.parseInt((postedDate.toString().substring(5, 7)));
        int postedHour = Integer.parseInt(postedDate.toString().substring(11, 13));
        int postedMinute = Integer.parseInt(postedDate.toString().substring(14, 16));
        int postedSecond = Integer.parseInt(postedDate.toString().substring(17, 19));
        int postedYear = Integer.parseInt(postedDate.toString().substring(0, 4));

        /*******************************POSTED DATE*************************************/



        /*******************************CURRENT DATE*************************************/

        int currentDayOfMonth = Integer.parseInt(currentDate.toString().substring(8, 10));
        int currentMonth = Integer.parseInt(currentDate.toString().substring(5, 7));
        int currentHour = Integer.parseInt(currentDate.toString().substring(11, 13));
        int currentMinute = Integer.parseInt(currentDate.toString().substring(14, 16));
        int currentSecond = Integer.parseInt(currentDate.toString().substring(17, 19));
        int currentYear = Integer.parseInt(currentDate.toString().substring(0, 4));

        /*******************************CURRENT DATE*************************************/

        String time = getPostedTime(currentYear, currentMonth, currentDayOfMonth, currentHour, currentMinute, currentSecond,
                postedYear, postedMonth, postedDayOfMonth, postedHour, postedMinute,postedSecond);

        return time;
    }



    public String getPostedTime(int currentYear,
            int currentMonth,
            int currentDayOfMonth,
            int currentHour,
            int currentMinute,
            int currentSecond,

            int postedYear,
            int postedMonth,
            int postedDayOfMonth,
            int postedHour,
            int postedMinute,
            int postedSecond) {

        String time;

        if(currentYear == postedYear) {
            if(currentMonth == postedMonth) {
                if(currentDayOfMonth == postedDayOfMonth) {
                    if(currentHour == postedHour) {
                        if(currentMinute == postedMinute) {
                            if(currentSecond == postedSecond) {
                                time = 1 + Constants.SECOND;
                            } else {
                                if(formatTime(currentSecond - postedSecond)) {
                                    time = (currentSecond - postedSecond)+ " " + Constants.SECOND;
                                } else {
                                    time = (currentSecond - postedSecond)+ " " + Constants.SECONDS;
                                }
                            }
                        } else {
                            if(formatTime((currentMinute - postedMinute))) {
                                time = (currentMinute - postedMinute) +" "+ Constants.MINUTE;
                            } else {
                                time = (currentMinute - postedMinute) +" "+ Constants.MINUTES;
                            }
                        }
                    } else {
                        if(formatTime(currentHour - postedHour)) {
                            time = (currentHour - postedHour) +" "+ Constants.HOUR;
                        } else {
                            time = (currentHour - postedHour) +" "+ Constants.HOURS;
                        }
                    }
                } else {
                    if(formatTime(currentDayOfMonth - postedDayOfMonth)) {
                        time = (currentDayOfMonth - postedDayOfMonth) +" "+ Constants.DAY;
                    } else {
                        time = (currentDayOfMonth - postedDayOfMonth) +" "+ Constants.DAYS;
                    }
                }
            } else {
                if(formatTime(currentMonth - postedMonth)) {
                    time = (currentMonth - postedMonth) +" "+ Constants.MONTH;
                } else {
                    time = (currentMonth - postedMonth) +" "+ Constants.MONTHS;
                }
            }
        } else {
            if(formatTime(currentYear - postedYear)) {
                time = (currentYear - postedYear) +" "+ Constants.YEAR;
            } else {
                time = (currentYear - postedYear) +" "+ Constants.YEARS;
            }

        }

        return time;
    }

    public boolean formatTime(int time) {
        if(time == 1) {
            return true;
        } else {
            return false;
        }
    }


    public int convertMonthStringIntoNumber(String month) {
        DateTimeFormatter format = DateTimeFormat.forPattern("MMM");
        DateTime jodatime = format.parseDateTime(month);
        // Format for output

        int month_number = jodatime.getMonthOfYear();
        return month_number;

    }


    public void updateCustomer(Customer customer){
        Map<String, ? extends Object> data = customer.convertMap();
        //Remove the password field..
        data.remove("password");
        CustomerRepository customerRepository = snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.updateAttributes((String) customer.getId(), data, new ObjectCallback<Customer>() {
            @Override
            public void onBefore() {
                //TODO SHOW PROGRESS BAR..
                startProgressBar(progressBar);
            }

            @Override
            public void onSuccess(Customer object) {
                replaceFragment(R.layout.fragment_main, null);
                Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                if (customer != null) {
                    if (customer.getStatus() != null) {
                        if (customer.getStatus().equals(Constants.ALLOW)) {
                        } else {
                            TastyToast.makeText(getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
                Log.v(Constants.TAG, "Error in update Customer Method");
            }

            @Override
            public void onFinally() {
                stopProgressBar(progressBar);
            }
        });
    }






    /**
     * AddUser method for adding user once the user is successfully signed in
     * @param  response
     */
    public void addUser(JSONObject response){

        if(response != null){
            Map<String, Object> responseMap = JsonUtil.fromJson(response);
            Map<String, Object> accessTokenMap = new HashMap<>();
            accessTokenMap.put("id", responseMap.get("id"));
            accessTokenMap.put("ttl", responseMap.get("ttl"));
            AccessTokenRepository accessTokenRepository = snaphyHelper.getLoopBackAdapter().createRepository(AccessTokenRepository.class);
            JSONObject userJson = response.optJSONObject("user");
            Log.i(Constants.TAG, userJson.toString());
            CustomerRepository customerRepository = snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
            customerRepository.addStorage(mainActivity);
            Customer user = userJson != null
                    ? customerRepository.createObject(JsonUtil.fromJson(userJson))
                    : null;
            accessTokenMap.put("userId", user.getId());
            AccessToken accessToken = accessTokenRepository.createObject(accessTokenMap);
            snaphyHelper.getLoopBackAdapter().setAccessToken(accessToken.getId().toString());
            customerRepository.setCurrentUserId(accessToken.getUserId());
            customerRepository.setCachedCurrentUser(user);
            user.save__db();
            Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, user);
            String mciNumber = Presenter.getInstance().getModel(String.class, Constants.MCI_NUMBER);
            if(mciNumber != null){
                if(!mciNumber.isEmpty()){
                    user.setMciNumber(mciNumber);
                    updateCustomer(user);
                }else{

                    //move to home..
                    //Now move to home fragment finally..
                    moveToHome();
                }
            }else{

                //move to home..
                //Now move to home fragment finally..
                moveToHome();
            }


            //Register for push service..
            //Register installation moved to home...
            //snaphyHelper.registerInstallation(user);
        }else{
            //Register for push service..
            //snaphyHelper.registerInstallation(null);
            //SHOW ERROR MESSAGE..
            Log.v(Constants.TAG, "Error in add User Method");
            //report faliure login
            Answers.getInstance().logLogin(new LoginEvent()
                    .putSuccess(false));
            TastyToast.makeText(getApplicationContext(), Constants.ERROR_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }

    }

    public void moveToHome(){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer == null) {
            //Customer not login
            moveToLogin();
        } else {
            //Register installation id of login customer..
            //Register installation moved to home...
            snaphyHelper.registerInstallation(customer);
            final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
            if(customer.getStatus() != null){
                if(customer.getStatus().equals(Constants.ALLOW)) {
                    if(getIntent().getStringExtra("event") != null){
                        //Show push message data..
                        parsePushMessage();
                    } else {
                        if(!isBranchIO) {
                            replaceFragment(R.layout.fragment_main, null);
                        }
                    }
                } else {
                    checkMCI(MCINumber);
                }
            }
            else {
                checkMCI(MCINumber);
            }
        }
    }


    public void showHomeFragment(){
        // Move to home
        replaceFragment(R.layout.fragment_main, null);
    }

    public void checkMCI(String MCINumber){
        if(MCINumber.isEmpty()) {
            // Open MCI Fragment
            replaceFragment(R.layout.fragment_main, null);
        } else {
            //Display message that verification is under process
/*
            TastyToast.makeText(getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
*/

            if(getIntent().getStringExtra("event") != null){
                //Show push message data..
                parsePushMessage();
            }else{
                // Move to home
                replaceFragment(R.layout.fragment_main,null);
            }

            //TODO: Make cases, books and News unclickable
        }
    }


    public void checkLogin(){
        final CustomerRepository customerRepository = snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.addStorage(mainActivity);
        Customer current = customerRepository.getCachedCurrentUser();
        if (current != null) {
            //Now add this to list..
            Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, current);
            //Move to home fragment
            moveToHome();
        } else {
            //First check if login customer present in database...
            Object userId = customerRepository.getCurrentUserId();
            //Now fetch user from database..
            Customer customer = customerRepository.getDb().get__db((String)userId);
            if(customer!=null){
                Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, customer);
                moveToHome();
                //Now check if user is logged in or logged out
                findCurrentUser(true);

            } else{
                findCurrentUser(false);
            }

        }

    }

    //For getting payment status from payu money sdk
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
          paymentIdNumber = data.getStringExtra(SdkConstants.PAYMENT_ID);
          verifyPaymentFromServer(requestCode);
      } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
          Fragment fragment = getSupportFragmentManager().findFragmentByTag(CaseUploadImageFragment.TAG);
          if(fragment != null) {
              fragment.onActivityResult(requestCode, resultCode, data);
          }
      }
    }*/

    private void handleSignInResult(GoogleSignInResult result) {
        if(result != null) {
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct.getIdToken() != null) {
                    Log.v(Constants.TAG, acct.getIdToken());
                    //BackgroundService.setAccessToken(acct.getIdToken());
                    //mainActivity.replaceFragment(R.layout.fragment_mciverification, null);
                    Presenter.getInstance().addModel(Constants.GOOGLE_ACCESS_TOKEN, acct.getIdToken());
                    mainActivity.replaceFragment(R.layout.fragment_mciverification, null);
                    //sendTokenToServer(acct.getIdToken());
                } else {
                    TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } else {
                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }
    }




    //Verifying the payment from server side
    public void verifyPaymentFromServer(final int resultCode){
        Payment payment = Presenter.getInstance().getModel(Payment.class, Constants.PAYMENT_MODEL_DATA);
        if(payment!=null) {
            String paymentId = payment.getId().toString();
            String txnId = Presenter.getInstance().getModel(String.class, Constants.GENERATED_TRANSACTION_ID);
            if (txnId != null && paymentId != null) {
                PaymentRepository paymentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PaymentRepository.class);
                paymentRepository.getPaymentStatus(new HashMap<String, Object>(), txnId, paymentId, new ObjectCallback<Order>() {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                        mainActivity.startProgressBar(mainActivity.progressBar);
                    }

                    @Override
                    public void onSuccess(Order object) {
                        super.onSuccess(object);
                        //Do nothing...here
                        if(object.getPaymentStatus().equals("success")){
                            mainActivity.replaceFragment(R.layout.fragment_success, null);
                        } else{
                            mainActivity.replaceFragment(R.layout.fragment_failure, null);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                        mainActivity.replaceFragment(R.layout.fragment_failure, null);
                    }

                    @Override
                    public void onFinally() {
                        super.onFinally();
                        mainActivity.stopProgressBar(mainActivity.progressBar);
                       /* if (resultCode == RESULT_OK) {
                            mainActivity.replaceFragment(R.layout.fragment_success, null);
                        } else {
                            mainActivity.replaceFragment(R.layout.fragment_failure, null);
                        }*/
                    }
                });
            }
        }
    }



    public void findCurrentUser(final boolean isHomeOpened){
        final CustomerRepository customerRepository = snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.addStorage(mainActivity);
        customerRepository.findCurrentUser(new com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback<Customer>() {
            @Override
            public void onBefore() {
                if(!isHomeOpened){
                    //Show progress bar..
                    startProgressBar(progressBar);
                }
            }

            @Override
            public void onSuccess(Customer object) {
                //Save to database..
                if(object != null) {
                   // object.save__db();
                    Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, object);
                    if(!isHomeOpened){
                        //Move to home fragment
                        moveToHome();
                    }
                } else {
                    TastyToast.makeText(mainActivity, "Login to continue", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                    moveToLogin();
                }
            }

            @Override
            public void onError(Throwable t) {
                Object userId = customerRepository.getCurrentUserId();
                if(userId != null){
                    //Check here for faliure..
                    if (t.getMessage() != null) {
                        if (t.getMessage().equals("Unauthorized")) {
                            deleteUserCredentials(userId.toString());
                        } else if(t.getMessage().equals("Not Found")){
                            deleteUserCredentials(userId.toString());
                        }else{
                            //Fetch user data from database..
                            Customer appUser = customerRepository.getDb().get__db((String) userId);
                            if(appUser != null){
                                Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, appUser);
                                //Open home fragment..
                                if(!isHomeOpened){
                                    //Move to home fragment
                                    moveToHome();
                                }

                            }else{
                                deleteUserCredentials(userId.toString());
                            }

                        }
                    }else{
                        deleteUserCredentials(userId.toString());
                    }
                }else{
                    googleLogout();
                    TastyToast.makeText(mainActivity, "Login to continue", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                    moveToLogin();
                }
            }

            @Override
            public void onFinally() {
                //END PROGRESS BAR..
                if(!isHomeOpened){
                    //Show progress bar..
                    stopProgressBar(progressBar);
                }
            }
        });
    }


    public void deleteUserCredentials(String userId){
        final CustomerRepository customerRepository = snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.addStorage(mainActivity);
        //Delete data from database too..
        customerRepository.getDb().delete__db(userId);
        googleLogout();
        TastyToast.makeText(mainActivity, "Login to continue", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
        moveToLogin();

    }


    @OnClick(R.id.activity_main_button1) void retryButton() {
        hideRetryButton(true);
        sharedPreferences = this.getApplicationContext().getSharedPreferences("HelpScreen", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(snaphyHelper.isNetworkAvailable()) {
            // Internet is connected
            boolean displayHelpScreen = sharedPreferences.getBoolean("showHelpScreen", false);
            if(!displayHelpScreen) {
                editor.putBoolean("showHelpScreen", true);
                editor.commit();
                replaceFragment(R.layout.fragment_help, null);
            } else {
                //Check Login
                checkLogin();

            }
        } else {
            // No Internet is connected
            TastyToast.makeText(getApplicationContext(), "Internet not connected", TastyToast.LENGTH_LONG, TastyToast.ERROR);

        }
    }

    public void hideRetryButton(boolean ishide) {
        if(ishide) {
            notConnectedText.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
        } else {
            notConnectedText.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


    public void googleLogout() {
        if(Presenter.getInstance().getModel(GoogleApiClient.class, Constants.GOOGLE_API_CLIENT) != null) {
            if (Presenter.getInstance().getModel(GoogleApiClient.class, Constants.GOOGLE_API_CLIENT).isConnected()) {
                Auth.GoogleSignInApi.signOut(Presenter.getInstance().getModel(GoogleApiClient.class, Constants.GOOGLE_API_CLIENT)).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.v(Constants.TAG, status.toString());
                                //Remove the data from presenter..
                                Presenter.getInstance().removeModelFromList(Constants.GOOGLE_API_CLIENT);
                            }
                        });
            }
        } else {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
            if (googleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Log.v(Constants.TAG, status.toString());
                            }
                        });
            }
        }
    }


    public void moveToLogin(){
        if(progressBar != null) {
            stopProgressBar(progressBar);
        }
        replaceFragment(R.layout.fragment_login, null);
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
