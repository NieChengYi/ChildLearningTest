package com.example.nie.childlearningtest;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String INDEX_KEY = "index_key";

    private static final String[] MP3_ARR = {"a01.mp3", "a02.mp3", "a03.mp3", "a04.mp3"};
    private static final int[] ANIMAL_ARR = {R.drawable.a001, R.drawable.a002, R.drawable.a003, R.drawable.a004};

    private int mIndex;

    private EditText mSoundET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                mIndex = bundle.getInt(INDEX_KEY);
            } else {
                Log.w(TAG, "bundle == null");
            }
        } else {
            Log.w(TAG, "intent == null");
        }

        if (mIndex >= MP3_ARR.length) {
            EJB.getInstance().setIndex(0);

            mIndex = EJB.getInstance().getIndex();
        }

        ImageView imageView = findViewById(R.id.imageView001);
        imageView.setImageResource(ANIMAL_ARR[mIndex]);
        imageView.setOnClickListener(this);

        mSoundET = findViewById(R.id.soundEditText);
        mSoundET.requestFocus();

        mSoundET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                MainActivity.this.action();

                return false;
            }
        });

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout_main);
        constraintLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MainActivity.this.action();

        mSoundET.requestFocus();
    }

    private void action() {
        try {
            AssetFileDescriptor descriptor = getAssets().openFd(MP3_ARR[mIndex]);

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());

            descriptor.close();

            mediaPlayer.prepare();

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int index = EJB.getInstance().getIndex();

                EJB.getInstance().setIndex(index + 1);

                Bundle bundle = new Bundle();

                bundle.putInt(INDEX_KEY, EJB.getInstance().getIndex());

                Intent intent = new Intent();

                intent.setClass(MainActivity.this, MainActivity.class);

                intent.putExtras(bundle);

                startActivity(intent);
                MainActivity.this.finish();
            }
        }, 1500);
    }
}
