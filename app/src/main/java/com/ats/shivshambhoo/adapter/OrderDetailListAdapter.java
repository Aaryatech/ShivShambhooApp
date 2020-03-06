package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.model.GetOrderDetail;

import java.util.ArrayList;

public class OrderDetailListAdapter extends RecyclerView.Adapter<OrderDetailListAdapter.MyViewHolder> {

    private ArrayList<GetOrderDetail> detailList;
    private Context context;

    public OrderDetailListAdapter(ArrayList<GetOrderDetail> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem, tvPOQty, tvRemQty, tvPORate, tvTotal;
        EditText edOrderQty;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvPOQty = view.findViewById(R.id.tvPOQty);
            tvRemQty = view.findViewById(R.id.tvRemQty);
            tvPORate = view.findViewById(R.id.tvPORate);
            edOrderQty = view.findViewById(R.id.edOrderQty);
            tvTotal = view.findViewById(R.id.tvTotal);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_detail_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GetOrderDetail model = detailList.get(position);

        holder.tvItem.setText(model.getItemName());
        holder.tvPOQty.setText("" + model.getPoQty());
        holder.tvRemQty.setText("" + model.getOrderQty());
        holder.tvPORate.setText("" + model.getPoRate());
        holder.edOrderQty.setText("" + model.getOrderQty());
        holder.tvTotal.setText("" + (model.getPoRate()*model.getOrderQty()));


        holder.edOrderQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float orderQty = Float.parseFloat(s.toString());
                model.setOrderQty(orderQty);

                float PoRate = model.getPoRate();
                float Qty = model.getOrderQty();

                float Total = PoRate * Qty;
                model.setTotal(Total);

                holder.tvTotal.setText("" + model.getTotal());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLight));
        }


    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

}
