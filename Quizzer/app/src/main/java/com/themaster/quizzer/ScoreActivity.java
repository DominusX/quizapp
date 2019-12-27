package com.themaster.quizzer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    TextView scored, total;
    Button Done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scored = findViewById(R.id.scored_txt);
        total = findViewById(R.id.total_txt);
        Done = findViewById(R.id.btn_done);

        scored.setText(String.valueOf(getIntent().getIntExtra("score", 0)));
        total.setText(String.format("%s%s", getString(R.string.out_of),
                String.valueOf(getIntent().getIntExtra("total", 0))));

        Done.setOnClickListener(v -> finish());
    }
}
