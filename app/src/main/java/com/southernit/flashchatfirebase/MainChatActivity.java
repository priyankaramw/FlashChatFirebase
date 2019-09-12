package com.southernit.flashchatfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        setupDisplayName();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMessage();
                return false;
            }
        });


        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(RegisterActivity.TAG, "Sent");
                sendMessage();
            }
        });

    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void setupDisplayName () {
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, "Anonymous");

    }


    private void sendMessage() {

        /** SASITH.
         * If the data is not receiving by firebase that's because permission. Check and change this path in firebase.
         * Goto app console -> Databse -> Realtime Database -> Rules. and change the rule as follows.
         * ".read": false,     into      ".read":"auth!=null",
         * ".write": false     into      ".write":"auth!=null"
         *
         * for more info check lesson 160 Q&A for the instructions.
         */

        // TODO: Grab the text the user typed in and push the message to Firebasef
        String message = mInputText.getText().toString();
        if (!message.equals("")) {
            InstantMessage msgObject = new InstantMessage(message, mDisplayName);
            ref.child("Messages").push().setValue(msgObject);
            mInputText.setText("");
//            Log.d(RegisterActivity.TAG, ref.getDatabase().toString());
//            Log.d(RegisterActivity.TAG, "Trace 2 "+message+", "+mDisplayName);
        }

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.


    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.

    }

}
