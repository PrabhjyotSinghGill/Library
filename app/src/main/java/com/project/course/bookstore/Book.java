package com.project.course.bookstore;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Book implements Parcelable{
    public int book_id, author_id;
    public String book_title, book_publisher, cover_url;
    public String date_stamp;
    public int available_quantity;
    public boolean ship_to_us, ship_to_canada;
    public ArrayList<String> book_categories;

    @Override
    public String toString() {
        String result = "";
        result += "Title: " + book_title + "\n";
        result += "Author: " + MainActivity.authorHashMap.get(author_id).author_name + "\n";
        result += "Ship To US: " + ship_to_us + "\n";
        result += "Ship To Canada: " + ship_to_canada + "\n";
        result += "Categories: ";
        String comma = "";
        for(String category:book_categories){
            result += comma + category;
            comma = ", ";
        }
        result += "\nAvailable Quantity: " + available_quantity + "\n";
        return result;
    }

    public Book(int id_book, int id_author, String title, String publisher, String url, String date, int quantity, boolean to_us, boolean to_canada, ArrayList<String> categories){
        book_id = id_book;
        author_id = id_author;
        book_title = title;
        book_publisher = publisher;
        cover_url = url;
        date_stamp = date;
        available_quantity = quantity;
        ship_to_us = to_us;
        ship_to_canada = to_canada;
        book_categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(book_id);
        parcel.writeInt(author_id);
        parcel.writeString(book_title);
        parcel.writeString(book_publisher);
        parcel.writeString(cover_url);
        parcel.writeString(date_stamp);
        parcel.writeInt(available_quantity);
        parcel.writeByte((byte) (ship_to_us ? 1 : 0));
        parcel.writeByte((byte) (ship_to_canada ? 1 : 0));
        parcel.writeStringList(book_categories);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel s) {
            ArrayList<String> mList = new ArrayList<String>();
            int book_id = s.readInt();
            int author_id = s.readInt();
            String book_title =  s.readString();
            String book_publisher = s.readString();
            String cover_url = s.readString();
            String date_stamp = s.readString();
            int available_quantity = s.readInt();
            boolean ship_to_us = s.readByte() != 0;
            boolean ship_to_canada = s.readByte() != 0;
            s.readStringList(mList);
            Book mBook = new Book(book_id, author_id, book_title, book_publisher, cover_url, date_stamp, available_quantity, ship_to_us, ship_to_canada, mList);
            return mBook;
        }

        public Book[] newArray(int size){
            return new Book[size];
        }
    };
}
