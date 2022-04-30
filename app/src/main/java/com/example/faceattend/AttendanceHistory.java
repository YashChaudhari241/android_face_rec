package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.faceattend.models.AttendanceDao;

import java.util.List;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;

public class AttendanceHistory extends AppCompatActivity {
    String idToken;
    MCalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        calendar=(findViewById(R.id.calendar));
        Intent i=getIntent();
        idToken=i.getStringExtra("idToken");
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        AttendanceDao adao = db.attendanceDao();
        List<Attendance> att=adao.getAll();
        DateData d[]=new DateData[att.size()];
            for(int j=0;j<d.length;j++){
                String datearr[]=((att.get(j).getTimeStamp().substring(0,10))).split("-",-2);
                d[j]=new DateData(0,0,0);
                d[j].setYear(Integer.parseInt(datearr[0]));
                d[j].setMonth(Integer.parseInt(datearr[1]));
                d[j].setDay(Integer.parseInt(datearr[2]));

            }

        for(DateData a:d){
            calendar.markDate(a.setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, R.attr.colorPrimary)));
            Log.v("date", a.getDayString());
        }

//        GETApi service =
//                ServiceGenerator.createService(GETApi.class);
//        Call<UserDetailsModel> call = service.userDetails("Bearer "+idToken);
//        call.enqueue(new Callback<UserDetailsModel>() {
//            @Override
//            public void onResponse(Call<UserDetailsModel> call,
//                                   retrofit2.Response<UserDetailsModel> response) {
//                Attendance att[]=response.body().getAttendance();
//                try {
//                    JSONObject atten=new JSONObject(response.body().toString());
//                    Log.v("body",atten.toString());
//                    JSONArray jsonArray=atten.getJSONArray("attendance");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Log.v("timestamp",att[0].getTimeStamp());
//                //Log.v("Upload", response.body().getAttendance());
//                //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
//
////                try {
////                    //JSONObject jsonObject = new JSONObject(response.toString());
////                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<UserDetailsModel> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());
//            }
//        });

    }
}