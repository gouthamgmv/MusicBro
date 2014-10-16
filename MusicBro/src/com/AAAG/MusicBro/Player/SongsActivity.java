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

public class SongsActivity extends ListActivity {
    // Songs list
    public static String Album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        ArrayList<String> songsListData = new ArrayList<String>();
        for (int i = 0; i < HomeActivity.Songs.size(); i++) {
            String song = HomeActivity.Songs.get(i).name;
            if(!songsListData.contains(song))
                songsListData.add(song);
        }
        Intent in = new Intent(SongsActivity.this, SongList.class);
        startActivity(in);
        finish();
    }
}
