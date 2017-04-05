package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public static String FROM;
    public final static String TAG = "EditProfileFragment";
    @Bind(R.id.fragment_edit_profile_editText1) EditText editText;

    public EditProfileFragment() {
        // Required empty public constructor
        FROM = "";
    }

    public static EditProfileFragment newInstance(String TAG) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        checkUpdateField();
        return view;
    }

    public void checkUpdateField(){
        if(EditProfileFragment.FROM.equals(OtherProfileFragment.CITY_TAG)){
            editText.setHint("Edit City");
        } else if(EditProfileFragment.FROM.equals(OtherProfileFragment.MCINUMBER_TAG)){
            editText.setHint("Edit MCI Number");
        } else if(EditProfileFragment.FROM.equals(OtherProfileFragment.WORKEXPERIENCETAG)){
            editText.setHint("Edit work experience");
        }
    }

    @OnClick(R.id.fragment_edit_profile_button1) void onEdit(){

        if(EditProfileFragment.FROM.equals(OtherProfileFragment.CITY_TAG)){
            updateCurrentWorkingData(editText.getText().toString());
            editText.setFocusable(true);
            mainActivity.hideSoftKeyboard(editText);
        } else if(EditProfileFragment.FROM.equals(OtherProfileFragment.MCINUMBER_TAG)){
            updateMCINumberData(editText.getText().toString());
            editText.setFocusable(true);
            mainActivity.hideSoftKeyboard(editText);
        } else if(EditProfileFragment.FROM.equals(OtherProfileFragment.WORKEXPERIENCETAG)){
           updateWorkExperienceData(editText.getText().toString());
            editText.setFocusable(true);
            mainActivity.hideSoftKeyboard(editText);
        }
    }

    @OnClick(R.id.fragment_edit_profile_imageview1) void onBack(){
        mainActivity.onBackPressed();
    }

    public void updateMCINumberData(final String mciNumber){

        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        customer.setMciNumber(mciNumber);
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.upsert(customer.toMap(), new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer object) {
                super.onSuccess(object);
                mainActivity.onBackPressed();
                TastyToast.makeText(mainActivity, "Successfully updated data", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                TastyToast.makeText(mainActivity, "Error in updating data", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });
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
                mainActivity.onBackPressed();
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
                mainActivity.onBackPressed();
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
