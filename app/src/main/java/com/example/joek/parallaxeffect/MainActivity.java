package com.example.joek.parallaxeffect;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "Training";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new BitmapLoaderTask().execute("asset_two.png");
    }

    private void setImageBitmap(Bitmap bmp, int vel) {
        ImageView imageView = new ScrollableImageView(this, vel);
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

            setImageBitmap(result[0], 20);
        }

    }
}
