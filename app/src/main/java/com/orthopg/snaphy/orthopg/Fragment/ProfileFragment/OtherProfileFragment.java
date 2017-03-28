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
import com.androidsdk.snaphy.snaphyandroidsdk.models.Qualification;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SpecialityRepository;
import com.google.common.collect.ContiguousSet;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.WordUtils;

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
  /*  @Bind(R.id.fragment_other_profile_textview2) TextView emailTxt;
    @Bind(R.id.fragment_other_profile_textview12) TextView mciNumberTxt;
    @Bind(R.id.fragment_other_profile_textview4) TextView specialityTxt;
    @Bind(R.id.fragment_other_profile_textview7) TextView workExperinceTxt;
    @Bind(R.id.fragment_other_profile_textview9) TextView currentWorkingTxt;
    @Bind(R.id.fragment_other_profile_textview11) TextView qualificationTxt;
    @Bind(R.id.fragment_profile_imagebutton2) ImageButton specialityButton;
    @Bind(R.id.fragment_profile_imagebutton5) ImageButton qualificationButton;
    @Bind(R.id.fragment_other_profile_textview5) TextView name;
    @Bind(R.id.layout_profile_image) ImageView profileImage;*/

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
            Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
            if (customer != null) {
                setCustomerProfileData(customer);
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

    public void setCustomerProfileData(Customer customer){
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


        if (customer.getSpecialities() != null) {
            if (customer.getSpecialities().size() == 0) {
                HashMap<String, Object> filter = new HashMap<>();
                customer.get__specialities(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Speciality>() {
                    @Override
                    public void onSuccess(DataList<Speciality> objects) {
                        super.onSuccess(objects);
                        Presenter.getInstance().addList(Constants.CUSTOMER_SPECIALITY_LIST,objects);
                        setSpeciality();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                    }
                });
            }
            setSpeciality();
        } else {
            specialityTxt.setVisibility(View.GONE);
            HashMap<String, Object> filter = new HashMap<>();
            customer.get__specialities(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Speciality>() {
                @Override
                public void onSuccess(DataList<Speciality> objects) {
                    super.onSuccess(objects);
                    Presenter.getInstance().addList(Constants.CUSTOMER_SPECIALITY_LIST,objects);
                    setSpeciality();
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                }
            });
        }
        if (customer.getQualifications() != null) {
            if (customer.getQualifications().size() == 0) {
                HashMap<String, Object> filter = new HashMap<>();
                customer.get__qualifications(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Qualification>() {
                    @Override
                    public void onSuccess(DataList<Qualification> objects) {
                        super.onSuccess(objects);
                        Presenter.getInstance().addList(Constants.CUSTOMER_QUALIFICATION_LIST,objects);
                        setQualification();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                    }
                });
            }
            setQualification();
        } else{
            qualificationTxt.setVisibility(View.GONE);
            HashMap<String, Object> filter = new HashMap<>();
            customer.get__qualifications(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Qualification>() {
                @Override
                public void onSuccess(DataList<Qualification> objects) {
                    super.onSuccess(objects);
                    Presenter.getInstance().addList(Constants.CUSTOMER_QUALIFICATION_LIST,objects);
                    setQualification();
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                }
            });
        }
    }

    @OnClick(R.id.fragment_profile_imageview2) void onEditCity(){
        mainActivity.replaceFragment(R.layout.fragment_edit_profile, CITY_TAG);
    }

    @OnClick(R.id.fragment_profile_textview3) void onEditCityText(){
        mainActivity.replaceFragment(R.layout.fragment_edit_profile, CITY_TAG);
    }

    @OnClick(R.id.fragment_profile_imageview3) void onMCIEdit(){
        mainActivity.replaceFragment(R.layout.fragment_edit_profile, MCINUMBER_TAG);
    }

    @OnClick(R.id.fragment_profile_imageview4) void onWorkExperienceEdit(){
        mainActivity.replaceFragment(R.layout.fragment_edit_profile, WORKEXPERIENCETAG);
    }

    @OnClick(R.id.fragment_profile_imageview5) void onSpecialityEdit(){
        mainActivity.replaceFragment(R.layout.fragment_speciality, null);
    }

    @OnClick(R.id.fragment_profile_imageview6) void onQualificationEdit(){
        mainActivity.replaceFragment(R.layout.fragment_qualification, null);
    }

    @OnClick(R.id.fragment_profile_relative_layout1) void onPastOrderList(){
        mainActivity.replaceFragment(R.layout.fragment_order_history, null);
    }

    public void setSpeciality() {

        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
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

    public void setQualification(){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer.getQualifications()==null) {
            if(OtherProfileFragment.FROM.equals(CaseFragment.TAG)){
                qualificationHeading.setVisibility(View.GONE);
            }
            return;
        }
        if(customer.getQualifications().size()!=0){
            qualificationTxt.setVisibility(View.VISIBLE);
            String qualifications = "";

            DataList<Qualification> qualification = customer.getQualifications();
            for(Qualification q : qualification){
                if(qualification.size()>1) {
                    qualifications = qualifications + q.getName() + "\n";
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

    public void showExperienceDialog() {

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        TextInputLayout textInputLayout = (TextInputLayout)dialog.findViewById(R.id.fragment_layout_dialog_editInput1);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updateWorkExperienceData(editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showCurrentWorkingDialog(){

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        TextInputLayout textInputLayout = (TextInputLayout)dialog.findViewById(R.id.fragment_layout_dialog_editInput1);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateCurrentWorkingData(editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    public void updateWorkExperienceData(final String workExperience){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        customer.setWorkExperience(Double.parseDouble(workExperience));
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.upsert(customer.toMap(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                super.onSuccess(object);
                workExperinceTxt.setText(workExperience);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
            }
        });
    }

    public void updateCurrentWorkingData(final String currentWorking){
        Customer customer = Presenter.getInstance().getModel(Customer.class,Constants.LOGIN_CUSTOMER);
        customer.setCurrentCity(currentWorking);
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.upsert(customer.toMap(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                super.onSuccess(object);
                currentWorkingTxt.setText(currentWorking);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
            }
        });
    }

/*
    @OnClick(R.id.fragment_profile_imagebutton2) void onSpeciality(){

        mainActivity.replaceFragment(R.layout.fragment_speciality,null);
    }

    @OnClick(R.id.fragment_profile_imagebutton5) void onQualification(){
        mainActivity.replaceFragment(R.layout.fragment_qualification, null);
    }

    @OnClick(R.id.fragment_profile_imagebutton3) void onWorkExperience(){
        showExperienceDialog();
    }

    @OnClick(R.id.fragment_profile_imagebutton4) void onCurrentWorking(){
        showCurrentWorkingDialog();
    }*/

    @OnClick(R.id.fragment_other_profile_imageview1) void onBack(){
        mainActivity.onBackPressed();
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
