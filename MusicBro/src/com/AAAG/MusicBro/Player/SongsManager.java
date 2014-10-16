package com.AAAG.MusicBro.Player;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> albumm = new ArrayList<String>();
    // Constructor
    public SongsManager(){

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getSongs(){
        getPlayList(Environment.getExternalStorageDirectory(),0);
        // return songs list array
        return songsList;
    }
    public ArrayList<HashMap<String, String>> getAlbums(){
        getPlayList(Environment.getExternalStorageDirectory(),0);
        // return songs list array
        return albumList;
    }
    public void getPlayList(File home, int level){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                HashMap<String, String> album = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());
                mmr.setDataSource(file.getAbsolutePath());
                String check = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ;
                if(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!=null)
                {
                    int exists = albumm.indexOf(check);
                    album.put("songTitle",mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                    if(exists==-1)    {
                        albumm.add(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                        albumList.add(album);}
                }
                else
                {
                    album.put("songTitle", "Unknown");
                    int exists = albumm.indexOf("Unknown");
                    if(exists==-1) {
                        albumm.add("Unknown");
                        albumList.add(album);}

                }
                album.put("songPath", file.getPath());

                // Adding each song to SongList
                songsList.add(song);

            }
        }
        for(File file : home.listFiles()) {
            if (file.isDirectory() && file.getName().charAt(0)!='.') {
                getPlayList(new File(file.getPath()),level+1);
            }
        }
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
