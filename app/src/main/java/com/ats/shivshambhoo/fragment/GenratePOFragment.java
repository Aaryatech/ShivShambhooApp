package com.ats.shivshambhoo.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.CustomerListDialogAdapter;
import com.ats.shivshambhoo.adapter.GenratePOListAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.Cust;
import com.ats.shivshambhoo.model.QuotationHeaderDisp;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;

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
public class GenratePOFragment extends Fragment {
    int plantId;
    private RecyclerView recyclerView;
    private TextView tvCustName, tvCustId;

    ArrayList<QuotationHeaderDisp> quotList = new ArrayList<>();

    ArrayList<String> custNameArray = new ArrayList<>();
    ArrayList<Integer> custIdArray = new ArrayList<>();
    ArrayList<Cust> customerList = new ArrayList<>();
    GenratePOListAdapter adapter;

    Dialog dialog;
    CustomerListDialogAdapter custAdapter;
    private BroadcastReceiver mBroadcastReceiver;


    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_genrate_po, container, false);
        getActivity().setTitle("Genrate PO");
        recyclerView = view.findViewById(R.id.recyclerView);

        plantId = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_PLANT_ID);
        getCustomerList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        getAllQuotationList(plantId, sdf.format(System.currentTimeMillis()), sdf.format(System.currentTimeMillis()), 0, 1);
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

    private void handlePushNotification(Intent intent) {
        Log.e("handlePushNotification", "------------------------------------**********");
        dialog.dismiss();
        String name = intent.getStringExtra("name");
        int custId = intent.getIntExtra("id", 0);
        Log.e("CUSTOMER NAME : ", " " + name);
        tvCustName.setText("" + name);
        tvCustId.setText("" + custId);
        Log.e("CUST ID : ", " ---------- " + tvCustId.getText());
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
    public void getAllQuotationList(int plantId, String fromDate, String toDate, int custId, int status) {

        Log.e("PARAMETERS : ", "        PLANT ID : " + plantId + "      FROM DATE : " + fromDate + "         TO DATE : " + toDate + "        CUST ID : " + custId + "             STATUS : " + status);

        ArrayList<Integer> statusList=new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<QuotationHeaderDisp>> listCall = Constants.myInterface.getAllQuotationListByPlantList(plantId, fromDate, toDate, custId, statusList);
            listCall.enqueue(new Callback<ArrayList<QuotationHeaderDisp>>() {
                @Override
                public void onResponse(Call<ArrayList<QuotationHeaderDisp>> call, Response<ArrayList<QuotationHeaderDisp>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ALL QUOTATION LIST : ", " - " + response.body());
                            Log.e("ALL QUOTATION LIST1 : ", " - " + response.body().size());
                            quotList.clear();
                            quotList = response.body();
                            // createQuotationPDF(quotList);

                            adapter = new GenratePOListAdapter(quotList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

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
                public void onFailure(Call<ArrayList<QuotationHeaderDisp>> call, Throwable t) {
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
                        if (response.body() != null) {

                            Log.e("CUSTOMER LIST : ", " - " + response.body());
                            custNameArray.clear();
                            custIdArray.clear();
                            customerList.clear();

                            custNameArray.add("All");
                            custIdArray.add(0);
                            customerList = response.body();

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    custNameArray.add(response.body().get(i).getCustName());
                                    custIdArray.add(response.body().get(i).getCustId());
                                }
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
                public void onFailure(Call<ArrayList<Cust>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        item.setVisible(true);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                new FilterDialog(getContext()).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class FilterDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;
        ImageView ivClose;
        CardView cardView;
        Spinner spinner, spStatus;
        CheckBox cbAll;

        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_filter_all_po_list);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edFromDate = findViewById(R.id.edFromDate);
            edToDate = findViewById(R.id.edToDate);
            tvFromDate = findViewById(R.id.tvFromDate);
            tvToDate = findViewById(R.id.tvToDate);
            Button btnFilter = findViewById(R.id.btnFilter);
            ivClose = findViewById(R.id.ivClose);
            spinner = findViewById(R.id.spinner);
            spStatus = findViewById(R.id.spStatus);
            cbAll = findViewById(R.id.cbAll);
            tvCustId = findViewById(R.id.tvCustId);
            tvCustName = findViewById(R.id.tvCustName);

//            Date todayDate = Calendar.getInstance().getTime();
////            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
////            String currentDate = formatter.format(todayDate);
////            Log.e("Mytag","todayString"+currentDate);
////
////            edToDate.setText(currentDate);

            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = formatter.format(todayDate);
            Log.e("Mytag","todayString"+currentDate);



            edToDate.setText(currentDate);

            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date ToDate = null;
            try {
                ToDate = formatter1.parse(currentDate);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           String DateTo = formatter2.format(ToDate);
            tvToDate.setText(DateTo);


            edFromDate.setOnClickListener(new View.OnClickListener() {
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

            edToDate.setOnClickListener(new View.OnClickListener() {
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

            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, custNameArray);
            spinner.setAdapter(spinnerAdapter);

            ArrayList<String> statusNameList = new ArrayList<>();
            statusNameList.add("All");
            statusNameList.add("Pending");
            statusNameList.add("Quotation Generated");
            statusNameList.add("PO Generated");

            final ArrayList<Integer> statusIdList = new ArrayList<>();
            statusIdList.add(-1);
            statusIdList.add(0);
            statusIdList.add(1);
            statusIdList.add(2);

            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, statusNameList);
            spStatus.setAdapter(statusAdapter);


            cbAll.setChecked(true);

            cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        tvCustName.setVisibility(View.GONE);
                        tvCustId.setText("0");
                    } else {
                        tvCustName.setVisibility(View.VISIBLE);
                        tvCustId.setText("");
                    }
                }
            });


            tvCustName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });


            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    } else {
                        //dismiss();

                        String fromDate = tvFromDate.getText().toString();
                        String toDate = tvToDate.getText().toString();
                        Log.e("Mytag","fromDate"+fromDate);
                        Log.e("Mytag","toDate"+toDate);


                        int custId = 0;
                        /*if (custIdArray.size() > 0) {
                            if (spinner.getSelectedItemPosition() == 0) {
                                custId = 0;
                            } else {
                                custId = custIdArray.get(spinner.getSelectedItemPosition());
                            }
                        }*/

                       // int status = statusIdList.get(spStatus.getSelectedItemPosition());
                        int status=1;

                        if (cbAll.isChecked()) {
                            getAllQuotationList(plantId, fromDate, toDate, custId, status);

                            dismiss();

                        } else {

                            if (tvCustId.getText().toString().isEmpty()) {
                                tvCustName.setError("Required");
                            } else {
                                tvCustName.setError(null);
                                custId = Integer.parseInt(tvCustId.getText().toString());
                                getAllQuotationList(plantId, fromDate, toDate, custId, status);

                                dismiss();
                            }
                        }


                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edFromDate.setText(dd + "-" + mm + "-" + yyyy);
                tvFromDate.setText(yyyy + "-" + mm + "-" + dd);

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
                edToDate.setText(dd + "-" + mm + "-" + yyyy);
                tvToDate.setText(yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                toDateMillis = calendar.getTimeInMillis();
            }
        };
    }

    public void showDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        custAdapter = new CustomerListDialogAdapter(customerList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(custAdapter);

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
                    if (custAdapter != null) {
                        filterCustomer(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterCustomer(String text) {
        ArrayList<Cust> temp = new ArrayList();
        for (Cust d : customerList) {
            Log.e("FILTER : ", "----------------- " + text);
            if (d.getCustName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        custAdapter.updateList(temp);
    }

}
