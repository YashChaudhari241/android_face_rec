package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faceattend.models.GetEmployeesModel;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserDetailsModel;
import com.example.faceattend.models.UserObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewEmp extends AppCompatActivity {
    String idToken;
    List<UserDetailsModel> empList;
    UserDetailsModel arr[];
    RecyclerView recyclerView;
    ViewEmpAdapter viewEmpAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emp);
        empList=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar5);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        viewEmpAdapter= new ViewEmpAdapter(ViewEmp.this,empList);
        recyclerView.setAdapter(viewEmpAdapter);
        GETApi service=ServiceGenerator.createService(GETApi.class);
        idToken=getIntent().getStringExtra("idToken");
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        UserDao userDao = db.userDao();
        List<UserObject> users = userDao.getAll();
        UserObject storedUser = users.get(0);
        Intent i = getIntent();
        String uniq = i.getStringExtra("uniqueStr");
        Call<GetEmployeesModel> call = service.getEmployees("Bearer " + idToken, uniq);
        call.enqueue(new Callback<GetEmployeesModel>() {
            @Override
            public void onResponse(Call<GetEmployeesModel> call, Response<GetEmployeesModel> response) {
                if (response.body() != null&& response.body().isResult()) {
                    arr = response.body().getEmployees();
                    empList = Arrays.asList(arr);
                    viewEmpAdapter.setEmpList(empList);
                    viewEmpAdapter.setToken(idToken);
                progressBar.setVisibility(View.GONE);
                Log.d("response",""+empList.get(0).getEmpName());
                }
                else{
                    Toast.makeText(ViewEmp.this, "No employees Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetEmployeesModel> call, Throwable t) {
                Log.e("Error:", t.getMessage());
            }
        });

    }
}