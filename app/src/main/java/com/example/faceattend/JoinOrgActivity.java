package com.example.faceattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.GetOrgModel;
import com.example.faceattend.models.InitUserModel;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import retrofit2.Call;
import retrofit2.Callback;

public class JoinOrgActivity extends AppCompatActivity {
    EditText codeText;
    TextView joinButton, heading, editHeader, cancelButton;
    boolean mode = false;
    String uniqueStr;
    ProgressBar progressBar;
    String orgName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_org);
        codeText = findViewById(R.id.emailInput2);
        heading = findViewById(R.id.welcome2);
        editHeader = findViewById(R.id.email2);
        cancelButton = findViewById(R.id.cancelButton);
        joinButton = findViewById(R.id.managerButton2);
        progressBar = findViewById(R.id.progressBar2);

        AppDatabase db = Room.databaseBuilder(this.getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        UserDao userDao = db.userDao();
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mode) {
                    uniqueStr = codeText.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    joinButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    getOrgDetails(codeText.getText().toString());
                } else {
                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                    progressBar.setVisibility(View.VISIBLE);
                    joinButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    mUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                GETApi service =
                                        ServiceGenerator.createService(GETApi.class);
                                Call<InitUserModel> call = service.joinOrg("Bearer " + idToken, uniqueStr, codeText.getText().toString());
                                call.enqueue(new Callback<InitUserModel>() {
                                    @Override
                                    public void onResponse(Call<InitUserModel> call,
                                                           retrofit2.Response<InitUserModel> response) {
                                        InitUserModel res = response.body();
                                        progressBar.setVisibility(View.GONE);
                                        joinButton.setVisibility(View.VISIBLE);
                                        cancelButton.setVisibility(View.VISIBLE);
                                        if(res.getResult()){
                                            userDao.updateOrg(orgName,uniqueStr);
//                                            HomeFragment fragm = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment);
//                                            fragm.setHasOrg(heading.getText().toString());
                                            Toast.makeText(JoinOrgActivity.this, "Successfully Joined Org", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else{
                                            Toast.makeText(JoinOrgActivity.this, res.getError(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<InitUserModel> call, Throwable t) {
                                        Log.e("Upload error:", t.getMessage());
                                    }
                                });
                            } else {
                            }
                        }
                    });

                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setText(String text, boolean passReq) {
        joinButton.setText("Join");
        mode = true;
        orgName = text;
        heading.setText("Join " + text + " ?");
        codeText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        codeText.setText("");
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
                progressBar.setVisibility(View.GONE);
                joinButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

                if (res.isResult()) {
                    setText(res.getOrgName(), !res.isVerified());
                } else {
                    Toast.makeText(JoinOrgActivity.this, res.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOrgModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}

