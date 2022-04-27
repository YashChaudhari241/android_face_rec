package com.example.faceattend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG="EmailPassword";
    EditText email,password,conPassword;
    TextView register,login_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)//if the user is already logged in
        {
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));//starting main activity
            finish();//destroying this activity
        }
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.emailInput);
        password=findViewById(R.id.passwordInput);
        conPassword=findViewById(R.id.confirmPasswordInput);
        register=findViewById(R.id.regButton);
        login_link=findViewById(R.id.loginButton);
        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }
    private void signUp()
    {
        Log.d(TAG,"signup"+email);
        if(validateForm()) {
            String em = email.getText().toString();
            String pass = password.getText().toString();
            mAuth.createUserWithEmailAndPassword(em, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, ChoosePriv.class));
                            } else {
                                Log.w(TAG, "signUpWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
    private boolean validateForm(){
        boolean valid=true;
//        String regex= "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
//        Pattern p=Pattern.compile(regex);

        String em=email.getText().toString();
        if(TextUtils.isEmpty(em)) {
            email.setError("Required");
            valid=false;
        }else{
            email.setError(null);
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Please enter a valid email");
            valid=false;
        }else{
            email.setError(null);
        }
        String pass=password.getText().toString();
        if(TextUtils.isEmpty(pass)) {
            password.setError("Required");
            valid=false;
        }else{
            password.setError(null);
        }
        if(pass.length()<8 || !isValidPassword(pass)){
            password.setError("Password must be atleast 8 characters long and must contain atleast 1 Uppercase, 1 Number and 1 Symbol");
            valid=false;
        }
        else {
            password.setError(null);
        }
        String cpass=conPassword.getText().toString();
        if(TextUtils.isEmpty(cpass)) {
            conPassword.setError("Required");
            valid=false;
        }else{
            conPassword.setError(null);
        }
        if(!TextUtils.equals(pass,cpass)){
            conPassword.setError("Password does not match");
            valid=false;
        }else{
            conPassword.setError(null);
        }
        //Matcher m=p.matcher(pass);
//        if(!m.matches()){
//            password.setError("Password criteria not satisfied");
//        }else{
//            password.setError(null);
//        }
        return valid;

    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


}