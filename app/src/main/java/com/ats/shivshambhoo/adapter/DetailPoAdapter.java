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
import com.ats.shivshambhoo.model.GetPoDetailList;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailPoAdapter extends RecyclerView.Adapter<DetailPoAdapter.MyViewHolder>  {
    private ArrayList<GetPoDetailList> detailPoModel;
    private Context context;
    Double Total;
    private int isTaxExtras;

//    public DetailPoAdapter(ArrayList<GetPoDetailList> detailPoModel, Context context) {
//        this.detailPoModel = detailPoModel;
//        this.context = context;
//    }

    public DetailPoAdapter(ArrayList<GetPoDetailList> detailPoModel, Context context, int isTaxExtras) {
        this.detailPoModel = detailPoModel;
        this.context = context;
        this.isTaxExtras = isTaxExtras;
    }

    @NonNull
    @Override
    public DetailPoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.detail_po_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailPoAdapter.MyViewHolder myViewHolder, int i) {
        final GetPoDetailList model = detailPoModel.get(i);
        myViewHolder.tvName.setText(model.getItemName());
        myViewHolder.etQty.setText(""+model.getPoQty());
        myViewHolder.etItemRate.setText(""+model.getTotal());
        myViewHolder.tvGst.setText(""+model.getTaxPer()+"%");
        myViewHolder.tvTax.setText(""+model.getTaxAmt());
        myViewHolder.etCostAfterTax.setText(""+model.getOtherCharges());
        myViewHolder.tvFinal.setText(""+model.getTotal());
        myViewHolder.etQty1.setText(""+model.getExtra1());

        DecimalFormat dtime = new DecimalFormat("#.##");

        try {

           // Log.e("Mytag","taxable----------------"+ model.getTaxableValue());
//            if (isTaxExtras == 1) {
            if (isTaxExtras == 1) {
                Double  taxValue = Double.valueOf(((model.getPoRate())*(model.getTaxPer()))/100);
                model.setTaxAmt(taxValue);
                Log.e("Taxxxxxxxxxx","-----------------------------"+taxValue);
            } else if (isTaxExtras == 0) {
                model.setTaxAmt( 0.0);
                Log.e("Taxaaaaaaaaaaaaaa","-----------------------------");
            }

            myViewHolder.tvTax.setText("" + dtime.format(model.getTaxAmt()));

             Log.e("Tax","-----------------------------"+ model.getTaxAmt());
             Log.e("Tax  Formate","-----------------------------"+dtime.format(model.getTaxAmt()));
           // Log.e("Mytag","GST----------------"+model.getSgstPer() + model.getCgstPer());

            Double taxable = model.getTotal();
            Double tax = model.getTaxAmt();
            int afterTax = (int) model.getOtherCharges();

//            Log.e("taxable","------------------------------"+taxable);
//            Log.e("tax","------------------------"+tax);
//            Log.e("afterTax","---------------------------"+afterTax);


            Double total = (taxable + tax + afterTax);
            Log.e("Totalllllll","---------------------------"+total);
            model.setTotal(total);
            myViewHolder.tvFinal.setText("" +dtime.format(model.getTotal()));
            Log.e("Total","---------------------------"+model.getTotal());
            Log.e("Total formate","---------------------------"+dtime.format(model.getTotal()));


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
                    String qty =charSequence.toString();
                    model.setPoQty(Integer.valueOf(qty));

                } catch (Exception e) {
                    float qty = 0;
                    model.setPoQty(0);
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
                    String qty =charSequence.toString();
                    model.setExtra1(Integer.valueOf(qty));

                } catch (Exception e) {
                    float qty = 0;
                    model.setExtra1(0);
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
                    String qty = s.toString();
                    model.setOtherCharges(Integer.valueOf(qty));
                    float et_final= model.getPoRate();
                    int  et_final1= (int) model.getOtherCharges();
                    Double et_final2= model.getTaxAmt();
                     Total=et_final+et_final1+et_final2;
                    model.setTotal(Total);
                    myViewHolder.tvFinal.setText(""+Total);

                } catch (Exception e) {
                    float qty = 0;
                    model.setOtherCharges(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myViewHolder.etItemRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    float qty = Float.parseFloat(s.toString());
                    model.setPoRate((int) qty);

                    if (isTaxExtras == 1) {
                        Double  taxValue = Double.valueOf(((model.getPoRate())*(model.getTaxPer()))/100);
                        model.setTaxAmt(taxValue);
                    } else if (isTaxExtras == 0) {
                        model.setTaxAmt( 0.0);
                    }

                    myViewHolder.tvTax.setText("" + model.getTaxAmt());

                    float et_final= model.getPoRate();
                    int et_final1= (int) model.getOtherCharges();
                    Double et_final2= model.getTaxAmt();
                    Total=et_final+et_final1+et_final2;
                    model.setTotal(Total);
                    myViewHolder.tvFinal.setText(""+Total);


                } catch (Exception e) {
                    float qty = 0;
                    model.setPoRate(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return detailPoModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvGst, tvTax,tvFinal;
        public EditText etQty,etItemRate,etCostAfterTax,etQty1;
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
            etItemRate = itemView.findViewById(R.id.edItemRate);
            tvFinal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
