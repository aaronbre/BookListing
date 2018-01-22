package com.example.aaronbrecher.booklisting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookSearch extends AppCompatActivity {
    private EditText mSearchText;
    private Button mSearchButton;
    private String mUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        mSearchButton = (Button)findViewById(R.id.search_submit);
        mSearchText = (EditText)findViewById(R.id.search_input);
        //get Search text and change to a string

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUrl = mSearchText.getText().toString();
                Intent intent = new Intent(view.getContext(), ResultsActivity.class);
                intent.putExtra("searchParam", mUrl);
                startActivity(intent);
            }
        });

    }
}
