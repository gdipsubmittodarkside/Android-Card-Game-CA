package iss.workshop.android_ca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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

    public static void saveLeaderBoard(@NonNull HashMap<String,Integer> scoreList){

        HashMap<String, Integer> sortedHM = scoreList.entrySet().stream()
                .sorted((x, y)-> y.getValue().compareTo(x.getValue())).collect(Collectors
                        .toMap(Map.Entry::getKey,Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream("scoreList.txt");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(sortedHM);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String,Integer> loadLeaderBoard(){
        HashMap<String,Integer> scoreList = new HashMap<>();
        try
        {
            FileInputStream fileInputStream  = new FileInputStream("scoreList.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            scoreList = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            return scoreList;
        }
        catch(ClassNotFoundException | IOException | ClassCastException e) {
            e.printStackTrace();
        }
        return scoreList;
    }


}
