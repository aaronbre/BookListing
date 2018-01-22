package com.example.aaronbrecher.booklisting;

import java.io.Serializable;

/**Book class holds basic information about the book
 * Created by aaronbrecher on 1/21/18.
 */

public class Book implements Serializable {
    private String mTitle;
    private String mAuthor;
    private String mCategory;
    private Double mPrice;
    private String mImage;
    private String mDescription;

    public Book(String title, String author, String description, String catagory, Double price, String image) {
        mAuthor = author;
        mTitle = title;
        mCategory = catagory;
        mPrice = price;
        mImage = image;
        mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getId() {
        return mCategory;
    }

    public Double getPrice(){ return mPrice; }

    public String getImage(){return mImage; }

    public String getDescription(){return mDescription; }
}
