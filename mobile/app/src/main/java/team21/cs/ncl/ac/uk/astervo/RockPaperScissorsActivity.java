package team21.cs.ncl.ac.uk.astervo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class RockPaperScissorsActivity extends BaseActivity {

    int petChoice = -1;
    RelativeLayout[] petOptions = new RelativeLayout[3];

    int userChoice = -1;
    RelativeLayout[] userOptions = new RelativeLayout[3];

    Button replay;
    TextView notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rock_paper_scissors);

        replay = (Button) findViewById(R.id.petSpeechButton);
        notif = (TextView) findViewById(R.id.petSpeechBubble);


        makePetChoice();
        setupButtons();
    }

    public void makePetChoice() {

        Random r = new Random();
        petChoice = r.nextInt(200);
        petChoice = petChoice % 3;

    }

    public void setupButtons() {

        petOptions[0] = (RelativeLayout) findViewById(R.id.petRock);
        petOptions[1] = (RelativeLayout) findViewById(R.id.petPaper);
        petOptions[2] = (RelativeLayout) findViewById(R.id.petScissors);

        userOptions[0] = (RelativeLayout) findViewById(R.id.rock);
        userOptions[1] = (RelativeLayout) findViewById(R.id.paper);
        userOptions[2] = (RelativeLayout) findViewById(R.id.scissors);

        userOptions[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 0;
                completeGame();
            }
        });
        userOptions[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 1;
                completeGame();
            }
        });
        userOptions[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 2;
                completeGame();
            }
        });

        for(int i = 0; i < petOptions.length; i++) {
            petOptions[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "These are the pets choices!\nClick your own!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void completeGame() {

        replay.setVisibility(View.VISIBLE);

        for (int i = 0; i < userOptions.length; i++) {
            if (userChoice != i) {
                userOptions[i].setVisibility(View.INVISIBLE);
            }
            if (petChoice != i) {
                petOptions[i].setVisibility(View.INVISIBLE);
            }
        }
        //If user has won
        if((userChoice == 0 && petChoice == 2) || (userChoice == 1 && petChoice == 0) || (userChoice == 2 && petChoice == 1)) {
            notif.setText("Congratulations! You win!");
        }
        //If it's a draw
        else if(userChoice == petChoice) {
            notif.setText("It's a draw!");
        }
        else {
            notif.setText("Oops, you lost!");
        }

    }

    public void replay(View v) {

        replay.setVisibility(View.INVISIBLE);
        notif.setText(R.string.rock_paper_scissors_intro);

        makePetChoice();

        for (int i = 0; i < userOptions.length; i++) {
            userOptions[i].setVisibility(View.VISIBLE);
            petOptions[i].setVisibility(View.VISIBLE);

        }

    }
}
