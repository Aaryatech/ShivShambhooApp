package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.activity.HomeActivity;
import com.ats.shivshambhoo.fragment.PODetailListFragment;
import com.ats.shivshambhoo.model.PoListModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class POListAdapter extends RecyclerView.Adapter<POListAdapter.MyViewHolder>  {
    private ArrayList<PoListModel> poList;
    private Context context;


    public POListAdapter(ArrayList<PoListModel> poList, Context context) {
        this.poList = poList;
        this.context = context;
    }

    @NonNull
    @Override
    public POListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.genrate_po_detail_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final POListAdapter.MyViewHolder myViewHolder, int i) {
        final PoListModel model = poList.get(i);
        // final DocumentHeader model1 = quoteTermList.get(position);

        myViewHolder.tvCustName.setText(model.getCustName());
        myViewHolder.tvQuoteNo.setText("Quotation No : "+model.getQuatationNo());
        myViewHolder.tvPoNo.setText("PO No : "+model.getPoNo());
        myViewHolder.tvDate.setText(model.getPoDate());
        //  holder.tvRemark.setText(model.getOtherRemark1());
        // holder.tvRemark.setText(Integer.toString(model.getStatus()));
        if(model.getStatus()==0)
        {
            myViewHolder.tvRemark.setText("Pending");
        }else if(model.getStatus()==1)
        {
            myViewHolder.tvRemark.setText("Partially Close");
        }else if(model.getStatus()==2)
        {
            myViewHolder.tvRemark.setText("Completed");
        }
//        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                String json = gson.toJson(model);
//
//                HomeActivity activity = (HomeActivity) context;
//
//                PODetailListFragment adf = new PODetailListFragment();
//                Bundle args = new Bundle();
//                args.putString("model", json);
//                args.putString("remark", myViewHolder.tvRemark.getText().toString());
//                adf.setArguments(args);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "POListFragment").commit();
//
//            }
//        });
        myViewHolder.ivEditPo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_po_list_edit, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            PODetailListFragment adf = new PODetailListFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            args.putString("remark", myViewHolder.tvRemark.getText().toString());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "POListFragment").commit();

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return poList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustName, tvQuoteNo, tvDate, tvRemark, tvPoNo;
        public CardView cardView;
        public ImageView ivEditPo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvQuoteNo = itemView.findViewById(R.id.tvQuoteNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            tvPoNo = itemView.findViewById(R.id.tvPoNo);
            cardView=itemView.findViewById(R.id.cardView);
            ivEditPo=itemView.findViewById(R.id.ivEditPo);
        }
    }
}
