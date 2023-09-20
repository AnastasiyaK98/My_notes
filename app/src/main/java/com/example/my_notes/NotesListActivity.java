package com.example.my_notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_notes.DataBase.AppDataBase;
import com.example.my_notes.Models.PersonalNotes;
import com.example.my_notes.Models.WorkNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

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
    PersonalNotes selectedNotePersonal;
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
        personalnotes = dataBase.personalDAO().getAll();
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

        //Событие при выполнении поиска заметки
        search_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //Поиск при нажатии на клавишу
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            //Поиск при вводе текста на основе фильтра
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
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

    //Привязка  recyclerView с заметками к адаптеру отображения для таблицы "Работа", те же действия, что для раздела "Личное"
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
            selectedNotePersonal = new PersonalNotes();
            //Заметка, на которую происходит нажатие
            selectedNotePersonal=personalnotes;
            //Вызов ысплывающего меню
            showPopUp (cardView);
        }
    };


    //Таблица "Работа"
    //Открытие заметки  для редактирования коротким нажатием на неё, те же действия, что для раздела "Личное"
    private final WorkNotesClickListener worknotesClickListener = new WorkNotesClickListener() {
        @Override
        public void onClick(WorkNotes worknotes) {
            Intent intent=new Intent(NotesListActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note",  worknotes);
            intent.putExtra("selected_folder_name", get_selected_folder1);
            startActivityForResult(intent, 102);
        }

        //Долгое нажатие вызывет меню закрепления/удаления заметки, те же действия, что для раздела "Личное"
        @Override
        public void onLongClick(WorkNotes worknotes, CardView cardView) {
            selectedNoteWork = new WorkNotes();
            selectedNoteWork=worknotes;
            showPopUp (cardView);
        }
    };


    //Меню выбора действия
    private void showPopUp(CardView cardView) {
        //Всплывающее меню, привязанное к элементу cardView
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        //Прослушиватель для ответа на события щелчка по пункту меню
        popupMenu.setOnMenuItemClickListener(this);
        //Получение всплывающего меню из xml-файла по его id
        popupMenu.inflate(R.menu.popup_menu);
        //Показать всплывающее меню
        popupMenu.show();
    }

    //Действия при выборе прикрепления/открепления и удалении заметок
    //Этот метод будет вызываться при щелчке по элементу меню, если сам элемент еще не обработал событие
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //Если выбран пункт меню Закрепить/Открепить
        if (item.getItemId()==R.id.pin) {
            //Если в разделе "Личное"
            if (get_selected_folder1.equalsIgnoreCase("Личное")) {
                //Если заметка откреплена
                if (selectedNotePersonal.isPinned()) {
                    //Обновление значения pin в таблице базы данных
                    dataBase.personalDAO().pin(selectedNotePersonal.getUid(), false);
                    //Вывод всплывающего сообщения
                    Toast.makeText(NotesListActivity.this, R.string.unpinned, Toast.LENGTH_SHORT).show();
                } else {
                    //Обновление значения pin в таблице базы данных
                    dataBase.personalDAO().pin(selectedNotePersonal.getUid(), true);
                    Toast.makeText(NotesListActivity.this, R.string.pinned, Toast.LENGTH_SHORT).show();
                }
                personalnotes.clear();
                personalnotes.addAll(dataBase.personalDAO().getAll());
                //Перерисовка списка
                adapterPersonal.notifyDataSetChanged();
            }
            //Если в разделе "Работа", действия те же, что и для раздела "Личное"
            else if (get_selected_folder1.equalsIgnoreCase("Работа")) {
                if (selectedNoteWork.isPinned()) {
                    dataBase.workDAO().pin(selectedNoteWork.getUid(), false);
                    Toast.makeText(NotesListActivity.this, R.string.unpinned, Toast.LENGTH_SHORT).show();
                } else {
                    dataBase.workDAO().pin(selectedNoteWork.getUid(), true);
                    Toast.makeText(NotesListActivity.this, R.string.pinned, Toast.LENGTH_SHORT).show();
                }
                worknotes.clear();
                worknotes.addAll(dataBase.workDAO().getAll());
                adapterWork.notifyDataSetChanged();
            }
            return true;
        }
        //Если выбран пункт меню Удалить
        if(item.getItemId()==R.id.delete) {
            //Для раздела "Личное"
            if (get_selected_folder1.equalsIgnoreCase("Личное")) {
                //Удаление выбранной заметки из таблицы БД
                dataBase.personalDAO().delete(selectedNotePersonal);
                //Удаление выбранной заметки из списка заметок
                personalnotes.remove(selectedNotePersonal);
                //Перерисовка списка на экране
                adapterPersonal.notifyDataSetChanged();
                //Вывод всплываюшего сообщения
                Toast.makeText(NotesListActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
            }
            //Для раздела "Работа", действия те же, что и для раздела "Личное"
            else if (get_selected_folder1.equalsIgnoreCase("Работа")) {
                dataBase.workDAO().delete(selectedNoteWork);
                worknotes.remove(selectedNoteWork);
                adapterWork.notifyDataSetChanged();
                Toast.makeText(NotesListActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    //Получение данных от активности NotesListActivity и сохранение/обновлении заметки в БД
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Проверка кода создания заметки
        if (requestCode==101){
            //Если заметка создавалась в разделе "Личное"
            if (get_selected_folder1.equalsIgnoreCase("Личное")) {
                //Проверка успешности результатирующего кода
                if(resultCode== Activity.RESULT_OK) {
                    //Создание новой заметки
                    PersonalNotes new_personal_notes = new PersonalNotes();
                    try {
                        //Получение введённой заметки
                        new_personal_notes = (PersonalNotes) data.getSerializableExtra("note");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Помещение заметки в таблицу "Личное"
                    dataBase.personalDAO().insert(new_personal_notes);
                    //Очистка списка
                    personalnotes.clear();
                    //Добавление всех данных таблицы PersonalBD в список
                    personalnotes.addAll(dataBase.personalDAO().getAll());
                    //Данные изменились, перерисовка списка на экране
                    adapterPersonal.notifyDataSetChanged();
                }
            }
            //Если заметка создавалась в разделе "Работа", те же действия, что для раздела "Личное"
            else  if (get_selected_folder1.equalsIgnoreCase("Работа")) {
                if (resultCode == Activity.RESULT_OK) {
                    WorkNotes new_work_notes = new WorkNotes();
                    try {
                        new_work_notes = ( WorkNotes) data.getSerializableExtra("note");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dataBase.workDAO().insert(new_work_notes);
                    worknotes.clear();
                    worknotes.addAll(dataBase.workDAO().getAll());
                    adapterWork.notifyDataSetChanged();
                }
            }
        }
        //Проверка кода редактирования заметки
        if (requestCode==102){
            //Если заметка создавалась в разделе "Личное"
            if (get_selected_folder1.equalsIgnoreCase("Личное")) {
                //Проверка успешности результатирующего кода
                if(resultCode== Activity.RESULT_OK) {
                    //Создание новой заметки
                    PersonalNotes new_personal_notes = new  PersonalNotes();
                    try {
                        //Получение введённой заметки
                        new_personal_notes = (PersonalNotes) data.getSerializableExtra("note");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Обновление заметки в таблице "Личное"
                    dataBase.personalDAO().update(new_personal_notes.getUid(), new_personal_notes.getTitle(), new_personal_notes.getText());
                    //Очистка списка
                    personalnotes.clear();
                    //Добавление всех данных таблицы PersonalBD в список
                    personalnotes.addAll(dataBase.personalDAO().getAll());
                    //Данные изменились, перерисовка списка на экране
                    adapterPersonal.notifyDataSetChanged();
                }
            }
            //Если заметка создавалась в разделе "Работа", те же действия, что для раздела "Личное"
            else if (get_selected_folder1.equalsIgnoreCase("Работа")) {
                if (resultCode == Activity.RESULT_OK) {
                    WorkNotes new_work_notes = new   WorkNotes();
                    try {
                        new_work_notes = (WorkNotes) data.getSerializableExtra("note");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dataBase.workDAO().update(new_work_notes.getUid(), new_work_notes.getTitle(), new_work_notes.getText());
                    worknotes.clear();
                    worknotes.addAll(dataBase.workDAO().getAll());
                    adapterWork.notifyDataSetChanged();
                }
            }
        }
    }

    //Фильтр для поиска
    private void filter (String newText){
        //Списки заметок разделов "Личное" и "Работа"
        List<PersonalNotes> filteredListPersonal = new ArrayList<>();
        List<WorkNotes> filteredListWork = new ArrayList<>();
        //Если поиск выполняется в разделе "Личное"
        if (get_selected_folder1.equalsIgnoreCase("Личное")) {
            //Сравнение отдельной составляющей со всем списком
            for (PersonalNotes singleNote : personalnotes) {
                //Поиск по названию заметки и тексту заметки без учёта регистра
                if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                        || singleNote.getText().toLowerCase().contains(newText.toLowerCase())) {
                    //Добавляем в список найденных заметок
                    filteredListPersonal.add(singleNote);
                }
            }
            //Обновление списка на экране
            adapterPersonal.filterListPersonal(filteredListPersonal);
        }
        //Если поиск выполняется в разделе "Работа", те же действия, что для раздела "Личное"
        else if (get_selected_folder1.equalsIgnoreCase("Работа")){
            for (WorkNotes singleNoteWork : worknotes) {
                if (singleNoteWork.getTitle().toLowerCase().contains(newText.toLowerCase())
                        || singleNoteWork.getText().toLowerCase().contains(newText.toLowerCase())) {
                    filteredListWork.add(singleNoteWork);
                }
            }
            adapterWork.filterListWork(filteredListWork);
        }
    }
}