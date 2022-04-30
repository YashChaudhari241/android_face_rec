package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.GetLeavesModel;
import com.example.faceattend.models.InitUserModel;
import com.example.faceattend.models.LeaveModel;
import com.example.faceattend.ui.manleaves.MyManageLeavesRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LeaveDetails extends AppCompatActivity {
    TextView startDate,endDate,status,approvalTime,message,orgOwner;
    String start,end,approvaltime,msg,owner;
    int stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);
        startDate=findViewById(R.id.startDate);
        endDate=findViewById(R.id.endDate);
        status=findViewById(R.id.status);
        message=findViewById(R.id.message);
        orgOwner=findViewById(R.id.orgOwner);
        findViewById(R.id.managerButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i=getIntent();
        owner=i.getStringExtra("orgOwner");

        if(i.getBooleanExtra("approve",false)){
            String pubID = i.getStringExtra("pubID");
            Log.v("lpubid",pubID);
            TextView approveButton = findViewById(R.id.managerButton5);
            approveButton.setVisibility(View.VISIBLE);
            orgOwner.setText("Submitted by: "+owner);
            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idToken = i.getStringExtra("idToken");
                    GETApi service =
                            ServiceGenerator.createService(GETApi.class);
                    Call<InitUserModel> call= service.approveLeave("Bearer "+idToken,pubID);
                    call.enqueue(new Callback<InitUserModel>() {
                        @Override
                        public void onResponse(Call<InitUserModel> call,
                                               retrofit2.Response<InitUserModel> response) {
                            //Log.d("MyLeaves Response",response.body().getError());
                            if(response.body().getResult()){
                                Toast.makeText(getApplicationContext(), "Successfully Approved Leave", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<InitUserModel> call, Throwable t) {
                            Log.e("Upload error:", t.getMessage());
                        }
                    });
                }
            });
        }
        else{
            orgOwner.setText("Submitted to: "+owner);
        }
        start=i.getStringExtra("startDate");
        end=i.getStringExtra("endDate");
        msg=i.getStringExtra("msg");
        stat=i.getIntExtra("status",0);
        approvaltime=i.getStringExtra("approvalTime");

        startDate.setText(start + " - "+ end);


        message.setText(msg);
        if(stat==0){
            status.setText("Not Approved");
        }
        else{
            status.setTextColor(0xFF5879EC);
            status.setText("Approved | "+approvaltime);
        }


    }
}