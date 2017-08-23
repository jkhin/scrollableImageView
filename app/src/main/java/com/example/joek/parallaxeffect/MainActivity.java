package com.example.joek.parallaxeffect;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ScrollableImageView layerLvl1, layerLvl2;
    private static final String DEBUG_TAG = "Training";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layerLvl1 = (ScrollableImageView) findViewById(R.id.layerLevelOne);
        layerLvl2 = (ScrollableImageView) findViewById(R.id.layerLevelTwo);
        layerLvl2.setChildren(layerLvl1);
        new BitmapLoaderTask().execute("asset_one.jpg", "asset_two.png");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (layerLvl1 != null && layerLvl2 != null) {
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

    private void setImageBitmap(Bitmap bmp, ImageView iv) {
        iv.setImageBitmap(bmp);
        iv.setLayoutParams(new FrameLayout.LayoutParams(bmp.getWidth(), bmp.getHeight()));
    }

    private class BitmapLoaderTask extends AsyncTask<String, Void, Bitmap[]> {

        private ProgressBar progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = (ProgressBar) findViewById(android.R.id.progress);
        }

        @Override
        protected Bitmap[] doInBackground(String... params) {
            AssetManager assets = getAssets();
            Bitmap[] bmp = new Bitmap[2];

            try {
                int i = 0;
                for (String item :
                        params) {
                    bmp[i] = BitmapFactory.decodeStream(assets.open(item));
                    i++;
                }

            } catch (IOException e) {
                Log.e(DEBUG_TAG, e.getMessage(), e);
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap[] result) {
            super.onPostExecute(result);
            progress.setVisibility(View.INVISIBLE);

            setImageBitmap(result[0], layerLvl1);
            setImageBitmap(result[1], layerLvl2);
        }

    }
}
