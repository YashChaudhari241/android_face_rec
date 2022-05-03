package com.example.faceattend;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.faceattend.models.InitOrgModel;
import com.example.faceattend.models.InitUserModel;
import com.example.faceattend.models.OrgDetails;
import com.example.faceattend.models.OwnedOrgsDao;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateOrg extends AppCompatActivity {
    View start_time,end_time; int start_hour,start_minute,end_hour,end_minute;
    String office_latitude,office_longitude,office_radius,office_location,str_start_hour,str_start_minute,str_end_hour,str_end_minute;
    String loc[]=new String[1];
    String rad[]=new String[1];
    String org_name,pwd,idToken;String defStart,defEnd;
    EditText org;
    EditText pass;boolean flag1=false,flag2=false;
    Switch switch_exitTime,switch_geofencing;
    boolean exitTime,geofencing;
    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result)
        {
            // gets fired on launch().

            if(result.getResultCode()==78)
            {
                Intent intent=result.getData();

                if(intent!=null)
                {
                    office_latitude= intent.getStringExtra("latitude");
                    office_longitude= intent.getStringExtra("longitude");
                    office_radius= intent.getStringExtra("radius");
                    office_location=office_latitude+":"+office_longitude;
                    loc[0]=office_location;
                    rad[0]=office_radius;
                    Log.d("data", office_latitude);
                    Log.d("data", office_longitude);
                    Log.d("data", office_radius);

                }

            }

        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_org);
        idToken=getIntent().getStringExtra("idToken");
        Log.d("idToken", "Empty"+idToken);

        start_time = findViewById(R.id.start_time);
        end_time= findViewById(R.id.end_time);


    }

    public String timeFormatter(int time)
    {
        String t=Integer.toString(time);

        if(t.length()==1)
        {
            t="0"+t;
        }
        return t;


    }

    public void pickStartTime(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                start_hour=selectedHour;
                start_minute=selectedMinute;

                //start_time.SetText(String.format(Locale.getDefault(), "%02d:%02d",start_hour, start_minute));

                defStart=timeFormatter(start_hour)+timeFormatter(start_minute);

                flag1=true;


            }
        };

        int style= AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog=new TimePickerDialog(this,R.style.TimePickerTheme,onTimeSetListener,start_hour,start_minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    public void pickEndTime(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                end_hour=selectedHour;
                end_minute=selectedMinute;

                //end_time.SetText(String.format(Locale.getDefault(), "%02d:%02d",start_hour, start_minute));
                defEnd=timeFormatter(end_hour)+timeFormatter(end_minute);
                flag2=true;


            }
        };

        int style= AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog=new TimePickerDialog(this,onTimeSetListener,end_hour,end_minute,true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();


    }

    public void location(View view)
    {
        Intent i_loc = new Intent(CreateOrg.this, SelectOfficeLocation.class);
        activityLauncher.launch(i_loc);
        //CreateOrg.this.startActivity(i_loc);
        Log.d("mytag", "printed");



    }

    public void create(View view)
    {
        org=(EditText) findViewById(R.id.emailInput2);
        pass=(EditText) findViewById(R.id.passwordInput);
        switch_exitTime=(Switch)findViewById(R.id.switch1);
        switch_geofencing=(Switch)findViewById(R.id.switch2);
        exitTime=switch_exitTime.isChecked();
        geofencing=switch_geofencing.isChecked();
        org_name=org.getText().toString();
        pwd =pass.getText().toString();
        Log.d("print", org_name);
        Log.d("print", pwd);
        Log.d("print", Boolean.toString(exitTime));
        Log.d("print", Boolean.toString(geofencing));

        if(org_name==""||pwd==""||flag1==false||flag2==false)
        {
            Toast.makeText(CreateOrg.this, "All the fields need to be filled", Toast.LENGTH_SHORT).show();

        }

        else // If fields filled
        {
            GETApi service =
                    ServiceGenerator.createService(GETApi.class);

            Call<InitOrgModel> call=service.initOrg("Bearer "+idToken,org_name,exitTime,false,0,pwd,defStart,defEnd,geofencing,loc,rad);

            call.enqueue(new Callback<InitOrgModel>() {
                @Override
                public void onResponse(Call<InitOrgModel> call,
                                       retrofit2.Response<InitOrgModel> response)
                {
                    Log.d("success2", "We are almost in ");
                    Log.d("success2", Boolean.toString(response.body().getResult()));

                    if(response.body().getResult()){
                        Log.d("success2", "We are in");
                        Toast.makeText(CreateOrg.this, "Org created!", Toast.LENGTH_SHORT).show();

                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                                .build();

                        OwnedOrgsDao oDao = db.ownedOrgsDao();
                        oDao.insert(new OrgDetails(response.body().getUniqueStr(),org_name));
                        oDao.selectOrg(true,response.body().getUniqueStr());
                        finish();

                    }
                    else
                    {
                        Log.d("success2", response.body().getError());

                    }


//                try {
//                    //JSONObject jsonObject = new JSONObject(response.toString());
//                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                }

                @Override
                public void onFailure(Call<InitOrgModel> call, Throwable t) {
                    Log.d("Error:", t.getMessage());
                }
            });





        }



    }

    /*public void getCoordinates()
    {
        office_location=SelectOfficeLocation.getActivityInstance().getData();
        double lat = office_location.latitude;
        double lng = office_location.longitude;
        Log.d("Latitude", Double.toString(lat));
        Log.d("Latitude", Double.toString(lng));

    }*/
}