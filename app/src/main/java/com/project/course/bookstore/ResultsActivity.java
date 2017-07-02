package com.project.course.bookstore;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout container = (LinearLayout) findViewById(R.id.container_books);


        ArrayList<Book> books = (ArrayList<Book>) getIntent().getSerializableExtra(MainActivity.BOOK_KEY);
        for(Book book: books){
            TextView tv = new TextView(this);
            tv.setTextColor(Color.BLACK);
            tv.setText(book.toString());
            tv.setPadding(10,10,10,10);
            container.addView(tv);
        }
    }

}
