package com.example.animacionsprite;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.icu.util.ICUUncheckedIOException;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class MainActivity extends AppCompatActivity implements AIListener, View.OnClickListener {
    private static final int REQUEST_INTERNET = 100;
    private Button btnVoice;
    private TextView tvResult;
    private AIService aiService;
    private TextToSpeech mTextToSpeech;

    boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button start= (Button)findViewById(R.id.button_voice);
         ImageView sophia = (ImageView)findViewById(R.id.imageView);
        sophia.setImageResource(R.drawable.runing);
        final AnimationDrawable walsophia = (AnimationDrawable)sophia.getDrawable();

        running=false;

        btnVoice=(Button)findViewById(R.id.button_voice);
        tvResult = (TextView) findViewById(R.id.tv_result_main);

        validateOS();

        final AIConfiguration config = new ai.api.android.AIConfiguration("b988cbe7cb1c47e3bcf9468efda5ac0f",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);
        aiService=AIService.getService(this,config);
        aiService.setListener(this);

        btnVoice.setOnClickListener(this);


        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        findViewById(R.id.button_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!running) {
                    aiService.startListening();
                    walsophia.start();
                    running=true;
                }else {
                    walsophia.stop();
                    running=true;
                }

            }
        });

    }
    private void validateOS() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_INTERNET);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onResult(AIResponse response) {

        Result result= response.getResult();
        mTextToSpeech.speak(result.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
        //Get parametros
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue();


            }
        }
        // show result in textview
        tvResult.setText("Query: " + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString);
    }

    @Override
    public void onError(AIError error) {
        tvResult.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_voice:
                aiService.startListening();
                break;
        }
    }

}
