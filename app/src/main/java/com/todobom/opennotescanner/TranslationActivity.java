package com.todobom.opennotescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.todobom.opennotescanner.helpers.GoogleTranslate;

import java.io.File;

public class TranslationActivity extends AppCompatActivity {

    TextView translateedittext;
    TextView translatabletext;

    ImageView ivTest;
    EditText tvTest;
    String filePath;

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

        ivTest = findViewById(R.id.imageViewTest);
        tvTest = findViewById(R.id.textViewTest);

        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");

        getTextFromImageView(filePath, ivTest, tvTest);

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

    public void getTextFromImageView(String filePath, ImageView imageView, EditText editText) {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(filePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        imageView.setImageBitmap(bitmap);

        //bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational())
        {
            Toast.makeText(getApplicationContext(), "Could not get the text", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i< items.size();++i)
            {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }
            editText.setText(sb.toString());
        }
    }

    public void translated(){

//        String srcText = translateedittext.getText().toString();//get the value of text
//        String text = translator.translate(srcText, "en", "ko");
//        translatabletext = (TextView) findViewById(R.id.translatabletext);
//        translatabletext.setText(text);

        String srcText = tvTest.getText().toString();//get the value of text
        String text = translator.translate(srcText, "en", "ko");
//        translatabletext = (TextView) findViewById(R.id.translatabletext);
        tvTest.setText(text);

    }

}

