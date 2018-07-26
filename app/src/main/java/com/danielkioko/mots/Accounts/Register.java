package com.danielkioko.mots.Accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danielkioko.mots.R;
import com.danielkioko.mots.Tabs;
import com.danielkioko.mots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText mNameSignUpEditText;
    private EditText mEmailSignUpEditText;
    private EditText mPasswordEditText;
    private Button mSignUpButton;
    private Button mGoToLoginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBref;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameSignUpEditText = findViewById(R.id.etName);
        mEmailSignUpEditText = findViewById(R.id.etRegEmail);
        mPasswordEditText = findViewById(R.id.etRegPassword);
        mSignUpButton = findViewById(R.id.btnRegister);
        mGoToLoginButton = findViewById(R.id.btnToLogin);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameSignUpEditText.getText().toString();
                String email = mEmailSignUpEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();


                if(name.isEmpty()){
                    Toast.makeText(Register.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(Register.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Register.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    signUpUserWithFirebase(name, email, password);
                }
            }
        });

        mGoToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

    }

    private void signUpUserWithFirebase(final String name, String email, String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(Register.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    final FirebaseUser newUser = task.getResult().getUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    newUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Log.d(Register.class.getName(), "User profile updated.");
                                        /***CREATE USER IN FIREBASE DB AND REDIRECT ON SUCCESS**/
                                        createUserInDb(newUser.getUid(), newUser.getDisplayName(), newUser.getEmail());

                                    }else{
                                        Toast.makeText(Register.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void createUserInDb(String userId, String displayName, String email) {
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, displayName, email);
        mUsersDBref.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(Register.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    //success adding user to db as well
                    //go to users chat list
                    goToChartUsersActivity();
                }
            }
        });
    }

    private void goToLoginActivity(){
        startActivity(new Intent(this, Login.class));
    }

    private void goToChartUsersActivity(){
        startActivity(new Intent(this, Tabs.class));
        finish();
    }

}
