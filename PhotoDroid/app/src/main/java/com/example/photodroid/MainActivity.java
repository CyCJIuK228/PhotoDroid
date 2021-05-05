package com.example.photodroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.SeekBar;

import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;
import android.provider.MediaStore;

import java.io.*;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    Bitmap originalBitmap = null;
    String Title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Bitmap original = originalBitmap;
                //Bitmap original=((BitmapDrawable)imageView.getDrawable()).getBitmap();

                int width = original.getWidth();
                int height = original.getHeight();

                Matrix matrix = new Matrix();
                matrix.preRotate(progress*360/100);

                Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(rotatedBitmap);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Bitmap original=((BitmapDrawable)imageView.getDrawable()).getBitmap();

                int width = original.getWidth();
                int height = original.getHeight();

                Matrix matrix = new Matrix();
                //matrix.preRotate(10);
                matrix.postScale(-1, 1, width, height);
                Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
                //Canvas canvas = new Canvas(rotatedBitmap);
                //canvas.drawBitmap(original, 5.0f, 0.0f, null);

                imageView.setImageBitmap(rotatedBitmap);

                width = originalBitmap.getWidth();
                height = originalBitmap.getHeight();
                originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, true);



            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                //Bitmap original=((BitmapDrawable)imageView.getDrawable()).getBitmap();

                //MediaStore.Images.Media.insertImage(getContentResolver(), original, Title.concat("_droided"), "Created by PhotoDroid");

                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                FileOutputStream outputStream = null;
                File file = Environment.getExternalStorageDirectory();
                File dir = new File(file.getAbsolutePath() + "/PhotoDroided");
                dir.mkdirs();

                String filename = String.format("%d.png",System.currentTimeMillis());
                File outFile = new File(dir,filename);
                try{
                    outputStream = new FileOutputStream(outFile);
                }catch (Exception e){
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                try{
                    outputStream.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    outputStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Photo saved into external_storage/PhotoDroided", Toast.LENGTH_SHORT);
                toast.show();
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Title = selectedImage.getLastPathSegment().toString();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);

                    originalBitmap = bitmap;
                    //Uri selectedImage = imageReturnedIntent.getData();
                    //imageView.setImageURI(selectedImage);

                }
        }
    }




}