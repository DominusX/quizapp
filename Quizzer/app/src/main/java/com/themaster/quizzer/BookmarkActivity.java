package com.themaster.quizzer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    public static final String FILE_NAME = "QUIZZER";
    public static final String KEY_NAME = "QUESTIONS";

    RecyclerView recyclerView;
    Toolbar toolbar;

    List<QuestionModel> bookmarkedList;

    SharedPreferences myPref;
    SharedPreferences.Editor editor;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        recyclerView = findViewById(R.id.rv_bookmarks);
        toolbar = findViewById(R.id.toolbarBookmarks);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        myPref = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = myPref.edit();
        gson = new Gson();

        getBookmarks();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        BookmarksAdapter adapter = new BookmarksAdapter(bookmarkedList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        StoreBookmarks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getBookmarks() {
        String json = myPref.getString(KEY_NAME, "");

        Type type = new TypeToken<List<QuestionModel>>() {
        }.getType();
        bookmarkedList = gson.fromJson(json, type);
        if (bookmarkedList == null) {
            bookmarkedList = new ArrayList<>();
        }


    }

    private void StoreBookmarks() {
        String json = gson.toJson(bookmarkedList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }
}
