package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostAnswerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostAnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostAnswerFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "PostAnswerFragment";
    MainActivity mainActivity;
    Comment comment;
    Post post;
    @Bind(R.id.fragment_post_answer_edittext1) EditText answer;

    public PostAnswerFragment() {
        // Required empty public constructor
    }

    public static PostAnswerFragment newInstance() {
        PostAnswerFragment fragment = new PostAnswerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_answer, container, false);
        ButterKnife.bind(this, view);

        Post post = Presenter.getInstance().getModel(Post.class, Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL);
        if(post != null){
            this.post = post;
            //Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL
            Comment comment = Presenter.getInstance().getModel(Comment.class, Constants.EDIT_IN_PROCESS_COMMENT_MODEL);
            if(comment == null){
                CommentRepository commentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CommentRepository.class);
                HashMap<String, Object> commentObj = new HashMap<>();
                commentObj.put("postId", post.getId());
                comment = commentRepository.createObject(commentObj);
                this.comment = comment;
            }else{
                this.comment = comment;
            }
            //Now load the data in edit..model
            loadData();
        }


        return view;
    }

    private void loadData(){
        if(comment != null){
            if(comment.getAnswer() != null){
                if(!comment.getAnswer().trim().isEmpty()){
                    answer.setText(comment.getAnswer());
                }
            }
        }
    }


    private void saveComment(){
        String commentAnswer = answer.getText().toString();
        if(commentAnswer != null){
          if(!commentAnswer.trim().isEmpty()){
            comment.setAnswer(commentAnswer.trim());
              //Now save comment..
              if(comment.getId() != null){
                  //TODO:SHOW LOADING BAR
                  comment.save(new VoidCallback() {
                      @Override
                      public void onSuccess() {
                          //TODO: ADD TO LIST..
                          //TODO: NOTIFY CHANGES TO LIST
                          mainActivity.onBackPressed();
                      }

                      @Override
                      public void onError(Throwable t) {
                          //TODO: STOP LOADING BAR
                          Log.e(Constants.TAG, t.toString());
                          TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                      }
                  });
              }else{
                  CommentRepository commentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CommentRepository.class);
                  HashMap<String, Object> commentObj = new HashMap<>();
                  commentObj.put("postId", post.getId());
                  Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                  if(loginCustomer != null){
                      commentObj.put("customerId", loginCustomer.getId());
                  }
                  commentObj.put("answer", comment.getAnswer());
                  commentRepository.create(commentObj, new ObjectCallback<Comment>() {
                      @Override
                      public void onBefore() {
                          //TODO:SHOW LOADING BAR
                      }

                      @Override
                      public void onSuccess(Comment object) {
                          //TODO: ADD TO LIST..
                          //TODO: NOTIFY CHANGES TO LIST
                          mainActivity.onBackPressed();
                      }

                      @Override
                      public void onError(Throwable t) {
                          Log.e(Constants.TAG, t.toString());
                          TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                      }

                      @Override
                      public void onFinally() {
                          //TODO: STOP LOADING BAR

                      }
                  });
              }
          }else{
              TastyToast.makeText(mainActivity.getApplicationContext(), Constants.BLANK_COMMENT_ERROR, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
          }
        }
    }

    @OnClick(R.id.fragment_post_answer_button1) void postButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        saveComment();

    }

    @OnClick(R.id.fragment_post_answer_image_button1) void crossButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
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
