package com.example.texttospeechdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button play,download;
    private EditText words;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        download = findViewById(R.id.download);
        words = findViewById(R.id.words);

        play.setOnClickListener(view -> {
            String text = words.getText().toString();
            int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            if (speechStatus == TextToSpeech.ERROR) {
                Log.e("TextToSpeech", "Error in converting Text to Speech!");
            }
        });

        download.setOnClickListener(v -> {
            String fileName = "/storage/emulated/0/Download/tts_output.wav"; // 文件路徑+檔案名

            //========================================================
            //建立相關配置選項
            HashMap<String, String> myHashRender = new HashMap<>();
            String utteranceID = "TextToSpeechDemo";
            myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceID);
            //========================================================

            int status = textToSpeech.synthesizeToFile(words.getText().toString(), myHashRender, fileName);
            Toast.makeText(getApplicationContext(),"下載成功",Toast.LENGTH_SHORT).show();

            if (status == TextToSpeech.ERROR) {
                Log.e("TextToSpeech", "Error in saving Text to Speech to file!");
            } else {
                Log.i("TextToSpeech", "Text to Speech saved successfully to " + fileName);
            }


        });



        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.TAIWAN); // 設定語言
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "Language not supported or missing data");
                }
            } else {
                Log.e("TextToSpeech", "Initialization failed");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}