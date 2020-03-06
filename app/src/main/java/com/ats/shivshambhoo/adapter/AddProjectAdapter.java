package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.activity.HomeActivity;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.fragment.AddProjectLisFragment;
import com.ats.shivshambhoo.fragment.EditProjectFragment;
import com.ats.shivshambhoo.model.AddProject;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.util.CommonDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProjectAdapter extends RecyclerView.Adapter<AddProjectAdapter.MyViewHolder> {
    private ArrayList<AddProject> projectList;
    private Context context;

    public AddProjectAdapter(ArrayList<AddProject> projectList, Context context) {
        this.projectList = projectList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddProjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_project_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProjectAdapter.MyViewHolder myViewHolder, int i) {
        final AddProject model = projectList.get(i);

        myViewHolder.tvPlantname.setText(model.getPlantName());
        myViewHolder.tvContactPersonname.setText(model.getContactPerName());
        myViewHolder.tvCustomerName.setText(model.getCustName());
        myViewHolder.tvLocation.setText(model.getLocation());
        myViewHolder.tvMobile.setText(model.getContactPerMob());
        myViewHolder.tvProjectName.setText(model.getProjName());


        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_quotation_pending, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            EditProjectFragment adf = new EditProjectFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "AllQuotationListFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete quotation?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteQuotation(model.getProjId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void deleteQuotation(Integer projId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteProject(projId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DELETE PROJECT: ", " - " + response.body());

                            if (!response.body().isError()) {

                                HomeActivity activity = (HomeActivity) context;

                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new AddProjectLisFragment(), "HomeFragment");
                                ft.commit();


                            } else {
                                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPlantname, tvMobile, tvLocation, tvCustomerName,tvProjectName,tvContactPersonname;
        public ImageView ivEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlantname=(TextView) itemView.findViewById(R.id.tvPlantName);
            tvContactPersonname=(TextView)itemView.findViewById(R.id.tvContactPersonName);
            tvCustomerName=(TextView)itemView.findViewById(R.id.tvCustomerName);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvMobile=(TextView)itemView.findViewById(R.id.tvMobile);
            tvProjectName=(TextView)itemView.findViewById(R.id.tvProjectName);
            ivEdit=(ImageView) itemView.findViewById(R.id.ivEdit);
        }
    }
}
