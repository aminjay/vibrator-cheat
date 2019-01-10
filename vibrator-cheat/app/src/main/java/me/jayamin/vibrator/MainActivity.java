package me.jayamin.vibrator;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {
    public int position = 0;
    public int[] digits = new int[]{1,4,1,5,9,2,6,5,3,5,8,9,7,9,3,2,3,8,4,6,
            2,6,4,3,3,8,3,2,7,9,5,11,2,8,8,4,1,9,7,1,
            6,9,3,9,9,3,7,5,1,11,5,8,2,11,9,7,4,9,4,4,
            5,9,2,3,11,7,8,1,6,4,11,6,2,8,6,2,11,8,9,9,
            8,6,2,8,11,3,4,8,2,5,3,4,2,1,1,7,11,6,7,9};

    private TextView mTextMessage;
    Vibrator vibrator;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.pi);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final Button button = findViewById(R.id.nucbutt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrateFor250ms();
                    }
                }
                position=0;
            }
        });



    }

    //volume button handler
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(position>0){
                position -= 1;
                vibrateNum(digits[position]);
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if(position<99){
                vibrateNum(digits[position]);
                position+= 1;
            }
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //notification vibrate
    private void vibrateFor250ms() {
        vibrator.vibrate(250);
    }

    //vibrate number method
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vibrateNum(int num) {
        long[] mVibratePattern = new long[num*2 + 1];
        int[] amps = new int[num*2+1];
        mVibratePattern[0]=0;
        amps[0]=0;
        for (int i = 1 ; i < num*2 ; i++) {
            if (i % 2 == 1) {
                mVibratePattern[i] = 100;
                amps[i] = 75;
            } else {
                mVibratePattern[i] = 200;
                amps[i] = 0;
            }
        }
        if (vibrator.hasAmplitudeControl()) {
            VibrationEffect effect = VibrationEffect.createWaveform(mVibratePattern, amps, -1);
            vibrator.vibrate(effect);
        }
    }
}
