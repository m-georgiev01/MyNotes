package com.mag.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mag.mynotes.DB.DBActivity;
import com.mag.mynotes.Model.Note;

import java.util.ArrayList;

public class MainActivity extends DBActivity {

    private ListView listView;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, InsertActivity.class);
            startActivity(i);
        });

        try {
            initDb();
            FillListView();
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

        private void FillListView() throws Exception{
        final ArrayList<Note> listResult = new ArrayList<>();

        selectSQL("SELECT * FROM Notes ORDER BY ID",
                null,
                (note) -> listResult.add(note));

        NoteAdapter elementAdapter = new NoteAdapter(
                getApplicationContext(),
                R.layout.activity_list_view_fragment,
                R.id.textViewId,
                listResult);

        listView.clearChoices();
        listView.setAdapter(elementAdapter);
    }
}

