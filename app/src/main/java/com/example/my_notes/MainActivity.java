package com.example.my_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

        //API для получения цитаты и автора
        String url = "https://api.forismatic.com/api/1.0/?method=getQuote&format=json";
        //Метод execute содержится в родительском классе AsyncTask
        //Передача url классу GETURL наследуемого от AsyncTask
        new GETURL().execute(url);
    }

    //Работа с API в фоновом потоке
    private class GETURL extends AsyncTask<String, String, String> {
        //Установка начального текста
        protected void onPreExecute(){
            super.onPreExecute();
            quoteText.setText("Загрузка цитаты...");
        }
        // Получение информации по URL
        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection myConnection = null;
            BufferedReader reader = null;

            try {
                //Создание URL
                URL url = new URL(strings[0]);
                //Открытие HTTP-соединения
                myConnection = (HttpsURLConnection) url.openConnection();
                myConnection.connect();
                InputStream stream;
                //Если запрос был успешно обработан сервером, то будет возвращен код 200
                if (myConnection.getResponseCode() == 200) {
                    //Чтение потока данных
                    stream = myConnection.getInputStream();
                }
                else {
                    stream = myConnection.getErrorStream();
                }

                //Чтение в формате строки
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                //Чтение информации из URL-адреса по одной линии
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            } catch(MalformedURLException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            } finally{
                if (myConnection != null) {
                    //Закрытие соединения
                    myConnection.disconnect();
                }
                try {
                    if (reader != null)
                        //Закрытие считывания
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        //Вывод полученной цитаты на экран
        protected void onPostExecute (String result){
            super.onPostExecute(result);
            try {
                //Разбор JSON ответа
                JSONObject jsonObject = new JSONObject(result);
                quoteText.setText("" + jsonObject.getString("quoteText"));
                quoteAuthor.setText("" + jsonObject.getString("quoteAuthor"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}