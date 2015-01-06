package com.example.cs_reversi;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.*;
import java.lang.Math;

public class Gameboard extends Activity {
	
	public int DIREC[][] = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };
	public int delay = 0;
    public int gameboard[][] = new int[8][8];
    public Stack<Integer> turntrack = new Stack<Integer>();
    public Vector<int[][]> undo_list = new Vector<int[][]>();
    public Vector<String> possible_move = new Vector<String>();
    public int turn;
    public int black_score;
    public int white_score;
    public int undo_counter;
    public int difficulty;
    public int total_score;

    public void initialize(){
    	turn = -1;
    	black_score = 0;
    	white_score = 0;
    	undo_counter = 10;
    	gameboard[3][4] = -1;
    	gameboard[4][3] = -1;
    	gameboard[3][3] = 1;
    	gameboard[4][4] = 1;
    	
    	Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    	    int value = extras.getInt("difficultysetter");
    	    difficulty = value;
    	}
    	get_legal_moves();
    	
    }

    public static boolean is_move(String token) {
	    return (token.charAt(0) >= 'a' && token.charAt(0) <= 'h'
			 && token.charAt(1) >= '1' && token.charAt(1) <= '8');
}

    public static String convertInt (int number) {
        String s = "" + number;
        return s;
    }

    public static boolean on_board(int x, int y) {
    	if (x < 0 || x > 7 || y < 0 || y > 7)
		    return false;
	    else
		    return true;
    }

    public boolean isLegalMove(String location, int [][] gameboard, int turn) {
        int row = location.charAt(1) - '1';                                      // the coordinate 
		int col = location.charAt(0) - 'a';
		int x = 0;                                                        // the increment
		int y = 0;
		if (!on_board(col, row)){                                         //given location is not on the game board
			return false;
		}
		if (gameboard[row][col] == 0){
			Boolean directioncheck = false;
			for (int i = 0; i < 8; ++i){                                   // check 0-7 for 8 directions
				x = DIREC[i][0];
				y = DIREC[i][1];
				while (on_board(col + x, row + y)){
					if (gameboard[row + y][col + x] == -turn){
						x = x + DIREC[i][0];                              // check the next location on this direction
						y = y + DIREC[i][1];
						directioncheck = true;
					}
					else
						break;
				}
				if (on_board(col + x, row + y) && directioncheck == true){
					if (gameboard[row + y][col + x] == turn) {
						return true;
					}
				}
				directioncheck = false;
			}
		}
		return false;
	}

    public void score() {
    	int ws = 0;
    	int bs = 0;
	    for(int i = 0; i < 8; ++i){
	    	for(int j = 0; j < 8; ++j){
	    		if(gameboard[i][j] == -1){
	    			bs = bs + 1;
	    		}
	    		else if(gameboard[i][j] == 1){
	    			ws = ws + 1;
	    		}
	    	}
	    }
	    black_score = bs;
	    white_score = ws;
	    String display_bs = "Black: " + black_score;
	    String display_ws = "White: " + white_score;
	    
	    TextView whitescore = (TextView) findViewById(R.id.whitescore);
	    TextView blackscore = (TextView) findViewById(R.id.blackscore);
	    whitescore.setText(display_ws);
	    blackscore.setText(display_bs);
	    
    }

    public void get_legal_moves() {
        Vector<String> legal_moves = new Vector<String>();
        legal_moves.add("no moves. :(");
        String location;
        for (int i = 0; i < 8; i++)
	        for (int j = 0; j < 8; j++) {
	            location = convert(i,j);
	            if (isLegalMove(location, gameboard, turn) )
	                legal_moves.add(location); 
	        }
        possible_move = legal_moves;
        if(possible_move.size()>1){
	        for(int i = 1; i < possible_move.size(); ++i){
		    	Button buffer;
		    	int resID = getResources().getIdentifier(possible_move.elementAt(i), "id", "com.example.cs_reversi");
		    	buffer = (Button) findViewById(resID);
		    	buffer.setBackgroundResource(R.drawable.possible);
		    }
        }
    }

    public int previous_turn() {
	    if (turntrack.size() > 0) {
		    int p = turntrack.pop();
		    return p;
	    }
	    else
		    return 0;
    }
    
    public void undo() {
        if (undo_counter > 0) {
		    if (undo_list.size() != 0) {      //undo_list contains some previous turns
			    for (int i = 0; i < 8; i++){
			    	for (int j = 0; j < 8; j++){
			    		gameboard[i][j] = undo_list.lastElement()[i][j];
			    	}
			    }
			    undo_list.remove(undo_list.size()-1);
			    if (previous_turn() == 1) {
				    undo();
			    }
			    else {              //undo_list is empty
				    turn = -1;
				    undo_counter = undo_counter - 1;
			    }
		    }
	    }
        
        for(int i = 0; i < 8; ++i){
        	for(int j = 0; j < 8; ++j){
        		String move = inttoString(i, j);
		    	Button buffer;
		    	int resID = getResources().getIdentifier(move, "id", "com.example.cs_reversi");
		    	buffer = (Button) findViewById(resID);
        		if(gameboard[i][j] == -1){
        			buffer.setBackgroundResource(R.drawable.black);
        		}
        		else if(gameboard[i][j] == 1){
        			buffer.setBackgroundResource(R.drawable.white);
        		}
        		else if(gameboard[i][j] == 0){
        			buffer.setBackgroundResource(R.drawable.empty);
        		}
        	}
        }
        
        get_legal_moves();
        
        TextView undotracker = (TextView) findViewById(R.id.undotracker);
        String undo_string = "Undos: " + undo_counter;
        undotracker.setText(undo_string);
        
    }
    
    public static String inttoString(int row, int col){
    	int row1 = (row + 1);
    	String srow = "" + row1;
    	String scol;
    	if(col==0){
    		scol = "a";
    	}
    	else if(col==1){
    		scol = "b";
    	}
    	else if(col==2){
    		scol = "c";
    	}
    	else if(col==3){
    		scol = "d";
    	}
    	else if(col==4){
    		scol = "e";
    	}
    	else if(col==5){
    		scol = "f";
    	}
    	else if(col==6){
    		scol = "g";
    	}
    	else if(col==7){
    		scol = "h";
    	}
    	else{
    		scol = "a";
    	}
    	String move = scol + srow;
    	return move;
    }
    
	public void move(String location) { //c4, row = 3, col = 2
		if(possible_move.size()>1){
			for(int i = 1; i < possible_move.size(); ++i){
		    	Button buffer;
		    	int resID = getResources().getIdentifier(possible_move.elementAt(i), "id", "com.example.cs_reversi");
		    	buffer = (Button) findViewById(resID);
		    	buffer.setBackgroundResource(R.drawable.empty);
		    }
		}
		
		
        int row = location.charAt(1) - '1';                                      // the coordinate 
		int col = location.charAt(0) - 'a';
		int x = 0;                                                        // the increment
		int y = 0;
        
        int[][] temp = new int[8][8];	
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                temp[i][j] = gameboard[i][j]; 

        turntrack.push(turn);
		undo_list.add(temp);
		
		gameboard[row][col] = turn;
		
		Button butbuffer;
		int butID = getResources().getIdentifier(inttoString(row, col), "id", "com.example.cs_reversi");
		butbuffer = (Button) findViewById(butID);
		
		if(turn == -1){
			butbuffer.setBackgroundResource(R.drawable.black);
		}
		else if(turn == 1){
			butbuffer.setBackgroundResource(R.drawable.white);
		}

		boolean directioncheck = false;
	    for (int i = 0; i < 8; ++i){       //conversion of pieces begin here
		    x = DIREC[i][0];
		    y = DIREC[i][1];
		    while (on_board(col + x, row + y)){
			    if (gameboard[row + y][col + x] == -turn){
				    x = x + DIREC[i][0];
				    y = y + DIREC[i][1];
				    directioncheck = true;
			    }
			    else{
				    break;
			    }
		    }

		    if (on_board(col + x, row + y) && directioncheck == true){
			    if (gameboard[row + y][col + x] == turn){

				    int k = 0;
				    int p = 0;

				    if (y > 0){ p = 1; }
				    else if (y < 0){ p = -1; }
				    else { p = 0; }
				    if (x > 0){ k = 1; }
				    else if (x < 0){ k = -1; }
				    else { k = 0; }

				    for (int j = 1; j < Math.max(Math.abs(x), Math.abs(y)); ++j){
				    	int crow = (row + (j*p));
				    	int ccol = (col + (j*k));
				    	String move = inttoString(crow, ccol);
				    	Button buffer;
				    	int resID = getResources().getIdentifier(move, "id", "com.example.cs_reversi");
				    	buffer = (Button) findViewById(resID);
				    	if(turn == -1){
							buffer.setBackgroundResource(R.drawable.black);
						}
						else if(turn == 1){
							buffer.setBackgroundResource(R.drawable.white);
						}
				    	
					    gameboard[crow][ccol] = turn;
					    //create new function here, takes two numbers, changes it to string like "a4", then change the image button.
				    }
			    }
		    }

		    directioncheck = false;
	    }

	    turn = turn * (-1);
	    
	    get_legal_moves();
	    score();
    }

    public void play(String location) {
        move(location);
        win();
        if (possible_move.size() == 1){
		    turn = -turn;
		    get_legal_moves();
	    }
    }
    
    public void win(){
    	if((black_score + white_score) == 64){
    		TextView test = (TextView) findViewById(R.id.debug);
    		new MediaPlayer();
			final MediaPlayer win = MediaPlayer.create(this, R.raw.win);
			new MediaPlayer();
			final MediaPlayer lose = MediaPlayer.create(this, R.raw.lose);
    		if(black_score > white_score){
    			win.start();
    			test.setText("You Win!");
    		}
    		else if(white_score > black_score){
    			lose.start();
    			test.setText("You Lose!");
    		}
    		else if(white_score == black_score){
    			lose.start();
    			test.setText("Draw!");
    		}
    	}
    	else if(black_score == 0){
			new MediaPlayer();
			final MediaPlayer lose = MediaPlayer.create(this, R.raw.lose);
    		TextView test = (TextView) findViewById(R.id.debug);
    		lose.start();
    		test.setText("You Lose!");
    	}
    	else if(white_score == 0){
    		new MediaPlayer();
			final MediaPlayer win = MediaPlayer.create(this, R.raw.win);
    		TextView test = (TextView) findViewById(R.id.debug);
    		win.start();
    		test.setText("You Win!");
    	}
    }

/* ***************************** A.I. *************************************** */
    
    public static int node_table[][] = { { 100, -5,  10, 5, 5, 10, -5, 100 },
						 {  -5, -45, 1,  1, 1, 1, -45, -5 },
						 {  10,  1,  3,  2, 2, 3,  1,   10 },
						 {   5,  1,  2,  1, 1, 2,  1,   5 },
						 {   5,  1,  2,  1, 1, 2,  1,   5 },
					     {  10,  1,  3,  2, 2, 3,  1,   10 },
						 {  -5,  -45, 1, 1, 1, 1,  -45, -5 },
						 { 100,  -5, 10, 5, 5, 10, -5,   100 } };

     static class node {
	    int board[][] = new int[8][8];
	    int my_turn;
	    int turn;
	    String location;
	    node() {}
	};
	
	//Not sure if this should be public either.
	static class result {
	    String location;
	    int value;  
	    result()  { location = "";value = 0;}
	    result(int val) { location = "";value = val;} 
	}; 


    public static String convert(int row, int col) {
        char ch1 = (char)('a'+col);
        char ch2 = (char)('1'+row);
        String str = "" + ch1+ch2;      
        return str;   
    }

   

  

    public Vector<String> getLegalMove(int[][] board, int turn) {
	    String location;
	   	Vector<String> legal_moves = new Vector<String>();
	    for (int i = 0; i < 8; i++)
	        for (int j = 0; j < 8; j++) {
	            location = convert(i,j);
	            if (isLegalMove(location, board, turn) )
	                legal_moves.add(location); 
	        }
	    return legal_moves;                         
	}

    public static int get_node_value(String location) {
	    int row = location.charAt(1) - '1';
		int col = location.charAt(0) - 'a';
	    return node_table[row][col];  //Unsure of what to change to make it mesh with the node_table in AI
	}
   
    
    public int[][] nodeMove(int[][] gameboard, String location, int turn) {
	    int row = location.charAt(1) - '1';
		int col = location.charAt(0) - 'a';
		int x = 0;
		int y = 0;
		turn = gameboard[row][col];

		Boolean directioncheck = false;
		for (int i = 0; i < 8; ++i){       //conversion of pieces begin here
			x = DIREC[i][0];
			y = DIREC[i][1];
			while (on_board(col + x, row + y)){
				if (gameboard[row + y][col + x] == -turn){
					x = x + DIREC[i][0];
					y = y + DIREC[i][1];
					directioncheck = true;
				}
				else{
					break;
				}
			}
			if (on_board(col + x, row + y) && directioncheck == true){
				if (gameboard[row + y][col + x] == turn){

					int k = 0;
					int p = 0;

					if (y > 0){ p = 1; }
					else if (y < 0){ p = -1; }
					else { p = 0; }
					if (x > 0){ k = 1; }
					else if (x < 0){ k = -1; }
					else { k = 0; }

					for (int j = 1; j < Math.max(Math.abs(x), Math.abs(y)); ++j){
						turn = gameboard[row + (j*p)][col + (j*k)];
					}
				}
			}
			directioncheck = false;
		} 
	    return gameboard;                
	}
	
	public Vector<node> getChildNode(Vector<String> possibleMove, node n) {
	    node tmp = new node();
	    Vector<node> child = new Vector<node>();
	    for (int i = 0; i < possibleMove.size(); i++) {
	        tmp.board = nodeMove(n.board, possibleMove.get(i), n.turn);
	        tmp.my_turn = n.my_turn;
	        tmp.turn = -n.turn;
	        tmp.location = possibleMove.get(i);
	        child.add(tmp);
	    } 
	    return child;          
	}
	
	public static result maximum(result a, result b){
		if(a.value > b.value){
			return a;
		}
		else 
			return b;
	}
	
	public static result minimum(result a, result b){
		if(a.value < b.value){
			return a;
		}
		else 
			return b;
	}
	
	public static int evaluate(int[][] b, int size){
		int boardstate = 0;
		for (int i = 0; i < 8; ++i){
			for (int j = 0; j < 8; ++j){
				boardstate = boardstate + (b[i][j] * node_table[i][j]);
			}
		}
		boardstate = boardstate + size;
		return boardstate;
	}
	
	public result minmax(node node1, result alpha, result beta, int height) {
		Vector<String> possibleMove = getLegalMove(node1.board, node1.turn);
		result r = new result();
		if (height == 0) {		
			r.value = evaluate(node1.board, possibleMove.size());   // + possibleMove.size()
			r.location = node1.location;
			return r;
		}
		else {
			Vector<node> possible = getChildNode(possibleMove, node1);
			if (node1.turn == node1.my_turn) {
				//result a = alpha;       //-9999
				for (int i = 0; i < possible.size(); i++){
					alpha = maximum(alpha, minmax(possible.get(i), alpha, beta,  height - 1));
					if (beta.value <= alpha.value)
						break;
				}
				if (node1.location != "")
					alpha.location = node1.location;
				return alpha;
			}
			else if (node1.turn == -node1.my_turn) {
				//result b = beta;      //9999
				for (int i = 0; i < possible.size(); i++){
					beta = minimum(beta, minmax(possible.get(i), alpha, beta, height - 1));
					if (beta.value <= alpha.value)
						break;
				}
				if (node1.location != "")
					beta.location = node1.location;
				return beta;
			}			
			else {
				System.out.println("turn = " + node1.turn);
				System.exit(-1);
			}		
		}
		return r;
	}
	

    public String ai_function(){

        node n = new node();
	    n.board = gameboard;
	    n.my_turn = turn;
	    n.turn = turn;
	    n.location = "";
	    result i = minmax(n, new result(-9999), new result(9999), difficulty);
	    String location = i.location;
	    return location;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameboard);
		initialize();
	}
	
	public void gotohighscores(View view) {
        // Do something in response to button
		new MediaPlayer();
		final MediaPlayer np = MediaPlayer.create(this, R.raw.menuclick);
		np.start();
    	Intent intent = new Intent(this, Highscores.class);
    	intent.putExtra("blackscore_value", black_score);
    	intent.putExtra("whitescore_value", white_score);
    	startActivity(intent);
    	this.finish();
    }
	
	public void onclick(View view){
		//what happens when we click game piece?
		
		int id = view.getId();
		String idString = "no id";
		if(id != View.NO_ID) {                    // make sure id is valid
		    Resources res = view.getResources();     // get resources
		    if(res != null)
		        idString = res.getResourceEntryName(id); // get id string entry
		}
		
		if (isLegalMove(idString, gameboard, turn))  {
			play(idString);
			
			new MediaPlayer();
			final MediaPlayer mp = MediaPlayer.create(this, R.raw.buttonclick);
			mp.start();
			
			
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				  @Override
				  public void run() {
					  if (turn == 1) {
						    String location = ai_function();
						    mp.start();
						    play(location);
					  }
				  }
				}, 1000);
			
	    }
	}
	
	public void undoclick(View view){
		undo();
	}
	
	
}
