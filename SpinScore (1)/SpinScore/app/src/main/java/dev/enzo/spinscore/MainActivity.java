package dev.enzo.spinscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int comboNumber = 7;
    private int coef1 = 72;
    private int coef2 = 142;
    private int coef3 = 212;
    private int position1 = 5;
    private int position2 = 5;
    private int position3 = 5;
    private int[] slot = {1, 2, 3, 4, 5, 6, 7};

    private RecyclerView rv1;
    private RecyclerView rv2;
    private RecyclerView rv3;
    SpinnerAdapter adapter;
    private CustomLayoutManager layoutManager1;
    private CustomLayoutManager layoutManager2;
    private CustomLayoutManager layoutManager3;

    ImageButton spinButton;
    ImageButton plusButton;
    ImageButton minusButton;
    ImageView settingsButton;
    private TextView jackpot;
    private TextView myCoins;
    private TextView play;

    int myCoinsVal;
    int betVal;
    int jackpotVal;

    private boolean firstRun;
    private Mechanics gameLogic;
    private SharedPreferences pref;
    MediaPlayer mp;
    MediaPlayer win;
    MediaPlayer bgsound;
    public static final String PREFS_NAME = "FirstRun";

    private int playmusic;
    private int playsound;
    private ImageView musicOff;
    private ImageView musicOn;
    private ImageView soundon;
    private ImageView soundoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bgsound = MediaPlayer.create(this,R.raw.bg_music);
        bgsound.setLooping(true);
        mp = MediaPlayer.create(this, R.raw.spin);
        win = MediaPlayer.create(this, R.raw.win);

        pref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        firstRun = pref.getBoolean("firstRun1", true);

        if (firstRun) {
            playmusic = 1;
            playsound = 1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstRun2", false);
            editor.apply();
        } else {
            playmusic= pref.getInt("music1", 1);
            playsound = pref.getInt("sound1", 1);
            checkmusic();
        }

        Log.d("MUSIC",String.valueOf(playmusic));

        //Initializations
        gameLogic = new Mechanics();
        settingsButton = findViewById(R.id.settings);
        spinButton = findViewById(R.id.spinButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);
        jackpot = findViewById(R.id.jackpot);
        myCoins = findViewById(R.id.myCoins);
        play = findViewById(R.id.bet);
        adapter = new SpinnerAdapter();

        //RecyclerView settings
        rv1 = findViewById(R.id.spinner1);
        rv2 = findViewById(R.id.spinner2);
        rv3 = findViewById(R.id.spinner3);
        rv1.setHasFixedSize(true);
        rv2.setHasFixedSize(true);
        rv3.setHasFixedSize(true);

        layoutManager1 = new CustomLayoutManager(this);
        layoutManager1.setScrollEnabled(false);
        rv1.setLayoutManager(layoutManager1);
        layoutManager2 = new CustomLayoutManager(this);
        layoutManager2.setScrollEnabled(false);
        rv2.setLayoutManager(layoutManager2);
        layoutManager3 = new CustomLayoutManager(this);
        layoutManager3.setScrollEnabled(false);
        rv3.setLayoutManager(layoutManager3);

        rv1.setAdapter(adapter);
        rv2.setAdapter(adapter);
        rv3.setAdapter(adapter);
        rv1.scrollToPosition(position1);
        rv2.scrollToPosition(position2);
        rv3.scrollToPosition(position3);

        setText();
        updateText();

        rv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rv1.scrollToPosition(gameLogic.getPosition(0));
                    layoutManager1.setScrollEnabled(false);
                }
            }
        });

        rv2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rv2.scrollToPosition(gameLogic.getPosition(1));
                    layoutManager2.setScrollEnabled(false);
                }
            }
        });

        rv3.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rv3.scrollToPosition(gameLogic.getPosition(2));
                    layoutManager3.setScrollEnabled(false);
                    updateText();

                    if (gameLogic.getHasWon()) {
                        if (playsound == 1) {
                            win.start();
                        }

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.win_splash, (ViewGroup) findViewById(R.id.win_splash));
                        TextView winCoins = layout.findViewById(R.id.win_coins);
                        winCoins.setText(gameLogic.getPrize());

                        Toast toast = new Toast(MainActivity.this);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setView(layout);
                        toast.show();

                        gameLogic.setHasWon(false);
                    }
                }
            }
        });

        //Button listeners
        spinButton.setOnClickListener(v -> {
            if(playsound == 1){
                mp.start();
            }
            layoutManager1.setScrollEnabled(true);
            layoutManager2.setScrollEnabled(true);
            layoutManager3.setScrollEnabled(true);
            gameLogic.getSpinResults();
            position1 = gameLogic.getPosition(0) + coef1;
            position2 = gameLogic.getPosition(1) + coef2;
            position3 = gameLogic.getPosition(2) + coef3;
            rv1.smoothScrollToPosition(position1);
            rv2.smoothScrollToPosition(position2);
            rv3.smoothScrollToPosition(position3);
        });

        plusButton.setOnClickListener(v -> {
            if(playsound == 1){
                mp.start();
            }
            gameLogic.betUp();
            updateText();
        });

        minusButton.setOnClickListener(v -> {
            if(playsound == 1){
                mp.start();
            }
            gameLogic.betDown();
            updateText();
        });

        settingsButton.setOnClickListener(v -> {
            if(playsound == 1){
                mp.start();
            }
            showSettingsDialog();
        });
    }
    private void setText(){
        if(firstRun){
            gameLogic.setMyCoins(1000);
            gameLogic.setBet(5);
            gameLogic.setJackpot(100000);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

        } else {
            String coins = pref.getString("coins","");
            String bet = pref.getString("bet","");
            String wins = pref.getString("jackpot","");
            Log.d("COINS",coins);
            myCoinsVal = Integer.valueOf(coins);
            betVal = Integer.valueOf(bet);
            jackpotVal = Integer.valueOf(wins);
            gameLogic.setMyCoins(myCoinsVal);
            gameLogic.setBet(betVal);
            gameLogic.setJackpot(jackpotVal);
        }
    }
    private void updateText() {
        jackpot.setText(gameLogic.getJackpot());
        myCoins.setText(gameLogic.getMyCoins());
        play.setText(gameLogic.getBet());

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("coins",gameLogic.getMyCoins());
        editor.putString("bet",gameLogic.getBet());
        editor.putString("jackpot",gameLogic.getJackpot());
        editor.apply();
    }
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;

        public ItemViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.spinner_item);
        }
    }

    private class SpinnerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater.inflate(R.layout.spinner_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            int i = position < 7 ? position : position % comboNumber;
            switch (slot[i]) {
                case 1:
                    holder.pic.setImageResource(R.drawable.soccer);
                    break;
                case 2:
                    holder.pic.setImageResource(R.drawable.volleyball);
                    break;
                case 3:
                    holder.pic.setImageResource(R.drawable.tennis);
                    break;
                case 4:
                    holder.pic.setImageResource(R.drawable.tabletennis);
                    break;
                case 5:
                    holder.pic.setImageResource(R.drawable.baseball);
                    break;
                case 6:
                    holder.pic.setImageResource(R.drawable.basketball);
                    break;
                case 7:
                    holder.pic.setImageResource(R.drawable.athletes);
                    break;
                default:
                    // Handle unexpected values of slot[i]
                    // You can set a default image or perform other actions here
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    private void showSettingsDialog() {
        final Dialog dialog;

        dialog = new Dialog(this, R.style.WinDialog);
        dialog.getWindow().setContentView(R.layout.settings);

        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        ImageView btnClose = dialog.findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        musicOn = dialog.findViewById(R.id.music_on);
        musicOn.setOnClickListener(v -> {
            playmusic = 0;
            checkmusic();
            musicOn.setVisibility(View.INVISIBLE);
            musicOff.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("music", playmusic);
            editor.apply();
        });

        musicOff = (ImageView)dialog.findViewById(R.id.music_off);
        musicOff.setOnClickListener(v -> {
            playmusic = 1;
            bgsound.start();
            recreate();
            dialog.show();
            musicOn.setVisibility(View.VISIBLE);
            musicOff.setVisibility(View.INVISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("music", playmusic);
            editor.apply();
        });

        soundon = (ImageView)dialog.findViewById(R.id.sounds_on);
        soundon.setOnClickListener(v -> {
            playsound = 0;
            soundon.setVisibility(View.INVISIBLE);
            soundoff.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("sound2", playsound);
            editor.apply();
        });

        soundoff = (ImageView)dialog.findViewById(R.id.sounds_off);
        soundoff.setOnClickListener(v -> {
            playsound = 1;
            recreate();
            dialog.show();
            soundon.setVisibility(View.INVISIBLE);
            soundoff.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("sound3", playsound);
            editor.apply();
        });

        checkmusicdraw();
        checksounddraw();

        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        bgsound.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkmusic();
    }

    private void checkmusic(){
        if (playmusic == 1){
            bgsound.start();
        }
        else {
            bgsound.pause();
        }
    }

    private void checkmusicdraw(){
        if (playmusic == 1){
            musicOn.setVisibility(View.VISIBLE);
            musicOff.setVisibility(View.INVISIBLE);
        }
        else {
            musicOn.setVisibility(View.INVISIBLE);
            musicOff.setVisibility(View.VISIBLE);
        }
    }

    private void checksounddraw(){
        if (playsound == 1){
            soundon.setVisibility(View.VISIBLE);
            soundoff.setVisibility(View.INVISIBLE);
        }
        else {
            soundon.setVisibility(View.INVISIBLE);
            soundoff.setVisibility(View.VISIBLE);
        }
    }
}