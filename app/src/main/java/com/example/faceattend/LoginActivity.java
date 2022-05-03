package com.example.faceattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.AttendanceDao;
import com.example.faceattend.models.GetOrgModel;
import com.example.faceattend.models.MultipleOrgsModel;
import com.example.faceattend.models.OrgDetails;
import com.example.faceattend.models.OwnedOrgsDao;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserDetailsModel;
import com.example.faceattend.models.UserObject;
import com.example.faceattend.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private static final String TAG = "EmailPassword";
    EditText email, password;
    TextView loginButton, regButton;
    String idToken="";
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        //Intent in=new Intent(this,MainActivity.class);
        if (mUser != null) {
            setContentView(R.layout.layout_loading);
            refreshData();
//        finish();
        } else {
            setContentView(R.layout.activity_login);

            mAuth = FirebaseAuth.getInstance();
            loginButton =

                    findViewById(R.id.loginButton);

            regButton =

                    findViewById(R.id.regButton);

            email =

                    findViewById(R.id.emailInput);

            password =

                    findViewById(R.id.passwordInput);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
            regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
            });
        }

    }
    public void refreshData(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "faceattend-database").allowMainThreadQueries().fallbackToDestructiveMigration()
                .build();
        UserDao userDao = db.userDao();
        AttendanceDao adao = db.attendanceDao();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            JSONObject jsonParam = new JSONObject();
                            try {
                                jsonParam.put("priv", i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            idToken = task.getResult().getToken();
                            //in.putExtra("token",idToken);
                            //startActivity(in);
                            Log.d("TOKEN", idToken);
                            Log.d("json", String.valueOf(jsonParam));
                            GETApi service =
                                    ServiceGenerator.createService(GETApi.class);
                            int j = 0; // <------- Whether User is employee or manager, Needs to be updated
//                                Log.d("err", );
//                                Call<InitUserModel> call = service.verifyToken("Bearer " + idToken, j);
                            Call<UserDetailsModel> call = service.userDetails("Bearer " + idToken);
//                                Call<InitOrgModel> call = service.initOrg("Bearer " + idToken,
//                                        "Org3",
//                                        false,
//                                        false,
//                                        0,
//                                        "abcd1234",
//                                        "0700",
//                                        "1400",false,null,null);
                            call.enqueue(new Callback<UserDetailsModel>() {
                                @Override
                                public void onResponse(Call<UserDetailsModel> call,
                                                       retrofit2.Response<UserDetailsModel> response) {
                                    UserDetailsModel res = response.body();
                                    if (res.getResult()) {
                                        //Log.v("Priv: ", Integer.toString(res.getPriv()));
                                        //Log.v("orgDetails", res.toString());
                                        OrgDetails org = res.getOrgDetails();
//                                            Intent i = new Intent(LoginActivity.this, ChoosePriv.class);
//                                            i.putExtra("name",mUser.getDisplayName());
//                                            startActivity(i);
                                        List<UserObject> users = userDao.getAll();
                                        OwnedOrgsDao odao = db.ownedOrgsDao();
                                        List<OrgDetails> orgs = odao.getAll();
//                                            String selUniq = null;
//                                            for(OrgDetails a: orgs){
//                                                if(a.isSelected()){
//                                                    selUniq = a.getUniqueString();
//                                                    break;
//                                                }
//                                            }
                                        OrgDetails a= odao.getSelected(true);
                                        odao.deleteAll();
                                        odao.insertAll(res.getOwnedOrgs());
//                                            if (selUniq == null && !orgs.isEmpty()){
                                        if (a==null && !orgs.isEmpty()){
                                            odao.selectOrg(true,orgs.get(0).getUniqueString());
                                        }
                                        else if(a!=null){
                                            odao.selectOrg(true,a.getUniqueString());
                                        }
                                        adao.deleteAll();
                                        if(res.getAttendance() !=null)
                                            adao.insertAll(res.getAttendance());
                                        db.close();
                                        if (users.isEmpty()) {
                                            if (org!=null){
                                                userDao.insertAll(new UserObject(mUser.getUid(), res.getPriv(), org.getOrgName(), org.getMarkExit(), org.getUniqueString(), org.getMarkLoc(), org.getJoinPass(), org.getDefStart(), org.getDefEnd(),res.getPubID()));
                                            }
                                            else {
                                                userDao.insertAll(new UserObject(mUser.getUid(), res.getPriv(), null, false, null, false, null, null, null, res.getPubID()));
                                                if(res.getPriv()==0) {
                                                    startActivity(new Intent(LoginActivity.this, JoinOrgActivity.class));
                                                }
                                            }
                                            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                            i.putExtra("priv", res.getPriv());
                                            startActivity(i);
                                        } else {
                                            userDao.deleteAll();
                                            if (org!=null) {
                                                userDao.insertAll(new UserObject(mUser.getUid(), res.getPriv(), org.getOrgName(), org.getMarkExit(), org.getUniqueString(), org.getMarkLoc(), org.getJoinPass(), org.getDefStart(), org.getDefEnd(),res.getPubID()));
                                            }
                                            else{
                                                userDao.insertAll(new UserObject(mUser.getUid(), res.getPriv(), null,false,null,false,null,null,null,res.getPubID()));
                                            }

                                        }

                                        finish();
                                    }
                                    else{
                                        Intent i = new Intent(LoginActivity.this, ChoosePriv.class);
                                        i.putExtra("name", mUser.getDisplayName());
                                        startActivity(i);
                                        finish();
                                    }
//                                        Log.v("Response", String.valueOf(response));
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response));
//                                            JSONObject body = jsonObject.getJSONObject("body");
//                                            Log.v("fea", body.getString("result"));
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                        Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                                        try {
//                                            //JSONObject jsonObject = new JSONObject(response.toString());
//                                            Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
                                }

                                @Override
                                public void onFailure(Call<UserDetailsModel> call, Throwable t) {
                                    Log.e("Upload error:", t.getMessage());
                                }
                            });

                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
        List<UserObject> users = userDao.getAll();
        if (!users.isEmpty()) {
            UserObject storedUser = users.get(0);
            Intent i = new Intent(this, DashboardActivity.class);
//                HomeFragment fragment = new HomeFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("priv",storedUser.priv);
//                fragment.setArguments(bundle);//Here pass your data
            i.putExtra("priv", storedUser.priv);
            Intent p=new Intent(LoginActivity.this,RequestLeave.class);
            p.putExtra("idToken","idToken");
            startActivity(i);
            if (storedUser.orgName == null && storedUser.priv==0) {
                startActivity(new Intent(this, JoinOrgActivity.class));
            }
            finish();
        } else {
//            SimpleArcDialog mDialog = new SimpleArcDialog(this);
//            mDialog.setConfiguration(new ArcConfiguration(this));
//            mDialog.show();

//            setContentView(R.layout.layout_loading);
        }
    }
    public void signIn() {
        Log.d(TAG, "signIn" + email);
        String em = email.getText().toString();
        String pass = password.getText().toString();
        ProgressBar p = findViewById(R.id.progressBar6);
        loginButton = findViewById(R.id.loginButton);
        regButton = findViewById(R.id.regButton);
        loginButton.setVisibility(View.GONE);
        regButton.setVisibility(View.GONE);
        p.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(em, pass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            mUser = FirebaseAuth.getInstance().getCurrentUser();
                            refreshData();
//                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}