package com.example.my_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NotesTakerActivity extends AppCompatActivity {

    //Поля ввода названия и текста заметки
    EditText editText_title, editText_notes;
    //Кнопка назад
    ImageView back_note;
    //Кнопка сохранить
    ImageView imageView_save;
    //Название раздела
    TextView text_folder_note;
    //Название раздела
    String folder_name;
    //Полученное предыдущее название раздела
    String getSelected_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        //Получение ссылки на View элементов (связываем графический элемент через его id с программным)
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        back_note = findViewById(R.id.back_note);
        imageView_save = findViewById(R.id.imageView_save);
        text_folder_note = findViewById(R.id.text_folder_note);

        //Получаем значение предыдущего раздела
        try {
            getSelected_folder = (String) getIntent().getSerializableExtra("selected_folder_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Название текущего раздела= значение предыдущего раздела + "-Заметка"
        folder_name = String.join("-", getSelected_folder, "Заметка");
        //Установка названия раздела
        text_folder_note.setText(folder_name);

        //Событие при нажатии на кнопку назад
        back_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Переход из NotesTakerActivity в NotesListActivity
                Intent intent = new Intent(NotesTakerActivity.this, NotesListActivity.class);
                //Передаём название раздела назад
                intent.putExtra("selected_folder", getSelected_folder);
                //Запуск Activity
                startActivity(intent);
                //Завершение текущего Activity
                finish();
            }
        });

    }
}