package com.example.cs_reversi;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Highscores extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscores);
		SharedPreferences pref = this.getSharedPreferences("highscorelist", MODE_PRIVATE);
		Editor editor = pref.edit();
		int blackscore_value;
		int whitescore_value;
		Vector<String> highscore_list = new Vector<String>();
		Vector<Integer> whitescores = new Vector<Integer>();
		Vector<Integer> blackscores = new Vector<Integer>();
		Vector<Integer> sorted_black = new Vector<Integer>();;
		Vector<Integer> sorted_white = new Vector<Integer>();;
		
		Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    	    int value1 = extras.getInt("blackscore_value");
    	    blackscore_value = value1;
    	    int value2 = extras.getInt("whitescore_value");
    	    whitescore_value = value2;
    	}
    	else{
    		blackscore_value = 0;
    		whitescore_value = 0;
    	}
    	
    	int blackscore1 = pref.getInt("blackscore1", 0);
    	int blackscore2 = pref.getInt("blackscore2", 0);
    	int blackscore3 = pref.getInt("blackscore3", 0);
    	int blackscore4 = pref.getInt("blackscore4", 0);
    	int blackscore5 = pref.getInt("blackscore5", 0);
    	int whitescore1 = pref.getInt("whitescore1", 0);
    	int whitescore2 = pref.getInt("whitescore2", 0);
    	int whitescore3 = pref.getInt("whitescore3", 0);
    	int whitescore4 = pref.getInt("whitescore4", 0);
    	int whitescore5 = pref.getInt("whitescore5", 0);
    	
    	whitescores.add(whitescore1);
    	whitescores.add(whitescore2);
    	whitescores.add(whitescore3);
    	whitescores.add(whitescore4);
    	whitescores.add(whitescore5);
    	whitescores.add(whitescore_value);
    	blackscores.add(blackscore1);
    	blackscores.add(blackscore2);
    	blackscores.add(blackscore3);
    	blackscores.add(blackscore4);
    	blackscores.add(blackscore5);
    	blackscores.add(blackscore_value);
    	
    	for (int j = 0; j < 5; j ++) {
            int tmp = blackscores.elementAt(0);
            int index = 0;
            for (int i = 1; i < blackscores.size(); i++) {
                if (tmp < blackscores.elementAt(i)) {
                    tmp = blackscores.elementAt(i);
                    index = i;
                }
            }
            sorted_black.add(blackscores.elementAt(index));
            sorted_white.add(whitescores.elementAt(index));
            blackscores.set(index,-1);
        }
        // now sorted_black and sorted_white both have 5 sorted values
    	
    	editor.putInt("blackscore1", sorted_black.elementAt(0));
    	editor.putInt("blackscore2", sorted_black.elementAt(1));
    	editor.putInt("blackscore3", sorted_black.elementAt(2));
    	editor.putInt("blackscore4", sorted_black.elementAt(3));
    	editor.putInt("blackscore5", sorted_black.elementAt(4));
    	editor.putInt("whitescore1", sorted_white.elementAt(0));
    	editor.putInt("whitescore2", sorted_white.elementAt(1));
    	editor.putInt("whitescore3", sorted_white.elementAt(2));
    	editor.putInt("whitescore4", sorted_white.elementAt(3));
    	editor.putInt("whitescore5", sorted_white.elementAt(4));
    	
    	
        
        String str;
        for (int i = 0; i < 5; i++) {
            str = (i+1) + ". " + sorted_black.elementAt(i) + ":" + sorted_white.elementAt(i);
            highscore_list.add(str);
        }
        // now high_score done
        TextView highscore1 = (TextView) findViewById(R.id.highscore1);
    	TextView highscore2 = (TextView) findViewById(R.id.highscore2);
    	TextView highscore3 = (TextView) findViewById(R.id.highscore3);
    	TextView highscore4 = (TextView) findViewById(R.id.highscore4);
    	TextView highscore5 = (TextView) findViewById(R.id.highscore5);
    	highscore1.setText(highscore_list.elementAt(0));
    	highscore2.setText(highscore_list.elementAt(1));
    	highscore3.setText(highscore_list.elementAt(2));
    	highscore4.setText(highscore_list.elementAt(3));
    	highscore5.setText(highscore_list.elementAt(4));
    	editor.commit();
	}

	public void gotostart(View view) {
        // Do something in response to button
		new MediaPlayer();
		final MediaPlayer np = MediaPlayer.create(this, R.raw.menuclick);
		np.start();
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    	this.finish();
    }
}
