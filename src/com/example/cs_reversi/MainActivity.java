package com.example.cs_reversi;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageView reversilogo = (ImageView) findViewById(R.id.reversilogo);
        Button start = (Button) findViewById(R.id.start);
        Button highscores = (Button) findViewById(R.id.highscores);
        reversilogo.bringToFront();
        start.bringToFront();
        highscores.bringToFront();
    }
    
    
    public void gotodifficulty(View view) {
        // Do something in response to button
    	new MediaPlayer();
		final MediaPlayer np = MediaPlayer.create(this, R.raw.menuclick);
		np.start();
    	Intent intent = new Intent(this, Difficulty.class);
    	startActivity(intent);
    	this.finish();
    }
    
    public void gotohighscores(View view) {
        // Do something in response to button
    	new MediaPlayer();
		final MediaPlayer op = MediaPlayer.create(this, R.raw.menuclick);
		op.start();
    	Intent intent = new Intent(this, Highscores.class);
    	startActivity(intent);
    	this.finish();
    }
}


