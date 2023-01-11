package com.example.note_and_todo_list_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import android.widget.Toast;

import com.example.note_and_todo_list_app.Adaptors.Noteslist_Adaptor;
import com.example.note_and_todo_list_app.Database.RoomDB;
import com.example.note_and_todo_list_app.Model.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    Noteslist_Adaptor noteslist_adaptor;
    List<Notes> notes=new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;
    Notes selecteNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_home);
        fab_add=findViewById(R.id.fab_add);

        database = RoomDB.getInstance(this);
        notes=database.mainDAO().getAll();
        searchView_home=findViewById(R.id.searchView_home);
        updateRecycler(notes);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,NotesTakerActivity.class);
                startActivityForResult(intent,101);

            }
        });


        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filteredList=new ArrayList<>();
        for(Notes singleNote :notes)
        {
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())|| singleNote.getNotes().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(singleNote);

            }
        }
        noteslist_adaptor.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Notes new_notes=(Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteslist_adaptor.notifyDataSetChanged();
            }
        }
        else if(requestCode==102)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteslist_adaptor.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
         noteslist_adaptor=new Noteslist_Adaptor(MainActivity.this,notes,notesClickListener);
         recyclerView.setAdapter(noteslist_adaptor);
    }
    private final NotesClickListener notesClickListener=new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent= new Intent(MainActivity.this,NotesTakerActivity.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
                selecteNote=new Notes();
                selecteNote=notes;
                showPopup(cardView);

        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu=new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId())
        {

            case R.id.delete:
                database.mainDAO().delete(selecteNote);
                notes.remove(selecteNote);
                noteslist_adaptor.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Note deleted",Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}
