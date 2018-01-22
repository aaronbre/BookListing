package com.example.aaronbrecher.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private String mUrl = null;
    private BookArrayAdapter mBookArrayAdapter;
    private ListView mBookListView;

    private final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //get the searchParams to add to the api url
        if (mUrl == null){
            mUrl = API_URL + getIntent().getStringExtra("searchParam")+"&maxResults=20";
        }
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
        if(i == 0 && mUrl != null){
            return new BooksLoader(ResultsActivity.this, mUrl);
        }else{
            return null;
        }
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
}
