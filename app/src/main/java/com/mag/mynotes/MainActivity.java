package com.mag.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
            Intent intent = new Intent(MainActivity.this, NoteInsertActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView selectedTextId = view.findViewById(R.id.textViewId);
                TextView selectedTextTitle = view.findViewById(R.id.textViewTitle);
                TextView selectedTextContent = view.findViewById(R.id.textViewContent);

                Bundle bundle = new Bundle();
                bundle.putString("Id", selectedTextId.getText().toString());
                bundle.putString("Title", selectedTextTitle.getText().toString());
                bundle.putString("Content", selectedTextContent.getText().toString());

                Intent intent = new Intent(MainActivity.this,NoteEditActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            FillListView();
        } catch (Exception e) {
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

