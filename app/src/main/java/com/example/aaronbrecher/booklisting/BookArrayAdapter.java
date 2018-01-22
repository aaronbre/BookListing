package com.example.aaronbrecher.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aaronbrecher on 1/21/18.
 */

public class BookArrayAdapter extends ArrayAdapter<Book> {
    public BookArrayAdapter(@NonNull Context context, ArrayList<Book> books, int resource) {
        super(context, resource, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book currentBook = getItem(position);

        View listViewItem = convertView;
        if(listViewItem == null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        TextView title = (TextView)listViewItem.findViewById(R.id.book_title);
        TextView author = (TextView)listViewItem.findViewById(R.id.book_author);
        TextView category = (TextView)listViewItem.findViewById(R.id.book_category);
        TextView price = (TextView)listViewItem.findViewById(R.id.book_price);

        title.setText(currentBook.getTitle());
        author.setText(currentBook.getAuthor());
        category.setText(currentBook.getId());
        if(currentBook.getPrice() == null){
            price.setText(R.string.price_not_found);
        } else {
            price.setText(currentBook.getPrice().toString());
        }




        return listViewItem;
    }
}
