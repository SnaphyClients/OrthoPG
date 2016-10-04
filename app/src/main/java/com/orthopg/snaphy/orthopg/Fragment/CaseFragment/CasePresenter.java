package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostDetailRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class CasePresenter {
    RestAdapter restAdapter;
    DataList<PostDetail> postDetails;
    //public int limit = 5;
    //TODO add loading bar as argument.
    public CasePresenter(RestAdapter restAdapter){
        this.restAdapter = restAdapter;
        postDetails = new DataList<>();
        Presenter.getInstance().addList(Constants.POST_DETAIL_LIST_CASE_FRAGMENT, postDetails);
    }

    /**
     *
     * @param listType String trending|unsolved|new
     */
    public void fetchPost(String listType){
        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, String> include = new HashMap<>();
        include.put("include", "post");
        HashMap<Object,Object> where = new HashMap<>();
        PostDetailRepository postDetailRepository =  restAdapter.createRepository(PostDetailRepository.class);
        postDetailRepository.find(filter, new DataListCallback<PostDetail>() {
            @Override
            public void onBefore() {
                //TODO start loading bar..
            }

            @Override
            public void onSuccess(DataList<PostDetail> objects) {
                postDetails.addAll(objects);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onFinally() {
                //TODO Stop loading bar..
            }
        });
    }
}
