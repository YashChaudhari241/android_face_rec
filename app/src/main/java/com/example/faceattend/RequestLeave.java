package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.InitUserModel;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserObject;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RequestLeave extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    String start,end,idToken;
    Button pickStartDate,pickEndDate;
    EditText message;
    DatePickerDialog datePickerDialog;
    TextView startDate,endDate,submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_leave);
        idToken=getIntent().getStringExtra("idToken");
        Log.d("requestLeave",idToken);

            // Use the current date as the default date in the picker
//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
//                .build();
//        UserDao userDao=db.userDao();
//        List<UserObject> udlist=userDao.getAll();
//        UserObject u=udlist.get(0);
//        String hasOrg=u.orgName;
        calendar = Calendar.getInstance();
        pickStartDate=findViewById(R.id.pickStartDateButton);
        pickEndDate=findViewById(R.id.pickEndDateButton);
        startDate=findViewById(R.id.startDate);
        endDate=findViewById(R.id.endDate);
        message=findViewById(R.id.message);
        submitButton=findViewById(R.id.requestButton);
        year = calendar.get(Calendar.YEAR);
        //dateView = (TextView) findViewById(R.id.startDate);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        pickStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RequestLeave.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String day=String.valueOf(dayOfMonth);
                                String month=String.valueOf(monthOfYear+1);
                                // set day of month , month and year value in the edit text
                                if(dayOfMonth<10){
                                    day="0"+day;
                                }
                                if(monthOfYear+1<10){
                                    month="0"+month;
                                }
                                start=year +"-"+ month +"-"+day;
                                // set day of month , month and year value in the edit text
//                                start=year +"-"+ (monthOfYear + 1) +"-"+dayOfMonth;
                                startDate.setText(start);

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()-10000);
                datePickerDialog.show();
            }
        });
        pickEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RequestLeave.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String day=String.valueOf(dayOfMonth);
                                String month=String.valueOf(monthOfYear+1);
                                // set day of month , month and year value in the edit text
                                if(dayOfMonth<10){
                                    day="0"+day;
                                }
                                if(monthOfYear+1<10){
                                    month="0"+month;
                                }
                                end=year +"-"+ month +"-"+day;
                                endDate.setText(end);

                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()-10000);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GETApi service =
                        ServiceGenerator.createService(GETApi.class);
                Call<InitUserModel> call=service.newLeave("Bearer "+idToken,start,end,message.getText().toString());
                call.enqueue(new Callback<InitUserModel>() {
                    @Override
                    public void onResponse(Call<InitUserModel> call,
                                           retrofit2.Response<InitUserModel> response)
                    {
                        //Log.v("RequestLeave response", response.body().getError());
                        if(response.body().getResult()){
                            Toast.makeText(RequestLeave.this, "Leave Request Submitted Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }


//                try {
//                    //JSONObject jsonObject = new JSONObject(response.toString());
//                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                    }

                    @Override
                    public void onFailure(Call<InitUserModel> call, Throwable t) {
                        Log.e("Error:", t.getMessage());
                    }
                });

            }
        });


        //Call<InitUserModel> call = service.markAttendance("Bearer "+idToken,body,true);

        }

    }
