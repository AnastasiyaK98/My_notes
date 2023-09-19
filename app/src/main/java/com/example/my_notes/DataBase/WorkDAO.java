package com.example.my_notes.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.my_notes.Models.WorkNotes;

import java.util.List;

//@Dao - аннотация для объявления интерфейса, который будет заниматься манипулированием данными базы данных
@Dao
public interface WorkDAO {
    //@Query - аннотация, которая позволяет выполнить SQL-запрос в методах DAO-интерфейса.
    // Извлечь все записи в отсортированном виде в порядке убывания по закреплению заметки и её идентификатору
    @Query( "SELECT * FROM WorkBD ORDER BY pinned DESC, uid DESC")
    List<WorkNotes> getAll();

    //Обновление названия и текста заметки при изменении
    @Query("UPDATE WorkBD SET title=:title, text =:text WHERE uid =:uid")
    void update(int uid, String title, String text);

    //@Insert - аннотация, которая позволяет выполнить вставку в таблицу базы данных
    //onConflict: стратегия обработки при возникновении конфликта при вставке
    //OnConflictStrategy.REPLACE: стратегия конфликта заключается в замене старых данных при продолжении транзакции
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkNotes notes);

    //@Delete - аннотация, которая позволяет выполнить удаление некоторых строк в таблице базы данных.
    @Delete
    void delete(WorkNotes note);
    //Обновление при откреплении заметки
    @Query("UPDATE WorkBD SET  pinned =:pin WHERE uid =:uid")
    void pin (int uid, boolean pin);
}
