package com.AAAG.MusicBro.Player;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.*;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.AAAG.MusicBro.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class SongList extends ListActivity {
    // Songs list
    public static ArrayList<Song> songsList = new ArrayList<Song>();
    public static ArrayList<String> songsListData = new ArrayList<String>();
    public static int totalSongsPlayed;
    public String weather, weather1;
    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        ArrayList<String> songsListData = new ArrayList<String>();
        //System.out.println("Song list size is: "+HomeActivity.Songs.s
        for(int i=0;i<HomeActivity.Songs.size();i++)
        {
            if(songsList.indexOf(HomeActivity.Songs.get(i))==-1)
                songsList.add(HomeActivity.Songs.get(i));
        }
        SharedPreferences settings = getSharedPreferences("MyPrefs",0);

        SharedPreferences.Editor editor = settings.edit();
        //System.out.println("Song list 2 size is: "+songsList.size());
        for(int i=0;i<songsList.size();i++)
        {
            songsList.get(i).count = settings.getInt(songsList.get(i).name,0);
            songsList.get(i).rating = settings.getInt(songsList.get(i).name+"r",0);
        }
        if(HomeActivity.Option==1)sort("album");
        if(HomeActivity.Option==7)sort("artist");
        if(HomeActivity.Option==3)
        {
            for(int i=0;i<songsList.size()-1;i++)
            {
                for (int j=0;j<songsList.size()-i-1;j++)
                {
                    if(songsList.get(j).count<songsList.get(j+1).count){
                        Collections.swap(songsList,j,j+1);
                    }
                }
            }
        }
        if(HomeActivity.Option==4)
        {
            for(int i=0;i<songsList.size()-1;i++)
            {
                for (int j=0;j<songsList.size()-i-1;j++)
                {
                    if(songsList.get(j).rating<songsList.get(j+1).rating){
                        Collections.swap(songsList,j,j+1);
                    }
                }
            }
        }
        double lat=0,lng=0;
        gps = new GPSTracker(SongList.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            lat = gps.getLatitude();
            lng = gps.getLongitude();

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        boolean testnet=isNetworkAvailable();
        if(testnet==true)
        {
        try
        {
        DefaultHttpClient client1 = new DefaultHttpClient();
        HttpGet get1 = new HttpGet("http://api.openweathermap.org/data/2.5/weather?lat="+ URLEncoder.encode(Double.toString(lat), "UTF-8")+"&lon="+URLEncoder.encode(Double.toString(lng), "UTF-8")+"&mode=xml");
        HttpResponse responseGet1 = client1.execute(get1);
        HttpEntity resEntityGet1 = responseGet1.getEntity();
        if (resEntityGet1 != null) {
            weather = EntityUtils.toString(resEntityGet1, HTTP.UTF_8);
            String temp = weather.substring(weather.indexOf("<weather number=\"") + 17);
            weather1 = temp.substring(0, temp.indexOf("\" value"));
            //System.out.println(weather1);
            settings = getSharedPreferences("MyPrefs",0);
             editor = settings.edit();
            editor.putString("lastWeather", weather1);
            editor.commit();
        }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        }

        settings = getSharedPreferences("MyPrefs",0);
        weather1=settings.getString("lastWeather","801");

        int weatherInt=Integer.parseInt(weather1);
        if(weatherInt>=200 && weatherInt<=232)
        {
            weather1="Thunderstorm"; }
        else if(weatherInt>=300 && weatherInt<=321) {
            weather1="Drizzle";      }
        else if(weatherInt>=500 && weatherInt<=531)   {
            weather1="Rain";           }
        else if(weatherInt>=600 && weatherInt<=622)     {
            weather1="Snow";              }
        else if(weatherInt>=700 && weatherInt<=781)       {
            weather1="Atmosphere";          }
        else if(weatherInt>=800 && weatherInt<=804)         {
            weather1="Sunny";                 }
        else                                                  {
            weather1="Extreme";              }



        for (int i = 0; i < songsList.size(); i++) {
            settings = getSharedPreferences("MyPrefs",0);
            HomeActivity.Songs.get(i).extremecount=settings.getInt(HomeActivity.Songs.get(i).name+"extreme",0);
            HomeActivity.Songs.get(i).drizzlecount=settings.getInt(HomeActivity.Songs.get(i).name+"drizzle",0);
            HomeActivity.Songs.get(i).raincount=settings.getInt(HomeActivity.Songs.get(i).name+"rain",0);
            HomeActivity.Songs.get(i).snowcount=settings.getInt(HomeActivity.Songs.get(i).name+"snow",0);
            HomeActivity.Songs.get(i).atmospherecount=settings.getInt(HomeActivity.Songs.get(i).name+"atmosphere",0);
            HomeActivity.Songs.get(i).sunnycount=settings.getInt(HomeActivity.Songs.get(i).name+"sunny",0);
            HomeActivity.Songs.get(i).thundercount=settings.getInt(HomeActivity.Songs.get(i).name+"thunder",0);
            if(HomeActivity.Option==1)if(PlayListActivity.Album.equalsIgnoreCase(songsList.get(i).album)&&!songsListData.contains(songsList.get(i).name))songsListData.add(songsList.get(i).name);
            if(HomeActivity.Option==7)if(ArtistActivity.Artist.equalsIgnoreCase(songsList.get(i).artist)&&!songsListData.contains(songsList.get(i).name))songsListData.add(songsList.get(i).name);
            if(HomeActivity.Option==2&&!songsListData.contains(songsList.get(i).name))
            {
                System.out.println("Total songs played is: "+totalSongsPlayed);
                if(MoodActivity.Mood.equals(songsList.get(i).emotion) || MoodActivity.Mood.equals("Indifferent")) songsListData.add(songsList.get(i).name);
            }
            if(HomeActivity.Option==3&&songsList.get(i).count!=0&&!songsListData.contains(songsList.get(i).name)) songsListData.add(songsList.get(i).name);
            if(HomeActivity.Option==4&&songsList.get(i).rating!=0&&!songsListData.contains(songsList.get(i).name)) songsListData.add(songsList.get(i).name);
            if(HomeActivity.Option==5&&songsList.get(i).rating!=0&&songsList.get(i).count!=0)
            {
                for (int j = 0; j < songsList.size(); j++)
                    if (songsList.get(j).artist.equalsIgnoreCase(songsList.get(i).artist))
                        songsListData.add(songsList.get(j).name);
            }
            if(HomeActivity.Option==6&&!songsListData.contains(songsList.get(i).name)) songsListData.add(songsList.get(i).name);
            if(HomeActivity.Option==8&&!songsListData.contains(songsList.get(i).name))
            {
                          int maxWeather=Math.max(songsList.get(i).thundercount,Math.max(songsList.get(i).raincount,Math.max(songsList.get(i).drizzlecount,Math.max(songsList.get(i).snowcount,Math.max(songsList.get(i).atmospherecount,Math.max(songsList.get(i).sunnycount,songsList.get(i).extremecount))))));
                if(maxWeather!=0) {
                           if(weather1.equals("Thunderstorm") && maxWeather==songsList.get(i).thundercount)
                                    songsListData.add(songsList.get(i).name);
                else if(weather1.equals("Drizzle") && maxWeather==songsList.get(i).drizzlecount)
                               songsListData.add(songsList.get(i).name);
                           else if(weather1.equals("Rain") && maxWeather==songsList.get(i).raincount)
                               songsListData.add(songsList.get(i).name);
                           else if(weather1.equals("Snow") && maxWeather==songsList.get(i).snowcount)
                               songsListData.add(songsList.get(i).name);
                           else if(weather1.equals("Atmosphere") && maxWeather==songsList.get(i).atmospherecount)
                               songsListData.add(songsList.get(i).name);
                           else if(weather1.equals("Sunny") && maxWeather==songsList.get(i).sunnycount)
                               songsListData.add(songsList.get(i).name);
                           else if(weather1.equals("Extreme") && maxWeather==songsList.get(i).extremecount)
                               songsListData.add(songsList.get(i).name);
        }                         }
        }
        //System.out.println("Song list 3 size is: "+songsListData.size());
        ListAdapter adapter = new ArrayAdapter<String>(this,R.layout.playlist_item,R.id.songTitle,songsListData);

        setListAdapter(adapter);

        ListView lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AndroidBuildingMusicPlayerActivity.currentSongIndex = index(parent.getItemAtPosition(position).toString());
                Intent in = new Intent(SongList.this, AndroidBuildingMusicPlayerActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        });

    }

    public static int index(String song){
        for(int i=0;i<HomeActivity.Songs.size();i++)
        {
            if(HomeActivity.Songs.get(i).name.equalsIgnoreCase(song))return i;
        }
        return -1;
    }

    public static int indexlist(String song){
        for(int i=0;i<songsListData.size();i++)
        {
            if(songsListData.get(i).equalsIgnoreCase(song))return i;
        }
        return -1;
    }

    public void sort(String attribute){
        for(int i=0;i<songsList.size()-1;i++)
        {
            for (int j=0;j<songsList.size()-i-1;j++)
            {
                if(songsList.get(j).album.compareToIgnoreCase(songsList.get(j+1).album)>0){
                    Collections.swap(songsList,j,j+1);
                }
            }

        }
    }
    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
