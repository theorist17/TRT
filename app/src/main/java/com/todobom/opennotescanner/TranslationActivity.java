package com.todobom.opennotescanner;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.todobom.opennotescanner.helpers.GoogleTranslate;

public class TranslationActivity extends AppCompatActivity {

    TextView translateedittext;
    TextView translatabletext;

    private static final String API_KEY = "AIzaSyBXmV0HSlh49fQ0aCDXjv7uzeTYxuDyo10";

    GoogleTranslate translator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        translateedittext = (EditText) findViewById(R.id.translateedittext);
        Button translatebutton = (Button) findViewById(R.id.translatebutton);
        translatebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Translatist().execute();

            }
        });
    }



    private class Translatist extends AsyncTask<String, Void, Void> {
        private ProgressDialog progress = null;

        protected void onError(Exception ex) {

        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                translator = new GoogleTranslate(API_KEY);

                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
                return null;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            //start the progress dialog
            progress = ProgressDialog.show(TranslationActivity.this, null, "Translating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            super.onPostExecute(result);
            translated();
        }
    }


    public void translated(){

        String srcText = translateedittext.getText().toString();//get the value of text
        String text = translator.translate(srcText, "en", "ko");
        translatabletext = (TextView) findViewById(R.id.translatabletext);
        translatabletext.setText(text);

    }

}

