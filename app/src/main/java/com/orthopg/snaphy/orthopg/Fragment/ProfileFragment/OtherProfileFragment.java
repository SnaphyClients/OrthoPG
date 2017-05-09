package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.DummyCustomerQualification;
import com.androidsdk.snaphy.snaphyandroidsdk.models.DummyCustomerSpeciality;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Qualification;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.DummyCustomerQualificationRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.DummyCustomerSpecialityRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.QualificationRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SpecialityRepository;
import com.google.common.collect.ContiguousSet;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.WordUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherProfileFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;

    @Bind(R.id.fragment_profile_imageview1) ImageView profileImage;
    @Bind(R.id.fragment_profile_imageview2) ImageView editImage;
    @Bind(R.id.fragment_profile_textview1) TextView name;
    @Bind(R.id.fragment_profile_textview2) TextView emailTxt;
    @Bind(R.id.fragment_profile_textview3) TextView editProfileTxt;
    @Bind(R.id.fragment_profile_textview4) TextView mciNumberTxt;
    @Bind(R.id.fragment_profile_textview5) TextView workExperinceTxt;
    @Bind(R.id.fragment_profile_textview6) TextView specialityTxt;
    @Bind(R.id.fragment_profile_textview7) TextView qualificationTxt;
    @Bind(R.id.fragment_profile_textview8) TextView currentWorkingTxt;

    @Bind(R.id.fragment_profile_textview10) TextView mciNumberHeading;
    @Bind(R.id.fragment_profile_textview11) TextView workExperienceHeading;
    @Bind(R.id.fragment_profile_textview12) TextView specialityHeading;
    @Bind(R.id.fragment_profile_textview13) TextView qualificationHeading;

    @Bind(R.id.fragment_profile_imageview3) ImageView mciEdit;
    @Bind(R.id.fragment_profile_imageview4) ImageView workExperienceEdit;
    @Bind(R.id.fragment_profile_imageview5) ImageView specialityEdit;
    @Bind(R.id.fragment_profile_imageview6) ImageView qualificationEdit;
    @Bind(R.id.fragment_profile_imageview7) ImageView profileNameTxt;
    @Bind(R.id.fragment_profile_relative_layout1) RelativeLayout orderListContainer;
    @Bind(R.id.fragment_profile_textview9) TextView orderHistoryTxt;

    public final static String CITY_TAG = "EditCityTag";
    public final static String MCINUMBER_TAG = "MCINumberTag";
    public final static String WORKEXPERIENCETAG = "WorkExperienceTag";

    public final static String TAG = "OtherProfileFragment";
    public static String FROM;

    public OtherProfileFragment() {
        // Required empty public constructor
        FROM = "";
    }

    public static OtherProfileFragment newInstance(String TAG) {
        OtherProfileFragment fragment = new OtherProfileFragment();
        FROM = TAG;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        ButterKnife.bind(this, view);
        setProfileData();
        return view;
    }

    public void setProfileData() {
        if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
            checkVisiblity();
            Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.CASE_PROFILE_DATA);
            if(customer!=null){setCustomerProfileData(customer);
            } else{
                emailTxt.setVisibility(View.GONE);
                mciNumberTxt.setVisibility(View.GONE);
                workExperinceTxt.setVisibility(View.GONE);
                currentWorkingTxt.setVisibility(View.GONE);
                qualificationTxt.setVisibility(View.GONE);
                specialityTxt.setVisibility(View.GONE);
            }
        } else {
            CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
            customerRepository.addStorage(mainActivity);
            Customer customer1 = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
            if (customer1 != null) {
                setCustomerProfileData(customer1);
            } else {
                emailTxt.setVisibility(View.GONE);
                mciNumberTxt.setVisibility(View.GONE);
                workExperinceTxt.setVisibility(View.GONE);
                currentWorkingTxt.setVisibility(View.GONE);
                qualificationTxt.setVisibility(View.GONE);
                specialityTxt.setVisibility(View.GONE);
                 }
            }
        }


    public void checkVisiblity(){
        editProfileTxt.setVisibility(View.GONE);
        editImage.setVisibility(View.GONE);
        mciEdit.setVisibility(View.GONE);
        workExperienceEdit.setVisibility(View.GONE);
        specialityEdit.setVisibility(View.GONE);
        qualificationEdit.setVisibility(View.GONE);
        orderListContainer.setVisibility(View.GONE);
        orderHistoryTxt.setVisibility(View.GONE);
    }

    public void setCustomerProfileData(final Customer customer){
        if (customer.getEmail() != null) {
            if (!customer.getEmail().isEmpty()) {
                emailTxt.setVisibility(View.VISIBLE);
                emailTxt.setText(customer.getEmail());
            } else {
                emailTxt.setVisibility(View.GONE);
            }
        } else {
            emailTxt.setVisibility(View.GONE);
        }

        if (customer.getMciNumber() != null) {
            if (!customer.getMciNumber().isEmpty()) {
                mciNumberTxt.setVisibility(View.VISIBLE);
                mciNumberTxt.setText(customer.getMciNumber());
            } else {
                if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                    mciNumberHeading.setVisibility(View.GONE);
                }
                mciNumberTxt.setVisibility(View.GONE);
            }
        } else {
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                mciNumberHeading.setVisibility(View.GONE);
            }
            mciNumberTxt.setVisibility(View.GONE);
        }

        String userName = mainActivity.snaphyHelper.getName(customer.getFirstName(), customer.getLastName());
        userName = Constants.Doctor + userName.replace("^[Dd][Rr]", "");
        if(!userName.isEmpty()){
            name.setVisibility(View.VISIBLE);
            name.setText(userName);
        }else{
            name.setVisibility(View.GONE);
        }

        if(customer.getProfilePic() != null){
            mainActivity.snaphyHelper.loadUnSignedThumbnailImage(customer.getProfilePic(), profileImage, R.mipmap.anonymous);
        }else{
            profileImage.setImageResource(R.mipmap.anonymous);
        }

        String workExperience = String.valueOf((int)customer.getWorkExperience());
        if (workExperience != null) {
            if (!workExperience.isEmpty()) {
                workExperinceTxt.setVisibility(View.VISIBLE);
                workExperinceTxt.setText(String.valueOf((int)customer.getWorkExperience()));
            } else {
                if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                    workExperienceHeading.setVisibility(View.GONE);
                }
                workExperinceTxt.setVisibility(View.GONE);
            }
        } else {
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                workExperienceHeading.setVisibility(View.GONE);
            }
            workExperinceTxt.setVisibility(View.GONE);
        }

        if (customer.getCurrentCity() != null) {
            if (!customer.getCurrentCity().isEmpty()) {
                currentWorkingTxt.setVisibility(View.VISIBLE);
                currentWorkingTxt.setText(WordUtils.capitalize(String.valueOf(customer.getCurrentCity())));
            } else {
                currentWorkingTxt.setVisibility(View.GONE);
            }
        } else {
            currentWorkingTxt.setVisibility(View.GONE);
        }

        final DummyCustomerSpecialityRepository dummyCustomerSpecialityRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(DummyCustomerSpecialityRepository.class);
        dummyCustomerSpecialityRepository.addStorage(mainActivity);

        final SpecialityRepository specialityRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(SpecialityRepository.class);
        specialityRepository.addStorage(mainActivity);

        DataList<Speciality> specialityDataList = new DataList<>();
            //if internet not..availaible....
        if(!mainActivity.snaphyHelper.isNetworkAvailable()) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("customerId", customer.getId());
            if (dummyCustomerSpecialityRepository.getDb().count__db(where) > 0) {
                DataList<DummyCustomerSpeciality> dummyCustomerSpecialities = dummyCustomerSpecialityRepository.getDb().getAll__db(where);

                for (DummyCustomerSpeciality dummyCustomerSpeciality : dummyCustomerSpecialities) {
                    specialityDataList.add(specialityRepository.getDb().get__db(dummyCustomerSpeciality.getSpecialityId()));
                }
            }
            customer.setSpecialities(specialityDataList);
            setSpeciality(customer);
        } else {
            if (customer.getSpecialities() != null) {
                if (customer.getSpecialities().size() == 0) {
                    HashMap<String, Object> filter = new HashMap<>();

                    customer.get__specialities(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Speciality>() {
                        @Override
                        public void onSuccess(DataList<Speciality> objects) {
                            super.onSuccess(objects);
                            SpecialityRepository specialityRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(SpecialityRepository.class);
                            specialityRepository.addStorage(mainActivity);
                            for (Speciality speciality : objects) {
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                DummyCustomerSpeciality dummyCustomerSpeciality = dummyCustomerSpecialityRepository.createObject(hashMap);
                                dummyCustomerSpeciality.setCustomerId(customer.getId().toString());
                                dummyCustomerSpeciality.setSpecialityId(speciality.getId().toString());
                                dummyCustomerSpeciality.setId(customer.getId());
                                dummyCustomerSpeciality.save__db();
                                specialityRepository.getDb().upsert__db(speciality.getId().toString(), speciality);
                            }
                            Presenter.getInstance().addList(Constants.CUSTOMER_SPECIALITY_LIST, objects);
                            setSpeciality(customer);
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                            Log.e(Constants.TAG, t.toString());
                        }
                    });
                } else {
                    setSpeciality(customer);
                }

            } else {
                specialityTxt.setVisibility(View.GONE);
                HashMap<String, Object> filter = new HashMap<>();
                customer.get__specialities(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Speciality>() {
                    @Override
                    public void onSuccess(DataList<Speciality> objects) {
                        super.onSuccess(objects);
                        SpecialityRepository specialityRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(SpecialityRepository.class);
                        specialityRepository.addStorage(mainActivity);
                        for (Speciality speciality : objects) {
                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            specialityRepository.getDb().upsert__db(speciality.getId().toString(), speciality);
                            DummyCustomerSpeciality dummyCustomerSpeciality = dummyCustomerSpecialityRepository.createObject(hashMap);
                            dummyCustomerSpeciality.setCustomerId(customer.getId().toString());
                            dummyCustomerSpeciality.setSpecialityId(speciality.getId().toString());
                            dummyCustomerSpeciality.setId(customer.getId());
                            dummyCustomerSpeciality.save__db();
                            specialityRepository.getDb().upsert__db(speciality.getId().toString(), speciality);
                        }
                        Presenter.getInstance().addList(Constants.CUSTOMER_SPECIALITY_LIST, objects);
                        setSpeciality(customer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                    }
                });
            }
        }

        final DummyCustomerQualificationRepository dummyCustomerQualificationRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(DummyCustomerQualificationRepository.class);
        dummyCustomerQualificationRepository.addStorage(mainActivity);

        QualificationRepository qualificationRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(QualificationRepository.class);
        qualificationRepository.addStorage(mainActivity);

        DataList<Qualification> qualificationDataList = new DataList<>();
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            HashMap<String, Object> where = new HashMap<>();
            where.put("customerId", customer.getId());
            if (dummyCustomerQualificationRepository.getDb().count__db(where) > 0) {
                DataList<DummyCustomerQualification> dummyCustomerQualifications = dummyCustomerQualificationRepository.getDb().getAll__db(where);

                for (DummyCustomerQualification dummyCustomerQualification : dummyCustomerQualifications) {
                    qualificationDataList.add(qualificationRepository.getDb().get__db(dummyCustomerQualification.getQualificationId()));
                }
            }
            customer.setQualifications(qualificationDataList);
            setQualification(customer);
        } else{
            if (customer.getQualifications() != null) {
                if (customer.getQualifications().size() == 0) {
                    HashMap<String, Object> filter = new HashMap<>();
                    customer.get__qualifications(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Qualification>() {
                        @Override
                        public void onSuccess(DataList<Qualification> objects) {
                            super.onSuccess(objects);
                            QualificationRepository qualificationRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(QualificationRepository.class);
                            qualificationRepository.addStorage(mainActivity);
                            for (Qualification qualification : objects) {
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                qualificationRepository.getDb().upsert__db(qualification.getId().toString(), qualification);
                                DummyCustomerQualification dummyCustomerQualification = dummyCustomerQualificationRepository.createObject(hashMap);
                                dummyCustomerQualification.setCustomerId(customer.getId().toString());
                                dummyCustomerQualification.setQualificationId(qualification.getId().toString());
                                dummyCustomerQualification.setId(customer.getId());
                                dummyCustomerQualification.save__db();
                                qualificationRepository.getDb().upsert__db(qualification.getId().toString(), qualification);
                            }
                            Presenter.getInstance().addList(Constants.CUSTOMER_QUALIFICATION_LIST,objects);
                            setQualification(customer);
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                            Log.e(Constants.TAG, t.toString());
                        }
                    });
                } else {
                    setQualification(customer);
                }
            } else{
                qualificationTxt.setVisibility(View.GONE);
                HashMap<String, Object> filter = new HashMap<>();
                customer.get__qualifications(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Qualification>() {
                    @Override
                    public void onSuccess(DataList<Qualification> objects) {
                        super.onSuccess(objects);
                        QualificationRepository qualificationRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(QualificationRepository.class);
                        qualificationRepository.addStorage(mainActivity);
                        for (Qualification qualification : objects) {
                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            qualificationRepository.getDb().upsert__db(qualification.getId().toString(), qualification);
                            DummyCustomerQualification dummyCustomerQualification = dummyCustomerQualificationRepository.createObject(hashMap);
                            dummyCustomerQualification.setCustomerId(customer.getId().toString());
                            dummyCustomerQualification.setQualificationId(qualification.getId().toString());
                            dummyCustomerQualification.setId(customer.getId());
                            dummyCustomerQualification.save__db();
                            qualificationRepository.getDb().upsert__db(qualification.getId().toString(), qualification);
                        }
                        Presenter.getInstance().addList(Constants.CUSTOMER_QUALIFICATION_LIST,objects);
                        setQualification(customer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                    }
                });
            }
        }

    }


    @OnClick(R.id.fragment_profile_textview3) void onEditCityText(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            final Dialog dialog = new Dialog(mainActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_profile);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            TextView header = (TextView) dialog.findViewById(R.id.dialog_edit_profile_textview1);
            final TextView editText = (EditText) dialog.findViewById(R.id.dialog_edit_profile_editText1);
            Button edit = (Button) dialog.findViewById(R.id.dialog_edit_profile_button1);
            header.setText("CITY");
            editText.setHint("Edit City");

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().isEmpty()) {
                        Snackbar.make(editText, "City cannot be blank", Snackbar.LENGTH_SHORT).show();
                    } else {
                        updateCurrentWorkingData(editText.getText().toString());
                        currentWorkingTxt.setText(editText.getText().toString());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    @OnClick(R.id.fragment_profile_imageview2) void onEditCity(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            final Dialog dialog = new Dialog(mainActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_profile);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            TextView header = (TextView) dialog.findViewById(R.id.dialog_edit_profile_textview1);
            final TextView editText = (EditText) dialog.findViewById(R.id.dialog_edit_profile_editText1);
            Button edit = (Button) dialog.findViewById(R.id.dialog_edit_profile_button1);
            header.setText("CITY");
            editText.setText(currentWorkingTxt.getText().toString());
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().isEmpty()) {
                        Snackbar.make(editText, "City cannot be blank", Snackbar.LENGTH_SHORT).show();
                    } else {
                        updateCurrentWorkingData(editText.getText().toString());
                        currentWorkingTxt.setText(editText.getText().toString());
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    @OnClick(R.id.fragment_profile_imageview3) void onMCIEdit(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            final Dialog dialog = new Dialog(mainActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_profile);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            TextView header = (TextView) dialog.findViewById(R.id.dialog_edit_profile_textview1);
            final TextView editText = (EditText) dialog.findViewById(R.id.dialog_edit_profile_editText1);
            Button edit = (Button) dialog.findViewById(R.id.dialog_edit_profile_button1);
            header.setText("MCI NUMBER");
            editText.setText(mciNumberTxt.getText().toString());
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().isEmpty()) {
                        Snackbar.make(editText, "City cannot be blank", Snackbar.LENGTH_SHORT).show();
                    } else {
                        updateMCINumberData(editText.getText().toString());
                        mciNumberTxt.setText(editText.getText().toString());
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    public void updateMCINumberData(final String mciNumber){

        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        customer.setMciNumber(mciNumber);
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.upsert(customer.toMap(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                super.onSuccess(object);
                /*mainActivity.onBackPressed();*/

                TastyToast.makeText(mainActivity, "Successfully updated data", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                TastyToast.makeText(mainActivity, "Error in updating data", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });
    }


   /* @OnClick(R.id.fragment_profile_imageview3) void onMCIEdit(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            mainActivity.replaceFragment(R.layout.fragment_edit_profile, MCINUMBER_TAG);
        }
    }*/

    @OnClick(R.id.fragment_profile_imageview4) void onWorkExperienceEdit(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            final Dialog dialog = new Dialog(mainActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_work_experience);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            TextView header = (TextView)dialog.findViewById(R.id.dialog_edit_work_textview1);
            final TextView editText = (EditText)dialog.findViewById(R.id.dialog_edit_work_editText1);
            Button edit = (Button)dialog.findViewById(R.id.dialog_edit_work_button1);
            header.setText("WORK EXPERIENCE");
            editText.setText(workExperinceTxt.getText().toString());
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int workExperience = Integer.parseInt(editText.getText().toString());
                    if(editText.getText().toString().isEmpty()) {
                        Snackbar.make(profileImage,"Work Experience cannot be empty", Snackbar.LENGTH_SHORT).show();
                    } else if(workExperience > 99 || workExperience < 0) {
                        Snackbar.make(profileImage,"Invalid work experience", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        updateWorkExperienceData(editText.getText().toString());
                        workExperinceTxt.setText(editText.getText().toString());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);

        }
    }

    public void updateWorkExperienceData(final String workExperience){
        final Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        customer.setWorkExperience(Double.parseDouble(workExperience));
        final CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.addStorage(mainActivity);
        customerRepository.upsert(customer.toMap(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                super.onSuccess(object);
                customerRepository.getDb().upsert__db(customer.getId().toString(), object);
                /*mainActivity.onBackPressed();*/
                TastyToast.makeText(mainActivity, "Successfully updated data", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                TastyToast.makeText(mainActivity, "Error in updating data", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                Log.e(Constants.TAG, t.toString());
            }
        });
    }

    @OnClick(R.id.fragment_profile_imageview5) void onSpecialityEdit(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            mainActivity.replaceFragment(R.layout.fragment_speciality, null);
        }
    }

    @OnClick(R.id.fragment_profile_imageview6) void onQualificationEdit(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            mainActivity.replaceFragment(R.layout.fragment_qualification, null);
        }
    }

    @OnClick(R.id.fragment_profile_relative_layout1) void onPastOrderList(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(profileImage,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            mainActivity.replaceFragment(R.layout.fragment_order_history, null);
        }
    }



    public void updateCurrentWorkingData(final String currentWorking){
        final Customer customer = Presenter.getInstance().getModel(Customer.class,Constants.LOGIN_CUSTOMER);
        customer.setCurrentCity(currentWorking);

        final CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.addStorage(mainActivity);
        customerRepository.upsert(customer.toMap(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                super.onSuccess(object);
                customerRepository.getDb().upsert__db(customer.getId().toString(),object);
                /*mainActivity.onBackPressed();*/
                TastyToast.makeText(mainActivity, "Successfully updated data", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                TastyToast.makeText(mainActivity, "Error in updating data", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                Log.e(Constants.TAG, t.toString());
            }
        });
    }

    public void setSpeciality(Customer customer) {

        if (customer.getSpecialities() == null) {
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                specialityHeading.setVisibility(View.GONE);
            }
            return;
        }
        if (customer.getSpecialities().size() != 0) {

            specialityTxt.setVisibility(View.VISIBLE);
            String specialities = "";
            int count = 0;
            DataList<Speciality> speciality = customer.getSpecialities();
            for (Speciality s : speciality) {
                if(count<speciality.size()-1) {
                    specialities = specialities + s.getName() + "\n";
                    count++;
                } else{
                    specialities = specialities + s.getName();
                }
            }
            specialityTxt.setText(specialities);
        } else {
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
               specialityHeading.setVisibility(View.GONE);
            }
            specialityTxt.setVisibility(View.GONE);
        }
    }

    public void setQualification(Customer customer){
        if(customer.getQualifications()==null) {
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                qualificationHeading.setVisibility(View.GONE);
            }
            return;
        }
        if(customer.getQualifications().size()!=0){
            qualificationTxt.setVisibility(View.VISIBLE);
            String qualifications = "";
            int qualificationCount = 0;
            DataList<Qualification> qualification = customer.getQualifications();
            for(Qualification q : qualification){
                if(qualificationCount<qualification.size()-1) {
                    qualifications = qualifications + q.getName() + "\n";
                    qualificationCount++;
                } else{
                    qualifications = qualifications + q.getName();
                }
            }
            qualificationTxt.setText(qualifications);
        } else{
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                qualificationHeading.setVisibility(View.GONE);
            }
            qualificationTxt.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fragment_profile_imageview7) void onEditName(){
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(name, "No Network Connection", Snackbar.LENGTH_SHORT).show();
        } else {
            final Dialog dialog = new Dialog(mainActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_name);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
            final EditText firstName = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
            final EditText lastName = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext2);

            firstName.setInputType(InputType.TYPE_CLASS_TEXT);
            lastName.setInputType(InputType.TYPE_CLASS_TEXT);

            final Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
            if (loginCustomer != null) {
                if (loginCustomer.getFirstName() != null) {
                    if (!loginCustomer.getFirstName().isEmpty()) {
                        firstName.setText(loginCustomer.getFirstName().trim());
                    } else {
                        Snackbar.make(name, "First Name cannot be blank", Snackbar.LENGTH_SHORT).show();
                    }

                    if (!loginCustomer.getLastName().isEmpty()) {
                        lastName.setText(loginCustomer.getLastName().trim());
                    } else {
                        Snackbar.make(name, "Last Name cannot be blank", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }


            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstName.getText() != null) {
                        if (firstName.getText().toString() != null) {
                            if (firstName.getText().toString().trim().isEmpty()) {
                                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_FIRST_NAME_EMPTY, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                                return;
                            } else {
                                loginCustomer.setFirstName(firstName.getText().toString().trim());
                            }
                        }
                    }

                    if (lastName.getText() != null) {
                        if (lastName.getText().toString() != null) {
                            loginCustomer.setLastName(lastName.getText().toString().trim());
                        }
                    }

                    String userName = mainActivity.snaphyHelper.getName(loginCustomer.getFirstName(), loginCustomer.getLastName());
                    userName = Constants.Doctor + userName.replace("^[Dd][Rr]", "");
                    final String oldName = name.getText().toString();
                    name.setText(userName);

                    //Now update the customer..
                    loginCustomer.save(new com.strongloop.android.loopback.callbacks.VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Throwable t) {
                            name.setText(oldName);
                            TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_UPDATING_NAME, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    });
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    @OnClick(R.id.fragment_other_profile_imageview1) void onBack(){
        mainActivity.onBackPressed();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
