package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faceattend.models.MultipleOrgsModel;
import com.example.faceattend.models.OrgDetails;
import com.example.faceattend.models.StatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;

public class OrgDetailsActivity extends AppCompatActivity {

    OrgDetails[] orgDetails;
    String idToken;
    String uniqueString;
    String defStart;
    String defEnd;
    String orgName;
    String ownerName;
    String manager_name;
    int org_index;
    TextView org,manager,off_start,off_end,uniq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_details);

        org=(TextView)findViewById(R.id.textView3) ;
        manager=(TextView) findViewById(R.id.textView5);
        off_start=(TextView)findViewById(R.id.starttime);
        off_end=(TextView)findViewById(R.id.endtime);
        uniq=(TextView)findViewById(R.id.uniquestr);

        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        idToken=getIntent().getStringExtra("idToken");
        uniqueString=getIntent().getStringExtra("uniqueStr");

        Call<MultipleOrgsModel> call= service.getOrgs("Bearer "+idToken);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser.getDisplayName()!=null && mUser.getDisplayName()!="")
        {
            manager_name=mUser.getDisplayName();
        }

        Log.d("manager1", ""+manager_name);

        call.enqueue(new Callback<MultipleOrgsModel>() {
            @Override
            public void onResponse(Call<MultipleOrgsModel> call,
                                   retrofit2.Response<MultipleOrgsModel> response) {


                if(response.body()!=null)
                {
                    orgDetails = response.body().getOrgDetails();

                    //Log.d("TAG", orgDetails[0].getOrgName());
                    Log.d("details1", "in1");

                    if(orgDetails!=null) {
                        Log.d("details1", "in2");

                        for (int i = 0; i < orgDetails.length; i++) {
                            if (orgDetails[i].getUniqueString() == uniqueString) {
                                org_index = i;
                                break;
                            }
                        }

                        defStart = orgDetails[org_index].getDefStart();
                        defEnd = orgDetails[org_index].getDefEnd();
                        orgName = orgDetails[org_index].getOrgName();
                        ownerName = orgDetails[org_index].getOwnerName();

                        defStart=defStart.substring(0,2)+":"+defStart.substring(2);
                        defEnd =defEnd.substring(0,2)+":"+defEnd.substring(2);

                        org.setText("Org name: "+orgName);
                        off_start.setText("Office starts at: "+defStart);
                        off_end.setText("Office ends at: "+defEnd);
                        uniq.setText("Unique code: "+uniqueString);
                        manager.setText("Manager: "+manager_name);

                    }











                }




            }


            @Override
            public void onFailure(Call<MultipleOrgsModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }
}