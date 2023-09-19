package com.example.my_notes;

import androidx.cardview.widget.CardView;

import com.example.my_notes.Models.WorkNotes;


public interface WorkNotesClickListener {
    //Короткое нажатие на заметку
    void onClick (WorkNotes notes);
    //Долгое нажатие на заметку
    void onLongClick (WorkNotes notes, CardView cardView);
}
