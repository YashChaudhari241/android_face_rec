package com.example.faceattend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceattend.models.InitUserModel;
import com.example.faceattend.models.MarkAttendModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ManEmployee extends AppCompatActivity {
    TextView calibrate1,calibrate2,calibrate3,removeEmp;
    final int CAMERA_PIC_REQUEST=1337;
    String idToken,pubID;
    CircleImageView empImg;
    ArrayList<File> imglist= new ArrayList<>();
    public  int PIC_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_employee);
        idToken=getIntent().getStringExtra("idToken");
        pubID=getIntent().getStringExtra("pubID");
        Log.d("pub"," "+pubID);
        empImg=findViewById(R.id.imageView);
        calibrate1=findViewById(R.id.calibrateFace1);
        calibrate2=findViewById(R.id.calibrateFace2);
        calibrate3=findViewById(R.id.calibrateFace3);
        removeEmp=findViewById(R.id.removeEmp);
        removeEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GETApi service =
                        ServiceGenerator.createService(GETApi.class);
                Call<InitUserModel> call=service.removeEmployee("Bearer "+idToken,pubID);
                call.enqueue(new Callback<InitUserModel>() {
                    @Override
                    public void onResponse(Call<InitUserModel> call,
                                           retrofit2.Response<InitUserModel> response) {
                        if(response.body()!=null){
                            Log.v("Upload", response.body().toString());
                            Toast.makeText(ManEmployee.this, "Employee Removed Successfully", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<InitUserModel> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                        Toast.makeText(ManEmployee.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        if(calibrate1.getVisibility()==View.VISIBLE){
            calibrate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra("id","1");
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    calibrate1.setVisibility(View.GONE);
                    calibrate2.setVisibility(View.VISIBLE);
                }
            });
        }
        calibrate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("2","click");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("id","2");
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                calibrate2.setVisibility(View.GONE);
                calibrate3.setVisibility(View.VISIBLE);
            }
        });
        calibrate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("id","3");
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                calibrate3.setVisibility(View.GONE);
                calibrate1.setVisibility(View.VISIBLE);
            }
        });
//        if(calibrate2.getVisibility()==View.VISIBLE){
//
//        }
//        if(calibrate3.getVisibility()==View.VISIBLE){
//
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                String id=data.getStringExtra("id");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                File destination = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(byteArray);
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(PIC_CODE<3){
                    imglist.add(destination);
                    // add new requset of picture like this
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    PIC_CODE++;
                }
                else{
                    imglist.add(destination);
                    //empImg.setImageBitmap();
                    uploadFile(imglist);
                }

//                if(id=="1"){
//                    calibrate1.setVisibility(View.GONE);
//                    imglist.add(destination);
//                    calibrate2.setVisibility(View.VISIBLE);
//                }
//                if(id=="2"){
//                    calibrate2.setVisibility(View.GONE);
//                    imglist.add(destination);
//                    calibrate3.setVisibility(View.VISIBLE);
//                }
//                if(id=="3"){
//                    calibrate3.setVisibility(View.GONE);
//                    imglist.add(destination);
//                    calibrate1.setVisibility(View.VISIBLE);
//                    uploadFile(imglist);
//                }

                //imageView.setImageBitmap(bitmap);

            }
        }
    }
    private void uploadFile(ArrayList<File> imglist) {
        // create upload service client
        GETApi service =
                ServiceGenerator.createService(GETApi.class);
        //File f = new File(this.getCacheDir(), file);



        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //File file = File(RealPathUtils.getRealPathFromURI_API19(MainActivity.this, fileUri))
        //File file1 = new File(getRealPathFromURI(fileUri));
        //File file1 = FileUtils;
        //showFileChooser();
        // create RequestBody instance from file
        RequestBody requestFile1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), imglist.get(0));

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body1 =
                MultipartBody.Part.createFormData("pic1", "img1", requestFile1);
        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("multipart/form-data"), imglist.get(1));

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("pic2", "img2", requestFile2);
        RequestBody requestFile3 =
                RequestBody.create(MediaType.parse("multipart/form-data"), imglist.get(2));

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body3 =
                MultipartBody.Part.createFormData("pic3", "img3", requestFile3);
//        RequestBody locx=RequestBody.create(MediaType.parse("part/form-data"),"51");
//        RequestBody locy=RequestBody.create(MediaType.parse("part/form-data"),"51");
//        RequestBody entryExit=RequestBody.create(MediaType.parse("multipart/form-data"),"true");




        // add another part within the multipart request
        //String descriptionString = "hello, this is description speaking";
        //RequestBody description =
        //RequestBody.create(
        //okhttp3.MultipartBody.FORM, descriptionString);
        //Call<ResponseBody> call = service.saveFace("matt","xyz1", body);


        Call<InitUserModel> call = service.calibrateFace("Bearer "+idToken,body1,body2,body3,pubID);
        Log.d("call", String.valueOf(call));
        call.enqueue(new Callback<InitUserModel>() {
            @Override
            public void onResponse(Call<InitUserModel> call,
                                   retrofit2.Response<InitUserModel> response) {
                if(response.body()!=null){
                    Log.v("Upload", response.body().toString());
                    Toast.makeText(ManEmployee.this, "Face Calibrated Succesfully", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<InitUserModel> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                Toast.makeText(ManEmployee.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}