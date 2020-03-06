package com.ats.shivshambhoo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.adapter.AddProjectAdapter;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.AddProject;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProjectLisFragment extends Fragment {
private RecyclerView recyclerView;
    ArrayList<AddProject> projectList = new ArrayList<>();
    AddProjectAdapter adapter;
    Plant plant;
    int plantId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_project_lis, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        getActivity().setTitle("Project List");
        try {
            String plantStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_PLANT);
            Gson gsonPlant = new Gson();
            plant = gsonPlant.fromJson(plantStr, Plant.class);
             plantId=plant.getPlantId();
           // tvPlantName.setText("" + plant.getPlantName());
        } catch (Exception e) {
        }

        DisplayProjectList(plantId);


        return view;
    }

    private void DisplayProjectList(int plantId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AddProject>> listCall = Constants.myInterface.getAllProList(plantId);
            listCall.enqueue(new Callback<ArrayList<AddProject>>() {
                @Override
                public void onResponse(Call<ArrayList<AddProject>> call, Response<ArrayList<AddProject>> response) {
                    Log.e("response","----------------------"+response.body());

                    try {
                        if (response.body() != null) {

                            projectList.clear();
                            projectList = response.body();

                            adapter = new AddProjectAdapter(projectList, getContext());
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
                public void onFailure(Call<ArrayList<AddProject>> call, Throwable t) {
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
