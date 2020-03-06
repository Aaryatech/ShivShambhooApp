package com.ats.shivshambhoo.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.CustomerListAddOrderAdapter;
import com.ats.shivshambhoo.adapter.POItemListAdapter;
import com.ats.shivshambhoo.adapter.POItemsListDialogAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.Cust;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.model.OrderDetail;
import com.ats.shivshambhoo.model.OrderHeader;
import com.ats.shivshambhoo.model.POHeader;
import com.ats.shivshambhoo.model.POItems;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.model.PlantDetail;
import com.ats.shivshambhoo.model.Project;
import com.ats.shivshambhoo.model.User;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrderFragment extends Fragment implements View.OnClickListener {

    private TextView tvOrderNo, tvDate, tvPlantName,tvCustName;
    private EditText edDate,edTime;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private Spinner spCustomer, spPO, spProject;
    private LinearLayout llSearch;

    private ArrayList<Cust> customerList = new ArrayList<>();
    private ArrayList<String> customerNameList = new ArrayList<>();
    private ArrayList<Integer> customerIdList = new ArrayList<>();


    ArrayList<String> projectNameList = new ArrayList<>();
    ArrayList<Integer> projectIdList = new ArrayList<>();

    ArrayList<POHeader> poHeaderList = new ArrayList<>();
    ArrayList<String> poNameList = new ArrayList<>();
    ArrayList<Integer> poIdList = new ArrayList<>();

    ArrayList<POItems> poItemList = new ArrayList<>();

    private BroadcastReceiver mBroadcastReceiver;

    CustomerListAddOrderAdapter custAdapter1;

    Dialog dialog;

    public static ArrayList<POItems> staticItemList = new ArrayList<>();

    int plantId;
    Plant plant;

    POItemListAdapter adapter;

    long dateMillis;
    int yyyy, mm, dd,hourse,min;

    User user;
    int custId=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_order, container, false);

        getActivity().setTitle("Add Order");

        tvOrderNo = view.findViewById(R.id.tvOrderNo);
        tvDate = view.findViewById(R.id.tvDate);
        edDate = view.findViewById(R.id.edDate);
        edTime = view.findViewById(R.id.edTime);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spCustomer = view.findViewById(R.id.spCustomer);
        spPO = view.findViewById(R.id.spPO);
        spProject = view.findViewById(R.id.spProject);
        llSearch = view.findViewById(R.id.llSearch);
        tvPlantName = view.findViewById(R.id.tvPlantName);
        tvCustName = view.findViewById(R.id.tvCustName);

        btnSubmit.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        edDate.setOnClickListener(this);
        edTime.setOnClickListener(this);
        tvCustName.setOnClickListener(this);

        staticItemList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

        edDate.setText("" + sdf1.format(System.currentTimeMillis()));
        tvDate.setText("" + sdf.format(System.currentTimeMillis()));

        String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_USER);
        Gson gson = new Gson();
        user = gson.fromJson(userStr, User.class);

        try {
            String plantStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_PLANT);
            Gson gsonPlant = new Gson();
            plant = gsonPlant.fromJson(plantStr, Plant.class);
            tvPlantName.setText("" + plant.getPlantName());
        } catch (Exception e) {
        }

        plantId = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_PLANT_ID);

        projectNameList.clear();
        projectIdList.clear();
        projectIdList.add(0);
        projectNameList.add("select");

        ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, projectNameList);
        spProject.setAdapter(projectAdapter);

        poNameList.clear();
        poIdList.clear();
        poIdList.add(0);
        poNameList.add("select");

        ArrayAdapter<String> poAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, poNameList);
        spPO.setAdapter(poAdapter);

       // getOrderNo(3);
        getOrderNo(plant.getPlantId());
        getCustomerList();

        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    spProject.setSelection(0);
                    spPO.setSelection(0);

                    getProjectList(customerIdList.get(i));
                    getPOList(customerIdList.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spPO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                staticItemList.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("CUSTOMER_DATA")) {
                    handlePushNotification(intent);
                }
            }
        };

        return view;
    }

    public void getOrderNo(int plantId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<PlantDetail> listCall = Constants.myInterface.getPlantByPlantId(plantId);
            listCall.enqueue(new Callback<PlantDetail>() {
                @Override
                public void onResponse(Call<PlantDetail> call, Response<PlantDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DOCUMENT : ", " - " + response.body());

                            if (response.body().getPlantId() > 0) {
                                int srNO = response.body().getExInt3();
                                int len=String.valueOf(srNO).length();
                                if(len==1)
                                {
                                    tvOrderNo.setText("ORDNO-" +"-" +"0000" + srNO);
                                   // +plant.getPlantFax1()
                                }else if(len==2)
                                {

                                    tvOrderNo.setText("ORDNO-" +"-" +"000" + srNO);
                                }else if(len==3)
                                {

                                    tvOrderNo.setText("ORDNO-" +"-" +"00" + srNO);
                                }else if(len==4)
                                {

                                    tvOrderNo.setText("ORDNO-" +"-"+"0" + srNO);
                                }else if(len==5)
                                {

                                    tvOrderNo.setText("ORDNO-" +"-" + srNO);
                                }

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
                public void onFailure(Call<PlantDetail> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCustomerList() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Cust>> listCall = Constants.myInterface.getCustomerList(plantId);
            listCall.enqueue(new Callback<ArrayList<Cust>>() {
                @Override
                public void onResponse(Call<ArrayList<Cust>> call, Response<ArrayList<Cust>> response) {
                    try {
                        customerList.clear();

                        customerIdList.clear();
                        customerNameList.clear();

                        customerIdList.add(0);
                        customerNameList.add("Select");

                        if (response.body() != null) {

                            Log.e("CUSTOMER LIST : ", " - " + response.body());
                            customerList = response.body();

                            if (customerList.size() > 0) {
                                for (int i = 0; i < customerList.size(); i++) {
                                    customerIdList.add(customerList.get(i).getCustId());
                                    customerNameList.add(customerList.get(i).getCustName());
                                }

                                ArrayAdapter<String> custAdapter; custAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, customerNameList);
                                spCustomer.setAdapter(custAdapter);
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            ArrayAdapter<String> custAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, customerNameList);
                            spCustomer.setAdapter(custAdapter);

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        ArrayAdapter<String> custAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, customerNameList);
                        spCustomer.setAdapter(custAdapter);

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Cust>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    ArrayAdapter<String> custAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, customerNameList);
                    spCustomer.setAdapter(custAdapter);

                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getProjectList(int custId) {

        Log.e("PARAMETER : ", "---------------- CUST ID : " + custId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Project>> listCall = Constants.myInterface.getProjectByCustomer(custId);
            listCall.enqueue(new Callback<ArrayList<Project>>() {
                @Override
                public void onResponse(Call<ArrayList<Project>> call, Response<ArrayList<Project>> response) {
                    try {
                        projectNameList.clear();
                        projectIdList.clear();

                        projectIdList.add(0);
                        projectNameList.add("select");

                        if (response.body() != null) {

                            Log.e("PROJECT LIST : ", " - " + response.body());

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    projectIdList.add(response.body().get(i).getProjId());
                                    projectNameList.add(response.body().get(i).getProjName());
                                }

                                ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, projectNameList);
                                spProject.setAdapter(projectAdapter);
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, projectNameList);
                            spProject.setAdapter(projectAdapter);
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, projectNameList);
                        spProject.setAdapter(projectAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Project>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                    ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, projectNameList);
                    spProject.setAdapter(projectAdapter);
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPOList(int custId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            ArrayList<Integer> statusList = new ArrayList<>();
            statusList.add(0);
            statusList.add(1);

            Call<ArrayList<POHeader>> listCall = Constants.myInterface.getPOListByCustId(custId, statusList);
            listCall.enqueue(new Callback<ArrayList<POHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<POHeader>> call, Response<ArrayList<POHeader>> response) {
                    try {
                        poHeaderList.clear();
                        poNameList.clear();
                        poIdList.clear();

                        poIdList.add(0);
                        poNameList.add("select");

                        if (response.body() != null) {

                            Log.e("PO LIST : ", " - " + response.body());

                            poHeaderList = response.body();

                            if (poHeaderList.size() > 0) {
                                for (int i = 0; i < poHeaderList.size(); i++) {
                                    poNameList.add(response.body().get(i).getPoNo());
                                    poIdList.add(response.body().get(i).getPoId());
                                }

                                ArrayAdapter<String> poAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, poNameList);
                                spPO.setAdapter(poAdapter);
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            ArrayAdapter<String> poAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, poNameList);
                            spPO.setAdapter(poAdapter);
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        ArrayAdapter<String> poAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, poNameList);
                        spPO.setAdapter(poAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<POHeader>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                    ArrayAdapter<String> poAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, poNameList);
                    spPO.setAdapter(poAdapter);
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePushNotification(Intent intent) {
        Log.e("handlePushNotification", "------------------------------------**********");
        dialog.dismiss();
        String name = intent.getStringExtra("name");
         custId = intent.getIntExtra("id", 0);
        Log.e("CUSTOMER NAME : ", " " + name);
        tvCustName.setText("" + name);
        getProjectList(custId);
        getPOList(custId);
       // tvCustId.setText("" + custId);

    }

    public void getPOItemList(int poId) {

        if (staticItemList != null) {
            if (staticItemList.size() == 0) {

                if (Constants.isOnline(getContext())) {
                    final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
                    commonDialog.show();

                    Call<ArrayList<POItems>> listCall = Constants.myInterface.getPOItemsByPOId(poId);
                    listCall.enqueue(new Callback<ArrayList<POItems>>() {
                        @Override
                        public void onResponse(Call<ArrayList<POItems>> call, Response<ArrayList<POItems>> response) {
                            try {
                                poItemList.clear();

                                if (response.body() != null) {

                                    Log.e("PO ITEM LIST : ", " - " + response.body());

                                    poItemList = response.body();
                                    if (poItemList.size() > 0) {
                                        showPOItemDialog(poItemList);
                                    } else {
                                        Toast.makeText(getContext(), "No items available", Toast.LENGTH_SHORT).show();
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
                        public void onFailure(Call<ArrayList<POItems>> call, Throwable t) {
                            commonDialog.dismiss();
                            Log.e("onFailure : ", "-----------" + t.getMessage());
                            t.printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
                }
            } else {
                showPOItemDialog(poItemList);
            }
        }

    }

    public void showPOItemDialog(final ArrayList<POItems> itemList) {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_po_item_list, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RecyclerView rvItems = dialog.findViewById(R.id.recyclerView);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        POItemsListDialogAdapter dialogAdapter = new POItemsListDialogAdapter(itemList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(dialogAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                staticItemList.clear();

                if (itemList.size() > 0) {
                    for (int i = 0; i < itemList.size(); i++) {
                        if (itemList.get(i).getQty() > 0) {
                            staticItemList.add(itemList.get(i));
                        }
                    }

                    if (staticItemList.size() > 0) {
                        dialog.dismiss();

                        adapter = new POItemListAdapter(staticItemList, getContext());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);

                    } else {
                        Toast.makeText(getContext(), "Please select PO items", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void getPlantDetail(int plantId, final OrderHeader header) {
        Log.e("ORDER HEADER","------------------------"+header);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<PlantDetail> listCall = Constants.myInterface.getPlantByPlantId(plantId);
            listCall.enqueue(new Callback<PlantDetail>() {
                @Override
                public void onResponse(Call<PlantDetail> call, Response<PlantDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DOCUMENT : ", " - " + response.body());

                            if (response.body().getPlantId() > 0) {
                                int srNO = response.body().getExInt3();
                                int len=String.valueOf(srNO).length();
                                String orderNo = null;
                                if(len==1)
                                {
                                     orderNo ="ORDNO-" +"-" +"0000" + srNO;
                                   // plant.getPlantFax1()
                                }else if(len==2)
                                {

                                     orderNo ="ORDNO-" +"-" +"000" + srNO;
                                }else if(len==3)
                                {

                                     orderNo ="ORDNO-" +"-" +"00" + srNO;
                                }else if(len==4)
                                {

                                     orderNo = "ORDNO-" +"-"+"0" + srNO;
                                }else if(len==5)
                                {

                                    orderNo = "ORDNO-" +"-" + srNO;
                                }


                               // String orderNo = response.body().getDocPrefix() + "" + (srNO);
                                header.setOrderNo(orderNo);

                                saveOrder(header, (srNO + 1));

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
                public void onFailure(Call<PlantDetail> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    //exInt1->userId

    public void saveOrder(OrderHeader header, final int srNo) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<OrderHeader> listCall = Constants.myInterface.saveOrder(header);
            listCall.enqueue(new Callback<OrderHeader>() {
                @Override
                public void onResponse(Call<OrderHeader> call, Response<OrderHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE ORDER: ", " - " + response.body());

                            if (response.body().getPoId() != 0) {
                                updateOrdCounter(plant.getPlantId(), srNo);

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
                public void onFailure(Call<OrderHeader> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateOrdCounter(int plantId, final int srNo) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateOrderCounter(plantId, srNo);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE DOCUMENT: ", " - " + response.body());

                            if (!response.body().isError()) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new OrderListFragment(), "HomeFragment");
                                ft.commit();

                                // updateDocumentForQuotation(2, quotSrNo, enquiryHeader);

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

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDate.setText(dd + "-" + mm + "-" + yyyy);
            tvDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            dateMillis = calendar.getTimeInMillis();
        }
    };



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edDate) {
            int yr, mn, dy;
            if (dateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(dateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), dateListener, yr, mn, dy);
            dialog.show();

        }else if (view.getId() == R.id.edTime) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    edTime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Start Time");
            mTimePicker.show();
        }
        else if (view.getId() == R.id.tvCustName) {
            showDialog();
        }
        else if (view.getId() == R.id.llSearch) {
            if (spPO.getSelectedItemPosition() > 0) {
                int poId = poIdList.get(spPO.getSelectedItemPosition());
                Log.e("PO ID : ", " " + poId);
                //Toast.makeText(getContext(), ""+poId, Toast.LENGTH_SHORT).show();
                Log.e("NAME : ", " " + poNameList.get(spPO.getSelectedItemPosition()));
                getPOItemList(poId);
            } else {
                Toast.makeText(getContext(), "Please select PO", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnSubmit) {
            String Time = edTime.getText().toString();
            if (edDate.getText().toString().isEmpty()) {
                edDate.setError("Required");

            }else if (Time.isEmpty()) {
                //edEndKM.setError(null);
                edTime.setError("Required");

            }
            else if (custId == 0) {
                edDate.setError(null);
                Toast.makeText(getContext(), "Please select customer", Toast.LENGTH_SHORT).show();

            } else if (spProject.getSelectedItemPosition() == 0) {
                Toast.makeText(getContext(), "Please select project", Toast.LENGTH_SHORT).show();

            } else if (spPO.getSelectedItemPosition() == 0) {
                Toast.makeText(getContext(), "Please select PO", Toast.LENGTH_SHORT).show();

            } else {
                if (staticItemList != null) {
                    if (staticItemList.size() > 0) {

                        ArrayList<OrderDetail> detailList = new ArrayList<>();
                        float orderTotal = 0;
                        for (int i = 0; i < staticItemList.size(); i++) {
                            POItems item = staticItemList.get(i);
                            float total=item.getTotal()*item.getQty();
                            OrderDetail detail = new OrderDetail(0, 0, item.getPoId(), item.getItemId(), item.getPoDetailId(), item.getQty(), item.getTotal(), total, 1, item.getQty(), 0);
                            detailList.add(detail);

                            orderTotal = orderTotal + detail.getTotal();
                        }

                       // int custId = customerIdList.get(spCustomer.getSelectedItemPosition());
                        int projId = projectIdList.get(spProject.getSelectedItemPosition());
                        int poId = poIdList.get(spPO.getSelectedItemPosition());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        String delDate = tvDate.getText().toString();
                        String date = sdf.format(System.currentTimeMillis());
                        String prodDate = sdf.format(System.currentTimeMillis());

                        int isTax = 0;
                        if (staticItemList.get(0).getTaxAmt() > 0) {
                            isTax = 1;
                        } else {
                            isTax = 0;
                        }

                        OrderHeader orderHeader = new OrderHeader(0, plantId, custId, poId, projId, delDate, date, prodDate, orderTotal, "", orderTotal, isTax, 1, user.getUserId(), Time,0, detailList);

                        getPlantDetail(plant.getPlantId(), orderHeader);

                    } else {
                        Toast.makeText(getContext(), "Please add items for PO", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please add items for PO", Toast.LENGTH_SHORT).show();
                }


            }


        }
    }

    private void showDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        custAdapter1 = new CustomerListAddOrderAdapter(customerList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(custAdapter1);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (custAdapter1 != null) {
                        filterCustomer(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("CUSTOMER_DATA"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }
    void filterCustomer(String text) {
        ArrayList<Cust> temp = new ArrayList();
        for (Cust d : customerList) {
            if (d.getCustName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        custAdapter1.updateList(temp);
    }
}
