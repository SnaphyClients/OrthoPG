package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PaymentRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by nikita on 24/3/17.
 */

public class SavedBooksPresenter {

    RestAdapter restAdapter;
    MainActivity mainActivity;
    DataList<Payment> paymentDataList;
    public double limit = 7;
    public double skip = 0;

    public SavedBooksPresenter(RestAdapter restAdapter, MainActivity mainActivity){

        this.restAdapter = restAdapter;
        this.mainActivity = mainActivity;

        if(Presenter.getInstance().getList(Payment.class, Constants.SAVED_BOOKS_LIST)==null){
            paymentDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.SAVED_BOOKS_LIST, paymentDataList);
        } else{
             paymentDataList = Presenter.getInstance().getList(Payment.class, Constants.SAVED_BOOKS_LIST);
        }
    }

    public void fetchSavedBooks(final boolean reset){
        if(reset){
            skip = 0;
        }

        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        String customerId = String.valueOf(customer.getId());
        if(customerId!=null){
            where.put("customerId", customerId);
            filter.put("where", where);
            PaymentRepository paymentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PaymentRepository.class);
            paymentRepository.find(filter, new DataListCallback<Payment>() {
                @Override
                public void onSuccess(DataList<Payment> objects) {
                    super.onSuccess(objects);
                    if(objects!=null){
                        if(reset){
                            paymentDataList.clear();
                        }
                        paymentDataList.addAll(objects);
                    }
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
