package com.danielkioko.mots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danielkioko.mots.Accounts.Login;
import com.danielkioko.mots.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileClass extends Fragment {

    private Button logout, userProfile;

    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    private TextView tv_name, tv_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_tab, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = rootView.findViewById(R.id.progressBar);

        tv_name = rootView.findViewById(R.id.tv_name);
        tv_email = rootView.findViewById(R.id.tv_email);

        logout = (Button) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLogout();
            }
        });

        userProfile = (Button) rootView.findViewById(R.id.userProfile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(getActivity(),UpdateProfileActivity.class);
                startActivity(profile);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void ToLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent logout = new Intent(getActivity(), Login.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logout);
    }
}
