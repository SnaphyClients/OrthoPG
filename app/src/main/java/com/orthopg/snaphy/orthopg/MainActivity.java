package com.orthopg.snaphy.orthopg;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.News;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BooksFragment;
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
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;
import com.orthopg.snaphy.orthopg.Interface.OnFragmentChange;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.AccessTokenRepository;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.remoting.JsonUtil;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements OnFragmentChange, LoginFragment.OnFragmentInteractionListener,
        MCIVerificationFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener,
        BooksFragment.OnFragmentInteractionListener, CaseFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener, AboutUsFragment.OnFragmentInteractionListener,
        FAQsFragment.OnFragmentInteractionListener, TermsAndConditionsFragment.OnFragmentInteractionListener,
        ContactUsFragment.OnFragmentInteractionListener, CaseHeadingFragment.OnFragmentInteractionListener,
        CaseDescriptionFragment.OnFragmentInteractionListener, CaseUploadImageFragment.OnFragmentInteractionListener,
        CaseDetailFragment.OnFragmentInteractionListener, PostAnswerFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener, CaseHelpFragment.OnFragmentInteractionListener,
        BooksHelpFragment.OnFragmentInteractionListener, NewsHelpFragment.OnFragmentInteractionListener {

    RestAdapter restAdapter;
    Context context;
    GoogleCloudMessaging gcm;
    public static LocalInstallation installation;
    public SnaphyHelper snaphyHelper;
    SharedPreferences sharedPreferences;
    CircleProgressBar progressBar;
    TextView notConnectedText;
    Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar = (CircleProgressBar) findViewById(R.id.activity_main_progressBar);
        notConnectedText = (TextView) findViewById(R.id.activity_main_textview1);
        retryButton = (Button) findViewById(R.id.activity_main_button1);
        context = getApplicationContext();
        snaphyHelper = new SnaphyHelper(this);
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
            // Internet is connected
            boolean displayHelpScreen = sharedPreferences.getBoolean("showHelpScreen", false);
            if(!displayHelpScreen) {
                stopProgressBar(progressBar);
                editor.putBoolean("showHelpScreen", true);
                editor.commit();
                replaceFragment(R.layout.fragment_help, null);
            } else {
                //Check Login
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
                        Presenter.getInstance().addModel(Constants.NOTIFICATION_ID, id);
                        replaceFragment(R.layout.fragment_case_detail, null);
                    } else if(event.equals("like")) {
                        Presenter.getInstance().addModel(Constants.NOTIFICATION_ID, id);
                        replaceFragment(R.layout.fragment_case_detail, null);
                    } else if(event.equals("save")) {
                        Presenter.getInstance().addModel(Constants.NOTIFICATION_ID, id);
                        replaceFragment(R.layout.fragment_case_detail, null);
                    } else {

                    }
                } else {
                    checkLogin();
                }

            }
        } else {
            // No Internet is connected
            //Toast.makeText(this, "Internet not connected", Toast.LENGTH_SHORT).show();
            TastyToast.makeText(getApplicationContext(), "Connection Error! Check your network", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            hideRetryButton(false);
        }
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
                openCaseHeadingFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_heading_button1:
                openImageUploadFragment(fragmentTransaction);
                break;

            case R.id.fragment_case_upload_image_button1:
                openCaseDescriptionFragment(fragmentTransaction);
                break;

            case R.id.layout_case_list_textview4:
                openCaseDetailFragment(fragmentTransaction, object);
                break;

            case R.id.fragment_case_detail_button4:
                openPostAnswerFragment(fragmentTransaction);
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
        fragmentTransaction.replace(R.id.main_container, caseDetailFragment, CaseDetailFragment.TAG).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void openPostAnswerFragment(FragmentTransaction fragmentTransaction) {
        PostAnswerFragment postAnswerFragment = (PostAnswerFragment) getSupportFragmentManager().
                findFragmentByTag(PostAnswerFragment.TAG);
        if (postAnswerFragment == null) {
            postAnswerFragment = PostAnswerFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.main_container, postAnswerFragment, PostAnswerFragment.TAG).addToBackStack(null);
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

    public String parseDate(String postedDate) {

        /*//Posted Case Parsing
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date date_ = null;
        try {
            date_ = format.parse(postedDate);
            Log.v(Constants.TAG, "Posted Date = "+date_);
        } catch (ParseException e) {
            e.printStackTrace();
        }
*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("IST"));
        java.util.Date date_ = null;
        try {
            date_ = format.parse(postedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Now parsing time..
        Log.v(Constants.TAG, "Posted Date = "+date_);



        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String strDate = sdf.format(calender.getTime());
        String currentDate = strDate;
        Log.v(Constants.TAG, currentDate);

        //Posted Case Parsing

       /* //JAVA Current Date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String strDate = sdf.format(c.getTime());
        String currentDate = strDate;

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("IST"));
        java.util.Date date_2 = null;
        try {
            date_2 = format2.parse(currentDate);
            Log.v(Constants.TAG, "Current Date = "+date_2);
            java.util.Date a  = date_2;
        } catch (ParseException e) {
            e.printStackTrace();
        }


*/

        String time = applyRegexOnDate(postedDate, currentDate);
        return time;

    }

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
            Customer user = userJson != null
                    ? customerRepository.createObject(JsonUtil.fromJson(userJson))
                    : null;
            accessTokenMap.put("userId", user.getId());
            AccessToken accessToken = accessTokenRepository.createObject(accessTokenMap);
            snaphyHelper.getLoopBackAdapter().setAccessToken(accessToken.getId().toString());
            customerRepository.setCurrentUserId(accessToken.getUserId());
            customerRepository.setCachedCurrentUser(user);

            Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, user);

            //move to home..
            //Now move to home fragment finally..
            moveToHome();

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
                    replaceFragment(R.layout.fragment_main, null);
                } else {
                    checkMCI(MCINumber);
                }
            }
            else {
                checkMCI(MCINumber);
            }


        }
    }

    public void checkMCI(String MCINumber){
        if(MCINumber.isEmpty()) {
            // Open MCI Fragment
            replaceFragment(R.layout.fragment_mciverification, null);
        } else {
            //Display message that verification is under process
            TastyToast.makeText(getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
            // Move to home
            replaceFragment(R.layout.fragment_main,null);
            // Make cases, books and News unclickable
        }
    }


    public void checkLogin(){
        CustomerRepository customerRepository = snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        Customer current = customerRepository.getCachedCurrentUser();
        if (current != null) {
            //Now add this to list..
            Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, current);
            //Move to home fragment
            moveToHome();
        } else {
            customerRepository.findCurrentUser(new com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback<Customer>() {
                @Override
                public void onBefore() {
                    //Show progress bar..
                    startProgressBar(progressBar);
                }

                @Override
                public void onSuccess(Customer object) {
                    Presenter.getInstance().addModel(Constants.LOGIN_CUSTOMER, object);
                    //Move to home fragment
                    moveToHome();
                }

                @Override
                public void onError(Throwable t) {
                    //report faliure login
                    Answers.getInstance().logLogin(new LoginEvent()
                            .putSuccess(false).
                    putCustomAttribute("Login Error", t.getMessage()));
                    //Retry Login
                    if (t.getMessage() != null) {
                        if (t.getMessage().equals("Unauthorized")) {
                            googleLogout();
                            moveToLogin();
                        } else {
                            //SHOW INTERNET CONNECTION ERROR.
                            hideRetryButton(false);
                        }

                    }
                }

                @Override
                public void onFinally() {
                    //END PROGRESS BAR..
                    stopProgressBar(progressBar);

                }
            });
        }

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
