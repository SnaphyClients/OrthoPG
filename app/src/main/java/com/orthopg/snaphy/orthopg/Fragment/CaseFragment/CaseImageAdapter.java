package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/29/2016.
 */
public class CaseImageAdapter extends RecyclerView.Adapter<CaseImageAdapter.ViewHolder> {

    List<Map<String, Object>> imageList;
    MainActivity mainActivity;
    public CaseImageAdapter(MainActivity mainActivity, List<Map<String, Object>> imageList) {
        this.imageList = imageList;
        this.mainActivity = mainActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View caseImageView = inflater.inflate(R.layout.layout_horizontal_recycler_view, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(caseImageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> drawableObj = imageList.get(position);
        ImageView caseImage = holder.caseImage;
        if(drawableObj != null){
            mainActivity.snaphyHelper.loadUnsignedUrl(drawableObj, caseImage);
        }else{
            caseImage.setImageDrawable(null);
        }

        //caseImage.setImageDrawable(drawableObj);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_horizontal_recycler_view_imageview)
        ImageView caseImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
