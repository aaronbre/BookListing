package com.example.aaronbrecher.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronbrecher on 1/21/18.
 * Loader Class to load a list of books from the Google Books API uses the Utils class with the networking functions
 */

public class BooksLoader extends AsyncTaskLoader <List<Book>>{
    private ArrayList<Book> mBooks;
    private String mUrl;
    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        //make sure a valid url was passed in to query
        if(mUrl == null || mUrl == ""){
            return null;
        }
        return Utils.fetchBooks(mUrl, getContext());
    }
}
