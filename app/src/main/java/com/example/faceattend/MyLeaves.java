package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.faceattend.models.GetLeavesModel;
import com.example.faceattend.models.LeaveModel;
import com.example.faceattend.models.MarkAttendModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyLeaves extends AppCompatActivity {
    public  static LeaveModel [] leavearr;
    public static List<LeaveModel> leaveList;
    RecyclerView recyclerView;
    MyLeavesAdapter myLeavesAdapter;
    String idToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leaves);
        leaveList=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.listView);
       RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myLeavesAdapter= new MyLeavesAdapter(MyLeaves.this,leaveList);
        recyclerView.setAdapter(myLeavesAdapter);
        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        idToken=getIntent().getStringExtra("idToken");
        Call<GetLeavesModel> call= service.getMyLeaves("Bearer "+idToken);
        call.enqueue(new Callback<GetLeavesModel>() {
            @Override
            public void onResponse(Call<GetLeavesModel> call,
                                   retrofit2.Response<GetLeavesModel> response) {
                //Log.d("MyLeaves Response",response.body().getError());
                if(response.body().getLeaves()!=null){
                    leavearr=response.body().getLeaves();
                    leaveList= Arrays.asList(leavearr);
                    //Log.d("leaveList",leaveList.get(1).getMsg());
                    myLeavesAdapter.setLeaveList(leaveList);
                }

                //leave.add(leaveList[0].getStartDate());

            }

            @Override
            public void onFailure(Call<GetLeavesModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }


}