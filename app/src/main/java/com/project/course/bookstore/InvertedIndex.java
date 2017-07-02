package com.project.course.bookstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static android.R.attr.author;
import static android.R.attr.id;

public class InvertedIndex {

    public static void addAuthor(HashMap<String, ArrayList<Integer>> invertedIndex, Author author, int book_id){
        String name = author.author_name.replaceAll("[^a-zA-Z0-9 ]", "");
        System.out.println(name);
        String[] tokens = name.split("\\s+");
        ArrayList<Integer> mList;
        for(String token: tokens){
            token = token.toLowerCase();
            mList = invertedIndex.getOrDefault(token, new ArrayList<Integer>());
            mList.add(book_id);
            invertedIndex.put(token, mList);
        }
    }

    public static void addBook(HashMap<String, ArrayList<Integer>> invertedIndex, Book book){
        String name = book.book_title.replaceAll("[^a-zA-Z0-9 ]", "");
        System.out.println(name);
        int id = book.book_id;
        String[] tokens = name.split("\\s+");
        ArrayList<Integer> mList;
        for(String token: tokens){
            token = token.toLowerCase();
            mList = invertedIndex.getOrDefault(token, new ArrayList<Integer>());
            mList.add(id);
            invertedIndex.put(token, mList);
        }
    }

    public static void addCategory(HashMap<String, ArrayList<Integer>> invertedIndex, Book book){
        int book_id = book.book_id;
        ArrayList<Integer> mList;
        for(String category: book.book_categories){
            category = category.toLowerCase();
            mList = invertedIndex.getOrDefault(category, new ArrayList<Integer>());
            mList.add(book_id);
            invertedIndex.put(category, mList);
        }
    }

    public static ArrayList<Book> getBooks(HashMap<String, ArrayList<Integer>> invertedIndexAuthors, HashMap<String, ArrayList<Integer>> invertedIndexBooks, HashMap<Integer, Author> authorHashMap, HashMap<Integer, Book> bookHashMap, HashSet<String> categorySet, String search_string){
        if(search_string.isEmpty()){
            for(Author author:MainActivity.authorHashMap.values()){
                search_string += author.author_name + " ";
            }
        }
        String[] tokens = search_string.toLowerCase().split("\\s+");
        ArrayList<Book> books = new ArrayList<Book>();
        HashSet<Integer> addedBooks = new HashSet<Integer>();
        if(MainActivity.LOGS_ENABLED)
            System.out.println(categorySet);
        //Let the search begin
        for(String token: tokens){
            if(invertedIndexAuthors.containsKey(token)){
                ArrayList<Integer> book_ids = invertedIndexAuthors.get(token);
                for(int book_id: book_ids){
                    if(!addedBooks.contains(book_id)){
                        Book my_book = bookHashMap.get(book_id);
                        for(String book_category : my_book.book_categories ){
                            if(categorySet.contains(book_category)){
                                if(MainActivity.LOGS_ENABLED)
                                    System.out.println(book_category);
                                books.add(my_book);
                                addedBooks.add(book_id);
                                break;
                            }
                        }
                    }
                }
            }
            if(invertedIndexBooks.containsKey(token)){
                ArrayList<Integer> book_ids = invertedIndexBooks.get(token);
                for(int book_id: book_ids){
                    if(!addedBooks.contains(book_id)){
                        Book my_book = bookHashMap.get(book_id);
                        for(String book_category : my_book.book_categories ){
                            if(categorySet.contains(book_category)){
                                if(MainActivity.LOGS_ENABLED)
                                    System.out.println(book_category);
                                books.add(my_book);
                                addedBooks.add(book_id);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return books;
    }

}
