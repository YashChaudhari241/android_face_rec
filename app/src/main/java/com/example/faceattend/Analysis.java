package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.GetLeavesModel;
import com.example.faceattend.models.StatModel;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserDetailsModel;
import com.example.faceattend.models.UserObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Analysis extends AppCompatActivity {

    String idToken,pubID;
    boolean result;
    double[] week = null;
    double total;
    double ot;
    int avg_weeklyHours;
    int avg_dailyHours;
    int missed;
    int days;
    double sum=0;
    int properLeaves;
    int unApprovedLeave;
    int unDocumented;
    int extraLeave;
    int daysCame;//days present
    int lateArr;
    int absent;
    String avgStart;
    String avgEnd;

    String error;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();
        Intent j = getIntent();
        if(j.getStringExtra("pubID") != null){
            pubID = j.getStringExtra("pubID");
        }
        else if(!users.isEmpty()){
            Log.d("in1", "in");
            Log.d("in1", "empty"+users.get(0).getPubID());
            Log.d("in1", "empty"+users.get(0).getPubID());


            if(users.get(0).getPubID()!=null){

                pubID=users.get(0).getPubID();
                Log.v("userid",users.get(0).getPubID());
            }
        }
        else{
            finish();
        }

        TextView AttendPercent = findViewById(R.id.attendPercent);
        TextView startTimeTxt = findViewById(R.id.attendPercent2);
        TextView endTimeTxt = findViewById(R.id.attendPercent3);
        TextView otText = findViewById(R.id.attendPercent4);
        TextView dailyText = findViewById(R.id.attendPercent11);
        TextView totalTxt = findViewById(R.id.attendPercent6);
        TextView daysPresentTxt = findViewById(R.id.attendPercent7);
        TextView lateTxt = findViewById(R.id.attendPercent8);
        TextView daysAbsentTxt = findViewById(R.id.attendPercent9);
        TextView apprText = findViewById(R.id.attendPercent13);
        TextView weeklyTxt = findViewById(R.id.attendPercent14);
        TextView uninfTxt = findViewById(R.id.attendPercent12);
        ProgressBar p7 = findViewById(R.id.progressBar7);

        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        idToken=getIntent().getStringExtra("idToken");

        Call<StatModel> call= service.getStatistics("Bearer "+idToken,pubID);

        call.enqueue(new Callback<StatModel>() {
            @Override
            public void onResponse(Call<StatModel> call,
                                   retrofit2.Response<StatModel> response) {

                if(response.body()!=null&&response.body().isResult())
                {
                    week = response.body().getWeek();
                    total = response.body().getTotal();
                    ot = response.body().getOt();
                    missed = response.body().getMissed();
                    days = response.body().getDays();
                    properLeaves = response.body().getProperLeaves();
                    unApprovedLeave = response.body().getUnApprovedLeave();
                    unDocumented = response.body().getUnDocumented();
                    extraLeave = response.body().getExtraLeave();
                    //NEW
                    daysCame = response.body().getDaysCame();
                    absent = response.body().getAbsent();
                    lateArr = response.body().getLateArr();
                    avgStart = response.body().getAvgStart();
                    avgEnd = response.body().getAvgEnd();
                    String start[] = avgStart.split(":");
                    String end[] = avgEnd.split(":");

                    avgStart = timeFormatter(start[0]) + ":" + timeFormatter(start[1]);
//                    avgEnd = timeFormatter(end[0]) + ":" + timeFormatter(end[1]);

//                    String values[] = new String[]{"Total no. of days: " + Integer.toString(days), "Total no. of days present: " + Integer.toString(daysCame), "Total no. of days absent: " + Integer.toString(absent),
//                            "Total no. of late arrivals: " + Integer.toString(lateArr), "Avg. office arrival time: " + avgStart,
//                            "Total hours worked: " + Integer.toString((int) (total / 60)), "Avg. weekly hours: " + Integer.toString(avg_weeklyHours),
//                            "Avg. daily hours: " + Integer.toString(avg_dailyHours), "Total overtime: " + Integer.toString((int) (ot / 60)), "Exits missed: " + Integer.toString(missed),
//                            "Approved leaves taken: " + Integer.toString(properLeaves), "Unapproved leaves: " + Integer.toString(unApprovedLeave), "Extra leaves taken: " + Integer.toString(extraLeave)};

//                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, Arrays.asList(values));
                    double atPer = (daysCame + absent == 0)? 0:((double)daysCame*100)/(absent+daysCame) ;
                    AttendPercent.setText(String.format("%.2f",atPer)+"%");
                    startTimeTxt.setText(avgStart);
                    endTimeTxt.setText(Integer.toString(days));
                    otText.setText(Integer.toString((int)(ot/60)));
                    dailyText.setText(Integer.toString(avg_dailyHours));
                    totalTxt.setText(Integer.toString((int) (total / 60)));
                    daysPresentTxt.setText(Integer.toString(daysCame));
                    lateTxt.setText(Integer.toString(lateArr));
                    daysAbsentTxt.setText(Integer.toString(absent));
                    apprText.setText(Integer.toString(properLeaves));
                    weeklyTxt.setText(Integer.toString(avg_weeklyHours));
                    uninfTxt.setText(Integer.toString(unApprovedLeave+unDocumented));
                    findViewById(R.id.textView).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView13).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView10).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView16).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView9).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView15).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView12).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView14).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView6).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView8).setVisibility(View.VISIBLE);
                    findViewById(R.id.view4).setVisibility(View.VISIBLE);
                    AttendPercent.setVisibility(View.VISIBLE);
                    startTimeTxt.setVisibility(View.VISIBLE);
                    endTimeTxt.setVisibility(View.VISIBLE);
                    otText.setVisibility(View.VISIBLE);
                    dailyText.setVisibility(View.VISIBLE);
                    totalTxt.setVisibility(View.VISIBLE);
                    daysPresentTxt.setVisibility(View.VISIBLE);
                    lateTxt.setVisibility(View.VISIBLE);
                    daysAbsentTxt.setVisibility(View.VISIBLE);
                    apprText.setVisibility(View.VISIBLE);
                    weeklyTxt.setVisibility(View.VISIBLE);
                    uninfTxt.setVisibility(View.VISIBLE);
                    p7.setVisibility(View.GONE);
//                    lv.setAdapter(arrayAdapter);
                }
                else if(response.body() != null && !response.body().isResult()){
                    Toast.makeText(Analysis.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                    p7.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(Analysis.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    p7.setVisibility(View.GONE);
                }
               // Log.d("Analysis1",Double.toString(total));

                //Log.d("Analysis1",Double.toString(ot));
               // Log.d("Analysis1",Integer.toString(missed));
                //Log.d("Analysis1",Integer.toString(properLeaves));
                //Log.d("Analysis1",Integer.toString(unApprovedLeave));
               // Log.d("Analysis1",Integer.toString(unDocumented));
               // Log.d("Analysis1",Integer.toString(extraLeave));*/

                //leavearr=response.body().getLeaves();
                //leaveList= Arrays.asList(leavearr);
                //Log.d("leaveList",leaveList.get(1).getMsg());
                //myLeavesAdapter.setLeaveList(leaveList);
                //leave.add(leaveList[0].getStartDate());
                //Toast.makeText(MyLeaves.this, leavearr[0].getStartDate(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<StatModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
        Log.d("Analysis2",Integer.toString(days));

        if(week!=null) {

            for (int i = 0; i < week.length; i++) {
                sum += (week[i] / 60);

            }
            avg_weeklyHours=(int)(sum/week.length);
            avg_dailyHours=(int) ((total/60)/days);

        }
        else
        {
            avg_weeklyHours=0;
            avg_dailyHours=0;
        }





        Log.d("days1",Integer.toString(days));












    }

    public String timeFormatter(String time)
    {


        if(time.length()==1)
        {
            time="0"+time;
        }
        return time;


    }

}