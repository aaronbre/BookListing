package com.example.aaronbrecher.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private String mSearchParam;
    private BookArrayAdapter mBookArrayAdapter;
    private ListView mBookListView;

    private final String API_URL = "https://www.googleapis.com/books/v1/volumes?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //get the search parameter from the intent
        mSearchParam = getIntent().getStringExtra("searchParam");
        // refrence to the listView to show the books
        mBookListView = (ListView)findViewById(R.id.results_list);

        //check that we are connected to the internet to get info from api
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            //create a new bookAdapter to attach to the list view
            mBookArrayAdapter = new BookArrayAdapter(this, new ArrayList<Book>(), R.layout.book_list_item);
            //init the loader to perform the api query in the background
            getLoaderManager().initLoader(0,null, this);
            //attach the array adapter to the listView
            mBookListView.setAdapter(mBookArrayAdapter);

            mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                    Book currentBook = mBookArrayAdapter.getItem(postion);
                    Intent intent = new Intent(view.getContext(), BookDetailsActivity.class);
                    intent.putExtra("book", currentBook);
                    startActivity(intent);
                }
            });
        }

    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String maxBooks = sharedPrefs.getString(getString(R.string.settings_book_limit_key), getString(R.string.settings_book_limit_default));
        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", mSearchParam);
        uriBuilder.appendQueryParameter("maxResults", maxBooks);
        Log.d("uri", "onCreateLoader: " + uriBuilder.toString());
        return new BooksLoader(ResultsActivity.this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mBookArrayAdapter.clear();
        if(data != null && !data.isEmpty()){
            mBookArrayAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookArrayAdapter.clear();
        mBookArrayAdapter.addAll(new ArrayList<Book>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
