package com.themaster.quizzer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;


public class SetsActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbarSet = findViewById(R.id.toolbarSets);
        gridView = findViewById(R.id.gridView);

        setSupportActionBar(toolbarSet);
        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        GridAdapter gridAdapter = new GridAdapter(getIntent().getIntExtra("sets", 0),
                getIntent().getStringExtra("title"));
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
