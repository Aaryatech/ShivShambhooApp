package com.ats.shivshambhoo.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.OrderDetailListAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.GetOrder;
import com.ats.shivshambhoo.model.GetOrderDetail;
import com.ats.shivshambhoo.model.OrderDetail;
import com.ats.shivshambhoo.model.OrderHeader;
import com.ats.shivshambhoo.util.CommonDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderNo, tvDate, tvCustomer, tvProject, tvTotal;
    private RecyclerView recyclerView;
    private EditText edDeliveryDate,edDeliveryTime;
    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;
    GetOrder orderHeader;
    Button submit;

    ArrayList<GetOrderDetail> orderDetailList = new ArrayList<>();
    OrderDetailListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Order Detail");

        tvOrderNo = findViewById(R.id.tvOrderNo);
        tvDate = findViewById(R.id.tvDate);
        tvCustomer = findViewById(R.id.tvCustomer);
        tvProject = findViewById(R.id.tvProject);
        edDeliveryDate = findViewById(R.id.tvDeliveryDate);
        edDeliveryTime = findViewById(R.id.tvDeliveryTime);
        tvTotal = findViewById(R.id.tvTotal);
        submit=(Button)findViewById(R.id.btnSubmit);

        recyclerView = findViewById(R.id.recyclerView);

        String strJson = getIntent().getStringExtra("model");
        Gson gson = new Gson();
        orderHeader = gson.fromJson(strJson, GetOrder.class);

        Log.e("orderHeader","--------------------------"+orderHeader);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deliveryDate=edDeliveryDate.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                Date DelDate = null;
                try {
                    DelDate = formatter1.parse(deliveryDate);//catch exception
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String delDate = formatter.format(DelDate);

                Date orderDate = null;
                try {
                    orderDate = formatter1.parse(orderHeader.getOrderDate());//catch exception
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String OrderDate = formatter.format(orderDate);


                orderHeader.setDeliveryDate(delDate);
                orderHeader.setOrderDate(OrderDate);
                Log.e("deliveryDate","---------------------------"+deliveryDate);

                String deliveryTime=edDeliveryTime.getText().toString();
                orderHeader.setExVar1(deliveryTime);

                ArrayList<OrderDetail> detailList = new ArrayList<>();
                for(int i=0;i<orderDetailList.size();i++) {
                    OrderDetail detail = new OrderDetail(0, 0, orderDetailList.get(i).getPoId(), orderDetailList.get(i).getItemId(), orderDetailList.get(i).getPoDetailId(), orderDetailList.get(i).getOrderQty(), orderDetailList.get(i).getPoRate(),orderDetailList.get(i).getTotal(), 1, orderDetailList.get(i).getOrderQty(), 0);
                    detailList.add(detail);

                }

                OrderHeader orderHeader1 = new OrderHeader(0, orderHeader.getPlantId(), orderHeader.getCustId(), orderHeader.getPoId(), orderHeader.getProjId(), delDate, orderHeader.getOrderDate(), orderHeader.getProdDate(), orderHeader.getOrderValue(), orderHeader.getOrderNo(), orderHeader.getTotal(), orderHeader.getIsTaxIncluding(), 1, 0, deliveryTime,0, detailList);


                saveOrder(orderHeader1);
            }
        });

        if (orderHeader != null) {

            tvOrderNo.setText("Order No : " + orderHeader.getOrderNo());
            tvDate.setText("" + orderHeader.getOrderDate());
            tvProject.setText("" + orderHeader.getProjName());
            edDeliveryDate.setText(orderHeader.getDeliveryDate());
            edDeliveryTime.setText(orderHeader.getExVar1());
            tvTotal.setText("" + orderHeader.getTotal());
            tvCustomer.setText(""+orderHeader.getCustName());


            getOrderDetailList(orderHeader.getOrderId());
        }

        edDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tvDeliveryDate) {
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
                    DatePickerDialog dialog = new DatePickerDialog(OrderDetailActivity.this, fromDateListener, yr, mn, dy);
                    dialog.show();

                }
            }
        });
        edDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OrderDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edDeliveryTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });

    }

    public void saveOrder(OrderHeader header) {
        if (Constants.isOnline(OrderDetailActivity.this)) {

            Log.e("PARAMETER : ", "---------------- GET ORDER : " + header);


            final CommonDialog commonDialog = new CommonDialog(OrderDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<OrderHeader> listCall = Constants.myInterface.saveOrder(header);
            listCall.enqueue(new Callback<OrderHeader>() {
                @Override
                public void onResponse(Call<OrderHeader> call, Response<OrderHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("GetOrder","-----------------------------"+response.body().toString());
//                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.content_frame, new OrderListFragment(), "HomeFragment");
//                            ft.commit();
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
            Toast.makeText(OrderDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }
    public void getOrderDetailList(int headerId) {

        Log.e("PARAMETERS : ", "     ORDER HEADER ID : " + headerId);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(OrderDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<GetOrderDetail>> listCall = Constants.myInterface.getOrderDetailList(headerId);
            listCall.enqueue(new Callback<ArrayList<GetOrderDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<GetOrderDetail>> call, Response<ArrayList<GetOrderDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ORDER LIST : ", " - " + response.body());
                            orderDetailList.clear();
                            orderDetailList = response.body();

                            adapter = new OrderDetailListAdapter(orderDetailList, OrderDetailActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrderDetailActivity.this);
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
                public void onFailure(Call<ArrayList<GetOrderDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(OrderDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDeliveryDate.setText(dd + "-" + mm + "-" + yyyy);
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
}
