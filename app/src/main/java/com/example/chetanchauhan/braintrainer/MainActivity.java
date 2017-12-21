package com.example.chetanchauhan.braintrainer;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    ImageView image;
    Random rand=new Random();
    int locofans;
    ArrayList<Integer> answers=new ArrayList<Integer>();
    ArrayList<String>symbols=new ArrayList<>();

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button go;
    Button playagain;
    RelativeLayout relativeLayout;
    TextView counter;
    TextView equ;
    TextView scoreview;
    TextView result;
    TextView maxdisplay;
    double maxscore=0;
    double score=0;
    double maxquestion=0;
    double no_question=0;
    boolean isactive=false;
    SharedPreferences max;
    SharedPreferences.Editor editor;

    public void playagain(View view)
    {
        score=0;
        no_question=0;
        counter.setText("0:30");
        result.setText("");
        scoreview.setText("0/0");
        playagain.setVisibility(View.INVISIBLE);
        isactive=true;
        maxdisplay.setVisibility(View.INVISIBLE);

        generatequestion();

        new CountDownTimer(30000+200,1000)
        {
            @Override
            public void onTick(long l) {
                counter.setText("0:"+String.valueOf((int)l/1000));
            }

            @Override
            public void onFinish() {
                counter.setText("0:00");
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                mediaPlayer.start();
                result.setText("Your score : "+Integer.toString((int)score)+"/"+Integer.toString((int)no_question));
                playagain.setVisibility(View.VISIBLE);
                isactive=false;
                maxscore=max.getInt("MaxScore",0);
                if((score)>(maxscore))
                {
                    editor.clear();
                    editor.putInt("MaxScore",(int)score);
                    editor.commit();
                    maxdisplay.setText("Max Score : "+Integer.toString((int)score));
                }
                else
                {
                    maxdisplay.setText("Max Score : "+Integer.toString((int)maxscore));
                }

                maxdisplay.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    public void generatequestion()
    {
        int a=rand.nextInt(21);
        int b=rand.nextInt(21);
        int sym=rand.nextInt(3);
        String z=symbols.get(sym);
        answers.clear();
        equ.setText(Integer.toString(a)+z+Integer.toString(b));
        int incorrectanswer;
        int finalans=0;
        locofans=rand.nextInt(4);
        switch (sym)
        {
            case 0:
                finalans=(a+b);
                break;
            case 1:
                finalans=(a-b);
                break;
            case 2:
                finalans=(a*b);
                break;
        }
        for(int i=0;i<4;i++)
        {
            if(i==locofans)
            {
                answers.add(finalans);
            }
            else
            {
                incorrectanswer=rand.nextInt(41);
                while(incorrectanswer==finalans)
                    incorrectanswer=rand.nextInt(41);
                answers.add(incorrectanswer);
            }
        }
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void chooseanswer(View view)
    {
        if(isactive)
        {
            if (view.getTag().toString().equals(String.valueOf(locofans))) {
                result.setText("Correct!");
                score++;
            } else {
                result.setText("Wrong!");
            }
            no_question++;
            scoreview.setText(Integer.toString((int)score) + "/" + Integer.toString((int)no_question));
            generatequestion();
        }
    }
    public void start(View view)
    {
        image.setVisibility(View.GONE);
        go.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        playagain(playagain);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=(ImageView)findViewById(R.id.imageView);
        go=(Button)findViewById(R.id.button);
        result=(TextView)findViewById(R.id.resultTextView);
        scoreview=(TextView)findViewById(R.id.scoreTextView);
        maxdisplay=(TextView)findViewById(R.id.maxscore);
        maxdisplay.setVisibility(View.INVISIBLE);

        playagain=(Button)findViewById(R.id.playagain);
        button0=(Button)findViewById(R.id.button0);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);

        image.setVisibility(View.VISIBLE);
        go.setVisibility(View.VISIBLE);

        relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout);
        relativeLayout.setVisibility(View.INVISIBLE);
        counter=(TextView)findViewById(R.id.counter);
        equ=(TextView)findViewById(R.id.equ);

        max=getSharedPreferences("MAX",0);
        editor=max.edit();

        if(!max.contains("MaxScore"))
        {
            editor.clear();
            editor.putInt("MaxScore", (int)maxscore);
            editor.commit();
        }

        symbols.add("+");
        symbols.add("-");
        symbols.add("*");
    }
}
