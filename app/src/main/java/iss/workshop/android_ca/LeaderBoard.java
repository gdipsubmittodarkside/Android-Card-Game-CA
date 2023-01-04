package iss.workshop.android_ca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LeaderBoard extends AppCompatActivity {

    ArrayList<String> names;
    ArrayList<Integer>scores;
    Button backButt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        Intent intent = getIntent();
        names = intent.getStringArrayListExtra("names");
        scores = intent.getIntegerArrayListExtra("scores");

        ListView listView = findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(new ListAdapter(this, names, scores));
        }

        backButt = findViewById(R.id.mBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startHomePage();
            }
        });



    }

    private void startHomePage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}
