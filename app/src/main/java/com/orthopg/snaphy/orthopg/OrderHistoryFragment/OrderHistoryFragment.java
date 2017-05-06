package com.orthopg.snaphy.orthopg.OrderHistoryFragment;

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
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public final static String TAG = "OrderHistoryFragment";
    DataList<Order> orderDataList = new DataList<>();
    OrderHistoryAdapter orderHistoryAdapter;
    OrderHistoryPresenter orderHistoryPresenter;
    @Bind(R.id.fragment_order_history_recyclerview) RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    public static OrderHistoryFragment newInstance() {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this,view);
        linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        subscribe();
        return view;
    }

    public void loadPresenter(){
        orderHistoryPresenter = new OrderHistoryPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), mainActivity);
        orderHistoryPresenter.fetchOrders(true);
    }

    public void subscribe(){
        orderDataList = Presenter.getInstance().getList(Order.class, Constants.ORDER_HISTORY_LIST);
        if(orderDataList!=null){
            orderDataList.subscribe(this, new Listen<Order>() {
                @Override
                public void onInit(DataList<Order> dataList) {
                    super.onInit(dataList);
                    orderHistoryAdapter = new OrderHistoryAdapter(mainActivity,orderDataList);
                    recyclerView.setAdapter(orderHistoryAdapter);
                }

                @Override
                public void onChange(DataList<Order> dataList) {
                    super.onChange(dataList);
                    orderHistoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onClear() {
                    super.onClear();
                    orderHistoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRemove(Order element, int index, DataList<Order> dataList) {
                    super.onRemove(element, index, dataList);
                }
            });
        }
    }

    @OnClick(R.id.fragment_order_history_imageview1) void onBack(){
        mainActivity.onBackPressed();
        mainActivity.stopProgressBar(mainActivity.progressBar);
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
