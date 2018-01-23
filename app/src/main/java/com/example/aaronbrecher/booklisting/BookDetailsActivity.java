package com.example.aaronbrecher.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private ImageView mCoverImage;
    private TextView mBookTitle;
    private TextView mBookAuthor;
    private TextView mBookDescription;
    private TextView mBookPrice;
    private Button mBookBuy;
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        mCoverImage = (ImageView)findViewById(R.id.details_image);
        mBookTitle = (TextView)findViewById(R.id.details_main_heading);
        mBookAuthor = (TextView)findViewById(R.id.details_author);
        mBookDescription = (TextView)findViewById(R.id.details_description);
        mBookPrice = (TextView)findViewById(R.id.details_price);
        mBookBuy = (Button)findViewById(R.id.details_buy);

        mBook = (Book)getIntent().getSerializableExtra("book");
        if (mBook != null){
            mBookTitle.setText(mBook.getTitle());
            mBookDescription.setText(mBook.getDescription());
            mBookAuthor.setText(mBook.getAuthor());
            mBookPrice.setText(mBook.getPrice() == null ? "No Price Available" : mBook.getPrice().toString());

            ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                getLoaderManager().initLoader(1, null, this);
            }

        }
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        if(i == 1 && mBook.getImage() != null){
            return new ImageLoader(BookDetailsActivity.this, mBook.getImage());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        if(data == null){
            return;
        }
        mCoverImage.setImageBitmap(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

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
