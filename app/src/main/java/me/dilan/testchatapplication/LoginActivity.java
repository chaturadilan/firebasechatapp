package me.dilan.testchatapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        firebase = new Firebase(getResources().getString(R.string.firebase_url));
    }


    public void onButtonRegisterClick(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Register");
        dialogBuilder.setMessage("Please register to continue");
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.login_layout, null));
        dialogBuilder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                EditText editTextUsername = (EditText) alertDialog.findViewById(R.id.editTextUsername);
                EditText editTextPassword = (EditText) alertDialog.findViewById(R.id.editTextPassword);
                firebase.createUser(editTextUsername.getText().toString(), editTextPassword.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "You have registered successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(LoginActivity.this, "Registration Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.show();
    }

    public void onButtonLoginClick(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Login");
        dialogBuilder.setMessage("Please login to continue");
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.login_layout, null));
        dialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                EditText editTextUsername = (EditText) alertDialog.findViewById(R.id.editTextUsername);
                EditText editTextPassword = (EditText) alertDialog.findViewById(R.id.editTextPassword);
                firebase.authWithPassword(editTextUsername.getText().toString(), editTextPassword.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user", authData.getProviderData().get("email").toString());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(LoginActivity.this, "Invalid Login!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.show();
    }

}
