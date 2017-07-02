package com.project.course.bookstore;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ParseJson {
    Context mContext;

    public ParseJson(Context context){
        mContext = context;
    }

    private String readFromAssets(){
        String json_string = null;
        try {
            InputStream is = mContext.getAssets().open("Books.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json_string = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
        if(MainActivity.LOGS_ENABLED)
            Log.d(MainActivity.TAG, json_string);
        return json_string;
    }

    public JSONObject getJsonObject(){
        String json_string = readFromAssets();
        try{
            JSONObject bookstore = new JSONObject(json_string);
            return bookstore;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
