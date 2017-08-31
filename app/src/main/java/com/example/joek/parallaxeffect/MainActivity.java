package com.example.joek.parallaxeffect;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ScrollableImageView layerLvl1, layerLvl2, layerLvl3;
    private static final String DEBUG_TAG = "Training";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layerLvl1 = (ScrollableImageView) findViewById(R.id.layerLevelOne);
        layerLvl2 = (ScrollableImageView) findViewById(R.id.layerLevelTwo);
        layerLvl3 = (ScrollableImageView) findViewById(R.id.layerLevelThree);
        layerLvl3.setChildren(layerLvl2);
        layerLvl3.setChildren(layerLvl1);
//        new BitmapLoaderTask().execute("asset_one.png", "asset_two.png", "asset_three.png");
//        new BitmapLoaderTask().execute("asset_one", "asset_two", "asset_three");


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (layerLvl1 != null) {
            switch (event.getAction()){
                case MotionEvent.ACTION_MOVE:
//                    layerLvl1.dispatchGenericMotionEvent()
                    layerLvl1.dispatchTouchEvent(event);
                    layerLvl2.dispatchTouchEvent(event);
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    private void setImageBitmap(String bmp, ImageView iv) {
        Context context = iv.getContext();
        int id = context.getResources().getIdentifier(bmp, "drawable", context.getPackageName());
        iv.setImageResource(id);

//        iv.setImageBitmap(bmp);
//        bmp.getWidth()
        iv.setLayoutParams(new FrameLayout.LayoutParams(
                iv.getWidth(),
                iv.getHeight())
        );
    }

    private class BitmapLoaderTask extends AsyncTask<String, Void, String[]> {

        private ProgressBar progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = (ProgressBar) findViewById(android.R.id.progress);
        }

        @Override
        protected String[] doInBackground(String... params) {
//            AssetManager assets = getAssets();
//            Bitmap[] bmp = new Bitmap[params.length];
            String[] myStringList = new String[params.length];
            try {
                int i = 0;
                for (String item :
                        params) {

                    myStringList[i] = item;
                    i++;
                }
            } catch (Exception e) {
                Log.e(DEBUG_TAG, e.getMessage(), e);
            }
            return myStringList;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
//            progress.setVisibility(View.INVISIBLE);

            setImageBitmap(result[0], layerLvl1);
//            setImageBitmap(result[1], layerLvl2);
//            setImageBitmap(result[2], layerLvl3);
        }

    }
}
