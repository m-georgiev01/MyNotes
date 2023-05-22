package com.mag.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mag.mynotes.DB.DBActivity;

import org.json.JSONException;

import java.io.IOException;

public class NoteEditActivity extends DBActivity {

    private EditText editTitle;
    private EditText editContent;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnShare;
    private Spinner spinnerTranslate;
    private Button btnTranslate;
    private Button btnGet;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        editTitle = findViewById(R.id.editTextTitleEdit);
        editContent = findViewById(R.id.editTextContentEdit);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnShare = findViewById(R.id.btnShare);
        spinnerTranslate = findViewById(R.id.spinnerTranslate);
        btnTranslate = findViewById(R.id.btnTranslate);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString("Id");
            editTitle.setText(bundle.getString("Title"));
            editContent.setText(bundle.getString("Content"));
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.translate_lang,
                android.R.layout.simple_spinner_item
        );
        spinnerTranslate.setAdapter(adapter);

        btnEdit.setOnClickListener(view -> {
            if (checkIfEditTextContolsAreEmpty(editTitle, editContent)){
                Toast.makeText(
                        getApplicationContext(),
                        "There is an empty field!",
                        Toast.LENGTH_LONG
                ).show();
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
                        }
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
                        new Object[] { id }
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

        btnShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            StringBuilder sb = new StringBuilder();
            sb.append("Title: ");
            sb.append(System.lineSeparator());
            sb.append(editTitle.getText().toString());
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
            sb.append("Content: ");
            sb.append(System.lineSeparator());
            sb.append(editContent.getText().toString());

            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());

            startActivity(Intent.createChooser(intent, null));
        });

        btnTranslate.setOnClickListener(view -> {
            String targetLang = getTargetLang(spinnerTranslate.getSelectedItem().toString());

            String noteContent = editContent.getText().toString();

            Thread t = new Thread(() -> {
                try {
                    String sourceLang = HTTPCommunication.postGetSourceLanguage(noteContent);

                    String translatedText = HTTPCommunication.postTranslateText(
                            sourceLang,
                            targetLang,
                            noteContent,
                            error -> runOnUiThread(() -> {
                                Toast.makeText(
                                        getApplicationContext(),
                                        error,
                                        Toast.LENGTH_LONG
                                ).show();
                            })
                    );

                    runOnUiThread(() ->{
                        editContent.setText(translatedText);
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Toast.makeText(
                                getApplicationContext(),
                                "Error: " + e.getLocalizedMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });
                }
            });

            t.start();
        });
    }

    private String getTargetLang(String selectedLang) {
        switch (selectedLang){
            case "French":
            case "Italian":
            case "Russian":
                return selectedLang.substring(0, 2).toLowerCase() +
                        "-" +
                        selectedLang.substring(0, 2).toUpperCase();

            case "German":
                return "de-DE";

            case "Spanish":
                return "es-ES";

            case "English":
                return "en-US";

            case "Bulgarian":
                return "bg-Bg";

            case "Chinese":
                return "zh-CN";

            case "Turkish":
                return "tr-Tr";

            default:
                return "";
        }
    }
}