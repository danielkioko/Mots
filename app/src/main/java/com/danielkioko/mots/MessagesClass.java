package com.danielkioko.mots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.danielkioko.mots.Accounts.Login;
import com.danielkioko.mots.model.User;
import com.danielkioko.mots.util.UsersAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class MessagesClass extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UsersAdapter adapter;
    private List<User> mUsersList = new ArrayList<>();
    private EditText searchUsers;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messages_tab, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUsersDBRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchUsers = (EditText) rootView.findViewById(R.id.findUsers);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.usersRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mUsersList.clear();

//reset adapter with empty array list (it did the trick animation)
        adapter = new UsersAdapter(mUsersList, getActivity());
        mRecyclerView.setAdapter(adapter);
        mUsersList.addAll(mUsersList);
        clearData();

        return rootView;
    }

    public void clearData() {
        mUsersList.removeAll(mUsersList);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    private void populaterecyclerView(){
        adapter = new UsersAdapter(mUsersList, getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void queryUsersAndAddthemToList(){
        mUsersDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot snap: dataSnapshot.getChildren()){
                        User user = snap.getValue(User.class);
                        //if not current user, as we do not want to show ourselves then chat with ourselves lol
                        try {
                            if(!user.getUserId().equals(mAuth.getCurrentUser().getUid())){
                                mUsersList.add(user);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                populaterecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        checkIfUserIsSignIn();
        queryUsersAndAddthemToList();
        clearData();
    }

    private void checkIfUserIsSignIn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
        } else {
            goToSignIn();
        }
    }

    private void goToSignIn(){
        startActivity(new Intent(getActivity(), Login.class));
    }

}
