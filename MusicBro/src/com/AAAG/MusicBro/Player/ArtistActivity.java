package com.AAAG.MusicBro.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.AAAG.MusicBro.HomeActivity;
import com.AAAG.MusicBro.R;

public class ArtistActivity extends ListActivity {
    // Songs list
    public static String Artist;
    public static ArrayList<String> songsListData = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);





        // looping through playlist
        for (int i = 0; i < HomeActivity.Songs.size(); i++) {
            // creating new HashMap
            String album = HomeActivity.Songs.get(i).artist;
            if(!songsListData.contains(album))
                // adding HashList to ArrayList
                songsListData.add(album);
            Collections.sort(songsListData);
        }

        // Adding menuItems to ListView
        ListAdapter adapter = new ArrayAdapter<String>(this,R.layout.playlist_item,R.id.songTitle,songsListData);

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();
        // listening to single listitem click
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Artist = parent.getItemAtPosition(position).toString();
                Intent in = new Intent(ArtistActivity.this, SongList.class);
                startActivity(in);
                finish();
            }
        });

    }


}

