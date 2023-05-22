package com.mag.mynotes.DB;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mag.mynotes.Model.Note;

public class DBActivity extends AppCompatActivity {
    protected void execSQL(
            String sql,
            Object[] args) throws Exception {

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() +
                        "contacts.db",
                null
        );

        if (args != null) {
            db.execSQL(sql, args);
        } else {
            db.execSQL(sql);
        }

        db.close();
    }

    protected void initDb() throws Exception {
        execSQL(
                "CREATE TABLE IF NOT EXISTS Notes( " +
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Title TEXT NOT NULL, " +
                        "Content TEXT NOT NULL, " +
                        "Date TEXT NOT NULL" +
                        "); ",
                null
        );
    }

    @SuppressLint("Range")
    protected void selectSQL(String SelectQ,
                             String[] args,
                             OnSelectSuccess success)
            throws Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() +
                        "contacts.db",
                null
        );

        Cursor cursor = db.rawQuery(SelectQ, args);

        while(cursor.moveToNext()){
            Note note = new Note();

            note.Id = cursor.getString(cursor.getColumnIndex("Id"));
            note.Title = cursor.getString(cursor.getColumnIndex("Title"));
            note.Content = cursor.getString(cursor.getColumnIndex("Content"));
            note.Date = cursor.getString(cursor.getColumnIndex("Date"));

            success.OnElementSelected(note);
        }

        cursor.close();
        db.close();
    }

    protected boolean checkIfEditTextContolsAreEmpty(EditText... editTexts){
        for (EditText et : editTexts ) {
            if (et.getText().toString().equals("")){
                return true;
            }
        }

        return false;
    }
}
