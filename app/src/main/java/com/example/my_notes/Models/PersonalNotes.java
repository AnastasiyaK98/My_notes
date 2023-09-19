package com.example.my_notes.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//@Entity - аннотация для объявления сущности базы данных
@Entity(tableName = "PersonalBD")
public class PersonalNotes implements Serializable {
    //@PrimaryKey - аннотация для объявления первичного ключа сущности
    //Идентификатор заметки
    @PrimaryKey(autoGenerate = true)
    int uid;
    //@ColumnInfo - аннотация для настроек конкретного столбца сущности
    //Название заметки
    @ColumnInfo(name = "title")
    String title;
    //Текст заметки
    @ColumnInfo(name = "text")
    String text;
    //Дата создания заметки
    @ColumnInfo(name = "Date")
    String Date;
    //Состояние заметки (закреплена/откреплена)
    @ColumnInfo(name = "pinned")
    boolean pinned=false;

    //Конструктор без параметров
    public PersonalNotes() {
    }

    //Геттеры и сеттеры для доступа к полям таблицы
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
