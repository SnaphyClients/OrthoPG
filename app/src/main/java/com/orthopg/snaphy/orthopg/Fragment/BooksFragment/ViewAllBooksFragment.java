package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewAllBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewAllBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllBooksFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public final static String TAG = "ViewAllBooksFragment";
    List<BookListModel> bookListModelList = new ArrayList<>();
    ViewAllBooksAdapter viewAllBooksAdapter;
    @Bind(R.id.fragment_view_all_books_recyclerview) RecyclerView recyclerView;

    public ViewAllBooksFragment() {
        // Required empty public constructor
    }

    public static ViewAllBooksFragment newInstance() {
        ViewAllBooksFragment fragment = new ViewAllBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeBookList();
    }

    public void initializeBookList(){

        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.sampleimage)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover1)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover2)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover3)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover4)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover5)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover6)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover7)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover8)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover9)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover1)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover2)));
        bookListModelList.add(new BookListModel("Theory of everything",getResources().getDrawable(R.drawable.bookcover3)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_all_books, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new GridLayoutManager(mainActivity,3));
        viewAllBooksAdapter = new ViewAllBooksAdapter(mainActivity,bookListModelList);
        recyclerView.setAdapter(viewAllBooksAdapter);
        return view;
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
