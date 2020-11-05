package com.example.bodyrehab.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.PlaylistWithVideos;
import com.example.bodyrehab.UserDB.Timer;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.Video;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Locale;


public class YTPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView ytPlayer;
    private YouTubePlayerTracker tracker;
    private TextView videoTitle;
    private static final String TAG = "Player";
    private float time, loadTime = 0;
    private long ACTIVE_TIME_MILLIS = 0;
    private long REST_TIME_MILLIS = 0;
    private TextView mTextViewCountDown, mTextViewAddTimer, mTextViewReps;

    private Button mBtnStartPause;
    private Button mBtnReset;
    private Button mBtnResetAll;
    private Button mBtnTimerSet;
    private FloatingActionButton mBtnAddTimer;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private boolean IS_ACTIVE;
    private long mTimeLeftMillis;
    private long minutes, seconds;
    private Video video;
    private int cont = 0;
    private int REPS = 0;

    private UserDataBase userDataBase;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private SoundPool soundPool;
    private int active_sound, rest_sound, end_sound;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytplayer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userDataBase = UserDataBase.getINSTANCE(this);

        Intent data = getIntent();
        final String videoId = data.getStringExtra("video_id");
        final String video_title = data.getStringExtra("video_title");

        if(savedInstanceState != null){
            loadTime = savedInstanceState.getFloat("time");
            Log.d(TAG, String.valueOf(loadTime));
        }


        ytPlayer = findViewById(R.id.yt_player);
        videoTitle = findViewById(R.id.vid_title);

        mTextViewCountDown = findViewById(R.id.countdown);
        mBtnStartPause = findViewById(R.id.btn_start);
        mBtnReset = findViewById(R.id.btn_reset);
        mBtnResetAll = findViewById(R.id.btn_reset_all);
        mTextViewAddTimer = findViewById(R.id.textViewTimer);
        mBtnAddTimer = findViewById(R.id.btn_add_timer);
        mTextViewReps = findViewById(R.id.textViewReps);
        mBtnTimerSet = findViewById(R.id.timer_set);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();
        active_sound = soundPool.load(this, R.raw.active, 1);
        rest_sound = soundPool.load(this, R.raw.rest, 1);
        end_sound = soundPool.load(this, R.raw.end, 1);

        if(!(data.hasExtra("video_info"))){
            mTextViewCountDown.setVisibility(View.GONE);
            mBtnStartPause.setVisibility(View.GONE);
            mBtnReset.setVisibility(View.GONE);
            mBtnResetAll.setVisibility(View.GONE);
            mTextViewAddTimer.setVisibility(View.GONE);
            mBtnAddTimer.setVisibility(View.GONE);
            mTextViewReps.setVisibility(View.GONE);
        }
        else
        {
            long vid_id = data.getLongExtra("video_info", 0);
            video = userDataBase.getUserDao().SearchVideo(vid_id);
            Timer timer = video.getTimer();
            if(!timer.isFlag()){
                mTextViewCountDown.setVisibility(View.GONE);
                mBtnStartPause.setVisibility(View.GONE);
                mBtnReset.setVisibility(View.GONE);
                mBtnResetAll.setVisibility(View.GONE);
                mTextViewReps.setVisibility(View.GONE);
            }
            else{
                mTextViewAddTimer.setVisibility(View.GONE);
                mBtnAddTimer.setVisibility(View.GONE);
                mBtnReset.setVisibility(View.INVISIBLE);
                mBtnResetAll.setVisibility(View.INVISIBLE);
                mBtnTimerSet.setVisibility(View.VISIBLE);
                ACTIVE_TIME_MILLIS = timer.getActiveTimer();
                REST_TIME_MILLIS = timer.getRestTimer();
                REPS = timer.getRepetitions();
                String reps = "Reps: " + (cont/2) + "/" + REPS;
                mTextViewReps.setText(reps);

                if (savedInstanceState == null){
                    IS_ACTIVE = true;
                    mTimeLeftMillis = ACTIVE_TIME_MILLIS;
                }
                else {
                    IS_ACTIVE = savedInstanceState.getBoolean("active");
                    boolean IS_RUNNING = savedInstanceState.getBoolean("running");
                    cont = savedInstanceState.getInt("counter");
                    mTimeLeftMillis = savedInstanceState.getLong("millis");
                    if (IS_RUNNING){
                        //Toast.makeText(getApplication(), "Running", Toast.LENGTH_SHORT).show();
                        reps = "Reps: " + (cont/2) + "/" + REPS;
                        mTextViewReps.setText(reps);
                        if (IS_ACTIVE){
                            mTextViewCountDown.setTextColor(getColor(R.color.holo_blue_light));
                        }
                        else{
                            mTextViewCountDown.setTextColor(getColor(R.color.holo_green_light));
                        }
                        startTimer();
                    }
                    else {
                        if (IS_ACTIVE){
                            mTextViewCountDown.setTextColor(getColor(R.color.holo_blue_light));
                        }
                        else{
                            mTextViewCountDown.setTextColor(getColor(R.color.holo_green_light));
                        }
                        mBtnStartPause.setText("Start");
                        mBtnReset.setVisibility(View.VISIBLE);
                        mBtnResetAll.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        tracker = new YouTubePlayerTracker();
        ytPlayer.addYouTubePlayerListener(tracker);

        ytPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, loadTime);
            }
        });

        videoTitle.setText(video_title);

        mBtnTimerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewVideoTimer();
            }
        });

        mBtnAddTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewVideoTimer();
            }
        });

        mBtnStartPause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    pauseTimer();
                }
                else {
                    if (IS_ACTIVE){
                        mTextViewCountDown.setTextColor(getColor(R.color.holo_blue_light));
                        soundPool.play(active_sound, 1, 1, 0, 0, 1);
                    }
                    else{
                        mTextViewCountDown.setTextColor(getColor(R.color.holo_green_light));
                        soundPool.play(rest_sound, 1, 1, 0, 0, 1);
                    }
                    startTimer();
                }
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mBtnResetAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                resetAllTimers();
            }
        });

        updateCountDowbText();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resetAllTimers() {
        mTextViewCountDown.setTextColor(getColor(R.color.holo_blue_light));
        mTimeLeftMillis = ACTIVE_TIME_MILLIS;
        IS_ACTIVE = true;
        updateCountDowbText();
        mBtnReset.setVisibility(View.INVISIBLE);
        mBtnStartPause.setVisibility(View.VISIBLE);
        cont = 0;
        mBtnReset.setVisibility(View.INVISIBLE);
        mBtnResetAll.setVisibility(View.INVISIBLE);
        mBtnStartPause.setVisibility(View.VISIBLE);
        String reps = "Reps: " + 0 + "/" + REPS;
        mTextViewReps.setText(reps);
    }

    private void resetTimer() {
        if (IS_ACTIVE){
            mTimeLeftMillis = ACTIVE_TIME_MILLIS;
        }
        else{
            mTimeLeftMillis = REST_TIME_MILLIS;
        }
        updateCountDowbText();
        mBtnReset.setVisibility(View.INVISIBLE);
        mBtnResetAll.setVisibility(View.INVISIBLE);
        mBtnStartPause.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeTimer(){
        String reps = "Reps: " + (cont/2) + "/" + REPS;
        mTextViewReps.setText(reps);
        if (IS_ACTIVE){
            mTimeLeftMillis = ACTIVE_TIME_MILLIS;
            mTextViewCountDown.setTextColor(getColor(R.color.holo_blue_light));
            soundPool.play(active_sound, 1, 1, 0, 0, 1);
        }
        else{
            mTimeLeftMillis = REST_TIME_MILLIS;
            mTextViewCountDown.setTextColor(getColor(R.color.holo_green_light));
            soundPool.play(rest_sound, 1, 1, 0, 0, 1);
        }
        startTimer();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        mTimerRunning = false;
        mBtnStartPause.setText("Start");
        mBtnReset.setVisibility(View.VISIBLE);
        mBtnResetAll.setVisibility(View.VISIBLE);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updateCountDowbText();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFinish() {
                cont += 1;
                if (cont >= REPS*2){
                    mTimerRunning = false;
                    mBtnStartPause.setText("Start");
                    mBtnStartPause.setVisibility(View.INVISIBLE);
                    mBtnReset.setVisibility(View.VISIBLE);
                    mBtnResetAll.setVisibility(View.VISIBLE);
                    String reps = "Reps: " + (cont/2) + "/" + REPS;
                    mTextViewReps.setText(reps);
                    soundPool.play(end_sound, 1, 1, 0, 0, 1);
                    mTextViewCountDown.setTextColor(getColor(R.color.holo_red_light));
                }
                else{
                   IS_ACTIVE = !IS_ACTIVE;
                   changeTimer();
                }

            }
        }.start();

        mTimerRunning = true;
        mBtnStartPause.setText("Pause");
        mBtnReset.setVisibility(View.INVISIBLE);
        mBtnResetAll.setVisibility(View.INVISIBLE);
    }

    private void updateCountDowbText() {
        int minutes = (int) (mTimeLeftMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        time = tracker.getCurrentSecond();
        outState.putFloat("time", time);
        outState.putLong("millis", mTimeLeftMillis);
        outState.putBoolean("active", IS_ACTIVE);
        outState.putBoolean("running", mTimerRunning);
        outState.putInt("counter", cont);
    }

    public void createNewVideoTimer(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = LayoutInflater.from(this).inflate(R.layout.popup_add_timer, null);

        final NumberPicker numberPickerMinutes = (NumberPicker) contactPopupView.findViewById(R.id.numpicker_minutes);
        final NumberPicker numberPickerSeconds = (NumberPicker) contactPopupView.findViewById(R.id.numpicker_seconds);
        Button btn_done = (Button) contactPopupView.findViewById(R.id.btn_done);
        Button btn_set_active = (Button) contactPopupView.findViewById(R.id.btn_set_active);
        Button btn_set_rest = (Button) contactPopupView.findViewById(R.id.btn_set_rest);

        numberPickerMinutes.setMaxValue(60);
        numberPickerMinutes.setMinValue(0);
        numberPickerSeconds.setMaxValue(60);
        numberPickerSeconds.setMinValue(0);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText repetitions = (EditText) contactPopupView.findViewById(R.id.rep_num);
                if(repetitions.getText().toString().equals("")){
                    Toast.makeText(getApplication(), "Insert Number of Reps", Toast.LENGTH_LONG).show();
                }
                else{
                    Timer timer = video.getTimer();
                    int reps = Integer.parseInt(repetitions.getText().toString());
                    timer.setRepetitions(reps);
                    userDataBase.getUserDao().UpdateVideoTitle(video);
                    dialog.dismiss();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        btn_set_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes = numberPickerMinutes.getValue();
                seconds = numberPickerSeconds.getValue();
                Timer timer = video.getTimer();
                long totalTimeMillis = minutes*60000 + seconds*1000;
                timer.setActiveTimer(totalTimeMillis);
                timer.setFlag(true);
                userDataBase.getUserDao().UpdateVideoTitle(video);
                Toast.makeText(getApplication(), "Active Timer Set: ", Toast.LENGTH_SHORT).show();
            }
        });

        btn_set_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes = numberPickerMinutes.getValue();
                seconds = numberPickerSeconds.getValue();
                Timer timer = video.getTimer();
                long totalTimeMillis = minutes*60000 + seconds*1000;
                timer.setRestTimer(totalTimeMillis);
                timer.setFlag(true);
                userDataBase.getUserDao().UpdateVideoTitle(video);
                Toast.makeText(getApplication(), "Rest Timer Set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       soundPool.release();
       // soundPool = null;
    }
}
