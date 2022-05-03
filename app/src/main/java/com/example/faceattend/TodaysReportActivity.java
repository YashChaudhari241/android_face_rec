package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.MarkAttendModel;
import com.example.faceattend.models.PresentModel;
import com.example.faceattend.models.ReportModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TodaysReportActivity extends AppCompatActivity {
    String idToken,uniqueStr;
    List<PresentModel> presentList;
    PresentModel arr[];
    RecyclerView recyclerView;
    ReportAdapter reportAdapter;
    ProgressBar progressBar;
    TextView totEmp,totPresent,totAbsent;
    int presentcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_report);
        totEmp=findViewById(R.id.totEmp);
        totPresent=findViewById(R.id.totPresent);
        totAbsent=findViewById(R.id.totAbsent);
        progressBar=findViewById(R.id.progressBar5);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        reportAdapter= new ReportAdapter(TodaysReportActivity.this,presentList);
        recyclerView.setAdapter(reportAdapter);
        idToken=getIntent().getStringExtra("idToken");
        uniqueStr=getIntent().getStringExtra("uniqueStr");
        GETApi service=ServiceGenerator.createService(GETApi.class);
        Call<ReportModel> call=service.getReport("Bearer "+idToken,uniqueStr);
        call.enqueue(new Callback<ReportModel>() {
            @Override
            public void onResponse(Call<ReportModel> call,
                                   retrofit2.Response<ReportModel> response) {
                //Log.v("Upload", response.body().getDist());
                if(response.body()!=null&&response.body().isResult()){
                    presentList = (response.body().getPresent());
                    for(PresentModel a:presentList){
                        if(a.isPresent()){
                            presentcount++;
                        }
                    }
                    totEmp.setText("Total Employees: "+presentList.size());
                    totPresent.setText("Total Present: "+presentcount);
                    totAbsent.setText("Total Absent: "+(presentList.size()-presentcount));

                    Log.d("response",""+presentList.get(0).getName());
                    reportAdapter.setEmpList((presentList));
                    reportAdapter.setToken(idToken);
                    progressBar.setVisibility(View.GONE);
                    Log.d("response",""+presentList.get(1).getName());
                    Toast.makeText(TodaysReportActivity.this, "Success", Toast.LENGTH_LONG).show();
                }
                else if(response.body()!=null){
                    Toast.makeText(TodaysReportActivity.this, "", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(TodaysReportActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ReportModel> call, Throwable t) {
                Toast.makeText(TodaysReportActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Upload error:", t.getMessage());
            }
        });

    }
}