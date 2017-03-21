package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SpecialityRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpecialityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpecialityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecialityFragment extends Fragment {
    MainActivity mainActivity;
    public final static String TAG = "SpecialityFragment";
    @Bind(R.id.fragment_speciality_recyclerview1) RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    SpecialityPresenter specialityPresenter;
    DataList<Speciality> specialityDataList = new DataList<>();
    LinearLayoutManager linearLayoutManager;
    SpecialityAdapter specialityAdapter;

    public SpecialityFragment() {
        // Required empty public constructor
    }


    public static SpecialityFragment newInstance() {
        SpecialityFragment fragment = new SpecialityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_speciality, container, false);
        ButterKnife.bind(this,view);
        linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        subscribe();
        return view;
    }

    public void subscribe(){
        specialityDataList = Presenter.getInstance().getList(Speciality.class, Constants.SPECIALITY_LIST);
        specialityDataList.subscribe(this, new Listen<Speciality>() {
            @Override
            public void onInit(DataList<Speciality> dataList) {
                super.onInit(dataList);
                specialityAdapter = new SpecialityAdapter(mainActivity, specialityDataList);
                recyclerView.setAdapter(specialityAdapter);
            }

            @Override
            public void onChange(DataList<Speciality> dataList) {
                super.onChange(dataList);
                specialityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClear() {
                super.onClear();
                specialityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(Speciality element, int index, DataList<Speciality> dataList) {
                super.onRemove(element, index, dataList);
            }
        });
    }

    public void loadPresenter(){
        specialityPresenter = new SpecialityPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(),mainActivity);
        specialityPresenter.fetchSpecialities(true);

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
