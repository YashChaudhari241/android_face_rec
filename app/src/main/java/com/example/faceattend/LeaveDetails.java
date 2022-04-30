package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.faceattend.models.LeaveModel;

import java.util.List;

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
        approvalTime=findViewById(R.id.approvalTime);
        message=findViewById(R.id.message);
        orgOwner=findViewById(R.id.orgOwner);
        Intent i=getIntent();
        start=i.getStringExtra("startDate").substring(0,10);
        end=i.getStringExtra("endDate").substring(0,10);
        msg=i.getStringExtra("msg");
        stat=i.getIntExtra("status",0);
        approvaltime=i.getStringExtra("approvalTime");
        owner=i.getStringExtra("orgOwner");
        startDate.setText("Start Date: "+start);
        endDate.setText("End Date: "+end);
        orgOwner.setText("Org Owner: "+owner);
        message.setText("Message: "+msg);
        if(stat==0){
            status.setText("Status: Not Approved");
            approvalTime.setText("Approval Time: -");
        }
        else{
            status.setText("Status: Approved");
            approvalTime.setText("Approval Time: "+approvaltime);
        }


    }
}