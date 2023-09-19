package com.example.my_notes;

import androidx.cardview.widget.CardView;

import com.example.my_notes.Models.PersonalNotes;

public interface PersonalNotesClickListener {
    //Короткое нажатие на заметку
    void onClick (PersonalNotes notes);
    //Долгое нажатие на заметку
    void onLongClick (PersonalNotes notes, CardView cardView);
}
