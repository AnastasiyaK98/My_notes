package com.example.my_notes.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.my_notes.Models.PersonalNotes;
import com.example.my_notes.Models.WorkNotes;

//@Database - аннотация для объявления базы данных
@Database(entities = {PersonalNotes.class, WorkNotes.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase database;
    private static String DataBase_Name = "AppMyNotes";
    //Создание и возврат Базы Данных (доступно из других ативностей)
    public synchronized static AppDataBase getInstance(Context context) {
        //Если база данных не создана - то она создаётся
        //allowMainThreadQueries() - разрешает запросы к бд в главном потоке
        //fallbackToDestructiveMigration() - разрешено деструктивно воссоздавать таблицы базы данных из определенных версий
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DataBase_Name)
                    .allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    //Доступ к манипулированию данными
    public abstract PersonalDAO personalDAO();
    public abstract WorkDAO workDAO();
}
