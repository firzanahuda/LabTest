package com.example.labtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Executor executorDonut;
    Handler handlerDonut;
    Executor executorBom;
    Handler handlerBom;
    Bitmap bitmapDonut = null;
    Bitmap bitmapBomboloni = null;
    ImageView imageDonut;
    ImageView imageBomboloni;
    EditText donutID, bomboloniId;
    TextView totalItem, totalPrice;

    Button buttonClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
        getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;

        executorDonut= Executors.newSingleThreadExecutor();
        handlerDonut = new Handler(Looper.getMainLooper());
        executorBom= Executors.newSingleThreadExecutor();
        handlerBom = new Handler(Looper.getMainLooper());
        buttonClick = findViewById(R.id.buttonClick);
        imageDonut = findViewById(R.id.imgDonut);
        imageBomboloni = findViewById(R.id.imgBomboloni);
        totalItem = findViewById(R.id.totalItem);
        totalPrice = findViewById(R.id.totalPrice);
        donutID = findViewById(R.id.donutId);
        bomboloniId = findViewById(R.id.bomboloniId);

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null  && networkInfo.isConnected())
        {
            // the background task executor and handler is done within this checking
            //….
            //….
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Network!! Please add data plan or connect to wifi network!", Toast.LENGTH_SHORT).show();
        }

        executorDonut.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL ImageURL = new URL("https://abmauri.com.my/wp-content/uploads/2021/06/ab_mauri_donut_recipe.jpg");
                    HttpURLConnection connection = (HttpURLConnection) ImageURL.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmapDonut = BitmapFactory.decodeStream(inputStream,null,options);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handlerDonut.post(new Runnable() {  // this is to update main thread -- UI
                    @Override
                    public void run() {
                        imageDonut.setImageBitmap(bitmapDonut);
                    }
                });
            }
        });

        executorBom.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL ImageURL = new URL("https://bazaronlinesgbuloh.my/wp-content/uploads/2021/03/bomboloni-red-velvet-rm1.00-scaled.jpg");
                    HttpURLConnection connection = (HttpURLConnection) ImageURL.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmapBomboloni = BitmapFactory.decodeStream(inputStream,null,options);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handlerBom.post(new Runnable() {  // this is to update main thread -- UI
                    @Override
                    public void run() {
                        imageBomboloni.setImageBitmap(bitmapBomboloni);
                    }
                });
            }
        });

        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalDonut = donutID.getText().toString();
                String totalbom = bomboloniId.getText().toString();

                int total = Integer.parseInt(totalDonut) + Integer.parseInt(totalbom);
                String item = Integer.toString(total);
                totalItem.setText(item);

                float totalprice = (float) ((Float.parseFloat(totalDonut)*3.5) + (Float.parseFloat(totalbom)*2.5));
                String price = Float.toString(totalprice);

                totalPrice.setText("RM" + price);

            }
        });

    }
}