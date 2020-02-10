package com.themaster.quizzer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Objects;


public class SetsActivity extends AppCompatActivity {

    GridView gridView;
    List<String> sets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbarSet = findViewById(R.id.toolbarSets);
        gridView = findViewById(R.id.gridView);

        setSupportActionBar(toolbarSet);
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        sets = CategoriesActivity.list.get(getIntent().getIntExtra("position", 0)).getSets();

        GridAdapter gridAdapter = new GridAdapter(sets,getIntent().getStringExtra("title"));
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) ;
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
