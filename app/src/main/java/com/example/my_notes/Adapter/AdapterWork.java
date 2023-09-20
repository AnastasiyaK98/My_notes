package com.example.my_notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.my_notes.Models.WorkNotes;
import com.example.my_notes.R;
import com.example.my_notes.WorkNotesClickListener;

import java.util.List;


//Наследование адаптера от RecyclerView.Adapter
//Указание собственного ViewHolder, который предоставит доступ к View-компонентам
public class AdapterWork extends RecyclerView.Adapter<WorkNotesViewHolder>{

    //Активность
    Context context;
    //Список заметок
    List<WorkNotes> list;
    //Интерфейс нажатия на заметку
    WorkNotesClickListener listener;


    //Конструктор с параметрами
    public AdapterWork(Context context, List<WorkNotes> list, WorkNotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    //Метод вызывается для создания объекта ViewHolder
    public WorkNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkNotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    //Метод отвечает за связь java объекта и View
    public void onBindViewHolder(@NonNull WorkNotesViewHolder holder, int position) {
        holder.text_title.setText(list.get(position).getTitle());
        //Прокрутка текста названия заметки
        holder.text_title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getText());

        holder.text_date_notes.setText(list.get(position).getDate());
        //Прокрутка даты заметки
        holder.text_date_notes.setSelected(true);

        //Если заметка закреплена - отобразить иконку закрепления
        if(list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.icon_pin);
        }
        //Иначе ничего не отображать
        else {
            holder.imageView_pin.setImageResource(0);
        }

        //Короткое нажатие на заметку
        holder.notes_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Возвращает обновленную позицию для текущего объекта
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        //Долгое нажатие на заметку
        holder.notes_block.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_block);
                return true;
            }
        });
    }

    @Override
    //Сообщает количество элементов в списке
    public int getItemCount() {
        return list.size();
    }

    //Для сообщения адаптеру о том, что список элементов изменился при поиске заметки
    // и ему нужно  перерисовать элементы на экране.
    public void filterListWork (List<WorkNotes> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}

// Предоставляет прямую ссылку на каждый View-компонент
// Используется для кэширования View-компонентов и последующего быстрого доступа к ним
class WorkNotesViewHolder extends RecyclerView.ViewHolder {

    //Блок заметки
    CardView notes_block;
    //Название, текст и Дата Заметки
    TextView text_title, textView_notes, text_date_notes;
    //Иконка закрепления заметки
    ImageView imageView_pin;

    //Конструктор принимает на вход View-компонент строки
    // и ищет все дочерние компоненты
    public WorkNotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_block = itemView.findViewById(R.id.notes_block);
        text_title = itemView.findViewById(R.id.text_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        text_date_notes = itemView.findViewById(R.id.text_date_notes);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}