package com.example.aaronbrecher.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by aaronbrecher on 1/22/18.
 * Loader to download the coverImage from the internet
 */

public class ImageLoader extends AsyncTaskLoader <Bitmap> {
    private String mUrl;

    public ImageLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public void onStartLoading(){
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Bitmap loadInBackground() {
        Bitmap image = null;
        InputStream inputStream = null;
        //create the url to get the image
        URL url = Utils.createUrl(mUrl);

        try{
            //open the stream and decode the bitmap image
            inputStream = url.openStream();
            image = BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            Log.e(TAG, "loadInBackground: not succesfull error opening stream ",e);
        }
        return image;
    }
}
