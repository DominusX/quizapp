package com.themaster.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    private int sets = 0;
    private String category;

    public GridAdapter(int sets, String category) {
        this.sets = sets;
        this.category = category;
    }

    @Override
    public int getCount() {
        return sets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item,
                    parent, false);
        } else {
            view = convertView;
        }

        view.setOnClickListener(v -> {
            Intent intent = new Intent(parent.getContext(), QuestionsActivity.class);
            intent.putExtra("category",category);
            intent.putExtra("setNumber",position+1);
            parent.getContext().startActivity(intent);
        });

        ((TextView) view.findViewById(R.id.textViewSet)).setText(String.valueOf(position + 1));

        return view;
    }
}
