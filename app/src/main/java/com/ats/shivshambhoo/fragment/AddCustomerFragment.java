package com.ats.shivshambhoo.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.AddProjectModel;
import com.ats.shivshambhoo.model.Cust;
import com.ats.shivshambhoo.model.CustomerType;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;
import com.ats.shivshambhoo.util.ValidationFile;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerFragment extends Fragment implements View.OnClickListener {

    private EditText edName, edRespPerson, edMobile, edEmail, edAddress, edPAN, edGST;
    private Spinner spType;
    private Button btnSubmit;
    private TextView tvPlantName;

    ArrayList<String> typeNameArray = new ArrayList<>();
    ArrayList<Integer> typeIdArray = new ArrayList<>();

    int plantId;
    Cust customer;
    String todayString;
    Plant plant;
    private final int REQUEST_CODE=99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);

        getActivity().setTitle("New Customer");

        edName = view.findViewById(R.id.edName);
        edRespPerson = view.findViewById(R.id.edRespPerson);
        edMobile = view.findViewById(R.id.edMobile);
        edEmail = view.findViewById(R.id.edEmail);
        edAddress = view.findViewById(R.id.edAddress);
        edPAN = view.findViewById(R.id.edPAN);
        edGST = view.findViewById(R.id.edGST);

        spType = view.findViewById(R.id.spType);

        tvPlantName = view.findViewById(R.id.tvPlantName);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        edMobile.setOnClickListener(this);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         todayString = formatter.format(todayDate);
         Log.e("Mytag","currentDate"+todayString);

         try {
            String plantStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_PLANT);
            Gson gsonPlant = new Gson();
            plant = gsonPlant.fromJson(plantStr, Plant.class);
            tvPlantName.setText("" + plant.getPlantName());
        } catch (Exception e) {
        }

        plantId = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_PLANT_ID);

        getCustomerTypeList();

        try {
            String customerStr = getArguments().getString("model");
            Gson gson = new Gson();
            customer = gson.fromJson(customerStr, Cust.class);

            if (customer != null) {

                edName.setText("" + customer.getCustName());
                edRespPerson.setText("" + customer.getRespPerson());
                edMobile.setText("" + customer.getCustMobNo());
                edEmail.setText("" + customer.getCustEmail());
                edAddress.setText("" + customer.getCustAddress());
                edPAN.setText("" + customer.getCustPanNo());
                edGST.setText("" + customer.getCustGstNo());

            }
        } catch (Exception e) {
            Log.e("ADD CUST FRG", " ------- EXCEPTION " + e.getMessage());
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edMobile){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (view.getId() == R.id.btnSubmit) {

            String strName, strRespPerson, strMobile, strEmail, strAddress, strPAN, strGST;

            boolean isValidName = false, isValidRespPerson = false, isValidMobile = false, isValidEmail = false, isValidPANCard = false, isValidGSTNo = false;

            strName = edName.getText().toString().trim();
            strRespPerson = edRespPerson.getText().toString().trim();
            strMobile = edMobile.getText().toString().trim();
            strEmail = edEmail.getText().toString().trim();
            strAddress = edAddress.getText().toString().trim();
            strPAN = edPAN.getText().toString().trim();
            strGST = edGST.getText().toString().trim();

            Log.e("Mytag","--------strName"+strName);
            Log.e("Mytag","--------strRespPerson"+strRespPerson);
            Log.e("Mytag","--------strMobile"+strMobile);
            Log.e("Mytag","--------strEmail"+strEmail);

            if (strName.isEmpty()) {
                edName.setError("required");
            } else if ((new ValidationFile().isValidNameString(strName))) {
                edName.setError("invalid string");
            } else {
                edName.setError(null);
                isValidName = true;
            }

            if (strRespPerson.isEmpty()) {
                edRespPerson.setError("required");
            } else if ((new ValidationFile().isValidNameString(strRespPerson))) {
                edName.setError("invalid string");
            } else {
                edRespPerson.setError(null);
                isValidRespPerson = true;
            }if (strMobile.isEmpty()) {
                edMobile.setError("required");
            }
//            else if (strMobile.length() != 10) {
//                edMobile.setError("required 10 digits");
//            } else if (strMobile.equalsIgnoreCase("0000000000")) {
//                edMobile.setError("invalid number");
//            if (!(new ValidationFile().isValidPAN(strPAN))) {
//                edPAN.setError("Invalid PAN");
//            } else {
//                edPAN.setError(null);
//                isValidPANCard = true;
//            if (!(new ValidationFile().isValidGST(strGST))) {
//                edGST.setError("Invalid GST number");
//            } else {
//                edGST.setError(null);
//                isValidGSTNo = true;
//            }
//            }
            else {
                edMobile.setError(null);
                isValidMobile = true;
            }

            if (strEmail.isEmpty()) {
                edEmail.setError("required");
            } else if (!(new ValidationFile().isValidEmailAddress(strEmail))) {
                edEmail.setError("Invalid email address");
            } else {
                edEmail.setError(null);
                isValidEmail = true;
            }

//            if (strPAN.isEmpty()) {
//                edPAN.setError("required");
//            } else {
//                edPAN.setError(null);
//                //isValidPANCard = true;
//            }
//
//            if (strGST.isEmpty()) {
//                edGST.setError("required");
//            } else {
//                edGST.setError(null);
//                //isValidGSTNo = true;
//            }

            int typeId = 0;

            if (typeIdArray != null) {
                if (typeIdArray.size() > 0) {
                    typeId = typeIdArray.get(spType.getSelectedItemPosition());
                }
            }


            //if (isValidName && isValidRespPerson && isValidMobile && isValidEmail && isValidPANCard && isValidGSTNo) {

                int plantId = CustomSharedPreference.getInt(getActivity(), CustomSharedPreference.KEY_PLANT_ID);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                int custId = 0;
                if (customer != null) {
                    custId = customer.getCustId();
                }

                Cust cust = new Cust(custId, strName, strRespPerson, strMobile, strEmail, strAddress, strPAN, strGST, typeId, 1, plantId, sdf.format(System.currentTimeMillis()),1);

                if (typeId != 0) {
                   // checkCustomerExists(strMobile, cust);
                    saveCustomer(cust);
                }
                // else {
//                    Toast.makeText(getContext(), "Please select type", Toast.LENGTH_SHORT).show();
//                }
            }

       // }
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
                                String contactNumberName = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                               String contactNumber = num.replaceAll("\\s+","");
                               // Toast.makeText(getContext(), "Number="+num, Toast.LENGTH_LONG).show();
                                edMobile.setText(contactNumber);
                                edRespPerson.setText(contactNumberName);
                            }
                        }
                    }
                    break;
                }
        }
    }


    public void getCustomerTypeList() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CustomerType>> listCall = Constants.myInterface.getCustomerTypeList();
            listCall.enqueue(new Callback<ArrayList<CustomerType>>() {
                @Override
                public void onResponse(Call<ArrayList<CustomerType>> call, Response<ArrayList<CustomerType>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CUSTOMER TYPE LIST : ", " - " + response.body());

                            typeNameArray.clear();
                            typeIdArray.clear();

                           // typeNameArray.add("Select");
                            typeIdArray.add(0);

                            for (int i = 0; i < response.body().size(); i++) {
                                typeNameArray.add(response.body().get(i).getCustTypeName());
                                typeIdArray.add(response.body().get(i).getCustTypeId());
                            }

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeNameArray);
                            spType.setAdapter(spinnerAdapter);

                            Log.e("typeIdArray","------------typeIdArray--------------"+typeIdArray);

                            if (customer != null) {
                                int position = 0;
                                if (typeIdArray.size() > 0) {
                                    for (int i = 0; i < typeIdArray.size(); i++) {
                                        if (customer.getCustType() == typeIdArray.get(i)) {
                                            position = i;
                                            break;
                                        }
                                    }
                                }
                                spType.setSelection(position);
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
                public void onFailure(Call<ArrayList<CustomerType>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveCustomer(final Cust cust) {

        Log.e("PARAMETER","----------Cust--------"+cust);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();
            Call<Cust> listCall = Constants.myInterface.saveCustomer(cust);
            listCall.enqueue(new Callback<Cust>() {
                @Override
                public void onResponse(Call<Cust> call, Response<Cust> response) {
                    Log.e("Responce","--------------------"+response.body());
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE CUSTOMER : ", " - " + response.body());
//                            if(cust.getCustId()==0) {
//                                Cust cust1 = response.body();
//                                AddProjectModel addProject = new AddProjectModel(0, cust1.getCustName(), cust1.getCustId(), cust1.getCustAddress(), todayString, todayString, 1, 1, cust1.getCustMobNo(), cust1.getCustName(), " ", 0, cust1.getCustAddress());
//                                saveProject(addProject);
//                            }else{
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new CustomerListFragment(), "HomeFragment");
                                ft.commit();
                           // }
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
                public void onFailure(Call<Cust> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProject(AddProjectModel addProject) {
        Log.e("PARAMETER","--------------addProject------------"+addProject);
        Call<AddProjectModel> listCall1 = Constants.myInterface.saveProject(addProject);
        listCall1.enqueue(new Callback<AddProjectModel>() {
            @Override
            public void onResponse(Call<AddProjectModel> call, Response<AddProjectModel> response) {
                Log.e("response","--------------addProject------------"+response.body());
                if (response.body().getProjId() != 0) {

                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    Log.e("SAVE PROJECT : ", " - " + response.body());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new CustomerListFragment(), "HomeFragment");
                    ft.commit();
                } else {
                    Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddProjectModel> call, Throwable t) {
                Log.e("onFailure : ", "-----------" + t.getMessage());
                t.printStackTrace();
            }
        });

    }

    public void checkCustomerExists(String mobile, final Cust cust) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.checkCustomerExists(mobile);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CHECK CUSTOMER : ", " - " + response.body());

                            Info info = response.body();

                            if (info.isError()) {
                                Toast.makeText(getContext(), "" + info.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                saveCustomer(cust);
                            }


                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


}
