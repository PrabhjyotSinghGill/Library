package com.project.course.bookstore;

public class Author {
    int author_id;
    String author_name;
    String image_url;

    @Override
    public String toString() {
        return "Author object for:" + author_name ;
    }

    public Author(int id, String name, String url){
        author_id = id;
        author_name = name;
        image_url = url;
    }

}
