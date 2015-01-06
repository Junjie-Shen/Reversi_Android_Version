package com.example.cs_reversi;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class Difficulty extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_difficulty);
	}
	
	public void easy(View view){
		new MediaPlayer();
		final MediaPlayer np = MediaPlayer.create(this, R.raw.menuclick);
		np.start();
		Intent intent = new Intent(this, Gameboard.class);
		intent.putExtra("difficultysetter", 3);
		startActivity(intent);
		this.finish();
	}
	
	public void medium(View view){
		new MediaPlayer();
		final MediaPlayer mp = MediaPlayer.create(this, R.raw.menuclick);
		mp.start();
		Intent intent = new Intent(this, Gameboard.class);
		intent.putExtra("difficultysetter", 5);
		startActivity(intent);
		this.finish();
	}
	
	public void hard(View view){
		new MediaPlayer();
		final MediaPlayer op = MediaPlayer.create(this, R.raw.menuclick);
		op.start();
		Intent intent = new Intent(this, Gameboard.class);
		intent.putExtra("difficultysetter", 7);
		startActivity(intent);
		this.finish();
	}
	
}
