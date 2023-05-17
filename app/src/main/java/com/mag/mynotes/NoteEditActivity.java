package com.mag.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mag.mynotes.DB.DBActivity;

public class NoteEditActivity extends DBActivity {

    private EditText editTitle;
    private EditText editContent;
    private Button btnEdit;
    private Button btnDelete;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        editTitle = findViewById(R.id.editTextTitleEdit);
        editContent = findViewById(R.id.editTextContentEdit);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            id = bundle.getString("Id");
            editTitle.setText(bundle.getString("Title"));
            editContent.setText(bundle.getString("Content"));
        }

        btnEdit.setOnClickListener(view -> {

            if (checkIfEditTextContolsAreEmpty(editTitle, editContent)){
                return;
            }

            try{
                execSQL(
                        "UPDATE Notes SET " +
                                "Title = ?, " +
                                "Content = ?" +
                                "WHERE Id = ? ",
                        new Object[]{
                                     editTitle.getText().toString(),
                                     editContent.getText().toString(),
                                     id
                        },
                        ()-> Toast.makeText(
                                getApplicationContext(),
                                "UPDATE SUCCESSFUL",
                                Toast.LENGTH_LONG
                        ).show()
                );
            }catch (Exception e){
                Toast.makeText(
                        getApplicationContext(),
                        "Error: "+e.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }finally {
                Intent intent = new Intent(NoteEditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(view -> {
            try{
                execSQL(
                        "DELETE FROM Notes WHERE Id = ?",
                        new Object[] { id },
                        ()-> Toast.makeText(
                                getApplicationContext(),
                                "DELETE SUCCESSFUL",
                                Toast.LENGTH_LONG
                        ).show()
                );
            }catch (Exception e){
                Toast.makeText(
                        getApplicationContext(),
                        "Error: "+e.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }finally {
                Intent intent = new Intent(NoteEditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}