package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Qualification;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SpecialityRepository;
import com.google.common.collect.ContiguousSet;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

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
    @Bind(R.id.fragment_other_profile_textview2) TextView emailTxt;
    @Bind(R.id.fragment_other_profile_textview12) TextView mciNumberTxt;
    @Bind(R.id.fragment_other_profile_textview4) TextView specialityTxt;
    @Bind(R.id.fragment_other_profile_textview7) TextView workExperinceTxt;
    @Bind(R.id.fragment_other_profile_textview9) TextView currentWorkingTxt;
    @Bind(R.id.fragment_other_profile_textview11) TextView qualificationTxt;
    @Bind(R.id.fragment_profile_imagebutton2) ImageButton specialityButton;
    @Bind(R.id.fragment_profile_imagebutton5) ImageButton qualificationButton;

    public final static String TAG = "OtherProfileFragment";

    public OtherProfileFragment() {
        // Required empty public constructor
    }

    public static OtherProfileFragment newInstance() {
        OtherProfileFragment fragment = new OtherProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);
        ButterKnife.bind(this, view);
        setProfileData();
        return view;
    }

    public void setProfileData() {
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if (customer != null) {
            //getSpecialityOfCustomer(customer);

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
                    mciNumberTxt.setVisibility(View.GONE);
                }
            } else {
                mciNumberTxt.setVisibility(View.GONE);
            }
            String workExperience = String.valueOf(customer.getWorkExperience());
            if (workExperience != null) {
                if (!workExperience.isEmpty()) {
                    workExperinceTxt.setVisibility(View.VISIBLE);
                    workExperinceTxt.setText(String.valueOf(customer.getWorkExperience()));
                } else {
                    workExperinceTxt.setVisibility(View.GONE);
                }
            } else {
                workExperinceTxt.setVisibility(View.GONE);
            }

            if (customer.getCurrentCity() != null) {
                if (!customer.getCurrentCity().isEmpty()) {
                    currentWorkingTxt.setVisibility(View.VISIBLE);
                    currentWorkingTxt.setText(String.valueOf(customer.getCurrentCity()));
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
                        setQualification();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                    }
                });
            }
        } else {
            emailTxt.setVisibility(View.GONE);
            mciNumberTxt.setVisibility(View.GONE);
            workExperinceTxt.setVisibility(View.GONE);
            currentWorkingTxt.setVisibility(View.GONE);
            qualificationTxt.setVisibility(View.GONE);
            specialityTxt.setVisibility(View.GONE);
        }
    }


    public void setSpeciality() {
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if (customer.getSpecialities() == null) {
            return;
        }
        if (customer.getSpecialities().size() != 0) {
            specialityTxt.setVisibility(View.VISIBLE);
            String specialities = "";

            DataList<Speciality> speciality = customer.getSpecialities();
            for (Speciality s : speciality) {
                specialities = specialities + s.getName() + "\n";
            }
            specialityTxt.setText(specialities);
        } else {
            specialityTxt.setVisibility(View.GONE);
        }
    }

    public void setQualification(){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer.getQualifications()==null) {
            return;
        }
        if(customer.getQualifications().size()!=0){
            qualificationTxt.setVisibility(View.VISIBLE);
            String qualifications = "";

            DataList<Qualification> qualification = customer.getQualifications();
            for(Qualification q : qualification){
                qualifications = qualifications + q.getName() + "\n";
            }
            qualificationTxt.setText(qualifications);
        } else{
            qualificationTxt.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.fragment_profile_imagebutton2) void onSpeciality(){

        mainActivity.replaceFragment(R.layout.fragment_speciality,null);
    }

    @OnClick(R.id.fragment_profile_imagebutton5) void onQualification(){
        mainActivity.replaceFragment(R.layout.fragment_qualification, null);
    }

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
