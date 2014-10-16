package com.AAAG.MusicBro;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.content.Intent;
import android.provider.MediaStore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.net.URLEncoder;


public class SplashActivity extends Activity  {
    String[] STAR = { "*" };
    int totalSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);
        ListAllSongs();

    int delay = 10000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(SplashActivity.this, GetAge.class);
                Intent intent2 = new Intent(SplashActivity.this, MoodActivity.class);
                SharedPreferences settings = getSharedPreferences("MyPrefs",0);
                GetAge.agegroup=settings.getInt("AgeGroup",0);
                // System.out.println("TEST!");
                 startActivity(intent2);
            }
        }, delay);
    }

    public void ListAllSongs()
    {
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(allsongsuri, STAR, selection, null, null);

        totalSongs = cursor.getCount();
       // System.out.println("Count 1 is: "+totalSongs);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.name = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                    song.path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));

                    song.album = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));

                    song.artist = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));

                    HomeActivity.Songs.add(song);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //System.out.println("Count 2 is: "+HomeActivity.Songs.size());
        }
    }

}
