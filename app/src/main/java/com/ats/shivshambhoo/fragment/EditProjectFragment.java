package com.ats.shivshambhoo.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.CustomerListAddOrderAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.AddProjectModel;
import com.ats.shivshambhoo.model.Cust;
import com.ats.shivshambhoo.model.Plant;
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
public class EditProjectFragment extends Fragment implements View.OnClickListener {
    AddProjectModel addProjectModel;
    private Button submit;
    private final int REQUEST_CODE=99;
    private EditText edPlant,edCustomer,edContace,edMobile,edProjectName,edProjectLocation,edStartDate,edEnddate,edAddress,edPincode,edKm;
    private String PlantName,CustomerName,Contace,Mobile,ProjectName,ProjectLocation,StartDate,Enddate,Address,Pincode,Km;
    private float KM;
    Plant plant;
    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;
    int plantId;
    CustomerListAddOrderAdapter custAdapter1;
    private BroadcastReceiver mBroadcastReceiver;
    int custId=0;
    Dialog dialog;

    private ArrayList<Cust> customerList = new ArrayList<>();
    private ArrayList<String> customerNameList = new ArrayList<>();
    private ArrayList<Integer> customerIdList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_project, container, false);

        String quoteStr = getArguments().getString("model");
        Gson gson = new Gson();
        addProjectModel = gson.fromJson(quoteStr, AddProjectModel.class);

        Log.e("addProjectModel","-----------------------------addProjectModel---------------"+addProjectModel);

        getActivity().setTitle("Edit Project");

        edPlant=(EditText)view.findViewById(R.id.edPlantName);
        edCustomer=(EditText)view.findViewById(R.id.edcustomerName);
        edContace=(EditText)view.findViewById(R.id.edcontact_person_name);
        edMobile=(EditText)view.findViewById(R.id.edMobile);
        edProjectName=(EditText)view.findViewById(R.id.edprojectName);
        edProjectLocation=(EditText)view.findViewById(R.id.edprojectLocation);
        edStartDate=(EditText)view.findViewById(R.id.edstartDate);
        edEnddate=(EditText)view.findViewById(R.id.edendDate);
        edAddress=(EditText)view.findViewById(R.id.edaddress);
        edPincode=(EditText)view.findViewById(R.id.edpinCode);
        edKm=(EditText)view.findViewById(R.id.edKm);
        submit=(Button)view.findViewById(R.id.btnSubmit);

        edContace.setText(addProjectModel.getContactPerName());
        edMobile.setText(addProjectModel.getContactPerMob());
        edProjectName.setText(addProjectModel.getProjName());
        edProjectLocation.setText(addProjectModel.getLocation());
        edStartDate.setText(addProjectModel.getStartDate());
        edEnddate.setText(addProjectModel.getEndDate());
        edPincode.setText(addProjectModel.getPincode());
        edAddress.setText(addProjectModel.getAddress());
       edKm.setText(String.valueOf(addProjectModel.getKm()));
       custId=addProjectModel.getCustId();

        //edCustomer.setText(addProjectModel.getCustId());
       // edKm.setText((int) addProjectModel.getKm());

        plantId = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_PLANT_ID);

        submit.setOnClickListener(this);
        edMobile.setOnClickListener(this);
        edStartDate.setOnClickListener(this);
        edEnddate.setOnClickListener(this);
        edCustomer.setOnClickListener(this);



        getCustomerList();

        try {
            String plantStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_PLANT);
            Gson gsonPlant = new Gson();
            plant = gsonPlant.fromJson(plantStr, Plant.class);
            edPlant.setText("" + plant.getPlantName());
        } catch (Exception e) {
        }

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
        custId = intent.getIntExtra("id", 0);
        Log.e("CUSTOMER NAME : ", " " + name);
        edCustomer.setText("" + name);
        // getProjectList(custId);
        //getPOList(custId);
        // tvCustId.setText("" + custId);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edMobile){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }  else if (v.getId() == R.id.edcustomerName) {
            showDialog();
        }
        else if (v.getId() == R.id.edstartDate) {
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

        } else if (v.getId() == R.id.edendDate) {
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
        else if (v.getId() == R.id.btnSubmit) {
            //boolean isValidName = false, isValidRespPerson = false, isValidMobile = false, isValidEmail = false, isValidPANCard = false, isValidGSTNo = false;

            PlantName = edPlant.getText().toString();
            CustomerName = edCustomer.getText().toString();
            Contace = edContace.getText().toString();
            Mobile = edMobile.getText().toString();
            ProjectName = edProjectName.getText().toString();
            ProjectLocation = edProjectLocation.getText().toString();
            StartDate = edStartDate.getText().toString();
            Enddate = edEnddate.getText().toString();
            Address = edAddress.getText().toString();
            Pincode = edPincode.getText().toString();
            Km = edKm.getText().toString();
            KM= Float.parseFloat(Km);

            Log.e("PlantName : ", "-----------"+PlantName);
            Log.e("CustomerName : ", "-----------"+CustomerName);
            Log.e("Contace : ", "-----------"+Contace);
            Log.e("Mobile : ", "-----------"+Mobile);
            Log.e("ProjectName : ", "-----------"+ProjectName);
            Log.e("KM : ", "-----------"+KM);


            if (PlantName.isEmpty()) {
                edPlant.setError("required");
            } else if (CustomerName.isEmpty()) {
                edCustomer.setError("required");
            } else if (Contace.isEmpty()) {
                edContace.setError("required");
            } else if (ProjectName.isEmpty()) {
                edProjectName.setError("required");
            } else if (ProjectLocation.isEmpty()) {
                edProjectLocation.setError("required");
            } else if (StartDate.isEmpty()) {
                edStartDate.setError("required");
            } else if (Enddate.isEmpty()) {
                edEnddate.setError("required");
            } else if (Address.isEmpty()) {
                edAddress.setError("required");
            } else if (Pincode.isEmpty()) {
                edPincode.setError("required");
            }else if (Km.isEmpty()) {
                edKm.setError("required");
            }
            else if (Mobile.isEmpty()) {
                edMobile.setError("required");
            }  else {
                edMobile.setError(null);
                edPlant.setError(null);
                edCustomer.setError(null);
                edContace.setError(null);
                edProjectName.setError(null);
                edProjectLocation.setError(null);
                edStartDate.setError(null);
                edEnddate.setError(null);
                edAddress.setError(null);
                edPincode.setError(null);
                edKm.setError(null);


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                Date FromDate = null;
                try {
                    FromDate = formatter1.parse(StartDate);//catch exception
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Date TODate = null;
                try {
                    TODate = formatter1.parse(Enddate);//catch exception
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String DateStart = formatter.format(FromDate);
                String DateEnd = formatter.format(TODate);

                AddProjectModel addProject = new AddProjectModel(addProjectModel.getProjId(), ProjectName, custId, ProjectLocation, DateStart, DateEnd, 1, 1, Mobile, Contace, Pincode, KM, Address);
                saveProject(addProject);
            }
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
                            customerList.clear();
                            customerList = response.body();
                            if(customerList!=null)
                            {
                                for(int i=0;i<customerList.size();i++)
                                {
                                    if(customerList.get(i).getCustId()==addProjectModel.getCustId())
                                    {
                                        edCustomer.setText(customerList.get(i).getCustName());
                                    }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor c = contentResolver.query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                // Toast.makeText(getContext(), "Number="+num, Toast.LENGTH_LONG).show();
                                edMobile.setText(num);
                            }
                        }
                    }
                    break;
                }
        }
    }

    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edStartDate.setText(dd + "-" + mm + "-" + yyyy);
            // tvFromDate.setText(yyyy + "-" + mm + "-" + dd);

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
            edEnddate.setText(dd + "-" + mm + "-" + yyyy);
            // tvToDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();
        }
    };

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
    private void saveProject(AddProjectModel addProject) {
        Call<AddProjectModel> listCall1 = Constants.myInterface.saveProject(addProject);
        listCall1.enqueue(new Callback<AddProjectModel>() {
            @Override
            public void onResponse(Call<AddProjectModel> call, Response<AddProjectModel> response) {

                if (response.body() != null) {
                   // if(response.body().getProjId()==addProjectModel.getProjId()) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.e("SAVE PROJECT : ", " - " + response.body().toString());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new AddProjectLisFragment(), "HomeFragment");
                        ft.commit();
                  //  }
                } else {
                    Log.e("Data Null : ", "-----------");
                }

            }

            @Override
            public void onFailure(Call<AddProjectModel> call, Throwable t) {
                Log.e("onFailure : ", "-----------" + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
