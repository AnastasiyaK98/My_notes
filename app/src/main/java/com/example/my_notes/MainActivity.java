package com.example.my_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Раздел "Личное"
    RelativeLayout personal;
    //Раздел "Работа"
    RelativeLayout work;
    //Текст цитаты
    TextView quoteText;
    //Автор цитаты
    TextView quoteAuthor;
    //Раздел
    String selected_folder = "";
   // Метод onCreate() вызывается при создании или перезапуска активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Получение ссылки на View элементов (связываем графический элемент через его id с программным)
        personal = findViewById(R.id.personal);
        work = findViewById(R.id.work);
        quoteText = findViewById(R.id.quoteText);
        quoteAuthor = findViewById(R.id.quoteAuthor);


        //События при нажатии на кнопки выбора раздела
        //Раздел "Личное
        personal.setOnClickListener(new View.OnClickListener() {
            // Обработка нажатия
            @Override
            public void onClick(View view) {
                selected_folder = "Личное";
                //Установка фонового изображения кнопок при нажатии на одну из них
                personal.setBackgroundResource(R.drawable.rounding_color);
                work.setBackgroundResource(R.drawable.rounding);
               //Переход из MainActivity в NotesListActivity
                Intent intent = new Intent(MainActivity.this, NotesListActivity.class);
                //Передача значения - строка с названием выбранного раздела
                intent.putExtra("selected_folder", selected_folder);
                //Запуск нового Activity
                startActivity(intent);
                //Завершение работы данной активности
                finish();
            }
        });
        //Раздел "Работа"
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_folder = "Работа";
                personal.setBackgroundResource(R.drawable.rounding);
                work.setBackgroundResource(R.drawable.rounding_color);
                //Переход из MainActivity в NotesListActivity
                Intent intent = new Intent(MainActivity.this, NotesListActivity.class);
                //Передача значения - строка с названием выбранного раздела
                intent.putExtra("selected_folder", selected_folder);
                startActivity(intent);
                finish();
            }
        });
    }
}