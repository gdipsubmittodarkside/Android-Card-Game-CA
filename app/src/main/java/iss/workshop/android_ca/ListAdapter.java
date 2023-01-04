package iss.workshop.android_ca;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ListAdapter extends ArrayAdapter<Object> {

    private final Context context;
    private ArrayList<Integer> scores ;
    protected ArrayList<String> names;
    int rankNum;

    public ListAdapter(Context context, ArrayList<String> names, ArrayList<Integer> scores) {
        super(context, R.layout.row);
        this.context = context;
        this.names = names;
        this.scores = scores;
        if(names != null){
            addAll(new Object[names.size()]);
        }
    }


    public View getView(int pos, View view, @NonNull ViewGroup parent){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row,parent,false);
        }
        if(names != null){
            TextView rank = view.findViewById(R.id.rankNumber);
            rank.setText(String.valueOf(pos+1));

            TextView name = view.findViewById(R.id.rankPlayerName);
            name.setText(names.get(pos));

            TextView score = view.findViewById(R.id.rankPlayerScore);
            score.setText(String.valueOf(scores.get(pos)));
        }
        return view;
    }
}
