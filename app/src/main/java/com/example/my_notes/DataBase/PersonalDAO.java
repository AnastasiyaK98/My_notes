package com.example.my_notes.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.my_notes.Models.PersonalNotes;

import java.util.List;

//@Dao - аннотация для объявления интерфейса, который будет заниматься манипулированием данными базы данных
@Dao
public interface PersonalDAO {

    //@Query - аннотация, которая позволяет выполнить SQL-запрос в методах DAO-интерфейса.
    // Извлечь все записи в отсортированном виде в порядке убывания по закреплению заметки и её идентификатору
    @Query( "SELECT * FROM PersonalBD ORDER BY pinned DESC, uid DESC")
    List<PersonalNotes> getAll();

    //Обновление названия и текста заметки при изменении
    @Query("UPDATE PersonalBD SET title=:title, text =:text WHERE uid =:uid")
    void update(int uid, String title, String text);

    //@Insert - аннотация, которая позволяет выполнить вставку в таблицу базы данных
    //onConflict: стратегия обработки при возникновении конфликта при вставке
    //OnConflictStrategy.REPLACE: стратегия конфликта заключается в замене старых данных при продолжении транзакции
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PersonalNotes notes);

    //@Delete - аннотация, которая позволяет выполнить удаление некоторых строк в таблице базы данных.
    @Delete
    void delete(PersonalNotes note);
    //Обновление при закреплении/откреплении заметки
    @Query("UPDATE PersonalBD SET  pinned =:pin WHERE uid =:uid")
    void pin (int uid, boolean pin);
}
