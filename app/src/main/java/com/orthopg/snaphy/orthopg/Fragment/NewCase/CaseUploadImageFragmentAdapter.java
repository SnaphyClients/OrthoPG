package com.orthopg.snaphy.orthopg.Fragment.NewCase;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.orthopg.snaphy.orthopg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/30/2016.
 */
public class CaseUploadImageFragmentAdapter extends RecyclerView.Adapter<CaseUploadImageFragmentAdapter.ViewHolder>{

    List<Uri> imageURI;

    public CaseUploadImageFragmentAdapter(List<Uri> imageURI) {
        this.imageURI = imageURI;
    }

    @Override
    public CaseUploadImageFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View caseImageView = inflater.inflate(R.layout.layout_upload_image_recycler_view, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(caseImageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CaseUploadImageFragmentAdapter.ViewHolder holder, int position) {
        Uri uri = imageURI.get(position);

        ImageView imageView = holder.imageView;
        ImageButton delete = holder.delete;

        imageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return imageURI.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_upload_image_recycler_view_imageview1) ImageView imageView;
        @Bind(R.id.layout_upload_image_recycler_view_button1)
        ImageButton delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
