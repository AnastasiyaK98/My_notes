package com.example.my_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_notes.DataBase.AppDataBase;
import com.example.my_notes.Models.PersonalNotes;
import com.example.my_notes.Models.WorkNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    //Переменная для получения значения о том какой раздел выбран
    String get_selected_folder;
    //Список заметок
    RecyclerView recyclerView;
    //Кнопка добавить
    FloatingActionButton float_button;
    //Название раздела
    TextView text_folder;
    //Стрелка назад
    ImageView back;
    //Переменная для передачи названия раздела
    String get_selected_folder1;
    //Адаптер для раздела "Личное"
    AdapterPersonal adapterPersonal;
    //Адаптер для раздела "Работа"
    AdapterWork adapterWork;
    //База данных
    AppDataBase dataBase;
    //Список заметок "Личное"
    List<PersonalNotes> personalnotes = new ArrayList<>();
    //Список заметок "Работа"
    List<WorkNotes> worknotes = new ArrayList<>();
    //Поичковая строка
    SearchView search_home;
    //Выбранная заметка "Личное"
    PersonalNotes selectedNote;
    //Выбранная заметка "Работа"
    WorkNotes selectedNoteWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        //Получение ссылки на View элементов (связываем графический элемент через его id с программным)
        recyclerView = findViewById(R.id.recycler_list);
        float_button = findViewById(R.id.add_note);
        back = findViewById(R.id.back);
        text_folder = findViewById(R.id.text_folder);
        search_home = findViewById(R.id.search_home);


        //Получаем значение какой раздел выбран
        try {
            get_selected_folder = (String) getIntent().getSerializableExtra("selected_folder");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Установка текста в названии ActionBar
        text_folder.setText(get_selected_folder);

        //Сохраняем название раздела в новой переменной для последующей передачи
        get_selected_folder1=get_selected_folder;

        //Событие при нажатии на кнопку назад
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Переход из NotesListActivity в MainActivity
                Intent intent = new Intent(NotesListActivity.this, MainActivity.class);
                //Запуск Activity
                startActivity(intent);
            }
        });


        //Получение базы данных
        dataBase = AppDataBase.getInstance(this);
        //Получения данных из таблицы "PersonalBD"
        personalnotes = dataBase.noteDAO().getAll();
        //Получения данных из таблицы "WorkBD"
        worknotes = dataBase.workDAO().getAll();

        //Если выбран раздел "Личное"
        if (get_selected_folder1.equalsIgnoreCase("Личное")) {
            updateRecyclrePersonal(personalnotes);
        }
        //Если выбран раздел "Работа"
        else if (get_selected_folder1.equalsIgnoreCase("Работа")){
            updateRecyclreWork(worknotes);
        }

        //Событие при нажатии на кнопку добавить
        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Переход из NotesListActivity в NotesTakerActivity
                Intent intent = new Intent(NotesListActivity.this, NotesTakerActivity.class);
                //Передача названия выбранного раздела
                intent.putExtra("selected_folder_name", get_selected_folder1);
                //Запуск новой Activity, возвращающей результат при завершении в виде requestCode 101 - создание заметки
                startActivityForResult(intent, 101);
            }
        });

    }

    //Привязка  recyclerView с заметками к адаптеру отображения для таблицы "Личное"
    private void updateRecyclrePersonal (List <PersonalNotes > notes) {
        //Фиксированный размер
        recyclerView.setHasFixedSize(true);
        //Установка макета в виде неравномерной сетки с вертикальной ориентацией и двумя столбцами
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //Создание адаптера
        adapterPersonal = new AdapterPersonal(NotesListActivity.this, notes, personalnotesClickListener);
        //Установка адаптера
        recyclerView.setAdapter(adapterPersonal);
    }

    //Привязка  recyclerView с заметками к адаптеру отображения для таблицы "Работа"
    private void updateRecyclreWork (List<WorkNotes> works) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapterWork = new AdapterWork(NotesListActivity.this, works, worknotesClickListener);
        recyclerView.setAdapter(adapterWork);
    }

    //Таблица "Личное"
    //Открытие заметки  для редактирования коротким нажатием на неё
    private final PersonalNotesClickListener personalnotesClickListener = new PersonalNotesClickListener() {
        @Override
        public void onClick(PersonalNotes personalnotes) {
            //Переход из NotesListActivity в NotesTakerActivity
            Intent intent=new Intent(NotesListActivity.this, NotesTakerActivity.class);
            //Передача значения из personalnotes
            intent.putExtra("old_note", personalnotes);
            //Передача названия выбранного раздела
            intent.putExtra("selected_folder_name", get_selected_folder1);
            //Запуск новой Activity, возвращающей результат при завершении в виде requestCode 102- редактирование заметки
            startActivityForResult(intent, 102);
        }

        //Долгое нажатие вызывет меню закрепления/удаления заметки
        @Override
        public void onLongClick(PersonalNotes personalnotes, CardView cardView) {

        }
    };

    //Таблица "Работа"
    //Открытие заметки  для редактирования коротким нажатием на неё
    private final WorkNotesClickListener worknotesClickListener = new WorkNotesClickListener() {
        @Override
        public void onClick(WorkNotes worknotes) {
            //Переход из NotesListActivity в NotesTakerActivity
            Intent intent=new Intent(NotesListActivity.this, NotesTakerActivity.class);
            //Передача значения из worknotes
            intent.putExtra("old_note",  worknotes);
            //Передача названия выбранного раздела
            intent.putExtra("selected_folder_name", get_selected_folder1);
            //Запуск новой Activity, возвращающей результат при завершении в виде requestCode 102- редактирование заметки
            startActivityForResult(intent, 102);
        }

        //Долгое нажатие вызывет меню закрепления/удаления заметки
        @Override
        public void onLongClick(WorkNotes worknotes, CardView cardView) {

        }
    };
}