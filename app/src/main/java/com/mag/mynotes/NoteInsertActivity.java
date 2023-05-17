package com.mag.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mag.mynotes.DB.DBActivity;
import java.time.LocalDate;

public class NoteInsertActivity extends DBActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        editTextTitle = findViewById(R.id.editTextTitleEdit);
        editTextContent = findViewById(R.id.editTextContentEdit);
        btnSave = findViewById(R.id.btnEdit);

        btnSave.setOnClickListener(view -> {

            if (checkIfEditTextContolsAreEmpty(editTextTitle, editTextContent)){
                return;
            }

            try {
                execSQL(
                        "INSERT INTO Notes (Title, Content, Date) VALUES (?, ?, ?);",
                        new Object[]{
                                editTextTitle.getText().toString(),
                                editTextContent.getText().toString(),
                                "Created on: " + LocalDate.now().toString()
                        },
                        () -> Toast.makeText(
                                getApplicationContext(),
                                "Insert ok",
                                Toast.LENGTH_LONG
                        ).show()
                );

                Intent intent = new Intent(NoteInsertActivity.this, MainActivity.class);
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(
                        getApplicationContext(),
                        "Error: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}