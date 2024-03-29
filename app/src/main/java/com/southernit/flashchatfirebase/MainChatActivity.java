package com.southernit.flashchatfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        Log.d(RegisterActivity.TAG, "onCreate");
        // TODO: Set up the display name and get the Firebase reference
        setupDisplayName();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();


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
        /** Below two lines set the user name from sharedPreferences.
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, "Anonymous");
         */

        /** Below lines set name from firebase  */
        retrieveDisplayNameFromFirebaes();
    }


    /** https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile   */
    private void retrieveDisplayNameFromFirebaes() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(RegisterActivity.TAG, "Came to here");
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId();

                // UID specific to the provider
//                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
//                String email = profile.getEmail();
//                Uri photoUrl = profile.getPhotoUrl();

                mDisplayName = name;
                Log.d(RegisterActivity.TAG, "Display name received from here. "+name);
            }
        }
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
        // TODO: Grab the text the user typed in and push the message to Firebase
        String message = mInputText.getText().toString();
        if (!message.equals("")) {
            InstantMessage msgObject = new InstantMessage(message, mDisplayName);
            mDatabaseReference.child(RegisterActivity.MESSAGES_TABLE).push().setValue(msgObject); // This is the line pushing message to firebase.
            mInputText.setText("");
//            Log.d(RegisterActivity.TAG, mDatabaseReference.getDatabase().toString());
//            Log.d(RegisterActivity.TAG, "Trace 2 "+message+", "+mDisplayName);
        }
    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.

    /**
     * This place calls the firebase to get and setup the chat as a list from adapter.
     */
    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mChatListView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.
        mAdapter.cleanUp();

    }

}
