package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.model.Cust;

import java.util.ArrayList;

public class CustomerListAddOrderAdapter extends RecyclerView.Adapter<CustomerListAddOrderAdapter.MyViewHolder> {
    private ArrayList<Cust> custaddOrderList;
    private Context context;

    public CustomerListAddOrderAdapter(ArrayList<Cust> custaddOrderList, Context context) {
        this.custaddOrderList = custaddOrderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerListAddOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_customer_addorder_dialog, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListAddOrderAdapter.MyViewHolder myViewHolder, int i) {
        final Cust model = custaddOrderList.get(i);

        myViewHolder.tvName.setText(model.getCustName());
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customerDataIntent = new Intent();
                customerDataIntent.setAction("CUSTOMER_DATA");
                customerDataIntent.putExtra("name", model.getCustName());
                customerDataIntent.putExtra("id", model.getCustId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return custaddOrderList.size();
    }

    public void updateList(ArrayList<Cust> list) {
        custaddOrderList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            linearLayout = itemView.findViewById(R.id.linearLayout_addorder);
        }
    }
}
