package com.example.checkers;

import android.app.Activity;
//import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
//import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.Log;
//import android.support.annotation.NonNull;
//import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.graphics.Color;

//import java.util.Calendar;


public class MainActivity extends Activity { //implements OnClickListener, OnTouchListener{
    public ImageView[] board = new ImageView[64];
//    public View viewcell=board[0];
    public Integer[] bdvals = new Integer [64];
    // 0=empty 1==redman 2=whtman 3=redking 4=whtking  - add 10 for yellow boarder

    protected TextView redscore;  //=findViewById(R.id.score);
    protected TextView whitescore; //=findViewById(R.id.white_score);
    protected TextView over;
    protected TextView PushTitle;
    protected TextView PushDesc;

    //protected EditText debugview;

    int i=0;    //int j=0;
    int player =1;  //player -  1=red 2=white   whose turn is it
    int step=0;  //steps in one turn 0=ready 1=choose mover 2=choose destination
    int start=0,midpt=0;
    int scorered=0, scorewhite=0;

    // JimD additional code, for Push Notification Sept 13, 2023 from Balal YouTube channel.
    // These three steps:
    //     1. specify a push notification channel ID
    //     2. specify a push notification channel name
    //     3. specify a push notification channel description (with manager)
    //
    private static final String Channel_ID = "Checkers game ID 50009260";
    private static final String Channel_Name = "Checkers DM JD";
    private static final String Channel_Description = "using notification, to indicate it's your turn";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // JimD additional code, for Push Notification Sept 13, 2023 from Balal YouTube channel.
        // These three steps:
        //     1. specify a push notification channel ID
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel (Channel_ID, Channel_Name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Channel_Description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        //     2. specify a push notification channel name
        //

        //     3. specify a push notification channel description (with manager)
        //


        board[0]=findViewById(R.id.board0);
        board[1]=findViewById(R.id.board1);
        board[2]=findViewById(R.id.board2);
        board[3]=findViewById(R.id.board3);
        board[4]=findViewById(R.id.board4);
        board[5]=findViewById(R.id.board5);
        board[6]=findViewById(R.id.board6);
        board[7]=findViewById(R.id.board7);
        board[8]=findViewById(R.id.board8);
        board[9]=findViewById(R.id.board9);
        board[10]=findViewById(R.id.board10);
        board[11]=findViewById(R.id.board11);
        board[12]=findViewById(R.id.board12);
        board[13]=findViewById(R.id.board13);
        board[14]=findViewById(R.id.board14);
        board[15]=findViewById(R.id.board15);
        board[16]=findViewById(R.id.board16);
        board[17]=findViewById(R.id.board17);
        board[18]=findViewById(R.id.board18);
        board[19]=findViewById(R.id.board19);
        board[20]=findViewById(R.id.board20);
        board[21]=findViewById(R.id.board21);
        board[22]=findViewById(R.id.board22);
        board[23]=findViewById(R.id.board23);
        board[24]=findViewById(R.id.board24);
        board[25]=findViewById(R.id.board25);
        board[26]=findViewById(R.id.board26);
        board[27]=findViewById(R.id.board27);
        board[28]=findViewById(R.id.board28);
        board[29]=findViewById(R.id.board29);
        board[30]=findViewById(R.id.board30);
        board[31]=findViewById(R.id.board31);
        board[32]=findViewById(R.id.board32);
        board[33]=findViewById(R.id.board33);
        board[34]=findViewById(R.id.board34);
        board[35]=findViewById(R.id.board35);
        board[36]=findViewById(R.id.board36);
        board[37]=findViewById(R.id.board37);
        board[38]=findViewById(R.id.board38);
        board[39]=findViewById(R.id.board39);
        board[40]=findViewById(R.id.board40);
        board[41]=findViewById(R.id.board41);
        board[42]=findViewById(R.id.board42);
        board[43]=findViewById(R.id.board43);
        board[44]=findViewById(R.id.board44);
        board[45]=findViewById(R.id.board45);
        board[46]=findViewById(R.id.board46);
        board[47]=findViewById(R.id.board47);
        board[48]=findViewById(R.id.board48);
        board[49]=findViewById(R.id.board49);
        board[50]=findViewById(R.id.board50);
        board[51]=findViewById(R.id.board51);
        board[52]=findViewById(R.id.board52);
        board[53]=findViewById(R.id.board53);
        board[54]=findViewById(R.id.board54);
        board[55]=findViewById(R.id.board55);
        board[56]=findViewById(R.id.board56);
        board[57]=findViewById(R.id.board57);
        board[58]=findViewById(R.id.board58);
        board[59]=findViewById(R.id.board59);
        board[60]=findViewById(R.id.board60);
        board[61]=findViewById(R.id.board61);
        board[62]=findViewById(R.id.board62);
        board[63]=findViewById(R.id.board63);
        int i=-1;
        while(i<63){++i;
            board[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {turn(v);}});
        }
        Button btnRefresh=(Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {refreshBoard();}
        });
        Button debug= findViewById(R.id.debug);
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int cell=0;cell<64;cell++) {
                    Log.d("bdvals ", String.valueOf(cell)+" "+bdvals[cell]);
                }
            }
        });
        EditText debugview=findViewById(R.id.debugview);
        debugview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("debugview", "  str="+String.valueOf(editable));
                int temp=Integer.parseInt("0"+String.valueOf(editable));
                Log.d("debug","player "+player+" bdval "+bdvals(temp));
                movesFrom(board[temp]);
            }
        });
//        debugview.addTextChangedListener(TextWatcher());
        redscore=(TextView) findViewById(R.id.red_score);
        whitescore=(TextView) findViewById(R.id.white_score);
        over=findViewById(R.id.over);
        PushTitle=findViewById(R.id.PushTitle);
        PushDesc=findViewById(R.id.message);

//        debug.setChecked(true);
    }
    // ------------ end on create
    public void refreshBoard(){
        for(i=0;i<64;i++){
            if((i+(row(i)))%2==0){
                board[i].setBackgroundColor(Color.BLACK);
                if(i<24){board[i].setImageDrawable(getResources().getDrawable(R.drawable._1redman));bdvals[i]=1;}
                else if(i>39){board[i].setImageDrawable(getResources().getDrawable(R.drawable._2whiteman));bdvals[i]=2;}
                else{board[i].setImageDrawable(null);bdvals[i]=0;}}
            else{board[i].setBackgroundColor(Color.RED);bdvals[i]=0;}
        }
        scorered=0; scorewhite=0;
        player=1;  //1=red  2=white
        step=0;
        turn(board[0]);
    }
    public void turn(View v){ //steps in one turn 0=ready 1=choose mover 2=choose destination
        int cell= Integer.parseInt(bdnbr(v));
        if(bdvals(cell)<10 && step>0){Log.d("turn - error not high ",String.valueOf(cell)+" "+bdvals(cell));return;}
        Log.v("turn begin step--------"," beginning step="+String.valueOf(step)+" start "+start+" cell "+cell);
//move or jump
        if (step==2){  //move man to this cell - remove man from previous cell
            Log.d("step=2   ","move this man----from "+start+" to "+String.valueOf(cell));
            int bdvalue=bdvals(start);
            if((row(cell)==0 || row(cell)==7)&& bdvalue<3){bdvalue=bdvalue+2;}
            if(bdvalue==1){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._1redman));}
            if(bdvalue==2){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._2whiteman));}
            if(bdvalue==3){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._3redking));}
            if(bdvalue==4){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._4whiteking));}
            bdvals[cell]=bdvalue;
            board[start].setImageDrawable(null); bdvals[start]=0;  //delete from original cell

            if(Math.abs(row(start)-row(cell))==2){ //jumped an opponent
                int jumped=(start+cell)/2;
                board[jumped].setImageDrawable(null); //delete the opponent
                bdvals[jumped]=0;
                score(1);
            }
            if(Math.abs(row(start)-row(cell))==4 || Math.abs(row(start)-row(cell))==0){ //double jump
                int jumped=((start+midpt)/2);  //midpt = start of second jump
                board[jumped].setImageDrawable(null); //delete the opponent
                bdvals[jumped]=0;
                jumped=((midpt+cell)/2);
                board[jumped].setImageDrawable(null); //delete the opponent
                bdvals[jumped]=0;
                score(2);
            }
            clearhighlights();
            player =player%2+1;
            step=0;
        }

        if (step==1) {  //click on man to move; highlight possible destinations
            start=cell;
            Log.v("step=1--------", "-----------------------start-"+String.valueOf(start));
            step++;
            if (bdvals[cell] >= 10) {
                clearhighlights();
                for(int poss:movesFrom(v)){
                    if(poss>=0) { //Log.d("possmove  ",bdnbr(v));
                        board[poss].setImageDrawable(getResources().getDrawable(R.drawable._10yellow));
                        bdvals[poss] = 10;
                        Log.d("possmoves yellow","cell"+poss);
                    }
                }
            }
        }

        if(step==0){  //highlight possible movers
            Log.d("step=0   ","highlight movers--------player "+String.valueOf(player));
            int cntmoves=0;
            for (cell=0;cell<64;cell++){  //loop through board - cell is man
                if(bdvals[cell]>0 && bdvals[cell]%2== player%2) {  //this cell contains a man currently on his turn
                    Log.d("player's man -","can move?  cell="+String.valueOf(cell)+" bdval="+String.valueOf(bdvals[cell])+" player"+ player);
                    if(movesFrom(board[cell])[0]>=0){  //who can move
                        Log.d("   yes - highlight", " cell="+cell +" bdval="+bdvals[cell]);
                        if(bdvals[cell]==1){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._11redmanyellow));}
                        if(bdvals[cell]==2){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._12whitemanyellow));}
                        if(bdvals[cell]==3){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._13redkingyellow));}
                        if(bdvals[cell]==4){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._14whitekingyellow));}
                        bdvals[cell]=bdvals[cell]+10;
                        cntmoves++;
                    }
                }
            }
            if(cntmoves==0){over.setText("GAME OVER");}
            step++;  //next step actually move
            Log.d("turn","-------------end step=0  "+player);
        }
    }
    public void score(int scores){
        if(player==1){
            scorered=scorered+scores;
            redscore.setText(String.valueOf(scorered));
        }else{
            scorewhite=scorewhite+scores;
            whitescore.setText(String.valueOf(scorewhite));
        }
    }
    public void clearhighlights(){
        for(i=0;i<64;i++){int cell=i;
            if(bdvals[cell]>=10){
                Log.d("clearhighlights",cell+" "+bdvals[cell]);
                bdvals[cell]=bdvals[cell]%10;
                if(bdvals[cell]==1){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._1redman));}
                if(bdvals[cell]==2){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._2whiteman));}
                if(bdvals[cell]==3){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._3redking));}
                if(bdvals[cell]==4){board[cell].setImageDrawable(getResources().getDrawable(R.drawable._4whiteking));}
                if(bdvals[cell]<1){board[cell].setImageDrawable(null);}
            }
        }
    }

    public int[] movesFrom(View v){  //returns an array of possible destinations
        int dest=0,dest2=0,dest3=0,dest4=0;
        int[] ret= {-1,-1,-1,-1};
        int retptr=0;
        int cell=Integer.parseInt(bdnbr(v));
        int[] sides={7,9};  //right and left
//        int dir=0;
        int dirx=3-(player*2);  //direction of play 1=south (Red) -1=north (White)
        int[] dirarray={dirx,-dirx};
        if(bdvals(cell)<3){dirarray[1]=0;}

        for(int dir:dirarray) {  if(dir==0){break;}
            for (int side : sides) {
                dest = jump(cell, side, dir);
                if (bdvals(dest) == 0) {
                    ret[retptr] = dest;
                    retptr++;
                }
                if (bdvals(dest)>0 && bdvals(dest)%2 == (1 + player) % 2) {   // occupied by opponent - look for jump
                    dest2 = jump(dest, side, dir);
                    if (bdvals(dest2) == 0) {  //jump to cell not occupied - possible jump
                        midpt = dest2;
                        ret[retptr] = dest2;
                        retptr++;
                    //look for double jump
                        for(int dir2:dirarray) {if (dir2 == 0) {break;}
                            for (int side2 : sides) {
                                dest3 = jump(dest2, side2, dir2);
                                if (bdvals(dest3)%2 == (1 + player % 2)) { // occupied by opponent
                                    dest4 = jump(dest3, side2, dir2);
                                    if (bdvals(dest4) == 0) { //empty spot to land
                                        if (ret[retptr - 1] == midpt) {
                                            ret[retptr - 1] = dest4;
                                        }//overlay midpt
                                        else {
                                            ret[retptr] = dest4;
                                            retptr++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.d("  moveFrom dest ",String.valueOf(dest2)+ dest3 + dest4+" cell="+cell+" bdval="+bdvals(cell));
        Log.d("  moveFrom ret ",String.valueOf(ret[0])+ ret[1] + ret[2]+ret[3]);
        return ret;
    }
    public int jump(int cell, int side, int dir){ //inputs starting cell, side (left or right), dir (north or south)
        int dest=cell+side*dir;
        dest=row(dest)==row(cell)+dir?dest:-1;  //must be in next row
        return dest; //output is cell after move or -1 if move is off the board
    }
    public String bdnbr(View v){
        String bdid=v.getResources().getResourceName(v.getId());
        return bdid.substring(29,bdid.length());
    }
    int bdvals(int cell){
        if(cell>=0 && cell<64){ //Log.d("bdvals",String.valueOf(cell)+" "+bdvals[cell]);
            return bdvals[cell];}
        else{return cell;}
    }
    public int row(int cell){return cell/8;}
//    public int col(int cell){return cell%8;}

    public boolean black(int cell){
        int row=cell/8;
        return (cell+row)%2==0;
    }
    public void paint(int cell){}
    //    public int cell;
    public void onPause(){
        super.onPause();
        SharedPreferences MyPref = getSharedPreferences("chk",0);
        SharedPreferences.Editor ed = MyPref.edit();
        int i=0;
        while(i<64){Log.d("onPause",String.valueOf(i)+" "+String.valueOf(bdvals[i]));
            ed.putInt("board"+String.valueOf(i), bdvals[i]); i++;
        }
        ed.putInt("invert",invert);
        ed.apply();
    }
    int invert=0;
    public void onResume(){
        super.onResume();
        SharedPreferences MyPref = getSharedPreferences("chk",0);
        int i=0,temp=0;
        while(i<64){temp=temp+MyPref.getInt("board"+i++, 0);}
        // if zero preferences create new board
        if(temp==0){ i=0; while(i<16){bdvals[i]=++i%64;}}
        else{ i=0; while(i<64){bdvals[i]=MyPref.getInt("board"+i++, 0);}}
        i=-1;
        while(i<63){++i;  paint(i);}
        invert=MyPref.getInt("invert",1);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0,1,0,"Invert "+invert+"->"+(invert+1)%2);
        return true;
    }
    public boolean onPrepareOptionsMenu(final Menu menu){
        menu.findItem(1).setTitle("Invert "+invert%15+">"+(16-invert)%15);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
//   	switch (item.getItemId()) {
//   	case 1:
        int i=0;invert=64-invert;
        while(i<64){bdvals[i]=(bdvals[i]+invert)%64;paint(i);i++;}
//   		return true;}
        return true;
    }
}