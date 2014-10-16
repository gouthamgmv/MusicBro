package com.AAAG.MusicBro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.AAAG.MusicBro.Player.*;

import java.util.ArrayList;

public class HomeActivity extends Activity {
    public static int Option;
    public static ArrayList<Song> Songs = new ArrayList<Song>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        Button albumbutton = (Button)findViewById(R.id.Album);
        Button moodbutton = (Button)findViewById(R.id.Mood);
        Button noplayedbutton = (Button)findViewById(R.id.NoPlayed);
        Button ratingbutton = (Button)findViewById(R.id.Rating);
        Button playlistbutton = (Button)findViewById(R.id.NewSongs);
        Button songsbutton = (Button)findViewById(R.id.Songs);
        Button artistsbutton = (Button)findViewById(R.id.Artist);
        Button intelligentbutton = (Button)findViewById(R.id.Intelligent);
        albumbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 1;
                Intent in = new Intent(HomeActivity.this, PlayListActivity.class);
                startActivity(in);
            }
        });
        moodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 2;
                startActivity(new Intent(HomeActivity.this, SongList.class));
            }
        });
        noplayedbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Option = 3;
                startActivity(new Intent(HomeActivity.this, CountActivity.class));
            }
        });
        ratingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 4;
                startActivity(new Intent(HomeActivity.this, RatingActivity.class));
            }
        });
        playlistbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 5;
                startActivity(new Intent(HomeActivity.this, NewActivity.class));
            }
        });
        songsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 6;
                startActivity(new Intent(HomeActivity.this, SongsActivity.class));
            }
        });
        artistsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 7;
                startActivity(new Intent(HomeActivity.this, ArtistActivity.class));
            }
        });
        intelligentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option = 8;
                startActivity(new Intent(HomeActivity.this, WeatherActivity.class));
            }
        });

    }

    }


