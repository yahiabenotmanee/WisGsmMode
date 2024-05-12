package com.yahiee.gsmmode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout lyercontrol=findViewById(R.id.layer_control);
        run_anim(lyercontrol);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                run_anim2(lyercontrol);

            }
        },5000);

        //                                                     Oncreat end
    }

    // for animation
    void run_anim(View view){
        view.animate().alpha(1).setDuration(1000).translationY(430);
    }
    void run_anim2(View view){
        view.animate().alpha(1).setDuration(1600).translationY(0);
    }
}