package com.example.note_and_todo_list_app;

import androidx.cardview.widget.CardView;

import com.example.note_and_todo_list_app.Model.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
