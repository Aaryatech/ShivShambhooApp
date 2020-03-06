package com.ats.shivshambhoo.fragment;


import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.PODetailListAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.Document;
import com.ats.shivshambhoo.model.GetQuotDetail;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.model.PoDetailModel;
import com.ats.shivshambhoo.model.PoHeaderModel;
import com.ats.shivshambhoo.model.QuotationHeaderDisp;
import com.ats.shivshambhoo.model.Status;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderFragment extends Fragment {
    private TextView  tvCustName,tvPaymentTerm;
    private EditText etPurchaseOrderDate, etPurchaseValidityDate, etDeliveryLocation,etRemark,tvPurchaseOrder;
    private RecyclerView recyclerView;
    private Button btnSubmit;

    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd,CurrentYear,CurrentMonth,ab,ab1;
    String name,payTerm,plantId,srNO,poNo;
    Plant plant;
    QuotationHeaderDisp quotationHeader;

    ArrayList<GetQuotDetail> detailList;

   // ArrayList<QuotationHeaderDisp> quotList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_order, container, false);
        tvPurchaseOrder = (EditText) view.findViewById(R.id.tvPurchaseOrder);
        tvCustName = (TextView) view.findViewById(R.id.tvCustName);
        etPurchaseOrderDate = (EditText) view.findViewById(R.id.etPurchaseOrderDate);
        etPurchaseValidityDate = (EditText) view.findViewById(R.id.etPurchaseValidityDate);
        tvPaymentTerm = (TextView) view.findViewById(R.id.tvPaymentTerm);
        etDeliveryLocation = (EditText) view.findViewById(R.id.etDeliveryLocation);
        etRemark=(EditText)view.findViewById(R.id.etRemark);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
         CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
         CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
        int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        String financiyalYearFrom = "";
        String financiyalYearTo = "";
        if (CurrentMonth < 4) {
            financiyalYearFrom = "" + (CurrentYear - 1);
            financiyalYearTo = "" + (CurrentYear);
        } else {
            financiyalYearFrom = "" + (CurrentYear);
            financiyalYearTo = "" + (CurrentYear + 1);
        }

         ab = (Integer.parseInt(financiyalYearFrom)) % 2000;
         ab1 = (Integer.parseInt(financiyalYearTo)) % 2000;
        Log.e("Mytag","ab"+ab);
        Log.e("Mytag","ab1"+ab1);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(todayDate);
        Log.e("Mytag","todayString"+currentDate);
        name = getArguments().getString("Name");
        poNo = getArguments().getString("poNo");
        Log.e("Mytag","name"+name);

        String plantStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_PLANT);
        Gson gsonPlant = new Gson();
        plant = gsonPlant.fromJson(plantStr, Plant.class);
        if (plant != null) {
            //tvPlantName.setText("" + plant.getPlantName());
           // getActivity().setTitle("New Enquiry -"+plant.getPlantName());
             plantId=plant.getPlantFax1();
            Log.e("Mytag","plantId"+plantId);
        }

        payTerm = getArguments().getString("payTerms");
        Log.e("Mytag","payTerm"+payTerm);
        tvCustName.setText(name);
        tvPaymentTerm.setText(payTerm);
        tvPurchaseOrder.setText(poNo);
        etPurchaseOrderDate.setText(currentDate);
        etPurchaseValidityDate.setText(currentDate);

        getDocument(7);
        loadData();
        getActivity().setTitle("Purchase Order Detail");

        etPurchaseOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int yr, mn, dy;
                if (fromDateMillis > 0) {
                    Calendar purchaseCal = Calendar.getInstance();
                    purchaseCal.setTimeInMillis(fromDateMillis);
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                } else {
                    Calendar purchaseCal = Calendar.getInstance();
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(getContext(), fromDateListener, yr, mn, dy);
                dialog.show();
            }
        });

        etPurchaseValidityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int yr, mn, dy;
                if (toDateMillis > 0) {
                    Calendar purchaseCal = Calendar.getInstance();
                    purchaseCal.setTimeInMillis(toDateMillis);
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                } else {
                    Calendar purchaseCal = Calendar.getInstance();
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, yr, mn, dy);
                dialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etDeliveryLocation.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "please select delivery location", Toast.LENGTH_SHORT).show();
                    etDeliveryLocation.setError("please select Delivery Location");
                } else{
                    etDeliveryLocation.setError(null);

                    String quoteStr = getArguments().getString("model");
                Gson gson = new Gson();
                quotationHeader = gson.fromJson(quoteStr, QuotationHeaderDisp.class);
                if (quotationHeader.getGetQuotDetailList() != null) {
                    ArrayList<GetQuotDetail> QutationdetailList = new ArrayList<>();
                    for (int j = 0; j < quotationHeader.getGetQuotDetailList().size(); j++) {
                        QutationdetailList.add(quotationHeader.getGetQuotDetailList().get(j));
                    }
                    String poId = tvPurchaseOrder.getText().toString();
                    String cusName = tvCustName.getText().toString();
                    String paymentTerm = tvPaymentTerm.getText().toString();
                    String purchaseOrderdate = etPurchaseOrderDate.getText().toString();
                    Log.e("Mytag","purchaseOrderdate"+purchaseOrderdate);
                    String purchaseOrderValidity = etPurchaseValidityDate.getText().toString();
                    String deliveryLocation = etDeliveryLocation.getText().toString();
                    String remark=etRemark.getText().toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                    Date purchaseDate = null;
                    try {
                        purchaseDate = formatter1.parse(purchaseOrderdate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date purchaseValidityDate = null;
                    try {
                        purchaseValidityDate = formatter1.parse(purchaseOrderValidity);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    String purchase = formatter.format(purchaseDate);
                    String purchaseValidity = formatter.format(purchaseValidityDate);
                    Log.e("Mytag","purchaseDate"+purchase);
                    Log.e("Mytag","purchaseValidityDate"+purchaseValidity);


                    if (detailList.size() > 0) {

                        ArrayList<PoDetailModel> podetailList = new ArrayList<>();

                        for (int i = 0; i < detailList.size(); i++) {

                            float taxPer=detailList.get(i).getSgstPer()+detailList.get(i).getCgstPer();
                            PoDetailModel poDetail = new PoDetailModel(0, 0, detailList.get(i).getItemId(), detailList.get(i).getTaxableValue(), detailList.get(i).getQuotQty(), detailList.get(i).getConvQty(), detailList.get(i).getQuotQty(), 0, "", detailList.get(i).getTaxValue(), taxPer, detailList.get(i).getTaxableValue(), detailList.get(i).getOtherCost(), detailList.get(i).getTotal(), detailList.get(i).getEnqDetailId(), detailList.get(i).getExDate1(), "",detailList.get(i).getExInt1(), detailList.get(i).getExInt2());
                            podetailList.add(poDetail);
                            Log.e("Mytag","-------------------PoDetail--------------"+podetailList);

                        }

                        if (QutationdetailList.size() > 0) {
                            PoHeaderModel header = new PoHeaderModel(0, poId, purchase, quotationHeader.getCustId(), remark, quotationHeader.getProjId(), "", "", quotationHeader.getQuotHeadId(), quotationHeader.getQuotNo(), purchaseValidity, quotationHeader.getPayTermId(), 1, plant.getPlantId(), 0, 0, 0, deliveryLocation, quotationHeader.getQuotDate(),podetailList);
                            Log.e("Mytag","-------------------header--------------"+header);
                            getSaveDocument(7, header);

                        } else {
                            Toast.makeText(getContext(), "Please select item", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(), "No Items found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            }
        });
        return view;
    }

    private void getSaveDocument(int i, final PoHeaderModel header) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Document> listCall = Constants.myInterface.getDocument(i);
            listCall.enqueue(new Callback<Document>() {
                @Override
                public void onResponse(Call<Document> call, Response<Document> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DOCUMENT : ", " ---------------- " + response.body());

                            if (response.body().getDocId() > 0) {
                                Log.e("Mytag","response"+response.body().getDocId() );
                                int srNO = response.body().getSrNo();
                                String poNumber=String.format("%05d", srNO);
                              //  String enqNo = response.body().getDocPrefix() + "" + srNO;
                               String PoNo = plantId +"-" +ab +"-" +ab1+"-" +poNumber;
                                String poNo=tvPurchaseOrder.getText().toString();
                                Log.e("Mytag","poNo"+PoNo);
                                Log.e("Mytag","srNO"+srNO);
                                Log.e("Mytag","tvPo"+poNo);
                                header.setPoNo(poNo);

                                savePurchaseOrder(header, (srNO + 1));

                            } else {
                                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<Document> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePurchaseOrder(PoHeaderModel header, final int srNo) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<PoHeaderModel> listCall = Constants.myInterface.savePurchaseOrder(header);
            listCall.enqueue(new Callback<PoHeaderModel>() {
                @Override
                public void onResponse(Call<PoHeaderModel> call, Response<PoHeaderModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE PO: ", " ------------------- " + response.body());

                            if (response.body().getPoId() != 0) {
                                //  Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
//                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                ft.replace(R.id.content_frame, new CustomerListFragment(), "HomeFragment");
//                                ft.commit();

                                updateDocument(7, srNo, response.body());

                            } else {
                                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
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

    private void updateDocument(int i, int srNo, final PoHeaderModel body) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateDocument(i, srNo);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE DOCUMENT: ", " - " + response.body().toString());

                            if (!response.body().isError()) {
                                  Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                                  UpdateStatus(quotationHeader.getQuotHeadId());

//                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                ft.replace(R.id.content_frame, new POListFragment(), "HomeFragment");
//                                ft.commit();

                                // updateDocumentForQuotation(2, quotSrNo, enquiryHeader);

                               // getDocumentForQuotation(7, body);

                            } else {
                                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateStatus(int quotHeadId) {
        Log.e("PARAMETERS : ", "        QUOTATION HEAD : " + quotHeadId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Status> listCall = Constants.myInterface.updateQuatationStatus(quotHeadId);
            listCall.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE STATUS : ", " - " + response.body().toString());

                            if (response.body() != null) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new POListFragment(), "HomeFragment");
                                ft.commit();

                                } else {
                                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<Status> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadData() {

            String quoteStr = getArguments().getString("model");
             Gson gson = new Gson();
            quotationHeader = gson.fromJson(quoteStr, QuotationHeaderDisp.class);
            Log.e("Mytag","--------------quotationHeader-----------"+quotationHeader);
            if (quotationHeader.getGetQuotDetailList()!= null) {
             detailList = new ArrayList<>();
            for (int j = 0; j < quotationHeader.getGetQuotDetailList().size(); j++) {
                detailList.add(quotationHeader.getGetQuotDetailList().get(j));
            }

            PODetailListAdapter adapter = new PODetailListAdapter(detailList, getContext(),(int)quotationHeader.getTaxValue());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);


        }


    }

       DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            etPurchaseOrderDate.setText(dd + "-" + mm + "-" + yyyy);
            //tvFromDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();
        }
    };

    DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            etPurchaseValidityDate.setText(dd + "-" + mm + "-" + yyyy);
            //tvToDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();
        }
    };

    public void getDocument(int docCode) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Document> listCall = Constants.myInterface.getDocument(docCode);
            listCall.enqueue(new Callback<Document>() {
                @Override
                public void onResponse(Call<Document> call, Response<Document> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DOCUMENT : ", " - " + response.body());

                            if (response.body().getDocId() > 0) {
                                 srNO = String.valueOf(response.body().getSrNo());

                                 int srNoLegnth=srNO.length();

//                                String poNumber=String.format("%05d", srNO);
//                                tvPurchaseOrder.setText( plantId +"-" +ab +"-" +ab1+"-" +poNumber);

                                 if(srNoLegnth==1)
                                 {
                                     tvPurchaseOrder.setText( plantId +"-" +ab +"-" +ab1+"-" +"0000"+srNO + "");
                                 }else if(srNoLegnth==2)
                                 {
                                     tvPurchaseOrder.setText(plantId +"-" +ab +"-" +ab1+"-" +"000"+srNO + "");
                                 }else if(srNoLegnth==3)
                                 {
                                     tvPurchaseOrder.setText(plantId +"-" +ab +"-" +ab1+"-" +"00"+srNO + "");
                                 }else if(srNoLegnth==4)
                                 {
                                     tvPurchaseOrder.setText(plantId +"-" +ab +"-" +ab1+"-" +"0"+srNO + "");
                                 }else if(srNoLegnth==5)
                                 {
                                     tvPurchaseOrder.setText(plantId +"-" +ab +"-" +ab1+"-" +srNO + "");
                                 }




                                String enqNo = response.body().getDocPrefix() + "" + srNO;
                                //header.setEnqNo(enqNo);

                                // saveEnquiry(header, (srNO + 1));

                            } else {
                                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<Document> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });

        }
    }
}
