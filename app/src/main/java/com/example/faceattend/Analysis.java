package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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

        lv=(ListView)findViewById(R.id.list_view);


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();

        if(!users.isEmpty()){
            Log.d("in1", "in");
            Log.d("in1", "empty"+users.get(0).getPubID());
            Log.d("in1", "empty"+users.get(0).getPubID());


            if(users.get(0).getPubID()!=null){

                pubID=users.get(0).getPubID();
                Log.v("userid",users.get(0).getPubID());
            }
        }



        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        idToken=getIntent().getStringExtra("idToken");

        Call<StatModel> call= service.getStatistics("Bearer "+idToken,pubID);

        call.enqueue(new Callback<StatModel>() {
            @Override
            public void onResponse(Call<StatModel> call,
                                   retrofit2.Response<StatModel> response) {




                week=response.body().getWeek();
                total=response.body().getTotal();
                ot=response.body().getOt();
                missed=response.body().getMissed();
                days=response.body().getDays();
                properLeaves=response.body().getProperLeaves();
                unApprovedLeave=response.body().getUnApprovedLeave();
                unDocumented =response.body().getUnDocumented();
                extraLeave=response.body().getExtraLeave();
                //NEW
                daysCame=response.body().getDaysCame();
                absent=response.body().getAbsent();
                lateArr=response.body().getLateArr();
                avgStart=response.body().getAvgStart();
                avgEnd=response.body().getAvgEnd();
                String start[]=avgStart.split(":");
                String end[]=avgEnd.split(":");

                avgStart=timeFormatter(start[0])+":"+timeFormatter(start[1]);
                avgEnd=timeFormatter(end[0])+":"+timeFormatter(end[1]);










                Log.d("Analysis1",Integer.toString(days));

                String values[]= new String[]{"Total no. of days: " + Integer.toString(days),"Total no. of days present: "+Integer.toString(daysCame), "Total no. of days absent: "+Integer.toString(absent) ,
                        "Total no. of late arrivals: "+Integer.toString(lateArr),"Avg. office arrival time: "+avgStart,"Avg. office departure time: "+avgEnd,
                        "Total hours worked: " + Integer.toString((int) (total / 60)), "Avg. weekly hours: " + Integer.toString(avg_weeklyHours),
                        "Avg. daily hours: " + Integer.toString(avg_dailyHours), "Total overtime: " + Integer.toString((int) (ot / 60)), "Exits missed: " + Integer.toString(missed),
                        "Approved leaves taken: " + Integer.toString(properLeaves), "Unapproved leaves: " + Integer.toString(unApprovedLeave), "Extra leaves taken: " + Integer.toString(extraLeave)};

                ArrayAdapter arrayAdapter =new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,Arrays.asList(values));

                lv.setAdapter(arrayAdapter);
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