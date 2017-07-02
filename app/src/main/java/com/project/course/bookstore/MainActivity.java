package com.project.course.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends Activity {
    TextView title;
    EditText search_bar;
    public static final boolean LOGS_ENABLED = false;
    public static final String TAG = "BookStore MainActivity";
    public static final String BOOK_KEY = "books_list";
    static HashMap<Integer, Author> authorHashMap;
    HashMap<Integer, Book> bookHashMap;
    ArrayList<String> availableCategories;
    HashMap<String, ArrayList<Integer>> invertedIndexAuthors, invertedIndexBooks, invertedIndexCategories;
    Button searchButton;
    CheckBox non_fiction, self_help, personal_growth, psycology, philosophy, children_book;

    View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HashSet<String> categorySet = new HashSet<String>();
            fillCategories(categorySet);
            String search_string = search_bar.getText().toString();
            ArrayList<Book> books = InvertedIndex.getBooks(invertedIndexAuthors, invertedIndexBooks, authorHashMap, bookHashMap, categorySet, search_string);
            Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
            intent.putExtra(BOOK_KEY, books);
            getApplicationContext().startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setTitile();
        readData();
    }

    void setTitile(){
        Typeface tfFutura = Typeface.createFromAsset(getAssets(), "futura.ttf");
        title.setTypeface(tfFutura);

        String blue = "#4285f4";
        String red = "#ea4335";
        String orange = "#fbbc05";
        String green = "#34a853";
        String text = "<font color=" + blue + ">B</font>";
        text += "<font color=" + red + ">o</font>";
        text += "<font color=" + orange + ">o</font>";
        text += "<font color=" + blue + ">k</font>";
        text += "<font color=" + green + ">s</font>";
        text += "<font color=" + red + ">t</font>";
        text += "<font color=" + orange + ">o</font>";
        text += "<font color=" + blue + ">r</font>";
        text += "<font color=" + green + ">e</font>";

        title.setText(Html.fromHtml(text));
    }

    private void init(){
        title = (TextView) findViewById(R.id.title);
        search_bar = (EditText) findViewById(R.id.search_edit_text);
        searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(searchListener);
        authorHashMap = new HashMap<Integer, Author>();
        bookHashMap = new HashMap<Integer, Book>();
        invertedIndexAuthors = new HashMap<String, ArrayList<Integer>>();
        invertedIndexBooks = new HashMap<String, ArrayList<Integer>>();
        invertedIndexCategories = new HashMap<String, ArrayList<Integer>>();

        non_fiction = (CheckBox) findViewById(R.id.checkbox_nonfiction);
        self_help = (CheckBox) findViewById(R.id.checkbox_self_help);
        personal_growth = (CheckBox) findViewById(R.id.checkbox_personal_growth);
        psycology = (CheckBox) findViewById(R.id.checkbox_psycology);
        philosophy = (CheckBox) findViewById(R.id.checkbox_philosophy);
        children_book = (CheckBox) findViewById(R.id.checkbox_children_book);
    }
    private void readData(){
        ParseJson parseJson = new ParseJson(this);
        JSONObject jsonObject = parseJson.getJsonObject();
        if (jsonObject==null) {
            if (LOGS_ENABLED)
                Log.d(TAG,"Json Object is null");
            return;
        }

        try{
            JSONObject authors = jsonObject.getJSONObject("authors");
            createAuthorHashMap(authors);

            JSONObject books = jsonObject.getJSONObject("books");
            createBookHashMap(books);

            JSONArray categories = jsonObject.getJSONArray("category");
            createAvailableCategoryList(categories);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void createAuthorHashMap(JSONObject authors){
        Iterator<?> keys = authors.keys();
        while( keys.hasNext() ) {
            String author_id = (String)keys.next();
            try {
                JSONObject author_details = (JSONObject) authors.get(author_id);
                String name = author_details.getString("name");
                String image_url = author_details.getString("image");
                Author author = new Author(Integer.valueOf(author_id), name, image_url);
                authorHashMap.put(Integer.valueOf(author_id), author);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        if(LOGS_ENABLED)
            Log.d(TAG, authorHashMap.toString());
    }

    public void createBookHashMap(JSONObject books){
        Iterator<?> keys = books.keys();
        while( keys.hasNext() ) {
            String book_id = (String)keys.next();
            try {
                JSONObject book_details = (JSONObject) books.get(book_id);
                int author_id = book_details.getInt("author_id");
                String book_title = book_details.getString("title");
                String book_publisher = book_details.getString("publisher");
                String cover_url = book_details.getString("cover");
                String date_stamp = book_details.getString("date");
                int available_quantity = book_details.getInt("quantity");
                boolean ship_to_us = book_details.getBoolean("shipToUSA");
                boolean ship_to_canada = book_details.getBoolean("shipToCanada");
                ArrayList<String> book_categories = jsonArrayTOArrayList(book_details.getJSONArray("category"));
                Book book = new Book(Integer.valueOf(book_id), author_id, book_title, book_publisher, cover_url, date_stamp, available_quantity, ship_to_us, ship_to_canada, book_categories);
                bookHashMap.put(book.book_id, book);
                InvertedIndex.addBook(invertedIndexBooks, book);
                InvertedIndex.addAuthor(invertedIndexAuthors, authorHashMap.get(book.author_id), book.book_id);
                InvertedIndex.addCategory(invertedIndexCategories, book);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        if(LOGS_ENABLED){
            Log.d(TAG, bookHashMap.toString());
            Log.d(TAG, invertedIndexAuthors.toString());
            Log.d(TAG, invertedIndexBooks.toString());
            Log.d(TAG, invertedIndexCategories.toString());
        }
    }

    public ArrayList<String> jsonArrayTOArrayList(JSONArray jsonArray){
        ArrayList<String> list = new ArrayList<String>();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0; i<len; i++){
                try {
                    list.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return list;
                }
            }
        }
        return list;
    }
    public void createAvailableCategoryList(JSONArray categories){
        availableCategories = jsonArrayTOArrayList(categories);
        if(LOGS_ENABLED)
            Log.d(TAG, availableCategories.toString());
    }

    public void fillCategories(HashSet<String> categories){
        if(non_fiction.isChecked()){
            categories.add("Nonfiction");
        }

        if(self_help.isChecked()){
            categories.add("Self-Help");
        }

        if(personal_growth.isChecked()){
            categories.add("Personal Growth");
        }

        if(psycology.isChecked()){
            categories.add("Psychology");
        }

        if(philosophy.isChecked()){
            categories.add("Philosophy");
        }

        if(children_book.isChecked()){
            categories.add("Children Book");
        }
    }
}
