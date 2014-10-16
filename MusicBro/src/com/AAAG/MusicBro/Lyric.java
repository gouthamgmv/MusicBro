package com.AAAG.MusicBro;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.AAAG.MusicBro.Player.AndroidBuildingMusicPlayerActivity;

public class Lyric extends Activity {
    private TextView myText = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout lView = new LinearLayout(this);
        myText = new TextView(this);
        myText.setTextSize(20);
        int i = AndroidBuildingMusicPlayerActivity.SongBeingPlayed;
                myText.setText(HomeActivity.Songs.get(i).name+" by "+HomeActivity.Songs.get(i).artist+", "+HomeActivity.Songs.get(i).emotion+" song.\n\nLyrics:\n"+HomeActivity.Songs.get(i).lyric);
        myText.setMovementMethod(new ScrollingMovementMethod());
        lView.addView(myText);
        setContentView(lView);
    }
}