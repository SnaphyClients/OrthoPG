package com.orthopg.snaphy.orthopg.OrderHistoryFragment;

import android.content.Context;
import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
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
    String localOrderBy = "datetime(added) DESC";

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

    public void fetchOrders(final boolean reset) {

        if (reset) {
            skip = 0;
        }

        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        String customerId = (String) customer.getId();
        if (customerId != null) {
            OrderRepository orderRepository = restAdapter.createRepository(OrderRepository.class);
            orderRepository.addStorage(mainActivity);
            if (!mainActivity.snaphyHelper.isNetworkAvailable()) {
                loadOfflineOrderData();
            } else {
                orderRepository.fetchPastOrder(customerId, new DataListCallback<Order>() {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                        if (mainActivity != null) {
                            mainActivity.startProgressBar(mainActivity.progressBar);
                        }
                        setOldFlag();
                    }

                    @Override
                    public void onSuccess(DataList<Order> objects) {
                        super.onSuccess(objects);
                        if (objects != null) {
                            if (reset) {
                                orderDataList.clear();
                            }
                            for (Order order : objects) {
                                if (order != null) {
                                    saveOrderData(order);
                                }
                            }
                            orderDataList.addAll(objects);
                            skip = skip + objects.size();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                        loadOfflineOrderData();
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

    public void setOldFlag(){
        OrderRepository orderRepository = restAdapter.createRepository(OrderRepository.class);
        orderRepository.addStorage(mainActivity);
        orderRepository.getDb().checkOldData__db();
    }

    public void saveOrderData(Order order){
        BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
        bookRepository.addStorage(mainActivity);
        if(order.getBook()!=null){
            bookRepository.getDb().upsert__db(order.getBook().getId().toString(), order.getBook());
        }

        OrderRepository orderRepository = restAdapter.createRepository(OrderRepository.class);
        orderRepository.addStorage(mainActivity);
        orderRepository.getDb().upsert__db(order.getId().toString(), order);
    }


    public void loadOfflineOrderData(){
        orderDataList.clear();
        OrderRepository orderRepository = restAdapter.createRepository(OrderRepository.class);
        orderRepository.addStorage(mainActivity);
        HashMap<String, Object> localFlagQuery = new HashMap<>();
        if(orderRepository.getDb().count__db(localFlagQuery, localOrderBy,50)>0){
            orderDataList.addAll(orderRepository.getDb().getAll__db(localFlagQuery, localOrderBy,50));
        }
    }
}
