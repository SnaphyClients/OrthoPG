package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

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
    @Bind(R.id.fragment_post_answer_progressBar)
    CircleProgressBar progressBar;
    MainActivity mainActivity;
    @Bind(R.id.fragment_post_answer_button1)
    Button postButton;
   public static String FROM;
    Comment comment;
    Post post;
    int position;
    @Bind(R.id.fragment_post_answer_edittext1) EditText answer;

    public PostAnswerFragment() {
        // Required empty public constructor
        FROM = "";
    }

    public static PostAnswerFragment newInstance(String TAG) {
        PostAnswerFragment fragment = new PostAnswerFragment();
        FROM = TAG;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!PostAnswerFragment.FROM.equals(CaseDetailFragment.TAG)) {
            Bundle bundle = this.getArguments();
            position = bundle.getInt("position");
        }
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_answer, container, false);
        ButterKnife.bind(this, view);
        mainActivity.stopProgressBar(progressBar);
        Post post = Presenter.getInstance().getModel(Post.class, Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL);
        answer.setMovementMethod(new ScrollingMovementMethod());
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

    public void enablePostButton(boolean enable) {
        if(enable) {
            postButton.setEnabled(true);
        } else {
            postButton.setEnabled(false);
        }
    }


    private void saveComment(){
        String commentAnswer = answer.getText().toString();
        if(commentAnswer != null){
          if(!commentAnswer.trim().isEmpty()){
            comment.setAnswer(commentAnswer.trim());
              //Now save comment..
              if(comment.getId() != null){
                  CommentRepository commentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CommentRepository.class);
                  HashMap<String, Object> commentObj = new HashMap<>();
                  commentObj.put("answer", comment.getAnswer());
                  commentRepository.updateAttributes((String) comment.getId(), commentObj, new ObjectCallback<Comment>() {
                      @Override
                      public void onBefore() {
                          //SHOW LOADING BAR
                          mainActivity.startProgressBar(progressBar);
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
                          //STOP LOADING BAR
                          mainActivity.stopProgressBar(progressBar);
                          postButton.setEnabled(true);
                      }
                  });
              }else{
                  CommentRepository commentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CommentRepository.class);
                  HashMap<String, Object> commentObj = new HashMap<>();
                  commentObj.put("postId", post.getId());
                  final Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                  if(loginCustomer != null){
                      commentObj.put("customerId", loginCustomer.getId());
                  }
                  commentObj.put("answer", comment.getAnswer());
                  commentRepository.create(commentObj, new ObjectCallback<Comment>() {
                      @Override
                      public void onBefore() {
                          //SHOW LOADING BAR
                          mainActivity.startProgressBar(progressBar);
                      }

                      @Override
                      public void onSuccess(Comment object) {
                          object.addRelation(loginCustomer);
                          object.addRelation(post);
                          if(String.valueOf(PostAnswerFragment.FROM).equals(CaseDetailFragment.TAG)) {
                              DataList<String> exceptIdNewAnswerList = Presenter.getInstance().getModel(DataList.class, Constants.EXCEPTED_NEW_ANSWER_LIST);
                              if (exceptIdNewAnswerList != null) {
                                  if (object != null) {
                                      exceptIdNewAnswerList.add((String) object.getId());
                                  }
                              }


                              //Now add comment to top of the list..
                              if (post != null) {
                                  if (post.getComments() == null) {
                                      post.setComments(new DataList<Comment>());
                                  }
                                  post.getComments().add(0, object);
                              }
                              mainActivity.onBackPressed();
                          } else {
                              DataList<String> homeAnswerList = new DataList<>();
                              if (object != null) {
                                  homeAnswerList.add((String) object.getId());
                                  Presenter.getInstance().addList(Constants.HOME_ANSWER_LIST, homeAnswerList);

                              }

                              if (post != null) {
                                  if (post.getComments() == null) {
                                      post.setComments(new DataList<Comment>());
                                  }
                                  post.getComments().add(0, object);
                              }
                              mainActivity.onBackPressed();
                              mainActivity.replaceFragment(R.id.layout_case_list_textview4, position);
                          }
                      }

                      @Override
                      public void onError(Throwable t) {
                          Log.e(Constants.TAG, t.toString());
                          TastyToast.makeText(mainActivity.getApplicationContext(), Constants.CASE_UPLOAD_ERROR, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                      }

                      @Override
                      public void onFinally() {
                          //STOP LOADING BAR
                          mainActivity.stopProgressBar(progressBar);
                          postButton.setEnabled(true);

                      }
                  });
              }
          }else{
              postButton.setEnabled(true);
              TastyToast.makeText(mainActivity.getApplicationContext(), Constants.BLANK_COMMENT_ERROR, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
          }
        } else {
            postButton.setEnabled(true);
        }
    }

    @OnClick(R.id.fragment_post_answer_button1) void postButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        saveComment();
        postButton.setEnabled(false);

    }

    @OnClick(R.id.fragment_post_answer_image_button1) void crossButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(answer.getWindowToken(), 0);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Remove  both model..
        Presenter.getInstance().removeModelFromList(Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL);
        Presenter.getInstance().removeModelFromList(Constants.EDIT_IN_PROCESS_COMMENT_MODEL);
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
        void onFragmentInteraction(Uri uri);
    }
}
