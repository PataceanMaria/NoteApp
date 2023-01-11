package com.example.note_and_todo_list_app.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.note_and_todo_list_app.Model.Notes;

@Database(entities = Notes.class,version = 1,exportSchema = false)
public abstract class RoomDB  extends RoomDatabase {

    private static RoomDB database;
    private static String  DATABASE_NAME="NoteApp";


    public synchronized static RoomDB getInstance(Context context)
    {
        if(database==null)
        {
            database= Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract  MainDAO mainDAO();

}
