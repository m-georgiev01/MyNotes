package com.mag.mynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mag.mynotes.Model.Note;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Note> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Note dataModel = getItem(position);

        View currentItemView = convertView;

        if (currentItemView == null){
            currentItemView = LayoutInflater
                    .from(getContext())
                    .inflate(
                            R.layout.activity_list_view_fragment,
                            parent,
                            false);
        }

        TextView textViewId = currentItemView.findViewById(R.id.textViewId);
        TextView textViewTitle = currentItemView.findViewById(R.id.textViewTitle);
        TextView textViewContent = currentItemView.findViewById(R.id.textViewContent);
        TextView textViewDate = currentItemView.findViewById(R.id.textViewDate);

        textViewId.setText(dataModel.Id);
        textViewTitle.setText(dataModel.Title);
        textViewContent.setText(dataModel.Content);
        textViewDate.setText(dataModel.Date);

        return currentItemView;
    }
}
