package com.example.joek.parallaxeffect;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView layerLvl1, layerLvl2;
    private static final String DEBUG_TAG = "Training";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layerLvl1 = new ScrollableImageView(this, 60);
        layerLvl1.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, layerLvl1.getHeight()));

        layerLvl2 = new ScrollableImageView(this, 20);
        layerLvl2.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, layerLvl2.getHeight()));

//        new BitmapLoaderTask().execute("asset_one.jpg", "asset_two.png");
        new BitmapLoaderTask().execute("asset_two.png");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (layerLvl1 != null) {
            switch (event.getAction()){
                case MotionEvent.ACTION_MOVE:
//                    layerLvl1.dispatchTouchEvent(event);
//                    layerLvl2.dispatchTouchEvent(event);
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    private void setImageBitmap(Bitmap bmp) {
        ImageView imageView = new ScrollableImageView(this, 10);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(bmp.getWidth(), bmp.getHeight()));
        imageView.setImageBitmap(bmp);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.addView(imageView);
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
            Bitmap[] bmp = new Bitmap[1];

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

            setImageBitmap(result[0]);
//            setImageBitmap(result[1], layerLvl2);
        }

    }
}
