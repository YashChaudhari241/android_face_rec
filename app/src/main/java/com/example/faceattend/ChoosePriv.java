package com.example.faceattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.faceattend.models.InitUserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import retrofit2.Call;
import retrofit2.Callback;


public class ChoosePriv extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_choose_priv);
        String name = intent.getStringExtra("name");

        TextView welText = findViewById(R.id.welcome);
        if(name.contains(" ")){
        name = name.replaceFirst(" ", "\n");
        }
        welText.setText("Welcome,\n" + name);
        TextView employee = findViewById(R.id.employeeButton);
        TextView manager = findViewById(R.id.managerButton);
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPriv(0);
                Intent i = new Intent(ChoosePriv.this, DashboardActivity.class);
                i.putExtra("priv",0);
                startActivity(i);
                finish();
            }
        });
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPriv(0);
                Intent i = new Intent(ChoosePriv.this, DashboardActivity.class);
                i.putExtra("priv",1);
                startActivity(i);
                finish();
            }
        });

    }

    public void setPriv(int priv) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            GETApi service =
                                    ServiceGenerator.createService(GETApi.class);
                            Call<InitUserModel> call = service.verifyToken("Bearer " + idToken, priv);
                            call.enqueue(new Callback<InitUserModel>() {
                                @Override
                                public void onResponse(Call<InitUserModel> call,
                                                       retrofit2.Response<InitUserModel> response) {
                                    InitUserModel res = response.body();

                                }
                                @Override
                                public void onFailure(Call<InitUserModel> call, Throwable t) {
                                    Log.e("Upload error:", t.getMessage());
                                }});
                        } else {

                        }
                    }
                });
    }
}