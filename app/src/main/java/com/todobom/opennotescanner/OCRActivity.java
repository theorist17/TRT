package com.todobom.opennotescanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class OCRActivity extends Activity {

    ImageView ivTest;
    EditText tvTest;
    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        ivTest = findViewById(R.id.imageViewTest);
        tvTest = findViewById(R.id.textViewTest);

        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");

        getTextFromImageView();
    }

    public void getTextFromImageView() {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(filePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        ivTest.setImageBitmap(bitmap);

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
            tvTest.setText(sb.toString());
        }

    }
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }
}
