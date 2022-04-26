package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;

public class JoinOrgActivity extends AppCompatActivity {
    EditText codeText;
    TextView joinButton, heading, editHeader;
    boolean mode = false;
    String uniqueStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_org);
        codeText = findViewById(R.id.emailInput2);
        heading = findViewById(R.id.welcome2);
        editHeader = findViewById(R.id.email2);
        joinButton = findViewById(R.id.managerButton2);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mode) {
                    getOrgDetails(codeText.getText().toString());
                }
                else{

                }
            }
        });
    }

    private void setText(String text, boolean passReq) {
        joinButton.setText("Join");
        mode = true;
        heading.setText("Join "+text+" ?");
        if (passReq) {
            editHeader.setText("Joining Password");
        } else {
            editHeader.setText("Joining Password (Not Required)");
        }
    }

    private void getOrgDetails(String uniqueStr) {
        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        Call<GetOrgModel> call = service.getOrgDetails(uniqueStr, "");
        call.enqueue(new Callback<GetOrgModel>() {
            @Override
            public void onResponse(Call<GetOrgModel> call,
                                   retrofit2.Response<GetOrgModel> response) {
                GetOrgModel res = response.body();
                if (res.isResult()) {
                    setText(res.getOrgName(), !res.isVerified());
                } else {
                    setText(res.getError(), false);
                }
            }

            @Override
            public void onFailure(Call<GetOrgModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}