package com.AAAG.MusicBro;


import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class GetAge extends ListActivity {
    public static int agegroup = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getage);
        final ArrayList<HashMap<String, String>> ages = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> age1 = new HashMap<String, String>();
        HashMap<String, String> age2 = new HashMap<String, String>();
        HashMap<String, String> age3 = new HashMap<String, String>();
        HashMap<String, String> age4 = new HashMap<String, String>();
        HashMap<String, String> age5 = new HashMap<String, String>();
        HashMap<String, String> age6 = new HashMap<String, String>();
        age1.put("agetype", "Kid"); age1.put("range", "<=12");ages.add(age1);
        age2.put("agetype", "Teen"); age2.put("range", "13-19");ages.add(age2);
        age3.put("agetype", "Legal"); age3.put("range", "20-30");ages.add(age3);
        age4.put("agetype", "Adult"); age4.put("range", "31-45");ages.add(age4);
        age5.put("agetype", "Middle Aged"); age5.put("range", "46-60");ages.add(age5);
        age6.put("agetype", "Senior Citizen"); age6.put("range", ">=61");ages.add(age6);
        ListAdapter adapter = new SimpleAdapter(this, ages,
                R.layout.playlist_item, new String[] { "agetype" }, new int[] {
                R.id.songTitle });
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                agegroup = position+1;
                SharedPreferences settings = getSharedPreferences("MyPrefs",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("AgeGroup", agegroup);
                editor.commit();

                startActivity(new Intent(GetAge.this, MoodActivity.class));
                finish();
            }
        });
    }

}

