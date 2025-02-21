package com.example.eyedetectionmusicplayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pd.lookatme.LookAtMe;

public class MainActivity extends AppCompatActivity {
    private LookAtMe lookAtMe;
    private TextView eyeStatus;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lookAtMe = findViewById(R.id.lookme);
        eyeStatus = findViewById(R.id.eye_status); // TextView to show status

        if (lookAtMe != null) {
            lookAtMe.init(this);

            int resId = getResources().getIdentifier("animation", "raw", getPackageName());
            if (resId != 0) {
                lookAtMe.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + resId));
                lookAtMe.start();
                lookAtMe.setLookMe();
            }

            // ✅ Periodically check if the video is playing
            handler.postDelayed(checkEyeStatus, 1000);
        }
    }

    // Runnable to check eye status every second
    private Runnable checkEyeStatus = new Runnable() {
        @Override
        public void run() {
            if (lookAtMe.isPlaying()) {
                eyeStatus.setText("Eye Detected and Open 👀, so video continues");
            } else {
                eyeStatus.setText("Eye Detected and Closed 😴, so video Paused");
            }
            handler.postDelayed(this, 1000); // Check every second
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkEyeStatus); // Stop checking when activity is destroyed
    }
}
