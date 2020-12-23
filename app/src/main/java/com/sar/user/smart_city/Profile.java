package com.sar.user.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.InputEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private AppCompatTextView name;
    private AppCompatTextView email;
    private AppCompatTextView team;
    private AppCompatTextView logout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private AppCompatImageView backIV;
    private ArrayList<Data> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        team=findViewById(R.id.team);
        logout=findViewById(R.id.logout);
        backIV=findViewById(R.id.backIV);

        data=new ArrayList<>();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });

        backIV.setOnClickListener(view -> {
            finish();
        });

        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!= null)
        {
            name.setText(firebaseAuth.getCurrentUser().getDisplayName());
            email.setText(firebaseAuth.getCurrentUser().getEmail());
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference("review");
        Query specific_user = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("time");

        specific_user.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Data user = postSnapshot.getValue(Data.class);
                            data.add(user);
                        }
                        String TAG="";
                        Log.i(TAG,"add university name = " + data.get(0).text);


//                        //here you will get the data
//                        String firstName = (String) dataSnapshot.getValue();
//                        team.setText(firstName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//        myRef.child(FirebaseAuth.getInstance().getUid().toString()).gaddOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Log.d("popo","oo");
//            }
//        });

//        myRef.child("review").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
//                    //Loop 1 to go through all the child nodes of users
//                    for(DataSnapshot booksSnapshot : uniqueKey.child("Books").getChildren()){
//                        //loop 2 to go through all the child nodes of books node
//                        String bookskey = booksSnapshot.getKey();
//                        String booksValue = booksSnapshot.getValue();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
}

    private void collectReviews(Map<String, Object> value) {
        ArrayList<Long> review = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : value.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            review.add((Long) singleUser.get("Input"));
        }

        team.setText(review.toString());
    }
}

class Data{
    String text;
    String image;

//    public Data(String text, String image) {
//        this.text = text;
//        this.image = image;
//    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
