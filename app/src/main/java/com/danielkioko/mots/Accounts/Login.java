package com.danielkioko.mots.Accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danielkioko.mots.R;
import com.danielkioko.mots.Tabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText mEmailLoginEditText, mPasswordLoginEditText;
    private Button mLoginButton, mGoToCreateNewAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    View loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = (View)findViewById(R.id.activity_login);

        mEmailLoginEditText = findViewById(R.id.etRegEmail);
        mPasswordLoginEditText = findViewById(R.id.etRegPassword);
        mLoginButton = findViewById(R.id.btnLogin);
        mGoToCreateNewAccount = findViewById(R.id.btnToRegister);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailLoginEditText.getText().toString().trim();
                String password = mPasswordLoginEditText.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(Login.this, "You must provide email", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Login.this, "You must provide password", Toast.LENGTH_SHORT).show();
                }else{
                    logInUsers(email, password);
                }
            }
        });

        mGoToCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateNewAccount();
            }
        });

    }

    private void logInUsers(String email, String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if(!task.isSuccessful()){
                    //error loging
                    Toast.makeText(Login.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    goToChatUsersActivity();
                }
            }
        });
    }

    private void goToChatUsersActivity(){
        Intent intent = new Intent(Login.this, Tabs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToCreateNewAccount(){
        startActivity(new Intent(Login.this, Register.class));
    }

}
