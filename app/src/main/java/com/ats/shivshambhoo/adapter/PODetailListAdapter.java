package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.model.GetQuotDetail;

import java.util.ArrayList;

public class PODetailListAdapter extends RecyclerView.Adapter<PODetailListAdapter.MyViewHolder> {
    private ArrayList<GetQuotDetail> detailList;
    private Context context;
     float Total;
    private float km;
    private int isTaxExtras;


//    public PODetailListAdapter(ArrayList<GetQuotDetail> detailList, Context context) {
//        this.detailList = detailList;
//        this.context = context;
//    }

    public PODetailListAdapter(ArrayList<GetQuotDetail> detailList, Context context, int isTaxExtras) {
        this.detailList = detailList;
        this.context = context;
        this.isTaxExtras = isTaxExtras;
    }

    @NonNull
    @Override
    public PODetailListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.purchase_order_detail_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PODetailListAdapter.MyViewHolder myViewHolder, final int i) {
        final GetQuotDetail model = detailList.get(i);
        float GST=detailList.get(i).getCgstPer();
        float GST1=detailList.get(i).getSgstPer();
        float GST2=detailList.get(i).getIgstPer();
        float GstAdd=GST+GST1+GST2;
        Log.e("Mytag","GSTAdd"+GstAdd);
        Log.e("Mytag","GST"+GST);
        Log.e("Mytag","GST1"+GST1);



        Log.e("Mytag","---------------model"+model);
        myViewHolder.tvName.setText(model.getItemName());
        myViewHolder.etQty.setText("" + model.getQuotQty());
        myViewHolder.etQty1.setText("" + model.getExInt1());
        myViewHolder.tvTaxable.setText("" +model.getTaxableValue());
        myViewHolder.tvTax.setText("" +model.getTaxValue());
        myViewHolder.etCostAfterTax.setText("" +model.getOtherCostAfterTax());
        myViewHolder.tvGst.setText(""+GstAdd +"%");

        myViewHolder.tvFinal.setText(""+model.getTotal());

        try {

            Log.e("Mytag","taxable----------------"+ model.getTaxableValue());
            Log.e("isTaxExtras","----------------"+isTaxExtras);
//            if (isTaxExtras == 1) {
            if (isTaxExtras == 1) {
                float taxValue = ((model.getTaxableValue())* (model.getSgstPer() + model.getCgstPer()))/100;
                model.setTaxValue(taxValue);
            } else if (isTaxExtras == 0) {
                model.setTaxValue(0);
            }
            myViewHolder.tvTax.setText("" + model.getTaxValue());

           // Log.e("Mytag","taxValue----------------"+taxValue);
            Log.e("Mytag","GST----------------"+model.getSgstPer() + model.getCgstPer());

            float taxable = model.getTaxableValue();
            float tax = model.getTaxValue();
            float afterTax = model.getOtherCostAfterTax();

            Log.e("taxable","------------------------------"+taxable);
            Log.e("tax","------------------------"+tax);
            Log.e("afterTax","---------------------------"+afterTax);


            float total = taxable + tax + afterTax;
            model.setTotal(total);
            myViewHolder.tvFinal.setText("" +model.getTotal());
            Log.e("Total","---------------------------"+total);


        } catch (Exception e) {
        }


        if (i % 2 == 0) {
            myViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        } else {
            myViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }


        myViewHolder.etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    float qty = Float.parseFloat(charSequence.toString());
                    model.setQuotQty(qty);

                } catch (Exception e) {
                    float qty = 0;
                    model.setQuotQty(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myViewHolder.etQty1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int qty = Integer.parseInt(charSequence.toString());
                    model.setExInt1(qty);

                } catch (Exception e) {
                    float qty = 0;
                    model.setExInt1(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        myViewHolder.etCostAfterTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {


                    float afterTaxVal = Float.parseFloat(s.toString());
                    model.setOtherCostAfterTax(afterTaxVal);

                    float rate = model.getRate();
                    float royalty = model.getRoyaltyRate();
                    float trans = model.getTransCost();
                    float toll = model.getTollCost();
                    float other = model.getOtherCost();

                    float taxableCost = rate + royalty + trans + other;
                    //toll + other;
                    model.setTaxableValue(taxableCost);
                    myViewHolder.tvTaxable.setText("" + model.getTaxableValue());

                    if (isTaxExtras == 1) {
                        float taxValue = ((model.getTaxableValue())* (model.getSgstPer() + model.getCgstPer()))/100;
                        model.setTaxValue(taxValue);
                    } else if (isTaxExtras == 0) {
                        model.setTaxValue(0);
                    }
                    myViewHolder.tvTax.setText("" + model.getTaxValue());

                    float taxable = model.getTaxableValue();
                    float tax = model.getTaxValue();
                    float afterTax = model.getOtherCostAfterTax();

                    float total = taxable + tax + afterTax;
                    model.setTotal(total);
                    myViewHolder.tvFinal.setText("" + total);

                } catch (Exception e) {

                    model.setOtherCostAfterTax(0);

                    float rate = model.getRate();
                    float royalty = model.getRoyaltyRate();
                    float trans = model.getTransCost();
                    float toll = model.getTollCost();
                    float other = model.getOtherCost();

                    float taxableCost = rate + royalty + trans + other;
                    //toll + other;
                    model.setTaxableValue(taxableCost);
                    myViewHolder.tvTaxable.setText("" + model.getTaxableValue());

                    if (isTaxExtras == 1) {
                        float taxValue = ((model.getTaxableValue())* (model.getSgstPer() + model.getCgstPer()))/100;
                        model.setTaxValue(taxValue);
                    } else if (isTaxExtras == 0) {
                        model.setTaxValue(0);
                    }
                    myViewHolder.tvTax.setText("" + model.getTaxValue());

                    float taxable = model.getTaxableValue();
                    float tax = model.getTaxValue();
                    float afterTax = model.getOtherCostAfterTax();

                    float total = taxable + tax + afterTax;
                    model.setTotal(total);
                    myViewHolder.tvFinal.setText("" + total);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
         myViewHolder.tvTaxable.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                float qty = Float.parseFloat(s.toString());
                model.setTaxableValue(qty);

                if (isTaxExtras == 1) {
                    float taxValue = ((model.getTaxableValue())* (model.getSgstPer() + model.getCgstPer()))/100;
                    model.setTaxValue(taxValue);
                } else if (isTaxExtras == 0) {
                    model.setTaxValue(0);
                }
                myViewHolder.tvTax.setText("" + model.getTaxValue());

                float et_final= model.getTaxableValue();
                float et_final1= model.getOtherCostAfterTax();
                float et_final2= model.getTaxValue();


                Total=et_final+et_final1+et_final2;

                model.setTotal(Total);
                myViewHolder.tvFinal.setText("" + Total);

            } catch (Exception e) {
                float qty = 0;
                model.setRate(0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });


    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvGst, tvTax,tvFinal,tvTaxable;
        public EditText etQty,etCostAfterTax,etQty1;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvGst = itemView.findViewById(R.id.tvGst);
            tvTax = itemView.findViewById(R.id.tvTax);
            etQty = itemView.findViewById(R.id.edQty);
            etQty1 = itemView.findViewById(R.id.edQty1);
            etCostAfterTax = itemView.findViewById(R.id.edCostAfterTax);
            tvTaxable = itemView.findViewById(R.id.tvTaxable);
            tvFinal = itemView.findViewById(R.id.tvTotal);

        }
    }
}
