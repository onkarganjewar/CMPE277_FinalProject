package com.example.tourguide.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tourguide.R;
import com.example.tourguide.main.MainActivity;
import com.example.tourguide.main.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class Sign_up extends AppCompatActivity {

    private Timer _t;
    private EditText Email;
    private EditText Password;
    private Button SignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private RelativeLayout layout;
    private static int count = 0;
    private int[] drawablearray = {R.drawable.w1,R.drawable.w3,R.drawable.w4,R.drawable.w5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        layout = (RelativeLayout) findViewById(R.id.signupLayout);
        progressDialog = new ProgressDialog(this);
        layout.setBackgroundResource(R.drawable.w1);
        firebaseAuth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.Sign_up_email);
        Password = (EditText) findViewById(R.id.Sign_up_password);

        SignUp = (Button) findViewById(R.id.Sign_up_button);

        _t = new Timer();

        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() // run on ui thread
                {
                    public void run() {
//                        for(int j=0; j<8 ; j++) {
                        if (count < drawablearray.length) {
                            layout.setBackgroundResource(drawablearray[count]);
                            setContentView(layout);
                            count = (count + 1) % drawablearray.length;
                        }
                    }
                });
            }
        }, 10000, 3000);


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email field cannot be left blank!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Password field cannot be left blank!",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Sign_up.this, "User registered successfully!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Sign_up.this, SearchActivity.class));
                        }else {

                            Log.d("DEBUG","Exception: "+task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(Sign_up.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Sign_up.this, "Please enter a valid email id!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
