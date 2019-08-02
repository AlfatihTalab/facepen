package com.alfatih.facepen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private EditText username, password;
    private Button loginButton, registerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        username =(EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar= (ProgressBar) findViewById(R.id.login_progress);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.createAccountButton);

        //RegisterButton

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        //LoginButtonOnClick
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = username.getText().toString();
                String loginPass = password.getText().toString();
                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(loginEmail,loginPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendToMainActivity();
                            }
                            else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error: "
                                        + error,Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null ){

          sendToMainActivity();
        }


    }

    private void sendToMainActivity() {

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
