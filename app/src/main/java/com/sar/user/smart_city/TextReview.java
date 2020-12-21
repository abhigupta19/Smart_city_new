/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sar.user.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.tensorflow.lite.examples.textclassification.client.Result;
import org.tensorflow.lite.examples.textclassification.client.TextClassificationClient;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/** The main activity to provide interactions with users. */
public class TextReview extends AppCompatActivity {
    private static final String TAG = "TextClassificationDemo";

    private TextClassificationClient client;

    private static String resultTextView;
    private EditText inputEditText;
    private Handler handler;
    private static String kaka="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_review);
        Log.v(TAG, "onCreate");

        client = new TextClassificationClient(getApplicationContext());
        handler = new Handler();
        inputEditText = findViewById(R.id.input_text);
        Button classifyButton = findViewById(R.id.nextBtn);
        classifyButton.setOnClickListener(
                (View v) -> {


                    Intent intent = new Intent(this, ImageSelect.class);
                    String text=classify(inputEditText.getText().toString());
                    Log.d("kaka",text);
                    intent.putExtra("text", text);
                    startActivity(intent);
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    private String classify(final String text) {
        final String[] textToShow = new String[1];
        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);

                    // Show classification result on screen
                    kaka = showResult(text, results);


                });
        Log.d("popo", "pppo  - " + textToShow[0]);
        return textToShow[0];
    }

    /**
     * Show classification result on the screen.
     */
    private String showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI
        final String[] textToShow = new String[1];
        runOnUiThread(
                () -> {
                    textToShow[0] = "Input: " + inputText + "\nOutput:\n";
                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        textToShow[0] += String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                    textToShow[0] += "---------\n";


                    // Append the result to the UI.

                    // Clear the input text.

                    // Scroll to the bottom to show latest entry's classification result.
                });
        return textToShow[0];

    }
}