package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.activity.HomeActivity;
import com.ats.shivshambhoo.fragment.PurchaseOrderFragment;
import com.ats.shivshambhoo.model.GetQuotDetail;
import com.ats.shivshambhoo.model.QuotationHeaderDisp;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GenratePOListAdapter extends RecyclerView.Adapter<GenratePOListAdapter.MyViewHolder> {
    private ArrayList<QuotationHeaderDisp> quoteList;
    private Context context;


    public GenratePOListAdapter(ArrayList<QuotationHeaderDisp> quoteList, Context context) {
        this.quoteList = quoteList;
        this.context = context;
    }

    @NonNull
    @Override
    public GenratePOListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.genrate_po_list_adapter, viewGroup, false);

        return new GenratePOListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GenratePOListAdapter.MyViewHolder myViewHolder, int i) {
        final QuotationHeaderDisp model = quoteList.get(i);
        // final DocumentHeader model1 = quoteTermList.get(position);

            myViewHolder.tvCustName.setText(model.getCustName());
            myViewHolder.tvQuoteNo.setText("Quotation No:"+model.getQuotNo());
            myViewHolder.tvDate.setText(model.getQuotDate());
            //  holder.tvRemark.setText(model.getOtherRemark1());
            if(model.getStatus()==1)
            {
                myViewHolder.tvRemark.setText("Quotation Generated");
            }else if(model.getStatus()==2)
            {
                myViewHolder.tvRemark.setText("Po Generated");
            }
            //myViewHolder.tvRemark.setText(Integer.toString(model.getStatus()));


//        if (model.getStatus() < 2) {
//            myViewHolder.ivEdit.setVisibility(View.VISIBLE);
//        } else {
//            myViewHolder.ivEdit.setVisibility(View.GONE);
//        }

        if (model.getGetQuotDetailList()!= null) {
            ArrayList<GetQuotDetail> detailList = new ArrayList<>();
            for (int j = 0; j < model.getGetQuotDetailList().size(); j++) {
                detailList.add(model.getGetQuotDetailList().get(j));
            }

            QuotationDetailListAdapter adapter = new QuotationDetailListAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(mLayoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            myViewHolder.recyclerView.setAdapter(adapter);
        }


        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItems.setVisibility(View.VISIBLE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
        } else {
            myViewHolder.llItems.setVisibility(View.GONE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
        }

        myViewHolder.tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItems.setVisibility(View.VISIBLE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItems.setVisibility(View.GONE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
                }
            }
        });
        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_genrate_po, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_genrate_po) {
                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            PurchaseOrderFragment adf = new PurchaseOrderFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            args.putString("Name", model.getCustName());
                            args.putString("payTerms", model.getPayTerms());
                            args.putString("poNo", model.getQuotNo());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "QuotationListFragment").commit();

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
        return quoteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustName, tvQuoteNo, tvDate, tvRemark, tvItems;
        public RecyclerView recyclerView;
        public CardView cardView;
        public ImageView ivEdit, imageView;
        public LinearLayout llItems;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvQuoteNo = itemView.findViewById(R.id.tvQuoteNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvItems = itemView.findViewById(R.id.tvItems);
            llItems = itemView.findViewById(R.id.llItems);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
