package com.yahiee.gsmmode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {
    private EditText editTextNewPhoneNumber;
    private Button buttonUpdate;

    public GridLayout gridLayout;
    private PhoneNumberDatabase database;

    private static final int SMS_PERMISSION_CODE = 1;
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private SMSReceiver smsReceiver;

    String isON1,isOFF1,isON2,isOFF2;
    CardView cardViewPOMP1,cardViewPOMP2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        if (checkSmsPermission()) {
            setupSmsReceiver();
        } else {
            requestSmsPermission();
        }



        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout update = findViewById(R.id.layer_edit);

        Button button_mod11 = findViewById(R.id.btn_mode11);
        Button button_mod12 = findViewById(R.id.btn_mode12);
        Button button_mod21 = findViewById(R.id.btn_mode21);
        Button button_mod22 = findViewById(R.id.btn_mode22);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView_update = findViewById(R.id.image_update);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView_help = findViewById(R.id.image_help);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView_relod = findViewById(R.id.image_reload);
        ImageView gif = findViewById(R.id.GIF);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView1 = findViewById(R.id.text1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView2 = findViewById(R.id.text2);

        gridLayout =findViewById(R.id.layer_grid);

        cardViewPOMP1=findViewById(R.id.Card_frist);
        cardViewPOMP2=findViewById(R.id.Card_second);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout layerCard1 = findViewById(R.id.layercard1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout layerCard2 = findViewById(R.id.layercard2);


        //                                       home Status

        showPopup();
        cardViewPOMP1.setVisibility(View.INVISIBLE);
        cardViewPOMP2.setVisibility(View.INVISIBLE);

        //run_anim(gridLayout);

        //              RLOAD IMAGE
        imageView_relod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage("n");
            }
        });

            //          Action Button

        button_mod11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("n");
            }
        });

        button_mod12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("m");
            }
        });

        button_mod21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("n");
            }
        });

        button_mod22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("m");
            }
        });

        //               end buttons



        //               UPDATE NUMBER IMAGE
        imageView_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  run_anim3(update);
            }
        });

        // Initialize the EditText and Button
        editTextNewPhoneNumber = findViewById(R.id.editTextPhoneNumberupdate);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Get an instance of the Room database
        database = PhoneNumberDatabase.getInstance(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> {}, 1000);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String newPhoneNumber = editTextNewPhoneNumber.getText().toString();
                if (!newPhoneNumber.isEmpty()) {
                    // Update the phone number in the database
                    new UpdatePhoneNumberTask().execute(new PhoneNumber(newPhoneNumber));
                } else {
                    editTextNewPhoneNumber.setError("Can't be empty");
                }

                run_anim(update);

            }
        });



    } /////////////                                  end oncreat



    // Animation methods
    void run_anim(View view) {
        view.animate().setDuration(600).translationY(0);
    }
    void run_anim3(View view) {
        view.animate().setDuration(600).translationY(-600);
    }


    // PERMISSION
    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_REQUEST_CODE);
    }

    // recieve FUNCTOION
    private void setupSmsReceiver() {
        smsReceiver = new SMSReceiver();
        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, intentFilter);

        // Register a content observer to listen for changes to the SMS inbox
        getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true
                , new SMSContentObserver(new Handler()));
    }

    // PERMISSION
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupSmsReceiver();
            } else {
                Toast.makeText(this, "Permission SMS refusée. Impossible de lire les messages.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    // SMS FUNCTION
    private class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                try {
                    displayLastReceivedMessage();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class SMSContentObserver extends ContentObserver {
        public SMSContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try {
                displayLastReceivedMessage();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


     //                                  DISPLAY MESSAGE

    private void displayLastReceivedMessage() throws Exception {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null && cursor.moveToFirst()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow("body"));

            //Toast.makeText(this, "message = "+message, Toast.LENGTH_SHORT).show();

                                                  // to confirme
            if (message.equals("n")){

                run_anim(gridLayout);
                cardViewPOMP1.setVisibility(View.VISIBLE);
                cardViewPOMP2.setVisibility(View.INVISIBLE);

                //gridLayout.setBackgroundResource(R.drawable.bachground_button_red);

            }

            if (message.equals("System ON")){
                showPopup();
            }

            if (message.equals("System activated (off)")){

                cardViewPOMP1.setVisibility(View.INVISIBLE);
                cardViewPOMP2.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Your system not acivated", Toast.LENGTH_SHORT).show();
            }

            if (message.equals("System activated (on)")){

                Toast.makeText(this,"Your system activated", Toast.LENGTH_SHORT).show();
            }

            if (message.equals("Pompe 1 OFF")){
               // cardViewPOMP1.setVisibility(View.INVISIBLE);
            }

            if (message.equals("Pompe 1 ON")){
                cardViewPOMP1.setVisibility(View.VISIBLE);
            }

            if (message.equals("Pompe 2 OFF")){

            }

            if (message.equals("Pompe 2 ON")){
                cardViewPOMP2.setVisibility(View.VISIBLE);

            }

            if (message.equals("Pompe 1 ON 45 minute")){
                cardViewPOMP1.setVisibility(View.VISIBLE);
            }

            if (message.equals("Pompe 2 ON 45 minute")){
                cardViewPOMP2.setVisibility(View.VISIBLE);
            }

            if (message.equals("Pompe 1 deja ON")){
                cardViewPOMP1.setVisibility(View.VISIBLE);
            }

            if (message.equals("Pompe 2 deja ON")){
                cardViewPOMP2.setVisibility(View.VISIBLE);
            }

            cursor.close();
        } else {

            //messageTextView.setText("Aucun message trouvé dans la boîte de réception.");
        }
    }


    // Method to send SMS
    private void sendMessage(String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            sendSMS(message);
        }
    }

    // Handle the result of the permission request

    // Method to send SMS
    private void sendSMS(String message) {
        new GetPhoneNumberTask(message).execute();
    }

    // AsyncTask to get the saved phone number from the database
    private class GetPhoneNumberTask extends AsyncTask<Void, Void, PhoneNumber> {
        private final String message;

        GetPhoneNumberTask(String message) {
            this.message = message;
        }

        @Override
        protected PhoneNumber doInBackground(Void... voids) {
            return database.phoneNumberDao().getPhoneNumber();
        }

        @Override
        protected void onPostExecute(PhoneNumber phoneNumber) {
            if (phoneNumber != null) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber.getPhoneNumber(), null, message, null, null);
                //Toast.makeText(DashboardActivity.this, "SMS sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DashboardActivity.this, "Phone number not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // AsyncTask to update the phone number in the database
    private class UpdatePhoneNumberTask extends AsyncTask<PhoneNumber, Void, Void> {
        @Override
        protected Void doInBackground(PhoneNumber... phoneNumbers) {
            // Clear the existing phone number(s)
            database.phoneNumberDao().clearPhoneNumbers();
            // Insert the new phone number
            database.phoneNumberDao().insert(phoneNumbers[0]);
            return null;
        }
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
