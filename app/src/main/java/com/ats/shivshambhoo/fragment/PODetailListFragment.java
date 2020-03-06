package com.ats.shivshambhoo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.DetailPoAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.DetailPoModel;
import com.ats.shivshambhoo.model.GetPoDetailList;
import com.ats.shivshambhoo.model.PoDetailModel;
import com.ats.shivshambhoo.model.PoHeaderModel;
import com.ats.shivshambhoo.model.PoListModel;
import com.ats.shivshambhoo.util.CommonDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PODetailListFragment extends Fragment {
    PoListModel poListModel;
    public TextView tvCustName, tvDate, tvRemark, tvPoNo;
    private RecyclerView recyclerView;
    DetailPoAdapter adapter;
    private Button btnSubmit;
    ArrayList<DetailPoModel> poDetailList = new ArrayList<>();
    ArrayList<GetPoDetailList> detailList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_podetail_list2, container, false);
        tvCustName = (TextView)view.findViewById(R.id.tvCustName);
        tvDate = (TextView)view.findViewById(R.id.tvDate);
        tvRemark = (TextView)view.findViewById(R.id.tvRemark);
        tvPoNo = (TextView)view.findViewById(R.id.tvPoNo);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
        getActivity().setTitle("Purchase Order Detail");
        String quoteStr = getArguments().getString("model");
        Gson gson = new Gson();
        poListModel = gson.fromJson(quoteStr, PoListModel.class);
       // String remark = getArguments().getString("type");
        tvCustName.setText(""+poListModel.getCustName());
        tvDate.setText(""+poListModel.getPoDate());

        String date=tvDate.getText().toString();
        String ValDate=poListModel.getPoValidityDate();
        String qutDate=poListModel.getQutDate();

        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

        Date ToDate = null;
        try {
            ToDate = formatter1.parse(date);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String DateTo = formatter3.format(ToDate);

        Date ToValDate = null;
        try {
            ToValDate = formatter1.parse(ValDate);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String DateValTo = formatter3.format(ToValDate);

        Date ToqutDate = null;
        try {
            ToqutDate = formatter1.parse(qutDate);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String DateQutTo = formatter3.format(ToqutDate);

        Log.e("DateTo","--------------DateTo-------------"+DateTo);
        Log.e("DateValTo","--------------DateValTo-------------"+DateValTo);


        //tvRemark.setText(""+remark);
        tvPoNo.setText(""+poListModel.getPoNo());

        if(poListModel.getStatus()==0)
        {
            tvRemark.setText("Pending");
        }else if(poListModel.getStatus()==1)
        {
            tvRemark.setText("Partially Close");
        }else if(poListModel.getStatus()==2)
        {
            tvRemark.setText("Completed");
        }
        final int poId=poListModel.getPoId();
        getAllPoDetailList(poId);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<PoDetailModel> detailList1 = new ArrayList<>();
                for(int i=0;i<detailList.size();i++) {
                    PoDetailModel detail = new PoDetailModel(detailList.get(i).getPoDetailId(), poId, detailList.get(i).getItemId(), detailList.get(i).getTotal().floatValue(), detailList.get(i).getPoQty(), detailList.get(i).getPoConsumeQty(), detailList.get(i).getPoRemainingQty(), detailList.get(i).getStatus(), "", detailList.get(i).getTaxAmt().floatValue(), 1, detailList.get(i).getTaxableAmt(), detailList.get(i).getOtherCharges(), detailList.get(i).getTotal().floatValue(), detailList.get(i).getQuDetailId(), detailList.get(i).getVarchar1(), "",detailList.get(i).getExtra1(), detailList.get(i).getExtra2());
                    detailList1.add(detail);

                }

                PoHeaderModel header = new PoHeaderModel(poId,poListModel.getPoNo(),DateTo,poListModel.getCustId(),poListModel.getRemark(),poListModel.getCustProjectId(),poListModel.getPoDocument(),poListModel.getPoDocument1(),poListModel.getQuatationId(),poListModel.getQuatationNo(),DateValTo,poListModel.getPoTermId(),1,poListModel.getPlantId(),poListModel.getStatus(),poListModel.getExtra1(),poListModel.getExtra2(),poListModel.getVarchar1(),DateQutTo,detailList1);
                savePo(header);

            }
        });

        return view;
    }

    private void savePo(PoHeaderModel header) {
        if (Constants.isOnline(getContext())) {
            Log.e("PARAMETER : ", "---------------- GET PO : " + header);

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<PoHeaderModel> listCall = Constants.myInterface.savePurchaseOrder(header);
            listCall.enqueue(new Callback<PoHeaderModel>() {
                @Override
                public void onResponse(Call<PoHeaderModel> call, Response<PoHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PO SAVE","-----------------------------"+response.body());
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new POListFragment(), "HomeFragment");
                            ft.commit();
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PoHeaderModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void getAllPoDetailList(int poId) {
        Log.e("PARAMETERS : ", "      PO ID : " + poId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<DetailPoModel> listCall = Constants.myInterface.getAllPoDetailListByPlant(poId);
            listCall.enqueue(new Callback<DetailPoModel>() {
                @Override
                public void onResponse(Call<DetailPoModel> call, Response<DetailPoModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ALL PO Detail LIST : ", "------------------------- - " + response.body());

                            DetailPoModel model=response.body();
                            poDetailList.clear();
                            poDetailList.add(model);
                            Log.e("ALL PO Detail Array : ", "------------------------ - " + poDetailList);
                           // poDetailList = response.body();
                            // createQuotationPDF(quotList);

                            if (model.getGetPoDetailList() != null) {
                                //ArrayList<GetPoDetailList>
                                        detailList = new ArrayList<>();
                                for (int i = 0; i < model.getGetPoDetailList().size(); i++) {
                                    detailList.add(model.getGetPoDetailList().get(i));
                                }

                                adapter = new DetailPoAdapter(detailList, getContext(),poListModel.getExtra1());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DetailPoModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
