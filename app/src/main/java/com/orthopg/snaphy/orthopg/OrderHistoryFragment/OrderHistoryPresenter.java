package com.orthopg.snaphy.orthopg.OrderHistoryFragment;

import android.content.Context;
import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.OrderRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SpecialityRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by nikita on 26/3/17.
 */

public class OrderHistoryPresenter {

    RestAdapter restAdapter;
    MainActivity mainActivity;
    DataList<Order> orderDataList;
    public double limit = 7;
    public double skip = 0;

    public OrderHistoryPresenter(RestAdapter restAdapter, MainActivity mainActivity){

        this.restAdapter  = restAdapter;
        this.mainActivity = mainActivity;

        if(Presenter.getInstance().getList(Order.class, Constants.ORDER_HISTORY_LIST)==null){
            orderDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.ORDER_HISTORY_LIST, orderDataList);
        } else{
            orderDataList = Presenter.getInstance().getList(Order.class, Constants.ORDER_HISTORY_LIST);
        }
    }

    public void fetchOrders(final boolean reset){

        if(reset){
            skip = 0;
        }

        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        String customerId = (String)customer.getId();
        if(customerId!=null){
            OrderRepository orderRepository = restAdapter.createRepository(OrderRepository.class);
            orderRepository.fetchPastOrder(customerId, new DataListCallback<Order>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    mainActivity.startProgressBar(mainActivity.progressBar);
                }

                @Override
                public void onSuccess(DataList<Order> objects) {
                    super.onSuccess(objects);
                    if(objects!=null){
                        if(reset){
                            orderDataList.clear();
                        }

                        orderDataList.addAll(objects);
                        skip = skip + objects.size();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                }

                @Override
                public void onFinally() {
                    super.onFinally();
                    mainActivity.stopProgressBar(mainActivity.progressBar);
                }
            });

        }
    }
}
