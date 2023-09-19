package com.example.my_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_notes.Models.PersonalNotes;
import com.example.my_notes.Models.WorkNotes;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    //Объект заметки "Личное"
    PersonalNotes personalnotes;
    //Объект заметки "Работа"
    WorkNotes worknotes;

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

        //Действия при нажатии на кнопку сохранить
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Получение названия заметки
                String title=editText_title.getText().toString();
                //Получение текста заветки
                String description=editText_notes.getText().toString();
                //Проверка введён ли текст заметки
                if (description.isEmpty()) {
                    //Всплывающее сообщение
                    Toast.makeText(NotesTakerActivity.this, R.string.no_notes_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Формат даты - день недели, число, месяц, год, время- часы:минуты
                SimpleDateFormat formater= new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                Date date = new Date();

                //Если заметка создаётся в разделе "Личное"
                if (getSelected_folder.equalsIgnoreCase("Личное")) {
                    //Создание новой заметки
                    personalnotes = new PersonalNotes();
                    //Получение названия заметки из поля ввода
                    personalnotes.setTitle(title);
                    //Получение текста заметки из поля ввода
                    personalnotes.setText(description);
                    //Получение даты создания заметки
                    personalnotes.setDate(formater.format(date));
                    //Создание новой Activity
                    Intent intent = new Intent();
                    //Передача заметки
                    intent.putExtra("note", personalnotes);
                    //Возвращение результата в вызвавшую активность
                    setResult(Activity.RESULT_OK, intent);
                    //Завершение активности
                    finish();
                }
                //Если заметка создаётся в разделе "Работа"
                else if (getSelected_folder.equalsIgnoreCase("Работа")) {
                    //Создание новой заметки
                    worknotes = new WorkNotes();
                    //Получение названия заметки из поля ввода
                    worknotes.setTitle(title);
                    //Получение текста заметки из поля ввода
                    worknotes.setText(description);
                    //Получение даты создания заметки
                    worknotes.setDate(formater.format(date));
                    //Создание новой Activity
                    Intent intent = new Intent();
                    //Передача заметки
                    intent.putExtra("note",   worknotes);
                    //Возвращение результата в вызвавшую активность
                    setResult(Activity.RESULT_OK, intent);
                    //Завершение активности
                    finish();
                }
            }
        });

    }
}