package com.orthopg.snaphy.orthopg.Fragment.NewCase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Scroller;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.NewCase;
import com.orthopg.snaphy.orthopg.CustomModel.TrackImage;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaseDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaseDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseDescriptionFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public static String TAG = "CaseDescriptionFragment";
    @Bind(R.id.fragment_case_description_edittext1) EditText description;
    @Bind(R.id.fragment_case_description_progressBar) CircleProgressBar progressBar;
    @Bind(R.id.fragment_case_description_checkbox) CheckBox checkBox;
    @Bind(R.id.fragment_case_description_button1) Button postCaseButton;
    @Bind(R.id.fragment_case_description_image_button1) ImageButton backButton;

    public CaseDescriptionFragment() {
        // Required empty public constructor
    }

    public static CaseDescriptionFragment newInstance() {
        CaseDescriptionFragment fragment = new CaseDescriptionFragment();
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
        View view = inflater.inflate(R.layout.fragment_case_description, container, false);
        ButterKnife.bind(this, view);
        mainActivity.stopProgressBar(progressBar);
        description.setScroller(new Scroller(mainActivity));
        description.setVerticalScrollBarEnabled(true);
        description.setMovementMethod(new ScrollingMovementMethod());
        //Load previous data..
        loadDescription();
        return view;
    }

    public void enablePostCaseButton() {
        postCaseButton.setEnabled(true);
        backButton.setEnabled(true);
    }

    public void disablePostCaseButton() {
        postCaseButton.setEnabled(false);
        backButton.setEnabled(false);
    }


    public void loadDescription(){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            if(newCase.getPost() != null){
                if(newCase.getPost().getDescription() != null){
                    if(!newCase.getPost().getDescription().isEmpty()){
                        //Load the heading..
                        description.setText(newCase.getPost().getDescription());
                    }
                }
            }
        }
    }


    public void saveDescription(String desc){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            if(newCase.getPost() != null){
                newCase.getPost().setDescription(desc);
            }
        }
    }



    @OnClick(R.id.fragment_case_description_button1) void postCase() {
        InputMethodManager im = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(description.getWindowToken(), 0);


        if(description.getText() != null) {
            String desc = description.getText().toString();
            if (desc != null) {
                desc = desc.trim();
                    //Now load desc to post.
                    saveDescription(desc);
                    postCaseAsAnonymousDialog();
                    //Now add to Presenter..
                    //TODO: DISPLAY THIS AFTER SUCCESSFULL SAVING ..
                    //mainActivity.replaceFragment(R.id.fragment_case_description_button1, null);
                }
        }

        //ASK FOR ANONYMOUS POST AND SAVE THE CASE...
        //Toast.makeText(mainActivity, "Case has been posted", Toast.LENGTH_SHORT).show();
    }


    public void saveCase(Boolean isAnonym){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            final Post post = newCase.getPost();
            if(post != null){
                post.setAnonymous(isAnonym);
                //Now save the data.
                newCase.saveAllImages(new DataListCallback<TrackImage>() {
                    @Override
                    public void onBefore() {
                        //TastyToast.makeText(mainActivity.getApplicationContext(), Constants.SAVING_POST, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
                        mainActivity.startProgressBar(progressBar);
                        //DISABLE ALL BUTTONS
                        disablePostCaseButton();
                    }

                    @Override
                    public void onSuccess(DataList<TrackImage> objects) {
                        mainActivity.startProgressBar(progressBar);
                        DataList<Map<String, Object>> downloadedImageList = new DataList<Map<String, Object>>();
                        for(TrackImage imageObj: objects){
                            if(imageObj.getImageModel() != null){
                                HashMap<String, Object> downloadedImageObj = imageObj.getImageModel().getHashMap();
                                downloadedImageList.add(downloadedImageObj);
                            }
                        }

                        //set post image data..
                        post.setPostImages(downloadedImageList);
                        PostRepository postRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PostRepository.class);
                        if(post.getId() != null){
                            Map<String, ? extends Object> postObj = post.toMap();
                            postObj.remove("id");
                            postObj.remove("comments");
                            postObj.remove("likePosts");
                            postObj.remove("savePosts");
                            postObj.remove("postSubscribers");
                            postObj.remove("postDetails");
                            postObj.remove("customer");
                            //Update..
                            postRepository.updateAttributes((String) post.getId(), postObj, new ObjectCallback<Post>() {
                                @Override
                                public void onBefore() {
                                    mainActivity.startProgressBar(progressBar);
                                    //DISABLE ALL BUTTONS
                                    disablePostCaseButton();
                                }

                                @Override
                                public void onSuccess(Post object) {
                                    moveToHome();
                                }

                                @Override
                                public void onError(Throwable t) {
                                    TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                }

                                @Override
                                public void onFinally() {
                                    mainActivity.stopProgressBar(progressBar);
                                    //ENABLE ALL BUTTONS
                                    enablePostCaseButton();
                                }
                            });

                        }else{
                            Map<String, Object> postObj = new HashMap<String, Object>();
                            Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                            if(loginCustomer == null){
                                mainActivity.stopProgressBar(progressBar);
                                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                //ENABLE ALL BUTTONS
                                enablePostCaseButton();
                                return;
                            }
                            postObj.put("customerId",  (String)loginCustomer.getId());
                            postObj.putAll(post.convertMap());
                            postObj.remove("id");
                            postObj.remove("comments");
                            postObj.remove("likePosts");
                            postObj.remove("savePosts");
                            postObj.remove("postSubscribers");
                            postObj.remove("postDetails");
                            postObj.remove("customer");
                            postRepository.create(postObj, new ObjectCallback<Post>() {
                                @Override
                                public void onBefore() {
                                    disablePostCaseButton();
                                    //DISABLE ALL BUTTONS
                                }

                                @Override
                                public void onSuccess(Post object) {
                                    moveToHome();
                                }

                                @Override
                                public void onError(Throwable t) {
                                    TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }

                                @Override
                                public void onFinally() {
                                    mainActivity.stopProgressBar(progressBar);
                                    //ENABLE ALL BUTTONS
                                    enablePostCaseButton();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                    @Override
                    public void onFinally() {
                        mainActivity.stopProgressBar(progressBar);
                        //ENABLE ALL BUTTONS
                        enablePostCaseButton();
                    }
                });
            }
        }
    }


    public void moveToHome(){
        TastyToast.makeText(mainActivity.getApplicationContext(), Constants.SUCCESS_SAVED, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        for(int i = 0; i< 3; i++) {
            mainActivity.onBackPressed();
        }
    }

    public void postCaseAsAnonymousDialog(){
        if(checkBox.isChecked()){
            saveCase(true);
        } else {
            saveCase(false);
        }
        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
        alertDialogBuilder.setMessage("Post this case as anonymous");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // Do if user in anonymous

                mainActivity.finish();

            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mainActivity.finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }


    @OnClick(R.id.fragment_case_description_image_button1) void backButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
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
        mainActivity = (MainActivity) getActivity();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
