package com.example.farmapp.ui.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.farmapp.R;

public class community extends AppCompatActivity {
    Button sendBtn;
    EditText msgEt;
    ListView listView;
    //ArrayAdapter msgAdapter;
    DatabaseReference myRef;
    ChatAdapter msgAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Community");



        // 1️⃣ Find views first
        listView = findViewById(R.id.listview);
        sendBtn = findViewById(R.id.sendBtn);
        msgEt = findViewById(R.id.msgEt);

        // 2️⃣ Initialize adapter after listView
        ArrayList<ChatMessage> msgList = new ArrayList<>();
        msgAdapter = new ChatAdapter(this, msgList);
        listView.setAdapter(msgAdapter);

        // 3️⃣ Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groupchat");

        // 4️⃣ Load messages
        loadMsg();

        // 5️⃣ Sender name from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String sender = prefs.getString("username", "Anonymous");

        // 6️⃣ Send button
        sendBtn.setOnClickListener(v -> {
            String msg = msgEt.getText().toString();
            if (msg.length() > 0 && sender.length() > 0) {
                ChatMessage chatMessage = new ChatMessage(sender, msg);
                myRef.push().setValue(chatMessage);
                msgEt.setText("");
            }
        });




    }
    private void loadMsg()
    {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                msgAdapter.add(dataSnapshot.getValue().toString());
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                if (chatMessage != null && msgAdapter != null) {
                    msgAdapter.add(chatMessage);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}