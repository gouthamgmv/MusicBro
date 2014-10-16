package com.AAAG.MusicBro;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class MoodActivity extends ListActivity {
    public static String Mood;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getmood);
        final ArrayList<HashMap<String, String>> moods = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mood1 = new HashMap<String, String>();
        HashMap<String, String> mood2 = new HashMap<String, String>();
        HashMap<String, String> mood3 = new HashMap<String, String>();
        HashMap<String, String> mood4 = new HashMap<String, String>();
        HashMap<String, String> mood5 = new HashMap<String, String>();
        mood1.put("moodtype", "Happy"); mood1.put("genre", "Pop");moods.add(mood1);
        mood2.put("moodtype", "Sad"); mood2.put("genre", "Slow");moods.add(mood2);
        mood3.put("moodtype", "Angry"); mood3.put("genre", "Rock");moods.add(mood3);
        mood4.put("moodtype", "Romantic"); mood4.put("genre", "R&B");moods.add(mood4);
        mood5.put("moodtype", "Indifferent"); mood5.put("genre", "Anything");moods.add(mood5);
        ListAdapter adapter = new SimpleAdapter(this, moods,
                R.layout.playlist_item, new String[] { "moodtype" }, new int[] {
                R.id.songTitle });
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Mood = moods.get(position).get("moodtype");
                startActivity(new Intent(MoodActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

}

