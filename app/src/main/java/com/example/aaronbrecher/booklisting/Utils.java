package com.example.aaronbrecher.booklisting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronbrecher on 1/21/18.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static final String NOT_FOUND = "Not Found";

    public Utils() {
    }

    public static List<Book> fetchBooks(String urlString, Context context){
        URL url = createUrl(urlString);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(TAG, "fetchBooks: Failed with IOException",e);
        }
        return extractJson(jsonResponse, context);
    }

    /**
     * create the url object to use for our query will return null if not a valid url
     * @param urlString string containing the url text
     * @return URL returns a URL object created from the string parameter
     */
    public static URL createUrl(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch (MalformedURLException e){
            Log.e(TAG, "createUrl: not a valid url", e);
        }
        return url;
    }

    /**
     * Make the http request using the supplied url
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        //set up the connection object and the input stream
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try{
            //set up the http connection using the url set the method to GET
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();

            //make sure we got a good response code from the server
            if(httpURLConnection.getResponseCode() == 200){
                //get the stream object from the url connection
                inputStream = httpURLConnection.getInputStream();
                // Call function to parse the jsonString from the url
                jsonResponse = getJsonResponse(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest: Not successfull, Response Code: " + httpURLConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(TAG, "makeHttpRequest: Failed with IOException",e);
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Build up the JSON string using the InputStreamReader and Buffered Reader
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String getJsonResponse(InputStream inputStream) throws IOException{
        StringBuilder builder = new StringBuilder();
        if(inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null){
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }

    /**
     * This function is specific to traverse the JSONObject and get the book infromation
     * will not work for a different task
     * @param json
     * @return
     */
    private static List<Book> extractJson(String json, Context context){
        ArrayList<Book> books = new ArrayList<>();
        if (json == null || json == ""){
            return books;
        }
        try {
            //create variables to set up the Book object
            String title = "Title " + NOT_FOUND;
            String author = "Author " +NOT_FOUND;
            String category = "Category " + NOT_FOUND;
            String description = "Description" + NOT_FOUND;
            Double price = null;
            boolean isPurchaseable = false;
            String image;
            // get the shared prefrences to check if only priced items should be shown
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean onlyPurchaseable = prefs.getBoolean(context.getString(R.string.settings_purchaseable_key), false);
            //traverse the JSON and get all the elements to add to Book object
            JSONObject root = new JSONObject(json);
            JSONArray bookList = root.getJSONArray("items");
            for(int i = 0; i < bookList.length(); i++){
                JSONObject currentBook = bookList.getJSONObject(i);
                //get object which will be the root of all the book details
                JSONObject bookDetails = currentBook.getJSONObject("volumeInfo");
                //every book must have a title
                title = bookDetails.getString("title");
                //check for and add authors truncates multiple authors to the first one
                if(bookDetails.has("authors")) author = bookDetails.getJSONArray("authors").getString(0);
                //check for and add description
                if (bookDetails.has("description")) description = bookDetails.getString("description");
                //check for and add category
                if (bookDetails.has("categories")) category = bookDetails.getJSONArray("categories").getString(0);
                //get an image from the object if no image set to null
                image = bookDetails.has("imageLinks") ? bookDetails.getJSONObject("imageLinks").getString("smallThumbnail") : null;

                //check if the object has sale info if so set price to the listPrice null if no SalesInfo
                if(currentBook.has("saleInfo")){
                    JSONObject saleDetails = currentBook.getJSONObject("saleInfo");
                    if (saleDetails.has("listPrice")){
                        price = saleDetails.getJSONObject("listPrice").getDouble("amount");
                        isPurchaseable = true;
                    }
                }
                //check if only purchaseable items should be added if so make sure is purchaseable
                if ((onlyPurchaseable && isPurchaseable) || !onlyPurchaseable){
                    books.add(new Book(title,author, description, category, price, image));
                }
            }
        }catch (JSONException e){
            Log.e(TAG, "extractJson: not Succesfull invalid JSON somewhere",e);
        }
        return books;
    }
}
