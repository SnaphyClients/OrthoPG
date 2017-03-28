package com.orthopg.snaphy.orthopg.OrderHistoryFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 26/3/17.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<Order> orderDataList;

    public OrderHistoryAdapter(MainActivity mainActivity, DataList<Order> orderDataList){

        this.mainActivity = mainActivity;
        this.orderDataList = orderDataList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderHistoryView = inflater.inflate(R.layout.layout_order_history, parent, false);
        OrderHistoryAdapter.ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(orderHistoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Order order = orderDataList.get(position);
        ImageView bookCover = holder.bookCover;
        TextView bookName = holder.bookName;
        TextView bookTransactionId = holder.bookTransactionId;
        TextView bookPrice = holder.bookPrice;
        TextView bookStatus = holder.bookStatus;
        TextView bookOrderNo = holder.bookOrderNo;
        TextView bookPaymentStatus= holder.bookPaymentStatus;

        if(order!=null){
            if(order.getBook()!=null){
                if(order.getBook().getFrontCover()!=null){
                    bookCover.setVisibility(View.VISIBLE);
                    mainActivity.snaphyHelper.loadUnsignedUrl(order.getBook().getFrontCover(), bookCover);
                } else{
                    bookCover.setVisibility(View.GONE);
                }

                if(order.getBook().getTitle()!=null){
                    if(!order.getBook().getTitle().isEmpty()){
                        bookName.setVisibility(View.VISIBLE);
                        bookName.setText(order.getBook().getTitle().toString());
                    } else{
                        bookName.setVisibility(View.GONE);
                    }
                } else{
                    bookName.setVisibility(View.GONE);
                }
            }
                if(order.getOrderNumber()!=null){
                   if(!order.getOrderNumber().isEmpty()){
                       bookOrderNo.setVisibility(View.VISIBLE);
                       bookOrderNo.setText(order.getOrderNumber());
                   } else{
                       bookOrderNo.setVisibility(View.GONE);
                   }

                } else{
                    bookOrderNo.setVisibility(View.GONE);
                }

                if(String.valueOf(order.getAmount())!=null){
                    if(!String.valueOf(order.getAmount()).isEmpty()){
                        bookPrice.setVisibility(View.VISIBLE);
                        bookPrice.setText(String.valueOf(order.getAmount()));
                    } else{
                        bookPrice.setVisibility(View.GONE);
                    }
                } else{
                    bookPrice.setVisibility(View.GONE);
                }

                if(order.getOrderStatus()!=null){
                    if(!order.getOrderStatus().isEmpty()){
                        bookStatus.setVisibility(View.VISIBLE);
                        bookStatus.setText(order.getOrderStatus());
                    } else{
                        bookStatus.setVisibility(View.GONE);
                    }
                } else{
                    bookStatus.setVisibility(View.GONE);
                }

                if(order.getTransactionId()!=null){
                    if(order.getTransactionId().isEmpty()){
                        bookTransactionId.setVisibility(View.VISIBLE);
                        bookTransactionId.setText(order.getTransactionId().toString());
                    } else{
                        bookTransactionId.setVisibility(View.GONE);
                    }
                } else{
                    bookTransactionId.setVisibility(View.GONE);
                }

            if (order.getPaymentStatus() != null) {
                if(order.getPaymentStatus().isEmpty()){
                    if(order.getPaymentStatus().equalsIgnoreCase("Failed")){
                        bookPaymentStatus.setVisibility(View.VISIBLE);
                    } else{
                        bookPaymentStatus.setVisibility(View.VISIBLE);
                    }
                } else{
                    bookPaymentStatus.setVisibility(View.VISIBLE);
                }
            } else{
                bookPaymentStatus.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_order_history_imageview1) ImageView bookCover;
        @Bind(R.id.layout_order_history_textview1) TextView bookName;
        @Bind(R.id.layout_order_history_textview2) TextView bookTransactionId;
        @Bind(R.id.layout_order_history_textview4) TextView bookPrice;
        @Bind(R.id.layout_order_history_textview5) TextView bookStatus;
        @Bind(R.id.layout_order_history_textview7) TextView bookOrderNo;
        @Bind(R.id.layout_order_history_textview8) TextView bookPaymentStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
