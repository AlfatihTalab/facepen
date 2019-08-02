package com.alfatih.facepen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button regButton, loginButton;
    private EditText userName, pass, passConf;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        regButton = (Button) findViewById(R.id.reg_Btn);
        loginButton = (Button) findViewById(R.id.loginRG_btn);
        userName = (EditText) findViewById(R.id.user_edt_text_rg);
        pass = (EditText) findViewById(R.id.pass_edt_text_rg);
        passConf = (EditText) findViewById(R.id.passConf_edt_text_rg);
        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //setting register button
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regButton.setFocusableInTouchMode(false);
                progressDialogShow();
                regButton.setFocusable(false);
                String email = userName.getText().toString();
                String password = pass.getText().toString();
                String ConfirmPassword = passConf.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(ConfirmPassword)) {
                    if (password.equals(ConfirmPassword)) {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressDialogHide();
                                            startActivity(new Intent(
                                                    RegisterActivity.this,
                                                    SetupActivity.class));
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(
                                                    RegisterActivity.this, "Error: "
                                                            + error, Toast.LENGTH_SHORT
                                            ).show();
                                        }//progressDialogHide();
                                    }
                                }
                        );
                        progressDialogHide();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Confirme your password pleas!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void progressDialogHide() {
        progressDialog.dismiss();
    }

    private void progressDialogShow() {

        progressDialog.setTitle("Authentication");
        progressDialog.setMessage("Please wait and relax !");
        progressDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {

            sentToMain();
        }


    }

    private void sentToMain() {

        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}
