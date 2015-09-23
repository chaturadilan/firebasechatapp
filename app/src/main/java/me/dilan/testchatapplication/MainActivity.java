package me.dilan.testchatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Firebase firebase;

    EditText editTextMessage;
    ListView listViewMessages;

    FirebaseListAdapter<ChatMessage> listAdapterMessages;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(getResources().getString(R.string.firebase_url));

        editTextMessage = (EditText) this.findViewById(R.id.editTextMessage);
        listViewMessages = (ListView) this.findViewById(R.id.listViewMessages);

        listAdapterMessages =  new FirebaseListAdapter<ChatMessage>(firebase, ChatMessage.class, R.layout.message_layout , this){

            @Override
            protected void populateView(View v, ChatMessage model) {
                ((TextView) v.findViewById(R.id.textViewMessageUsername)).setText(model.getUser());
                ((TextView) v.findViewById(R.id.textViewMessageMessage)).setText(model.getMessage());


            }
        };

        listViewMessages.setAdapter(listAdapterMessages);

        user = getIntent().getExtras().getString("user");
    }

    public void onButtonSendClick(View view){
        String message = editTextMessage.getText().toString();
        HashMap<String, String> values = new HashMap<>();
        values.put("user", user);
        values.put("message", message);
        firebase.push().setValue(values);

        editTextMessage.setText("");
    }

}
