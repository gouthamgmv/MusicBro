
package com.AAAG.MusicBro.Player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.*;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.io.*;
import java.util.*;

import android.os.Environment;
import android.os.Handler;
import android.view.View;

import android.widget.*;
import be.ac.ulg.montefiore.run.jadti.*;
import be.ac.ulg.montefiore.run.jadti.io.DecisionTreeToDot;
import be.ac.ulg.montefiore.run.jadti.io.ItemSetReader;
import com.AAAG.MusicBro.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import java.net.URLEncoder;
import java.util.Set;

public class AndroidBuildingMusicPlayerActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    public static int hcount, scount, acount, rcount, lastIndex = 0;
    // Media Player
    private  MediaPlayer mp;
    public int weatherInt;
    public static int SongBeingPlayed;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();;
    private SongsManager songManager;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    public static int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    public String finalLyrics;
    public String songName;
    public static String lyric;
    public String weather, weather1;
    public GPSTracker gps;
    final static String jadtiURL = "http://www.run.montefiore.ulg.ac.be/" +
            "~francois/software/jaDTi/";
    String[] Happysyn = {"happy", "rock", "angel","party", "contented", "content", "cheerful", "all", "floor", "cheery", "merry", "beach", "joyful", "jovial", "jolly", "joking", "jocular", "gleeful", "air", "shout", "carefree", "untroubled", "delighted", "honor", "smiling", "beaming", "grinning", "glowing", "satisfied", "gratified", "buoyant", "radiant", "sunny", "blithe", "joyous", "beatific", "blessed", "cock-a-hoop", "in good spirits", "in high spirits", "in a good mood", "light-hearted", "good-humoured", "thrilled", "exuberant", "elated", "exhilarated", "ecstatic", "blissful", "euphoric", "overjoyed", "exultant", "rapturous", "rapt", "enraptured", "in seventh heaven", "on cloud nine", "over the moon", "walking on air", "beside oneself with joy", "jumping for joy", "chirpy", "on top of the world", "as happy as a sandboy", "tickled pink", "tickled to death", "like a dog with two tails", "as pleased as Punch", "on a high", "blissed out", "sent", "chuffed", "as happy as Larry", "made up", "as happy as a clam", "wrapped", "gay", "blithesome"};
    String[] Sadsyn = {"sad", "break", "broke", "morose", "let down", "please", "dread", "sorry", "tired", "unhappy", "fear", "sorrowful", "dejected", "regretful", "depressed", "downcast", "miserable", "downhearted", "down", "despondent", "despairing", "disconsolate", "out of sorts", "desolate", "bowed down", "wretched", "glum", "gloomy", "doleful", "dismal", "blue", "melancholy", "melancholic", "low-spirited", "mournful", "woeful", "woebegone", "forlorn", "crestfallen", "broken-hearted", "heartbroken", "inconsolable", "grief-stricken", "down in the mouth", "down in the dumps"};
    String[] Angrysyn = {"angry", "kill", "satan", "hell", "stop", "destroy", "doom", "fire", "dare", "blood", "die", "death", "hit", "beat", "rush", "burn", "provoke", "incense", "rage", "scream", "throw", "yell", "heat", "hot", "fury", "fiery", "mad", "wild", "livid", "wrath", "wroth", "hostile", "dark", "frenzy", "shoot", "shot", "revenge", "strike", "blow", "gun", "temper", "pissed", "ranting"};
    String[] Romanticsyn = {"romantic", "romance", "love", "heart", "hold", "dear", "passion", "tender", "fond", "affection", "the one", "life", "loving", "passionate", "engage", "relationship", "adore", "intimate", "attached", "let go", "kiss", "hug", "warm", "amorous", "beautiful", "wonder", "dote", "worship", "heaven", "cheesy"};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);


        // All player buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
                                                          // Mediaplayer

        mp = new MediaPlayer();
        songManager = new SongsManager();
        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important

        // Getting all songs list

        // By default play first song
        playSong(currentSongIndex);

        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                int ind = SongList.indexlist(HomeActivity.Songs.get(currentSongIndex).name);
                if(ind <= (SongList.songsListData.size() - 1)){
                    playSong(SongList.index(SongList.songsListData.get(ind+1)));
                    currentSongIndex = SongList.index(SongList.songsListData.get(ind+1));
                }else{
                    // play first song
                    playSong(SongList.index(SongList.songsListData.get(0)));
                    currentSongIndex = SongList.index(SongList.songsListData.get(0));
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int ind = SongList.indexlist(HomeActivity.Songs.get(currentSongIndex).name);
                if(currentSongIndex > 0){
                    playSong(SongList.index(SongList.songsListData.get(ind-1)));
                    currentSongIndex = SongList.index(SongList.songsListData.get(ind-1));
                }else{
                    // play last song
                    currentSongIndex = SongList.index(SongList.songsListData.get(SongList.songsListData.size()-1));
                    playSong(currentSongIndex);
                }

            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

        /**
         * Button Click event for Play list click event
         * Launches list activity which displays list of songs
         * */
        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(HomeActivity.Option==1)startActivity(new Intent(AndroidBuildingMusicPlayerActivity.this, PlayListActivity.class));
                else if(HomeActivity.Option==3) startActivity(new Intent(AndroidBuildingMusicPlayerActivity.this,CountActivity.class));
                else if(HomeActivity.Option==4) startActivity(new Intent(AndroidBuildingMusicPlayerActivity.this,RatingActivity.class));
            }
        });

    }

    /**
     * Function to play a song
     * @param songIndex - index of song
     *
     ***/
    static private DecisionTree buildTree(ItemSet learningSet,
                                          AttributeSet testAttributes,
                                          SymbolicAttribute goalAttribute) {
        DecisionTreeBuilder builder =
                new DecisionTreeBuilder(learningSet, testAttributes,
                        goalAttribute);

        return builder.build().decisionTree();
    }


    /*
     * Prints a dot file content depicting a tree.
     */
    static private void printDot(DecisionTree tree) {
        System.out.println((new DecisionTreeToDot(tree)).produce());
    }


    /*
     * Prints an item's guessed goal attribute value.
     */
    static private void printGuess(Item item, DecisionTree tree) {
        AttributeSet itemAttributes = tree.getAttributeSet();
        SymbolicAttribute goalAttribute = tree.getGoalAttribute();

        KnownSymbolicValue goalAttributeValue =
                (KnownSymbolicValue) item.valueOf(itemAttributes, goalAttribute);
        KnownSymbolicValue guessedGoalAttributeValue =
                tree.guessGoalAttribute(item);

        String s = "Item goal attribute value is " +
                goalAttribute.valueToString(goalAttributeValue) + "\n";

        s += "The value guessed by the tree is " +
                tree.getGoalAttribute().valueToString(guessedGoalAttributeValue);

        System.out.println(s);
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
     public void  playSong(int songIndex){
        // Play song
        try {
            mp.reset();
            mp.setDataSource(HomeActivity.Songs.get(songIndex).path);
            mp.prepare();
            mp.start();
            songName=HomeActivity.Songs.get(songIndex).name;
            HomeActivity.Songs.get(songIndex).count++;
            if(HomeActivity.Songs.get(songIndex).count==1)
                SongList.totalSongsPlayed++;
            SharedPreferences settings = getSharedPreferences("MyPrefs",0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt(songName, HomeActivity.Songs.get(songIndex).count);
            editor.commit();
            HttpClient client;
            HttpGet get;
            HttpResponse responseGet;
            HttpEntity resEntityGet;
            String temp, tempLyric;
            int useindex = SongList.index(songName);
            HomeActivity.Songs.get(useindex).agegroup = GetAge.agegroup;
            HomeActivity.Songs.get(useindex).mood = MoodActivity.Mood;
            SongBeingPlayed = useindex;
            HomeActivity.Songs.get(useindex).retrieved=settings.getBoolean(songName+"ret",false);
            Geocoder geocoder;
            String bestProvider;
            List<Address> user = null;

            double lat=0,lng=0;
            gps = new GPSTracker(AndroidBuildingMusicPlayerActivity.this);

            // check if GPS enabled
            if(gps.canGetLocation()){

                lat = gps.getLatitude();
                lng = gps.getLongitude();

                // \n is for new line
               // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
            boolean testnet=isNetworkAvailable();
            if(testnet==true)
            {
            DefaultHttpClient client1 = new DefaultHttpClient();
            HttpGet get1 = new HttpGet("http://api.openweathermap.org/data/2.5/weather?lat="+ URLEncoder.encode(Double.toString(lat), "UTF-8")+"&lon="+URLEncoder.encode(Double.toString(lng), "UTF-8")+"&mode=xml");
            HttpResponse responseGet1 = client1.execute(get1);
            HttpEntity resEntityGet1 = responseGet1.getEntity();
            if (resEntityGet1 != null) {
                weather = EntityUtils.toString(resEntityGet1, HTTP.UTF_8);
                temp = weather.substring(weather.indexOf("<weather number=\"") + 17);
                weather1 = temp.substring(0, temp.indexOf("\" value"));
                //System.out.println(weather1);
                settings = getSharedPreferences("MyPrefs",0);
                editor = settings.edit();
                editor.putString("lastWeather", weather1);
                editor.commit();

            }
            }
                settings = getSharedPreferences("MyPrefs",0);
                weather1=settings.getString("lastWeather","801");


            weatherInt=Integer.parseInt(weather1);
            settings = getSharedPreferences("MyPrefs",0);
            editor = settings.edit();
            if(weatherInt>=200 && weatherInt<=232)
            {      HomeActivity.Songs.get(useindex).thundercount++;
                editor.putInt(HomeActivity.Songs.get(useindex).name+"thunder", HomeActivity.Songs.get(useindex).thundercount);
                weather1="Thunderstorm"; }
            else if(weatherInt>=300 && weatherInt<=321) {
                weather1="Drizzle";       HomeActivity.Songs.get(useindex).drizzlecount++;    editor.putInt(HomeActivity.Songs.get(useindex).name+"drizzle", HomeActivity.Songs.get(useindex).drizzlecount);            }
            else if(weatherInt>=500 && weatherInt<=531)   {
                weather1="Rain";            HomeActivity.Songs.get(useindex).raincount++;     editor.putInt(HomeActivity.Songs.get(useindex).name+"rain", HomeActivity.Songs.get(useindex).raincount);           }
            else if(weatherInt>=600 && weatherInt<=622)     {
                weather1="Snow";                 HomeActivity.Songs.get(useindex).snowcount++;     editor.putInt(HomeActivity.Songs.get(useindex).name+"snow", HomeActivity.Songs.get(useindex).snowcount);        }
            else if(weatherInt>=700 && weatherInt<=781)       {
                weather1="Atmosphere";           HomeActivity.Songs.get(useindex).atmospherecount++;     editor.putInt(HomeActivity.Songs.get(useindex).name+"atmosphere", HomeActivity.Songs.get(useindex).atmospherecount);          }
            else if(weatherInt>=800 && weatherInt<=804)         {
                weather1="Sunny";                 HomeActivity.Songs.get(useindex).sunnycount++;      editor.putInt(HomeActivity.Songs.get(useindex).name+"sunny", HomeActivity.Songs.get(useindex).sunnycount);          }
            else                                                  {
                weather1="Extreme";                HomeActivity.Songs.get(useindex).extremecount++;  editor.putInt(HomeActivity.Songs.get(useindex).name+"extreme", HomeActivity.Songs.get(useindex).extremecount);               }

            editor.commit();
            if(!HomeActivity.Songs.get(useindex).retrieved)
            {
                try {
                    hcount = scount = acount = rcount = 0;
                    HomeActivity.Songs.get(useindex).retrieved = true;
                    settings = getSharedPreferences("MyPrefs",0);
                    editor = settings.edit();
                    editor.putBoolean(songName+"ret", HomeActivity.Songs.get(useindex).retrieved);
                    editor.commit();
                    client = new DefaultHttpClient();
                    get = new HttpGet("http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist="+ URLEncoder.encode(HomeActivity.Songs.get(useindex).artist, "UTF-8")+"&song="+URLEncoder.encode(HomeActivity.Songs.get(useindex).name, "UTF-8"));
                    responseGet = client.execute(get);
                    resEntityGet = responseGet.getEntity();

                    if (resEntityGet != null) {
                        lyric = EntityUtils.toString(resEntityGet, HTTP.UTF_8);
                        temp = lyric.substring(lyric.indexOf("<Lyric>") + 7);
                        HomeActivity.Songs.get(useindex).lyric = temp.substring(0, temp.indexOf("</Lyric>"));
                        tempLyric = HomeActivity.Songs.get(useindex).lyric;
                        //HomeActivity.Songs.get(useindex).lyric="Lol!";
                        settings = getSharedPreferences("MyPrefs", 0);
                        editor = settings.edit();
                        editor.putString(songName + "lyric", HomeActivity.Songs.get(useindex).lyric);
                        editor.commit();
                        String[] tokens = tempLyric.split("\n");
                        StringBuilder resultBuilder = new StringBuilder();
                        Set<String> alreadyPresent = new HashSet<String>();

                        boolean first = true;
                        for (String token : tokens) {

                            if (!alreadyPresent.contains(token)) {
                                if (first) first = false;
                                else resultBuilder.append("\n");

                                if (!alreadyPresent.contains(token))
                                    resultBuilder.append(token);
                            }

                            alreadyPresent.add(token);
                        }
                        String result1 = resultBuilder.toString();
                        HomeActivity.Songs.get(useindex).lyric = result1;
                                     finalLyrics=result1;
                        InputStream modelIn = new FileInputStream(Environment.getExternalStorageDirectory().getPath()
                                + "/en-token.bin");

                        TokenizerModel model = null;
                        try {
                            model = new TokenizerModel(modelIn);


                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (modelIn != null) {
                                try {
                                    modelIn.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                        Tokenizer tokenizer = new TokenizerME(model);
                        String tokens1[] = tokenizer.tokenize(result1);

                        modelIn = null;

                        POSModel model1 = null;
                        try {
                            modelIn = new FileInputStream(Environment.getExternalStorageDirectory().getPath() +
                                    "/en-pos-perceptron.bin");
                            model1 = new POSModel(modelIn);
                        } catch (IOException e) {
                            // Model loading failed, handle the error
                            e.printStackTrace();
                        } finally {
                            if (modelIn != null) {
                                try {
                                    modelIn.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                        POSTaggerME tagger = new POSTaggerME(model1);
                        String sent[] = tokens1;
                        String tags[] = tagger.tag(sent);

                        StringBuilder finalLyrics=new StringBuilder();
                        for (int i = 0; i < tags.length; i++)
                        {
                            if(tags[i].contains("JJ") || tags[i].contains("JJR") || tags[i].contains("JJS")
                                    || tags[i].contains("NN") || tags[i].contains("NNS") || tags[i].contains("RB") ||
                                    tags[i].contains("RBR") || tags[i].contains("RBS") || tags[i].contains("VB") ||
                                    tags[i].contains("VBD") || tags[i].contains("VBG") || tags[i].contains("VBN") ||
                                    tags[i].contains("VBP") || tags[i].contains("VBZ"))
                                finalLyrics.append(sent[i]).append(" ");
                        }


                        lastIndex = 0;
                        for (int j = 0; j <= 75; j++) {
                            lastIndex = 0;
                            while (lastIndex != -1) {
                                lastIndex = finalLyrics.indexOf(Happysyn[j], lastIndex);
                                if (lastIndex != -1) {
                                    hcount++;
                                    lastIndex += Happysyn[j].length();
                                }
                            }
                        }
                        lastIndex = 0;
                        for (int j = 0; j <= 44; j++) {
                            lastIndex = 0;
                            while (lastIndex != -1) {
                                lastIndex = finalLyrics.indexOf(Sadsyn[j], lastIndex);
                                if (lastIndex != -1) {
                                    scount++;
                                    lastIndex += Sadsyn[j].length();
                                }
                            }
                        }
                        lastIndex = 0;
                        for (int j = 0; j <= 39; j++) {
                            lastIndex = 0;
                            while (lastIndex != -1) {
                                lastIndex = finalLyrics.indexOf(Angrysyn[j], lastIndex);
                                if (lastIndex != -1) {
                                    acount++;
                                    lastIndex += Angrysyn[j].length();
                                }
                            }
                        }
                        lastIndex = 0;
                        for (int j = 0; j <= 28; j++) {
                            lastIndex = 0;
                            while (lastIndex != -1) {
                                lastIndex = finalLyrics.indexOf(Romanticsyn[j], lastIndex);
                                if (lastIndex != -1) {
                                    rcount++;
                                    lastIndex += Romanticsyn[j].length();
                                }
                            }
                        }
                        lastIndex = 0;
                    }
                   /* String writeMood;
                    int maxMood=returnmax(hcount,scount,acount,rcount);
                    double totalCount=hcount+scount+acount+rcount;
                    double hc=(double)hcount/totalCount;
                    double sc=(double)scount/totalCount;
                    double ac=(double)acount/totalCount;
                    double rc=(double)rcount/totalCount;
                    if(maxMood==1)
                            writeMood="Happy";
                    else if(maxMood==2)
                        writeMood="Sad";
                    else if(maxMood==3)
                        writeMood="Angry";
                    else if(maxMood==4)
                        writeMood="Romantic";
                    else
                        writeMood="Indifferent";        */
                    File logFile = new File(Environment.getExternalStorageDirectory().toString(), "myTrainingFile.txt");
                    if(!logFile.exists()) {
                        logFile.createNewFile();
                    }
                    StringBuilder yesno=new StringBuilder();

                    for(int i=0;i<=75;i++) {
                        if(finalLyrics.contains(Happysyn[i]))
                                yesno.append("yes ");
                        else
                            yesno.append("no ");
                    }
                    for(int i=0;i<=44;i++) {
                        if(finalLyrics.contains(Sadsyn[i]))
                            yesno.append("yes ");
                        else
                            yesno.append("no ");
                    }
                    for(int i=0;i<=39;i++) {
                        if(finalLyrics.contains(Angrysyn[i]))
                            yesno.append("yes ");
                        else
                            yesno.append("no ");
                    }
                    for(int i=0;i<=28;i++) {
                        if(finalLyrics.contains(Romanticsyn[i]))
                            yesno.append("yes ");
                        else
                            yesno.append("no ");
                    }
                   /* System.out.println(yesno);
                    BufferedWriter output = new BufferedWriter(new FileWriter(logFile));
                    output.newLine();
                    output.write(yesno);
                    output.close();*/
                    /*File logFile = new File(Environment.getExternalStorageDirectory().toString(), "zoo.db");
                    ItemSet learningSet = null;
                    try {
                        learningSet = ItemSetReader.read(new FileReader(logFile));
                    }
                    catch(FileNotFoundException e) {
                        System.err.println("File not found : " + logFile + ".");
                        System.err.println("This file is included in the source " +
                                "distribution of jaDti.  You can find it at " +
                                jadtiURL);
                        System.exit(-1);
                    }
                    AttributeSet attributeSet = learningSet.attributeSet();
                    Vector testAttributesVector = new Vector();
                    testAttributesVector.add(attributeSet.findByName("Happy1"));
                    testAttributesVector.add(attributeSet.findByName("Happy2"));
                    testAttributesVector.add(attributeSet.findByName("Happy3"));
                    testAttributesVector.add(attributeSet.findByName("Happy4"));
                    testAttributesVector.add(attributeSet.findByName("Happy5"));
                    testAttributesVector.add(attributeSet.findByName("Happy6"));
                    testAttributesVector.add(attributeSet.findByName("Happy7"));
                    testAttributesVector.add(attributeSet.findByName("Happy8"));
                    testAttributesVector.add(attributeSet.findByName("Happy9"));
                    testAttributesVector.add(attributeSet.findByName("Happy10"));
                    testAttributesVector.add(attributeSet.findByName("Happy11"));
                    testAttributesVector.add(attributeSet.findByName("Happy12"));
                    testAttributesVector.add(attributeSet.findByName("Happy13"));
                    testAttributesVector.add(attributeSet.findByName("Happy14"));
                    testAttributesVector.add(attributeSet.findByName("Happy15"));
                    testAttributesVector.add(attributeSet.findByName("Happy16"));
                    testAttributesVector.add(attributeSet.findByName("Happy17"));
                    testAttributesVector.add(attributeSet.findByName("Happy18"));
                    testAttributesVector.add(attributeSet.findByName("Happy19"));
                    testAttributesVector.add(attributeSet.findByName("Happy20"));
                    testAttributesVector.add(attributeSet.findByName("Happy21"));
                    testAttributesVector.add(attributeSet.findByName("Happy22"));
                    testAttributesVector.add(attributeSet.findByName("Happy23"));
                    testAttributesVector.add(attributeSet.findByName("Happy24"));
                    testAttributesVector.add(attributeSet.findByName("Happy25"));
                    testAttributesVector.add(attributeSet.findByName("Happy26"));
                    testAttributesVector.add(attributeSet.findByName("Happy27"));
                    testAttributesVector.add(attributeSet.findByName("Happy28"));
                    testAttributesVector.add(attributeSet.findByName("Happy29"));
                    testAttributesVector.add(attributeSet.findByName("Happy30"));
                    testAttributesVector.add(attributeSet.findByName("Happy31"));
                    testAttributesVector.add(attributeSet.findByName("Happy32"));
                    testAttributesVector.add(attributeSet.findByName("Happy33"));
                    testAttributesVector.add(attributeSet.findByName("Happy34"));
                    testAttributesVector.add(attributeSet.findByName("Happy35"));
                    testAttributesVector.add(attributeSet.findByName("Happy36"));
                    testAttributesVector.add(attributeSet.findByName("Happy37"));
                    testAttributesVector.add(attributeSet.findByName("Happy38"));
                    testAttributesVector.add(attributeSet.findByName("Happy39"));
                    testAttributesVector.add(attributeSet.findByName("Happy40"));
                    testAttributesVector.add(attributeSet.findByName("Happy41"));
                    testAttributesVector.add(attributeSet.findByName("Happy42"));
                    testAttributesVector.add(attributeSet.findByName("Happy43"));
                    testAttributesVector.add(attributeSet.findByName("Happy44"));
                    testAttributesVector.add(attributeSet.findByName("Happy45"));
                    testAttributesVector.add(attributeSet.findByName("Happy46"));
                    testAttributesVector.add(attributeSet.findByName("Happy47"));
                    testAttributesVector.add(attributeSet.findByName("Happy48"));
                    testAttributesVector.add(attributeSet.findByName("Happy49"));
                    testAttributesVector.add(attributeSet.findByName("Happy50"));
                    testAttributesVector.add(attributeSet.findByName("Happy51"));
                    testAttributesVector.add(attributeSet.findByName("Happy52"));
                    testAttributesVector.add(attributeSet.findByName("Happy53"));
                    testAttributesVector.add(attributeSet.findByName("Happy54"));
                    testAttributesVector.add(attributeSet.findByName("Happy55"));
                    testAttributesVector.add(attributeSet.findByName("Happy56"));
                    testAttributesVector.add(attributeSet.findByName("Happy57"));
                    testAttributesVector.add(attributeSet.findByName("Happy58"));
                    testAttributesVector.add(attributeSet.findByName("Happy59"));
                    testAttributesVector.add(attributeSet.findByName("Happy60"));
                    testAttributesVector.add(attributeSet.findByName("Happy61"));
                    testAttributesVector.add(attributeSet.findByName("Happy62"));
                    testAttributesVector.add(attributeSet.findByName("Happy63"));
                    testAttributesVector.add(attributeSet.findByName("Happy64"));
                    testAttributesVector.add(attributeSet.findByName("Happy65"));
                    testAttributesVector.add(attributeSet.findByName("Happy66"));
                    testAttributesVector.add(attributeSet.findByName("Happy67"));
                    testAttributesVector.add(attributeSet.findByName("Happy68"));
                    testAttributesVector.add(attributeSet.findByName("Happy69"));
                    testAttributesVector.add(attributeSet.findByName("Happy70"));
                    testAttributesVector.add(attributeSet.findByName("Happy71"));
                    testAttributesVector.add(attributeSet.findByName("Happy72"));
                    testAttributesVector.add(attributeSet.findByName("Happy73"));
                    testAttributesVector.add(attributeSet.findByName("Happy74"));
                    testAttributesVector.add(attributeSet.findByName("Happy75"));
                    testAttributesVector.add(attributeSet.findByName("Happy76"));
                    testAttributesVector.add(attributeSet.findByName("Sad1"));
                    testAttributesVector.add(attributeSet.findByName("Sad2"));
                    testAttributesVector.add(attributeSet.findByName("Sad3"));
                    testAttributesVector.add(attributeSet.findByName("Sad4"));
                    testAttributesVector.add(attributeSet.findByName("Sad5"));
                    testAttributesVector.add(attributeSet.findByName("Sad6"));
                    testAttributesVector.add(attributeSet.findByName("Sad7"));
                    testAttributesVector.add(attributeSet.findByName("Sad8"));
                    testAttributesVector.add(attributeSet.findByName("Sad9"));
                    testAttributesVector.add(attributeSet.findByName("Sad10"));
                    testAttributesVector.add(attributeSet.findByName("Sad11"));
                    testAttributesVector.add(attributeSet.findByName("Sad12"));
                    testAttributesVector.add(attributeSet.findByName("Sad13"));
                    testAttributesVector.add(attributeSet.findByName("Sad14"));
                    testAttributesVector.add(attributeSet.findByName("Sad15"));
                    testAttributesVector.add(attributeSet.findByName("Sad16"));
                    testAttributesVector.add(attributeSet.findByName("Sad17"));
                    testAttributesVector.add(attributeSet.findByName("Sad18"));
                    testAttributesVector.add(attributeSet.findByName("Sad19"));
                    testAttributesVector.add(attributeSet.findByName("Sad20"));
                    testAttributesVector.add(attributeSet.findByName("Sad21"));
                    testAttributesVector.add(attributeSet.findByName("Sad22"));
                    testAttributesVector.add(attributeSet.findByName("Sad23"));
                    testAttributesVector.add(attributeSet.findByName("Sad24"));
                    testAttributesVector.add(attributeSet.findByName("Sad25"));
                    testAttributesVector.add(attributeSet.findByName("Sad26"));
                    testAttributesVector.add(attributeSet.findByName("Sad27"));
                    testAttributesVector.add(attributeSet.findByName("Sad28"));
                    testAttributesVector.add(attributeSet.findByName("Sad29"));
                    testAttributesVector.add(attributeSet.findByName("Sad30"));
                    testAttributesVector.add(attributeSet.findByName("Sad31"));
                    testAttributesVector.add(attributeSet.findByName("Sad32"));
                    testAttributesVector.add(attributeSet.findByName("Sad33"));
                    testAttributesVector.add(attributeSet.findByName("Sad34"));
                    testAttributesVector.add(attributeSet.findByName("Sad35"));
                    testAttributesVector.add(attributeSet.findByName("Sad36"));
                    testAttributesVector.add(attributeSet.findByName("Sad37"));
                    testAttributesVector.add(attributeSet.findByName("Sad38"));
                    testAttributesVector.add(attributeSet.findByName("Sad39"));
                    testAttributesVector.add(attributeSet.findByName("Sad40"));
                    testAttributesVector.add(attributeSet.findByName("Sad41"));
                    testAttributesVector.add(attributeSet.findByName("Sad42"));
                    testAttributesVector.add(attributeSet.findByName("Sad43"));
                    testAttributesVector.add(attributeSet.findByName("Sad44"));
                    testAttributesVector.add(attributeSet.findByName("Sad45"));
                    testAttributesVector.add(attributeSet.findByName("Angry1"));
                    testAttributesVector.add(attributeSet.findByName("Angry2"));
                    testAttributesVector.add(attributeSet.findByName("Angry3"));
                    testAttributesVector.add(attributeSet.findByName("Angry4"));
                    testAttributesVector.add(attributeSet.findByName("Angry5"));
                    testAttributesVector.add(attributeSet.findByName("Angry6"));
                    testAttributesVector.add(attributeSet.findByName("Angry7"));
                    testAttributesVector.add(attributeSet.findByName("Angry8"));
                    testAttributesVector.add(attributeSet.findByName("Angry9"));
                    testAttributesVector.add(attributeSet.findByName("Angry10"));
                    testAttributesVector.add(attributeSet.findByName("Angry11"));
                    testAttributesVector.add(attributeSet.findByName("Angry12"));
                    testAttributesVector.add(attributeSet.findByName("Angry13"));
                    testAttributesVector.add(attributeSet.findByName("Angry14"));
                    testAttributesVector.add(attributeSet.findByName("Angry15"));
                    testAttributesVector.add(attributeSet.findByName("Angry16"));
                    testAttributesVector.add(attributeSet.findByName("Angry17"));
                    testAttributesVector.add(attributeSet.findByName("Angry18"));
                    testAttributesVector.add(attributeSet.findByName("Angry19"));
                    testAttributesVector.add(attributeSet.findByName("Angry20"));
                    testAttributesVector.add(attributeSet.findByName("Angry21"));
                    testAttributesVector.add(attributeSet.findByName("Angry22"));
                    testAttributesVector.add(attributeSet.findByName("Angry23"));
                    testAttributesVector.add(attributeSet.findByName("Angry24"));
                    testAttributesVector.add(attributeSet.findByName("Angry25"));
                    testAttributesVector.add(attributeSet.findByName("Angry26"));
                    testAttributesVector.add(attributeSet.findByName("Angry27"));
                    testAttributesVector.add(attributeSet.findByName("Angry28"));
                    testAttributesVector.add(attributeSet.findByName("Angry29"));
                    testAttributesVector.add(attributeSet.findByName("Angry30"));
                    testAttributesVector.add(attributeSet.findByName("Angry31"));
                    testAttributesVector.add(attributeSet.findByName("Angry32"));
                    testAttributesVector.add(attributeSet.findByName("Angry33"));
                    testAttributesVector.add(attributeSet.findByName("Angry34"));
                    testAttributesVector.add(attributeSet.findByName("Angry35"));
                    testAttributesVector.add(attributeSet.findByName("Angry36"));
                    testAttributesVector.add(attributeSet.findByName("Angry37"));
                    testAttributesVector.add(attributeSet.findByName("Angry38"));
                    testAttributesVector.add(attributeSet.findByName("Angry39"));
                    testAttributesVector.add(attributeSet.findByName("Angry40"));
                    testAttributesVector.add(attributeSet.findByName("Romantic1"));
                    testAttributesVector.add(attributeSet.findByName("Romantic2"));
                    testAttributesVector.add(attributeSet.findByName("Romantic3"));
                    testAttributesVector.add(attributeSet.findByName("Romantic4"));
                    testAttributesVector.add(attributeSet.findByName("Romantic5"));
                    testAttributesVector.add(attributeSet.findByName("Romantic6"));
                    testAttributesVector.add(attributeSet.findByName("Romantic7"));
                    testAttributesVector.add(attributeSet.findByName("Romantic8"));
                    testAttributesVector.add(attributeSet.findByName("Romantic9"));
                    testAttributesVector.add(attributeSet.findByName("Romantic10"));
                    testAttributesVector.add(attributeSet.findByName("Romantic11"));
                    testAttributesVector.add(attributeSet.findByName("Romantic12"));
                    testAttributesVector.add(attributeSet.findByName("Romantic13"));
                    testAttributesVector.add(attributeSet.findByName("Romantic14"));
                    testAttributesVector.add(attributeSet.findByName("Romantic15"));
                    testAttributesVector.add(attributeSet.findByName("Romantic16"));
                    testAttributesVector.add(attributeSet.findByName("Romantic17"));
                    testAttributesVector.add(attributeSet.findByName("Romantic18"));
                    testAttributesVector.add(attributeSet.findByName("Romantic19"));
                    testAttributesVector.add(attributeSet.findByName("Romantic20"));
                    testAttributesVector.add(attributeSet.findByName("Romantic21"));
                    testAttributesVector.add(attributeSet.findByName("Romantic22"));
                    testAttributesVector.add(attributeSet.findByName("Romantic23"));
                    testAttributesVector.add(attributeSet.findByName("Romantic24"));
                    testAttributesVector.add(attributeSet.findByName("Romantic25"));
                    testAttributesVector.add(attributeSet.findByName("Romantic26"));
                    testAttributesVector.add(attributeSet.findByName("Romantic27"));
                    testAttributesVector.add(attributeSet.findByName("Romantic28"));
                    testAttributesVector.add(attributeSet.findByName("Romantic29"));
                    AttributeSet testAttributes = new AttributeSet(testAttributesVector);
                    SymbolicAttribute goalAttribute =
                            (SymbolicAttribute) learningSet.attributeSet().findByName("type");
                    DecisionTree tree = buildTree(learningSet, testAttributes,
                            goalAttribute);
                    printDot(tree);
                    printGuess(learningSet.item(0), tree);*/
                    switch(returnmax(hcount, scount, acount, rcount))
                    {
                        case 1 : { HomeActivity.Songs.get(useindex).emotion = "Happy"; settings = getSharedPreferences("MyPrefs",0);
                            editor = settings.edit();
                            editor.putString(songName+"mood", HomeActivity.Songs.get(useindex).emotion);
                            editor.commit();
                            break;            }
                        case 2 : { HomeActivity.Songs.get(useindex).emotion = "Sad"; settings = getSharedPreferences("MyPrefs",0);
                            editor = settings.edit();
                            editor.putString(songName+"mood", HomeActivity.Songs.get(useindex).emotion);
                            editor.commit();
                            break;            }
                        case 3 : { HomeActivity.Songs.get(useindex).emotion = "Angry"; settings = getSharedPreferences("MyPrefs",0);
                            editor = settings.edit();
                            editor.putString(songName+"mood", HomeActivity.Songs.get(useindex).emotion);
                            editor.commit();
                            break;            }
                        case 4 : { HomeActivity.Songs.get(useindex).emotion = "Romantic"; settings = getSharedPreferences("MyPrefs",0);
                            editor = settings.edit();
                            editor.putString(songName+"mood", HomeActivity.Songs.get(useindex).emotion);
                            editor.commit();
                            break;            }
                        case 0 : { HomeActivity.Songs.get(useindex).emotion = "Indifferent"; settings = getSharedPreferences("MyPrefs",0);
                            editor = settings.edit();
                            editor.putString(songName+"mood", HomeActivity.Songs.get(useindex).emotion);
                            editor.commit();
                            break;            }
                    }


                }
                catch(Exception e) {
                    e.printStackTrace();
                }

            }
            else
            {
                HomeActivity.Songs.get(useindex).lyric=settings.getString(songName+"lyric","null");
               HomeActivity.Songs.get(useindex).emotion=settings.getString(songName+"mood","null");
            }

                    // Displaying Song title
            String songTitle = HomeActivity.Songs.get(songIndex).name;
            songTitleLabel.setText(songTitle);
            ImageView img = (ImageView) findViewById(R.id.lyricin);
            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent in = new Intent(AndroidBuildingMusicPlayerActivity.this, Lyric.class);
                    startActivity(in);
                }
            });
            final int rateIndex=songIndex;
            RatingBar rating = (RatingBar) findViewById(R.id.ratingbar);
            rating.setRating(SongList.songsList.get(rateIndex).rating);
            //if rating is changed,
            //display the current rating value in the result (textview) automatically
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                        SongList.songsList.get(rateIndex).rate((int) ratingBar.getRating());
                    SharedPreferences settings = getSharedPreferences("MyPrefs",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(songName+"r", SongList.songsList.get(rateIndex).rating);
                    editor.commit();
                     }
            });



            //Toast.makeText(getApplicationContext(),"Count :"+SongList.songsList.get(songIndex).count+"Rating : "+SongList.songsList.get(songIndex).rating,Toast.LENGTH_LONG).show();

                    // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);


            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public int returnmax(int a, int b, int c, int d)
        {
        if(a>b&&a>c&&a>d)
        return 1;
else if (b>a&&b>c&&b>d)
        return 2;
else if (c>a&&c>b&&c>d)
        return 3;
else if(d>a&&d>b&&d>c)
        return 4;
else return 0;
}
    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(mp.isPlaying())
            {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
            }
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     * */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if(isRepeat){
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if(isShuffle){
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((SongList.songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else{
            // no repeat or shuffle ON - play next song
            if(currentSongIndex < (SongList.songsList.size() - 1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mp.release();
    }


}