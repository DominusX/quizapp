package com.themaster.quizzer;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    public static final String FILE_NAME = "QUIZZER";
    public static final String KEY_NAME = "QUESTIONS";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("fir-project-4dbab");

    TextView questions, indicator;
    FloatingActionButton floatingActionButton;
    LinearLayout optionsContainer;
    Button ShareBtn, NextBtn;
    Dialog loadingDialog;

    List<QuestionModel> list;

    int position = 0;
    int count = 0;
    int myScore = 0;
    int setNumber;
    int matchedPosition;

    String category;

    List<QuestionModel> bookmarkedList;

    SharedPreferences myPref;
    SharedPreferences.Editor editor;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = findViewById(R.id.toolbarQuestions);
        setSupportActionBar(toolbar);

        questions = findViewById(R.id.txt_question);
        indicator = findViewById(R.id.txt_indicator);
        optionsContainer = findViewById(R.id.optionsContainer);
        floatingActionButton = findViewById(R.id.float_bookmark);
        ShareBtn = findViewById(R.id.btnShare);
        NextBtn = findViewById(R.id.btnNext);

        myPref = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = myPref.edit();
        gson = new Gson();

        getBookmarks();

        floatingActionButton.setOnClickListener(v -> {

            if (modelMatch()) {
                bookmarkedList.remove(matchedPosition);
                floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_bookmark_solid));
                Toast.makeText(this, "Bookmarked!!!", Toast.LENGTH_SHORT).show();
            } else {

                bookmarkedList.add(list.get(position));
                floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
                Toast.makeText(this, "Bookmark removed!!!", Toast.LENGTH_SHORT).show();
            }
        });

        category = getIntent().getStringExtra("category");
        setNumber = getIntent().getIntExtra("setNumber", 1);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        list = new ArrayList<>();
        loadingDialog.show();
        myRef.child("sets")
                .child("Categories")
                .child("questions")
                .orderByChild("setNumber").equalTo(setNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(QuestionModel.class));
                }
                if (list.size() > 0) {
                    for (int i = 0; i < 4; i++) {
                        optionsContainer.getChildAt(i).setOnClickListener(v ->
                                checkAnswer((Button) v)
                        );
                    }
                    playAnim(questions, 0, list.get(position).getQuestion());

                    NextBtn.setOnClickListener(v -> {
                        NextBtn.setEnabled(false);
                        NextBtn.setAlpha(0.5f);
                        enableOption(true);
                        position++;
                        if (position == list.size()) {
                            Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                            scoreIntent.putExtra("score", myScore);
                            scoreIntent.putExtra("total", list.size());
                            startActivity(scoreIntent);
                            finish();
                            return;
                        }
                        count = 0;
                        playAnim(questions, 0, list.get(position).getQuestion());
                    });
                    ShareBtn.setOnClickListener(v -> {
                        String body = list.get(position).getQuestion() + "\n" +
                                list.get(position).getOption1() + "\n" +
                                list.get(position).getOption2() + "\n" +
                                list.get(position).getOption3() + "\n" +
                                list.get(position).getOption4();

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plane");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quizzer Challenge");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));

                    });
                } else {
                    finish();
                    Toast.makeText(QuestionsActivity.this, "No Questions Found!", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        StoreBookmarks();
    }

    private void playAnim(final View view, final int value, final String data) {
        view.animate()
                .alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String Option = "";
                    if (count == 0) {
                        Option = list.get(position).getOption1();
                    } else if (count == 1) {
                        Option = list.get(position).getOption2();
                    } else if (count == 2) {
                        Option = list.get(position).getOption3();
                    } else if (count == 3) {
                        Option = list.get(position).getOption4();
                    }
                    playAnim(optionsContainer.getChildAt(count), 0, Option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //data change

                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        indicator.setText(position + 1 + "/" + list.size());
                        if (modelMatch()) {
                            floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_bookmark_solid));
                        } else {
                            floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_bookmark));

                        }
                    } catch (ClassCastException e) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        NextBtn.setEnabled(true);
        NextBtn.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getAnswer())) {
            //Correct Answer
            myScore++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            //Incorrect Answer
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            for (int i = 0; i < 4; i++) {
                Button correctOption;
                correctOption = optionsContainer.findViewWithTag(list.get(position).getAnswer());
                correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }
        }
    }

    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
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

    private boolean modelMatch() {
        boolean matched = false;
        int i = 0;
        for (QuestionModel model : bookmarkedList) {
            if (model.getQuestion().equals(list.get(position).getQuestion())
                    && model.getAnswer().equals(list.get(position).getAnswer())
                    && model.getSetNumber() == list.get(position).getSetNumber()) {

                matched = true;
                matchedPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void StoreBookmarks() {
        String json = gson.toJson(bookmarkedList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }
}
