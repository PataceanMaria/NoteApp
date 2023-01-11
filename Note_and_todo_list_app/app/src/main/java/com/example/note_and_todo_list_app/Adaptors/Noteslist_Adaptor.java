package com.example.note_and_todo_list_app.Adaptors;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_and_todo_list_app.Model.Notes;
import com.example.note_and_todo_list_app.NotesClickListener;
import com.example.note_and_todo_list_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Noteslist_Adaptor extends RecyclerView.Adapter<NotesViewHolder>{
    Context context;
    List<Notes> list;
    NotesClickListener listener;


    public Noteslist_Adaptor(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,parent,false));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
            holder.textView_title.setText(list.get(position).getTitle());
            holder.textView_title.setSelected(true);
            holder.textView_notes.setText(list.get(position).getNotes());
            holder.textView_date.setText(list.get(position).getDate());
            holder.textView_date.setSelected(true);

            if(list.get(position).isPinned())
            {
                holder.imageView_pin.setImageResource(R.drawable.ic_pin);
            }
            else
            {
                holder.imageView_pin.setImageResource(0);
            }

            holder.notes_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(list.get(holder.getAdapterPosition()));
                }
            });

            holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(list.get(holder.getAdapterPosition()),holder.notes_container);
                    return true;
                }
            });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void filterList(List<Notes> filteredList)
    {
        list=filteredList;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder
{

    CardView notes_container;
    TextView textView_title,textView_notes,textView_date;
    ImageView imageView_pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container=itemView.findViewById(R.id.notes_container);
        textView_title=itemView.findViewById(R.id.textView_title);
        textView_notes=itemView.findViewById(R.id.textView_notes);
        textView_date=itemView.findViewById(R.id.textView_date);
        imageView_pin=itemView.findViewById(R.id.imageView_pin);
    }
}
