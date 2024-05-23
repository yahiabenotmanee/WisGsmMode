package com.yahiee.gsmmode;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Import for starting new activities
import android.os.AsyncTask; // Import for performing background operations
import android.os.Bundle; // Import for saving and restoring activity state
import android.widget.Button; // Import for Button widget
import android.widget.EditText; // Import for EditText widget
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity; // Import for AppCompatActivity

public class MainActivity extends AppCompatActivity {
    private EditText editTextPhoneNumber; // EditText for entering phone number
    private Button buttonSave; // Button for saving phone number

    LinearLayout linearLayout_control;
    private PhoneNumberDatabase database; // Reference to the Room database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for this activity

        // Initialize the EditText and Button
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSave = findViewById(R.id.buttonSave);
        linearLayout_control=findViewById(R.id.layer_control);

        // Get an instance of the Room database
        database = PhoneNumberDatabase.getInstance(this);

        // Check if a phone number already exists in the database
        new CheckPhoneNumberTask().execute();

        // Set a click listener for the save button
        buttonSave.setOnClickListener(v -> {

            String phoneNumber = editTextPhoneNumber.getText().toString();
            if (!phoneNumber.isEmpty()) {
                // Save the phone number in the database
                run_anim(linearLayout_control);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        new SavePhoneNumberTask().execute(new PhoneNumber(phoneNumber));

                    }
                },2000);


            }
        });
    }

    // AsyncTask to check if a phone number exists in the database
    private class CheckPhoneNumberTask extends AsyncTask<Void, Void, PhoneNumber> {
        @Override
        protected PhoneNumber doInBackground(Void... voids) {
            // Query the database for an existing phone number
            return database.phoneNumberDao().getPhoneNumber();
        }

        @Override
        protected void onPostExecute(PhoneNumber phoneNumber) {
            if (phoneNumber != null) {
                // If a phone number exists, navigate to the next activity
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        }
    }

    // AsyncTask to save a phone number in the database
    private class SavePhoneNumberTask extends AsyncTask<PhoneNumber, Void, Void> {
        @Override
        protected Void doInBackground(PhoneNumber... phoneNumbers) {
            // Insert the phone number into the database
            database.phoneNumberDao().insert(phoneNumbers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // After saving, navigate to the next activity
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }

    }
    // for animation
    void run_anim(View view){view.animate().alpha(1).setDuration(600).translationY(600);}
    void run_anim2(View view){
        view.animate().alpha(1).setDuration(1000).translationY(0);
    }
}
