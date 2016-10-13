package com.orthopg.snaphy.orthopg.Fragment.NewCase;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaseUploadImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaseUploadImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseUploadImageFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CaseUploadImageFragment";
    MainActivity mainActivity;
    @Bind(R.id.fragment_case_upload_image_recycler_view) RecyclerView recyclerView;
    List<Uri> imageURI = new ArrayList<>();
    CaseUploadImageFragmentAdapter caseUploadImageFragmentAdapter;
    final int CROP_PIC = 2;
    //http://stackoverflow.com/questions/15807766/android-crop-image-size

    public CaseUploadImageFragment() {
        // Required empty public constructor
    }

    public static CaseUploadImageFragment newInstance() {
        CaseUploadImageFragment fragment = new CaseUploadImageFragment();
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
        View view = inflater.inflate(R.layout.fragment_case_upload_image, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        caseUploadImageFragmentAdapter = new CaseUploadImageFragmentAdapter(imageURI);
        recyclerView.setAdapter(caseUploadImageFragmentAdapter);
        EasyImage.configuration(mainActivity)
                .setImagesFolderName("OrthoPG")
                .saveInRootPicturesDirectory()
                .setCopyExistingPicturesToPublicLocation(true);
        return view;
    }

    @OnClick(R.id.fragment_case_upload_image_imageButton1) void backButton() {
        mainActivity.onBackPressed();
    }

    /*@OnClick(R.id.fragment_case_upload_image_imageButton2) void postAsAnonymous() {

    }
*/
    @OnClick(R.id.fragment_case_upload_image_linear_layout3) void openGallery() {
        openGalleryFolder();
    }

    @OnClick(R.id.fragment_case_upload_image_imageButton3) void cameraButton() {
        openGalleryFolder();
    }

    @OnClick(R.id.fragment_case_upload_image_textview1) void cameraText() {
        openGalleryFolder();
    }

    @OnClick(R.id.fragment_case_upload_image_button1) void nextButton() {
        mainActivity.replaceFragment(R.id.fragment_case_upload_image_button1, null);
    }

    public void openGalleryFolder() {
        View view1 = mainActivity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        int permissionCheck = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            EasyImage.openDocuments(this);
        } else {
            Nammu.askForPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    EasyImage.openGallery(mainActivity);
                }

                @Override
                public void permissionRefused() {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, mainActivity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handle the image
                final Uri uri = Uri.fromFile(imageFile);
                performCrop(uri);

                //Now upload image..
            }

        });

            if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                imageURI.add(getImageUri(mainActivity, thePic));
                caseUploadImageFragmentAdapter.notifyDataSetChanged();
            }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * this function does the crop operation.
     */
    private void performCrop(Uri uri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 3);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(mainActivity, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
